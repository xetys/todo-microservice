(function() {
    'use strict';

    angular
        .module('myuaagatewayApp')
        .factory('PasswordResetFinish', PasswordResetFinish);

    PasswordResetFinish.$inject = ['$resource'];

    function PasswordResetFinish($resource) {
        var service = $resource('myuaa/api/account/reset_password/finish', {}, {});

        return service;
    }
})();
