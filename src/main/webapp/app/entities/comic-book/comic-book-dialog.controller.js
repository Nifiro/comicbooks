(function () {
    'use strict';

    angular
        .module('comicbooksApp')
        .controller('ComicBookDialogController', ComicBookDialogController);

    ComicBookDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$mdToast', 'DataUtils', 'entity',
        'Upload', 'ComicBook', 'Author', 'Genre', 'ComicBookGenres'];

    function ComicBookDialogController($timeout, $scope, $stateParams, $mdToast, DataUtils, entity,
                                       Upload, ComicBook, Author, Genre, ComicBookGenres) {
        var vm = this;

        vm.comicBook = entity;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.authors = Author.query();
        vm.genres = Genre.query();
        vm.selectedAuthor = null;
        vm.selectedStatus = null;
        vm.selectedGenres = null;
        vm.coverFile = null;
        vm.imageFile = null;

        vm.statuses = [
            {
                name: 'ONGOING',
                value: 'Продолжающийся'
            },
            {
                name: 'COMPLETED',
                value: 'Завершенный'
            }
        ];

        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });

        function save() {
            vm.isSaving = true;
            vm.comicBook.authorId = vm.selectedAuthor.id;
            if (vm.selectedStatus != null)
                vm.comicBook.status = vm.selectedStatus.name;
            if (vm.comicBook.id !== null) {
                ComicBook.update(vm.comicBook, onSaveSuccess, onSaveError);
            } else {
                ComicBook.save(vm.comicBook, onSaveSuccess, onSaveError);
            }
        }

        function showAlert(message) {
            var toast = $mdToast.simple()
                .textContent(message)
                .parent(angular.element(document.getElementById('toastParent')))
                .position('bottom right')
                .capsule(true);
            $mdToast.show(toast);
        }

        function onSaveSuccess(result) {
            vm.isSaving = false;
            vm.comicBook.id = result.id;

            vm.selectedGenres.forEach(function (genre) {
                ComicBookGenres.save({
                    comicBookId: vm.comicBook.id,
                    genreId: genre.id
                });
            });

            Upload.upload({
                url: '/api/comic-books/upload',
                data: {
                    file: vm.coverFile,
                    id: vm.comicBook.id,
                    type: 'cover'
                }
            }).then(function (response) {
                console.log('after upload cover');
                console.log(response);
                Upload.upload({
                    url: '/api/comic-books/upload',
                    data: {
                        file: vm.imageFile,
                        id: vm.comicBook.id,
                        type: 'background'
                    }
                }).then(function (response) {
                    vm.comicBook = {
                        title: vm.comicBook.title,
                        chapters: vm.comicBook.chapters,
                        description: vm.comicBook.description,
                        publisher: vm.comicBook.publisher,
                        serializedFrom: vm.comicBook.serializedFrom,
                        serializedTo: vm.comicBook.serializedTo,
                        imagePath: vm.comicBook.imagePath,
                        coverPath: vm.comicBook.coverPath,
                        status: vm.comicBook.status,
                        id: null,
                        authorId: null
                    };

                    showAlert('Комикс "' + vm.comicBook.title + '" успешно добавлен');
                });
            });
        }

        function onSaveError() {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.serializedFrom = false;
        vm.datePickerOpenStatus.serializedTo = false;
        vm.datePickerOpenStatus.createdDate = false;
        vm.datePickerOpenStatus.lastModifiedDate = false;

        function openCalendar(date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
