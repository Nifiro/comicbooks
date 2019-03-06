(function() {
    'use strict';

    angular
        .module('comicbooksApp')
        .controller('SeriesDialogController', SeriesDialogController);

    SeriesDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Series', 'ComicBook', 'Chapter'];

    function SeriesDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Series, ComicBook, Chapter) {
        var vm = this;

        vm.series = entity;
        vm.clear = clear;
        vm.save = save;
        vm.comicbooks = ComicBook.query();
        vm.chapters = Chapter.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.series.id !== null) {
                Series.update(vm.series, onSaveSuccess, onSaveError);
            } else {
                Series.save(vm.series, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('comicbooksApp:seriesUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
