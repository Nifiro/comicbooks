(function() {
    'use strict';

    angular
        .module('comicbooksApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('comic-book-genres', {
            parent: 'entity',
            url: '/comic-book-genres',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'comicbooksApp.comicBookGenres.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/comic-book-genres/comic-book-genres.html',
                    controller: 'ComicBookGenresController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('comicBookGenres');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('comic-book-genres-detail', {
            parent: 'comic-book-genres',
            url: '/comic-book-genres/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'comicbooksApp.comicBookGenres.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/comic-book-genres/comic-book-genres-detail.html',
                    controller: 'ComicBookGenresDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('comicBookGenres');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ComicBookGenres', function($stateParams, ComicBookGenres) {
                    return ComicBookGenres.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'comic-book-genres',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('comic-book-genres-detail.edit', {
            parent: 'comic-book-genres-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/comic-book-genres/comic-book-genres-dialog.html',
                    controller: 'ComicBookGenresDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ComicBookGenres', function(ComicBookGenres) {
                            return ComicBookGenres.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('comic-book-genres.new', {
            parent: 'comic-book-genres',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/comic-book-genres/comic-book-genres-dialog.html',
                    controller: 'ComicBookGenresDialogController',
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
                    $state.go('comic-book-genres', null, { reload: 'comic-book-genres' });
                }, function() {
                    $state.go('comic-book-genres');
                });
            }]
        })
        .state('comic-book-genres.edit', {
            parent: 'comic-book-genres',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/comic-book-genres/comic-book-genres-dialog.html',
                    controller: 'ComicBookGenresDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ComicBookGenres', function(ComicBookGenres) {
                            return ComicBookGenres.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('comic-book-genres', null, { reload: 'comic-book-genres' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('comic-book-genres.delete', {
            parent: 'comic-book-genres',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/comic-book-genres/comic-book-genres-delete-dialog.html',
                    controller: 'ComicBookGenresDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ComicBookGenres', function(ComicBookGenres) {
                            return ComicBookGenres.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('comic-book-genres', null, { reload: 'comic-book-genres' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
