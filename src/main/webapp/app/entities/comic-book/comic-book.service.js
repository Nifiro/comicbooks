(function() {
    'use strict';
    angular
        .module('comicbooksApp')
        .factory('ComicBook', ComicBook);

    ComicBook.$inject = ['$resource', 'DateUtils'];

    function ComicBook ($resource, DateUtils) {
        var resourceUrl =  'api/comic-books/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.serializedFrom = DateUtils.convertDateTimeFromServer(data.serializedFrom);
                        data.serializedTo = DateUtils.convertDateTimeFromServer(data.serializedTo);
                        data.createdDate = DateUtils.convertDateTimeFromServer(data.createdDate);
                        data.lastModifiedDate = DateUtils.convertDateTimeFromServer(data.lastModifiedDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
