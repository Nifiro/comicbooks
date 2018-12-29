(function () {
    'use strict';

    angular
        .module('comicbooksApp')
        .controller('ChapterDetailController', ChapterDetailController);

    ChapterDetailController.$inject = ['$http', '$scope', '$rootScope', '$stateParams',
        '$document', 'previousState', 'entity', 'Chapter'];

    function ChapterDetailController($http, $scope, $rootScope, $stateParams, $document,
                                     previousState, entity, Chapter) {
        var vm = this;

        vm.chapter = entity;
        vm.previousState = previousState.name;
        vm.page = 1;
        vm.currentPage = 1;
        vm.image = null;
        vm.pages = [];

        for(var i = 1; i <= vm.chapter.pages; i++) {
            vm.pages.push(i)
        }

        vm.nextPage = nextPage;
        vm.prevPage = prevPage;
        vm.lastPage = lastPage;
        vm.firstPage = firstPage;
        vm.loadPage = loadPage;

        var unsubscribe = $rootScope.$on('comicbooksApp:chapterUpdate', function (event, result) {
            vm.chapter = result;
        });
        $scope.$on('$destroy', unsubscribe);

        loadPage();

        function loadPage() {
            console.log('in loadPage. vm.page = ' + vm.page);
            $http({
                method: 'GET',
                url: '/api/chapter/' + vm.chapter.id + '/page/' + vm.page,
                responseType: 'blob'
            }).success(function (data, status, headers) {
                var contentType = headers('Content-Type');
                var file = new Blob([data], {type: contentType});
                vm.image = URL.createObjectURL(file);
            });
        }

        function prevPage() {
            if (vm.page > 1) {
                vm.page--;
                loadPage();
            }
        }

        function nextPage() {
            if (vm.page < vm.chapter.pages) {
                vm.page++;
                loadPage();
            }
        }

        function lastPage() {
            vm.page = vm.chapter.pages;
            loadPage();
        }

        function firstPage() {
            vm.page = 1;
            loadPage();
        }

        $document.keydown(function keyPressed(event) {
            if (event.keyCode === 37)
                prevPage();
            else if (event.keyCode === 39)
                nextPage();
        });
    }
})();
