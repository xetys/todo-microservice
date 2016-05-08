(function() {
    'use strict';

    angular
        .module('myuaagatewayApp')
        .controller('TodoEntryDialogController', TodoEntryDialogController);

    TodoEntryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TodoEntry'];

    function TodoEntryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TodoEntry) {
        var vm = this;
        vm.todoEntry = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('myuaagatewayApp:todoEntryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.todoEntry.id !== null) {
                TodoEntry.update(vm.todoEntry, onSaveSuccess, onSaveError);
            } else {
                TodoEntry.save(vm.todoEntry, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.deadLine = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
