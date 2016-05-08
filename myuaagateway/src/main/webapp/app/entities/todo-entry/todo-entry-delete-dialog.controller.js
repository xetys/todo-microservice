(function() {
    'use strict';

    angular
        .module('myuaagatewayApp')
        .controller('TodoEntryDeleteController',TodoEntryDeleteController);

    TodoEntryDeleteController.$inject = ['$uibModalInstance', 'entity', 'TodoEntry'];

    function TodoEntryDeleteController($uibModalInstance, entity, TodoEntry) {
        var vm = this;
        vm.todoEntry = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            TodoEntry.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
