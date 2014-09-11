(function (window, angular, undefined) {
  'use strict';
  
  /**
   * @ngdoc function
   * @name usfTokenPluginTestApp.controller:AboutCtrl
   * @description
   * # AboutCtrl
   * Controller of the usfTokenPluginTestApp
   */
  angular.module('usfTokenPluginTestApp')
    .controller('AboutCtrl', function ($scope) {
      $scope.awesomeThings = [
        'HTML5 Boilerplate',
        'AngularJS',
        'Karma'
      ];
    });
})(window, window.angular);