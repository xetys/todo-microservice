(function () {
    'use strict';

    angular
        .module('myuaagatewayApp')
        .factory('Register', Register);

    Register.$inject = ['$resource'];

    function Register ($resource) {
        return $resource('myuaa/api/register', {}, {});
    }
})();
