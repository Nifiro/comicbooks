(function () {
    'use strict';

    angular
        .module('comicbooksApp')
        .controller('ComicBookDialogController', ComicBookDialogController);

    ComicBookDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$mdToast', 'DataUtils', 'entity', 'ComicBook', 'Author', 'Genre'];

    function ComicBookDialogController($timeout, $scope, $stateParams, $mdToast, DataUtils, entity, ComicBook, Author, Genre) {
        var vm = this;

        vm.comicBook = entity;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.authors = Author.query();
        vm.genres = Genre.query();
        vm.selectedAuthor = null;

        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });

        function save() {
            vm.isSaving = true;
            vm.comicBook.authorId = vm.selectedAuthor.id;
            if (vm.comicBook.id !== null) {
                ComicBook.update(vm.comicBook, onSaveSuccess, onSaveError);
            } else {
                ComicBook.save(vm.comicBook, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess(result) {
            $scope.$emit('comicbooksApp:comicBookUpdate', result);
            vm.isSaving = false;

            var toast = $mdToast.simple()
                .textContent('Комикс "' + vm.comicBook.title + '" успешно добавлен')
                .parent(angular.element(document.getElementById('toastParent')))
                .position('bottom right')
                .capsule(true);
            $mdToast.show(toast);

            vm.comicBook = {
                title: vm.comicBook.title,
                chapters: vm.comicBook.chapters,
                description: vm.comicBook.description,
                publisher: vm.comicBook.publisher,
                serializedFrom: vm.comicBook.serializedFrom,
                serializedTo: vm.comicBook.serializedTo,
                imagePath: vm.comicBook.imagePath,
                coverPath: vm.comicBook.coverPath,
                status: vm.comicBook.status,
                id: null,
                authorId: null
            };
        }

        function onSaveError() {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.serializedFrom = false;
        vm.datePickerOpenStatus.serializedTo = false;
        vm.datePickerOpenStatus.createdDate = false;
        vm.datePickerOpenStatus.lastModifiedDate = false;

        function openCalendar(date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
