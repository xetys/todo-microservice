(function() {
    'use strict';
    angular
        .module('myuaagatewayApp')
        .factory('TodoEntry', TodoEntry);

    TodoEntry.$inject = ['$resource', 'DateUtils'];

    function TodoEntry ($resource, DateUtils) {
        var resourceUrl =  'api/todo-entries/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.deadLine = DateUtils.convertLocalDateFromServer(data.deadLine);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.deadLine = DateUtils.convertLocalDateToServer(data.deadLine);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.deadLine = DateUtils.convertLocalDateToServer(data.deadLine);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
