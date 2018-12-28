(function() {
    'use strict';

    angular
        .module('comicbooksApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', '$http', 'Principal', 'LoginService', '$state'];

    function HomeController ($scope, $http, Principal, LoginService, $state) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
        function register () {
            $state.go('register');
        }

        function loadImage(id, type, comicBook) {
            $http({
                method: 'GET',
                url: '/api/comic-books/' + id + '/' + type,
                responseType: 'blob'
            }).success(function (data, status, headers) {
                var contentType = headers('Content-Type');
                var file = new Blob([data], {type: contentType});
                comicBook.image = URL.createObjectURL(file);
            });
        }

        function getAuthor(id, comicBook) {
            $http({
                method: 'GET',
                url: '/api/authors/' + id
            }).success(function (result) {
                comicBook.firstName = result.firstName;
                comicBook.lastName = result.lastName
            });
        }
    }
})();
