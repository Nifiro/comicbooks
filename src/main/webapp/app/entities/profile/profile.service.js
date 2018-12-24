(function() {
    'use strict';
    angular
        .module('comicbooksApp')
        .factory('Profile', Profile);

    Profile.$inject = ['$resource'];

    function Profile ($resource) {
        var resourceUrl =  'api/profiles/:id';

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
