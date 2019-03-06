(function() {
    'use strict';

    angular
        .module('comicbooksApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('profile', {
            parent: 'entity',
            url: '/profile',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'comicbooksApp.profile.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/profile/profiles.html',
                    controller: 'ProfileController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('profile');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('profile-detail', {
            parent: 'profile',
            url: '/profile/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'comicbooksApp.profile.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/profile/profile-detail.html',
                    controller: 'ProfileDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('profile');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Profile', function($stateParams, Profile) {
                    return Profile.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'profile',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('profile-detail.edit', {
            parent: 'profile-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/profile/profile-dialog.html',
                    controller: 'ProfileDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Profile', function(Profile) {
                            return Profile.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('profile.new', {
            parent: 'profile',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/profile/profile-dialog.html',
                    controller: 'ProfileDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                login: null,
                                lastName: null,
                                firstName: null,
                                email: null,
                                imageUrl: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('profile', null, { reload: 'profile' });
                }, function() {
                    $state.go('profile');
                });
            }]
        })
        .state('profile.edit', {
            parent: 'profile',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/profile/profile-dialog.html',
                    controller: 'ProfileDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Profile', function(Profile) {
                            return Profile.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('profile', null, { reload: 'profile' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('profile.delete', {
            parent: 'profile',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/profile/profile-delete-dialog.html',
                    controller: 'ProfileDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Profile', function(Profile) {
                            return Profile.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('profile', null, { reload: 'profile' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
