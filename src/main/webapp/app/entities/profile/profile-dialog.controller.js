(function() {
    'use strict';

    angular
        .module('comicbooksApp')
        .controller('ProfileDialogController', ProfileDialogController);

    ProfileDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Profile', 'User'];

    function ProfileDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Profile, User) {
        var vm = this;

        vm.profile = entity;
        vm.clear = clear;
        vm.save = save;
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.profile.id !== null) {
                Profile.update(vm.profile, onSaveSuccess, onSaveError);
            } else {
                Profile.save(vm.profile, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('comicbooksApp:profileUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
