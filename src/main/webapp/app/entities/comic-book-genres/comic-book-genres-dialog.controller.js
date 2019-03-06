(function() {
    'use strict';

    angular
        .module('comicbooksApp')
        .controller('ComicBookGenresDialogController', ComicBookGenresDialogController);

    ComicBookGenresDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ComicBookGenres', 'ComicBook', 'Genre'];

    function ComicBookGenresDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ComicBookGenres, ComicBook, Genre) {
        var vm = this;

        vm.comicBookGenres = entity;
        vm.clear = clear;
        vm.save = save;
        vm.comicbooks = ComicBook.query();
        vm.genres = Genre.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.comicBookGenres.id !== null) {
                ComicBookGenres.update(vm.comicBookGenres, onSaveSuccess, onSaveError);
            } else {
                ComicBookGenres.save(vm.comicBookGenres, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('comicbooksApp:comicBookGenresUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
