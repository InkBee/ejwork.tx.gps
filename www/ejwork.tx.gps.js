var exec = require('cordova/exec');

exports.getLocation = function (arg0, success, error) {
    exec(success, error, 'ejwork.tx.gps', 'getLocation', [arg0]);
};

exports.stopLocation = function (arg0, success, error) {
    exec(success, error, 'ejwork.tx.gps', 'stopLocation', [arg0]);
};
