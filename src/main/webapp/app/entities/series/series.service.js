(function() {
    'use strict';
    angular
        .module('comicbooksApp')
        .factory('Series', Series);

    Series.$inject = ['$resource'];

    function Series ($resource) {
        var resourceUrl =  'api/series/:id';

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
