(function() {
    'use strict';

    angular
        .module('comicbooksApp')
        .controller('ComicBookDetailController', ComicBookDetailController);

    ComicBookDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'ComicBook', 'Author'];

    function ComicBookDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, ComicBook, Author) {
        var vm = this;

        vm.comicBook = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('comicbooksApp:comicBookUpdate', function(event, result) {
            vm.comicBook = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
