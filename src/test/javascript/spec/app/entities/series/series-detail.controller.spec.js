'use strict';

describe('Controller Tests', function() {

    describe('Series Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockSeries, MockComicBook, MockChapter;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockSeries = jasmine.createSpy('MockSeries');
            MockComicBook = jasmine.createSpy('MockComicBook');
            MockChapter = jasmine.createSpy('MockChapter');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Series': MockSeries,
                'ComicBook': MockComicBook,
                'Chapter': MockChapter
            };
            createController = function() {
                $injector.get('$controller')("SeriesDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'comicbooksApp:seriesUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
