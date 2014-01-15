
// Input caret position
(function($) {
    $.fn.getCursorPosition = function() {
        var input = this.get(0);
        if (!input) return; // No (input) element found
        if ('selectionStart' in input) {
            // Standard-compliant browsers
            return input.selectionStart;
        } else if (document.selection) {
            // IE
            input.focus();
            var sel = document.selection.createRange();
            var selLen = document.selection.createRange().text.length;
            sel.moveStart('character', -input.value.length);
            return sel.text.length - selLen;
        }
    }
})(jQuery);


$(function() {

    var socket = new WebSocket("ws://" + window.location.host + "/socket");

    socket.onopen = function() {
        console.log("Socket has been opened");
    }

    socket.onmessage = function(msg) {
        var data = JSON.parse(msg.data);
        var input = $("#search-input").val();
        var sepIndex = input.lastIndexOf(" ");
        $("#input-fixed").empty();
        if (sepIndex >= 0) {
            var inputFixed = input.substr(0, sepIndex) + '\xA0';
            $("#input-fixed").text(inputFixed);
        }

        $("#input-suggestion").empty();
        $.each(data, function(index, value) {
            if (index == 0) {
                if (sepIndex < 0) {
                    var inputLast = input;
                } else {
                    var inputLast = input.substr(sepIndex + 1);
                }
                var suggestionHidden = inputLast;
                var suggestionAutoComplete = value.replace(suggestionHidden, "");
                $("#input-suggestion").append('<li>' +
                    '<span id="hidden-suggestion">' + suggestionHidden + '</span>' +
                    '<span id="auto-complete-suggestion">' + suggestionAutoComplete + '</span>' +
                '</li>');
            } else {
                $("#input-suggestion").append('<li>' + value + '</li>');
            }
        });
    }

    $("#search-input").after('<ul id="input-suggestion"></ul>');
    $("#search-input").after('<text id="input-fixed"></text>');

    $("#search-input").each(function() {
        var elem = $(this);

        // Save current value of element
        elem.data('old-value', elem.val());

        // Look for changes in the value
        elem.bind("propertychange keyup input paste", function(event) {
            // If value has changed...
            if (elem.data('old-value') != elem.val()) {
                // Updated stored value
                elem.data('old-value', elem.val());

                // Do action
                socket.send(JSON.stringify({
                    content: elem.val()
                }));
            }
        });

        elem.bind("keypress", function(event) {
            switch(event.keyCode) {
                case 13: // Enter
                    elem.val(elem.val() + $("#auto-complete-suggestion").text() + " ");
            }
        });
    });
});