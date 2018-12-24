(function() {
    'use strict';

    angular
        .module('comicbooksApp')
        .controller('ChapterDetailController', ChapterDetailController);

    ChapterDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Chapter'];

    function ChapterDetailController($scope, $rootScope, $stateParams, previousState, entity, Chapter) {
        var vm = this;

        vm.chapter = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('comicbooksApp:chapterUpdate', function(event, result) {
            vm.chapter = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
