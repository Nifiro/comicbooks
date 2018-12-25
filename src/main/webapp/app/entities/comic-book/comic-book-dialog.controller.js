(function () {
    'use strict';

    angular
        .module('comicbooksApp')
        .controller('ComicBookDialogController', ComicBookDialogController);

    ComicBookDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$mdDialog', 'DataUtils', 'entity', 'ComicBook', 'Author'];

    function ComicBookDialogController($timeout, $scope, $stateParams, $mdDialog, DataUtils, entity, ComicBook, Author) {
        var vm = this;

        vm.comicBook = entity;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.authors = Author.query();
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
            var dialog = $mdDialog.alert()
                .clickOutsideToClose(true)
                .title('Ура!')
                .textContent('Комикс "' + vm.comicBook.title + '" успешно добавлен')
                .ok('OK');
            $mdDialog.show(dialog);
            vm.comicBook = {
                title: null,
                chapters: null,
                description: null,
                publisher: null,
                serializedFrom: null,
                serializedTo: null,
                imagePath: null,
                coverPath: null,
                status: null,
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
