(function() {
    'use strict';

    angular
        .module('comicbooksApp')
        .factory('LoginService', LoginService);

    LoginService.$inject = ['$mdDialog'];

    function LoginService ($mdDialog) {
        var service = {
            open: open,
            close: close
        };

        var modalInstance = null;
        var resetModal = function () {
            modalInstance = null;
        };

        return service;

        function open () {
            modalInstance = $mdDialog.show({
                templateUrl: 'app/components/login/login.html',
                controller: 'LoginController',
                controllerAs: 'vm',
                clickOutsideToClose: true,
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('login');
                        return $translate.refresh();
                    }]
                }
            });
        }

        function close() {
            $mdDialog.cancel(modalInstance);
        }
    }
})();
