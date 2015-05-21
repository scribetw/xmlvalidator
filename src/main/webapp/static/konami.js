// written by othree, 2008
// konami command 1.0 <http://blog.othree.net/>
// better working with events.js <http://dean.edwards.name/weblog/2005/10/add-event/>

var konami = function (something, customCommand) {
    if (typeof something != 'function') return 0;

    var command = customCommand || [38, 38, 40, 40, 37, 39, 37, 39, 66, 65];
    var stepnow = 0;

    var checkCommand = function(e) {
        if (stepnow != command.length) {
            var code;
            if (!e) var e = window.event;
            if (e.keyCode) code = e.keyCode;
            else if (e.which) code = e.which;
            if (command[stepnow] == code) {
                stepnow++;
            } else stepnow = 0;
            if (stepnow == command.length) something();
        }
    };

    if (document.addEventListener) document.addEventListener('keydown', checkCommand, false);
    else if (typeof attachEvent == 'function') attachEvent(document, 'keydown', checkCommand);
    else {
        var onkeypressFuncs = document.onkeydown;
        document.onkeydown = function () {
            if (onkeypressFuncs) onkeypressFuncs();
            checkCommand();
        };
    }
};
