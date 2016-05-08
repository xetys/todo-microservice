(function() {
    'use strict';

    angular
        .module('myuaagatewayApp')
        .factory('PasswordResetInit', PasswordResetInit);

    PasswordResetInit.$inject = ['$resource'];

    function PasswordResetInit($resource) {
        var service = $resource('myuaa/api/account/reset_password/init', {}, {});

        return service;
    }
})();
