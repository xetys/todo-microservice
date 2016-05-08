(function() {
    'use strict';

    angular
        .module('myuaagatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('todo-entry', {
            parent: 'entity',
            url: '/todo-entry?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'myuaagatewayApp.todoEntry.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/todo-entry/todo-entries.html',
                    controller: 'TodoEntryController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('todoEntry');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('todo-entry-detail', {
            parent: 'entity',
            url: '/todo-entry/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'myuaagatewayApp.todoEntry.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/todo-entry/todo-entry-detail.html',
                    controller: 'TodoEntryDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('todoEntry');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'TodoEntry', function($stateParams, TodoEntry) {
                    return TodoEntry.get({id : $stateParams.id});
                }]
            }
        })
        .state('todo-entry.new', {
            parent: 'todo-entry',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/todo-entry/todo-entry-dialog.html',
                    controller: 'TodoEntryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                title: null,
                                percentDone: null,
                                deadLine: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('todo-entry', null, { reload: true });
                }, function() {
                    $state.go('todo-entry');
                });
            }]
        })
        .state('todo-entry.edit', {
            parent: 'todo-entry',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/todo-entry/todo-entry-dialog.html',
                    controller: 'TodoEntryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TodoEntry', function(TodoEntry) {
                            return TodoEntry.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('todo-entry', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('todo-entry.delete', {
            parent: 'todo-entry',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/todo-entry/todo-entry-delete-dialog.html',
                    controller: 'TodoEntryDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TodoEntry', function(TodoEntry) {
                            return TodoEntry.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('todo-entry', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
