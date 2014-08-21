(function (window, angular, undefined) {
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
      var tokenResources = {
        getUsernameResource: $resource(tokenAuth.getResourceUrl('getUsername'),{},{
            'getUsername': {
              method: 'GET', params: {},responseType: 'json', headers: { 'X-Auth-Token': tokenAuth.getStoredToken('getUsername') }
            }
        }),
        getAttributesResource: $resource(tokenAuth.getResourceUrl('getAttributes'),{},{
            'getAttributes': {
              method: 'GET', params: {},responseType: 'json', headers: { 'X-Auth-Token': tokenAuth.getStoredToken('getAttributes') }
            }
        }),
        getEppaResource: $resource(tokenAuth.getResourceUrl('getEppa'),{},{
            'getEppa': {
              method: 'GET', params: {},responseType: 'json', headers: { 'X-Auth-Token': tokenAuth.getStoredToken('getEppa') }
            }
        })
      };
      
      // Public API here
      return {
        getUsername: function () {
          return tokenResources.getUsernameResource.getUsername().$promise;
        },
        getAttributes: function () {
          return tokenResources.getAttributesResource.getAttributes().$promise;
        },
        getEppa: function () {
          return tokenResources.getEppaResource.getEppa().$promise;
        }
      };
    }]);
})(window, window.angular);