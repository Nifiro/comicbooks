(function() {
    'use strict';

    angular
        .module('comicbooksApp')
        .controller('ComicBookGenresDetailController', ComicBookGenresDetailController);

    ComicBookGenresDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ComicBookGenres', 'ComicBook', 'Genre'];

    function ComicBookGenresDetailController($scope, $rootScope, $stateParams, previousState, entity, ComicBookGenres, ComicBook, Genre) {
        var vm = this;

        vm.comicBookGenres = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('comicbooksApp:comicBookGenresUpdate', function(event, result) {
            vm.comicBookGenres = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
