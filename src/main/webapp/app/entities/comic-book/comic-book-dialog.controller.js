(function() {
    'use strict';

    angular
        .module('comicbooksApp')
        .controller('ComicBookDialogController', ComicBookDialogController);

    ComicBookDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'ComicBook', 'Author'];

    function ComicBookDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, ComicBook, Author) {
        var vm = this;

        vm.comicBook = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.authors = Author.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.comicBook.id !== null) {
                ComicBook.update(vm.comicBook, onSaveSuccess, onSaveError);
            } else {
                ComicBook.save(vm.comicBook, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('comicbooksApp:comicBookUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.serializedFrom = false;
        vm.datePickerOpenStatus.serializedTo = false;
        vm.datePickerOpenStatus.createdDate = false;
        vm.datePickerOpenStatus.lastModifiedDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
