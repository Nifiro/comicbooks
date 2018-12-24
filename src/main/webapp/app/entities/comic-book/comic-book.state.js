(function() {
    'use strict';

    angular
        .module('comicbooksApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('comic-book', {
            parent: 'entity',
            url: '/comic-book',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'comicbooksApp.comicBook.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/comic-book/comic-books.html',
                    controller: 'ComicBookController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('comicBook');
                    $translatePartialLoader.addPart('status');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('comic-book-detail', {
            parent: 'comic-book',
            url: '/comic-book/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'comicbooksApp.comicBook.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/comic-book/comic-book-detail.html',
                    controller: 'ComicBookDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('comicBook');
                    $translatePartialLoader.addPart('status');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ComicBook', function($stateParams, ComicBook) {
                    return ComicBook.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'comic-book',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('comic-book-detail.edit', {
            parent: 'comic-book-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/comic-book/comic-book-dialog.html',
                    controller: 'ComicBookDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ComicBook', function(ComicBook) {
                            return ComicBook.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('comic-book.new', {
            parent: 'comic-book',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/comic-book/comic-book-dialog.html',
                    controller: 'ComicBookDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                title: null,
                                chapters: null,
                                description: null,
                                publisher: null,
                                serializedFrom: null,
                                serializedTo: null,
                                imagePath: null,
                                coverPath: null,
                                createdBy: null,
                                createdDate: null,
                                lastModifiedBy: null,
                                lastModifiedDate: null,
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('comic-book', null, { reload: 'comic-book' });
                }, function() {
                    $state.go('comic-book');
                });
            }]
        })
        .state('comic-book.edit', {
            parent: 'comic-book',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/comic-book/comic-book-dialog.html',
                    controller: 'ComicBookDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ComicBook', function(ComicBook) {
                            return ComicBook.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('comic-book', null, { reload: 'comic-book' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('comic-book.delete', {
            parent: 'comic-book',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/comic-book/comic-book-delete-dialog.html',
                    controller: 'ComicBookDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ComicBook', function(ComicBook) {
                            return ComicBook.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('comic-book', null, { reload: 'comic-book' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
