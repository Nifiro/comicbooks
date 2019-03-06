(function() {
    'use strict';

    angular
        .module('comicbooksApp')
        .controller('ComicBookDeleteController',ComicBookDeleteController);

    ComicBookDeleteController.$inject = ['$uibModalInstance', 'entity', 'ComicBook'];

    function ComicBookDeleteController($uibModalInstance, entity, ComicBook) {
        var vm = this;

        vm.comicBook = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ComicBook.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
