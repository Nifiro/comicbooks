(function() {
    'use strict';

    angular
        .module('comicbooksApp')
        .controller('SeriesDeleteController',SeriesDeleteController);

    SeriesDeleteController.$inject = ['$uibModalInstance', 'entity', 'Series'];

    function SeriesDeleteController($uibModalInstance, entity, Series) {
        var vm = this;

        vm.series = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Series.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
