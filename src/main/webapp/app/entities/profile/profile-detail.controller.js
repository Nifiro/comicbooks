(function() {
    'use strict';

    angular
        .module('comicbooksApp')
        .controller('ProfileDetailController', ProfileDetailController);

    ProfileDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Profile', 'User'];

    function ProfileDetailController($scope, $rootScope, $stateParams, previousState, entity, Profile, User) {
        var vm = this;

        vm.profile = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('comicbooksApp:profileUpdate', function(event, result) {
            vm.profile = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
