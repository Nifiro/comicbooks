(function() {
    'use strict';

    angular
        .module('comicbooksApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('series', {
            parent: 'entity',
            url: '/series',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'comicbooksApp.series.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/series/series.html',
                    controller: 'SeriesController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('series');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('series-detail', {
            parent: 'series',
            url: '/series/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'comicbooksApp.series.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/series/series-detail.html',
                    controller: 'SeriesDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('series');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Series', function($stateParams, Series) {
                    return Series.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'series',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('series-detail.edit', {
            parent: 'series-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/series/series-dialog.html',
                    controller: 'SeriesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Series', function(Series) {
                            return Series.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('series.new', {
            parent: 'series',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/series/series-dialog.html',
                    controller: 'SeriesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('series', null, { reload: 'series' });
                }, function() {
                    $state.go('series');
                });
            }]
        })
        .state('series.edit', {
            parent: 'series',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/series/series-dialog.html',
                    controller: 'SeriesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Series', function(Series) {
                            return Series.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('series', null, { reload: 'series' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('series.delete', {
            parent: 'series',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/series/series-delete-dialog.html',
                    controller: 'SeriesDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Series', function(Series) {
                            return Series.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('series', null, { reload: 'series' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
