'use strict';

/**
 * @ngdoc service
 * @name usfTokenPluginTestApp.UsfTokenPluginTestService
 * @description
 * # UsfTokenPluginTestService
 * Factory in the usfTokenPluginTestApp.
 */
angular.module('usfTokenPluginTestApp')
  .factory('UsfTokenPluginTestService',['$resource','tokenAuth', function ($resource,tokenAuth) {
    
    // Service logic
    var tokenTestResource = $resource(tokenAuth.getResourceUrl('tokenTest'),{},{
        'getUsername': {
          method: 'GET', params: {},responseType: 'json', headers: { 'X-Auth-Token': tokenAuth.getStoredToken('tokenTest') }
        }
    });
    
    // Public API here
    return {
      getUsername: function () {
        return tokenTestResource.getUsername().$promise;
      }
    };
  }]);
