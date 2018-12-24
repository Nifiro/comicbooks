(function() {
    'use strict';

    angular
        .module('comicbooksApp')
        .controller('SeriesDetailController', SeriesDetailController);

    SeriesDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Series', 'ComicBook', 'Chapter'];

    function SeriesDetailController($scope, $rootScope, $stateParams, previousState, entity, Series, ComicBook, Chapter) {
        var vm = this;

        vm.series = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('comicbooksApp:seriesUpdate', function(event, result) {
            vm.series = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
