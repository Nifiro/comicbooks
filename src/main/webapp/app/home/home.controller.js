(function () {
    'use strict';

    angular
        .module('comicbooksApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', '$http', 'Principal', 'LoginService', '$state', 'paginationConstants',
        'ParseLinks', 'Chapter'];

    function HomeController($scope, $http, Principal, LoginService, $state, paginationConstants,
                            ParseLinks, Chapter) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        vm.loadImage = loadImage;
        vm.getMonth = getMonth;
        vm.getYear = getYear;
        vm.getDate = getDate;
        vm.getTitle = getTitle;
        $scope.$on('authenticationSuccess', function () {
            getAccount();
        });
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.page = 0;
        vm.links = {
            last: 0
        };
        vm.predicate = 'id';
        vm.reverse = true;
        vm.chapters = [];

        getAccount();
        loadAll();

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

        function loadAll() {
            var oneWeekAgo = new Date();
            oneWeekAgo.setDate(oneWeekAgo.getDate() - 7);

            Chapter.query({
                page: vm.page,
                size: vm.itemsPerPage,
                sort: sort(),
                'createdDate.greaterThen': oneWeekAgo.toISOString()
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
                    vm.chapters.push(data[i]);
                }
            }
        }

        function getDate(iso) {
            return new Date(iso).getDate();
        }

        function getYear(iso) {
            return new Date(iso).getFullYear();
        }

        function getMonth(iso) {
            return new Date(iso).getMonth() + 1;
        }

        function getTitle(chapter) {
            $http.get('/api/comic-books/' + chapter.comicBookId).success(function (result) {
                chapter.title = result.title;
            })
        }
    }
})();
