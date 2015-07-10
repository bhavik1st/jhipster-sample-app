'use strict';

angular.module('jhipsterApp')
    .factory('SocialConnection', function Account($resource) {
        return $resource('api/account/connections/:providerId', {}, {
        	'get': { method: 'GET', isArray: true}
        });
    });
