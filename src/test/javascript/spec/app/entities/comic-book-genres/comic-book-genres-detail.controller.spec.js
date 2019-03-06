'use strict';

describe('Controller Tests', function() {

    describe('ComicBookGenres Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockComicBookGenres, MockComicBook, MockGenre;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockComicBookGenres = jasmine.createSpy('MockComicBookGenres');
            MockComicBook = jasmine.createSpy('MockComicBook');
            MockGenre = jasmine.createSpy('MockGenre');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'ComicBookGenres': MockComicBookGenres,
                'ComicBook': MockComicBook,
                'Genre': MockGenre
            };
            createController = function() {
                $injector.get('$controller')("ComicBookGenresDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'comicbooksApp:comicBookGenresUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
