(function () {
    'use strict';

    angular
        .module('comicbooksApp')
        .controller('ComicBookController', ComicBookController);

    ComicBookController.$inject = ['$http', 'DataUtils', 'ComicBook', 'ParseLinks',
        'AlertService', 'paginationConstants', 'Author'];

    function ComicBookController($http, DataUtils, ComicBook, ParseLinks,
                                 AlertService, paginationConstants, Author) {

        var vm = this;

        vm.comicBooks = [];
        vm.loadPage = loadPage;
        vm.loadImage = loadImage;
        vm.getAuthor = getAuthor;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.page = 0;
        vm.links = {
            last: 0
        };
        vm.predicate = 'id';
        vm.reset = reset;
        vm.reverse = true;
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;

        loadAll();

        function loadAll() {
            ComicBook.query({
                page: vm.page,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);

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

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function reset() {
            vm.page = 0;
            vm.comicBooks = [];
            loadAll();
        }

        function loadPage(page) {
            vm.page = page;
            loadAll();
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
