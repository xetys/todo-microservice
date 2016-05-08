'use strict';

describe('Controller Tests', function() {

    describe('TodoEntry Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockTodoEntry;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockTodoEntry = jasmine.createSpy('MockTodoEntry');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'TodoEntry': MockTodoEntry
            };
            createController = function() {
                $injector.get('$controller')("TodoEntryDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'myuaagatewayApp:todoEntryUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
