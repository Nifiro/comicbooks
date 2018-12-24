(function() {
    'use strict';
    angular
        .module('comicbooksApp')
        .factory('ComicBookGenres', ComicBookGenres);

    ComicBookGenres.$inject = ['$resource'];

    function ComicBookGenres ($resource) {
        var resourceUrl =  'api/comic-book-genres/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
