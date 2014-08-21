(function (window, angular, undefined) {
  'use strict';
  
  /**
   * @ngdoc overview
   * @name usfTokenPluginTestApp
   * @description
   * # usfTokenPluginTestApp
   *
   * Main module of the application.
   */
  angular
    .module('usfTokenPluginTestApp', [
      'ngAnimate',
      'ngCookies',
      'ngResource',
      'ngRoute',
      'ngSanitize',
      'ngTouch',
      'UsfCAStokenAuth'
    ])
    .constant('UsfCAStokenAuthConstant',{
      'applicationUniqueId': '1111222233334444555566667777',
      'applicationResources': {
          'getUsername': 'http://localhost:8080/UsfTokenPluginTest/services/tokenTest',
          'getAttributes': 'http://localhost:8080/UsfTokenPluginTest/services/tokenTest/attributes',
          'getEppa': 'http://localhost:8080/UsfTokenPluginTest/services/tokenTest/eppa'
      },
      'unauthorizedRoute': '/unauthorized'
    })
    .config(function ($routeProvider) {
      $routeProvider
        .when('/', {
          templateUrl: 'views/main.html',
          controller: 'MainCtrl'
        })
        .when('/about', {
          templateUrl: 'views/about.html',
          controller: 'AboutCtrl'
        })
        .when('/unauthorized', {
          templateUrl: 'views/unauthorized.html',
          controller: 'UnauthorizedCtrl'
        })
        .otherwise({
          redirectTo: '/'
        });
    });
})(window, window.angular);