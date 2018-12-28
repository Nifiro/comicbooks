(function () {
    'use strict';

    angular
        .module('comicbooksApp')
        .controller('AuthorDetailController', AuthorDetailController);

    AuthorDetailController.$inject = ['$scope', '$http', '$rootScope', '$stateParams', 'previousState', 'entity', 'Author',
        'ComicBook', 'ParseLinks', 'paginationConstants'];

    function AuthorDetailController($scope, $http, $rootScope, $stateParams, previousState, entity, Author,
                                    ComicBook, ParseLinks, paginationConstants) {
        var vm = this;

        vm.author = entity;
        vm.previousState = previousState.name;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.loadImage = loadImage;
        vm.page = 0;
        vm.page = 0;
        vm.links = {
            last: 0
        };
        vm.predicate = 'id';
        vm.comicBooks = [];

        var unsubscribe = $rootScope.$on('comicbooksApp:authorUpdate', function (event, result) {
            vm.author = result;
        });
        $scope.$on('$destroy', unsubscribe);

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

        loadAll();

        function loadAll() {
            ComicBook.query({
                page: vm.page,
                size: vm.itemsPerPage,
                'authorId.equals': vm.author.id,
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
