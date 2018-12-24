(function() {
    'use strict';

    angular
        .module('comicbooksApp')
        .controller('ComicBookGenresDeleteController',ComicBookGenresDeleteController);

    ComicBookGenresDeleteController.$inject = ['$uibModalInstance', 'entity', 'ComicBookGenres'];

    function ComicBookGenresDeleteController($uibModalInstance, entity, ComicBookGenres) {
        var vm = this;

        vm.comicBookGenres = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ComicBookGenres.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
