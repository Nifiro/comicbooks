(function () {
    'use strict';

    angular
        .module('comicbooksApp')
        .controller('ComicBookDetailController', ComicBookDetailController);

    ComicBookDetailController.$inject = ['$http', '$scope', '$rootScope', '$stateParams', 'previousState', 'Genre',
        'DataUtils', 'entity'];

    function ComicBookDetailController($http, $scope, $rootScope, $stateParams, previousState, Genre,
                                       DataUtils, entity) {
        var vm = this;

        vm.comicBook = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        vm.loadImage = loadImage;
        vm.getAuthor = getAuthor;
        vm.getMonth = getMonth;
        vm.getYear = getYear;
        vm.getDate = getDate;
        vm.genres = [];
        vm.chapters = [];

        var unsubscribe = $rootScope.$on('comicbooksApp:comicBookUpdate', function (event, result) {
            vm.comicBook = result;
        });
        $scope.$on('$destroy', unsubscribe);

        loadGenres();
        loadChapters();

        function loadGenres() {
            $http.get('/api/comic-book-genres?comicBookId.equals=' + vm.comicBook.id).success(function (response) {
                response.forEach(function (genre) {
                    $http.get('/api/genres?id.equals=' + genre.genreId).success(function (genre) {
                        vm.genres.push(genre[0]);
                    })
                })
            });
        }

        function loadChapters() {
            $http.get('/api/series?comicBookId.equals=' + vm.comicBook.id).success(function (response) {
                response.forEach(function (series) {
                    $http.get('/api/chapters?id.equals=' + series.chapterId).success(function (chapter) {
                        console.log(chapter);
                        vm.chapters.push(chapter[0]);
                    })
                })
            });
        }

        function loadImage(id, type, comicBook) {
            $http({
                method: 'GET',
                url: '/api/comic-books/' + id + '/' + type,
                responseType: 'blob'
            }).success(function (data, status, headers) {
                var contentType = headers('Content-Type');
                var file = new Blob([data], {type: contentType});
                if (type === 'background')
                    comicBook.image = URL.createObjectURL(file);
                else
                    comicBook.cover = URL.createObjectURL(file);
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

        function getDate(iso) {
            return new Date(iso).getDate() + 1;
        }

        function getYear(iso) {
            return new Date(iso).getFullYear();
        }

        function getMonth(iso) {
            return new Date(iso).getMonth() + 1;
        }
    }
})();
