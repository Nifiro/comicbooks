(function () {
    'use strict';

    angular
        .module('comicbooksApp')
        .controller('AuthorController', AuthorController);

    AuthorController.$inject = ['$http', '$mdDialog', 'Author', 'ParseLinks', 'AlertService', 'paginationConstants'];

    function AuthorController($http, $mdDialog, Author, ParseLinks, AlertService, paginationConstants) {

        var vm = this;

        vm.authors = [];
        vm.loadPage = loadPage;
        vm.loadAvatar = loadAvatar;
        vm.loadAll = loadAll;
        vm.showAuthorDialog = showAuthorDialog;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.page = 0;
        vm.links = {
            last: 0
        };
        vm.predicate = 'id';
        vm.reset = reset;
        vm.reverse = true;
        vm.maxPage = Number.POSITIVE_INFINITY;
        vm.busy = false;

        function loadAll() {
            if (vm.busy) return;
            vm.busy = true;

            Author.query({
                page: vm.page,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);

            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.maxPage = vm.totalItems / vm.itemsPerPage;
                for (var i = 0; i < data.length; i++) {
                    vm.authors.push(data[i]);
                }
                vm.page++;
                vm.busy = false;
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function reset() {
            vm.page = 0;
            vm.authors = [];
            loadAll();
        }

        function loadPage(page) {
            vm.page = page;
            loadAll();
        }

        function showAuthorDialog(event) {
            $mdDialog.show({
                controller: AuthorDialogController,
                templateUrl: 'app/entities/author/author-dialog.html',
                parent: angular.element(document.body),
                targetEvent: event
            });
        }

        function loadAvatar(id, author) {
            if (author.avatarPath !== null) {
                $http({
                    method: 'GET',
                    url: '/api/authors/' + id + '/avatar',
                    responseType: 'blob'
                }).success(function (data, status, headers) {
                    var contentType = headers('Content-Type');
                    var file = new Blob([data], {type: contentType});
                    author.image = URL.createObjectURL(file);
                });
            }
        }

        AuthorDialogController.$inject = ['$scope', '$http', '$mdDialog', 'Author', 'Upload'];

        function AuthorDialogController($scope, $http, $mdDialog, Author, Upload) {
            $scope.isSaving = false;
            $scope.author = {
                firstName: null,
                lastName: null,
                avatarPath: null,
                id: null
            };

            $scope.cancel = function () {
                $mdDialog.cancel();
            };

            $scope.save = function () {
                $scope.isSaving = true;
                if ($scope.author.id !== null) {
                    Author.update($scope.author, onSaveSuccess);
                } else {
                    Author.save($scope.author, onSaveSuccess);
                }
            };

            function onSaveSuccess(result) {
                $scope.author.id = result.id;
                $scope.isSaving = false;
                if($scope.author.avatar) {
                    Upload.upload({
                        url: '/api/authors/upload',
                        data: {
                            file: $scope.author.avatar,
                            id: $scope.author.id
                        }
                    }).then(function (response) {
                        $http({
                            method: 'GET',
                            url: '/api/authors/' + $scope.author.id + '/avatar',
                            responseType: 'blob'
                        }).success(function (data, status, headers) {
                            var contentType = headers('Content-Type');
                            var file = new Blob([data], {type: contentType});
                            $scope.author.image = URL.createObjectURL(file);
                            vm.authors.push($scope.author);
                            $scope.cancel();
                        });
                    });
                }
                else {
                    vm.authors.push($scope.author);
                    $scope.cancel();
                }
            }
        }
    }
})();
