(function () {
    'use strict';

    angular
        .module('comicbooksApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', '$http', 'Principal', 'LoginService', '$state', 'paginationConstants',
        'ParseLinks', 'ComicBook'];

    function HomeController($scope, $http, Principal, LoginService, $state, paginationConstants,
                            ParseLinks, ComicBook) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        $scope.$on('authenticationSuccess', function () {
            getAccount();
        });
        vm.loadImage = loadImage;
        vm.getAuthor = getAuthor;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.page = 1;
        vm.links = {
            last: 0
        };
        vm.predicate = 'id';
        vm.reverse = true;
        vm.comicBooks = [];

        getAccount();

        function getAccount() {
            Principal.identity().then(function (account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }

        function register() {
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

        function loadAll() {
            ComicBook.query({
                page: vm.page,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess);

            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                for (var i = 0; i < data.length; i++) {
                    vm.comicBooks.push(data[i]);
                }
            }
        }
    }
})();
