(function() {
    'use strict';

    angular
        .module('myuaagatewayApp')
        .factory('Password', Password);

    Password.$inject = ['$resource'];

    function Password($resource) {
        var service = $resource('myuaa/api/account/change_password', {}, {});

        return service;
    }
})();
