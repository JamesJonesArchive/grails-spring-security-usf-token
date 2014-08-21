(function (window, angular, undefined) {
  'use strict';
  
  /**
   * @ngdoc function
   * @name usfTokenPluginTestApp.controller:GetusernameCtrl
   * @description
   * # GetusernameCtrl
   * Controller of the usfTokenPluginTestApp
   */
  angular.module('usfTokenPluginTestApp')
    .controller('GetusernameCtrl',['$scope','$window','UsfTokenPluginTestService', function ($scope,$window,UsfTokenPluginTestService) {
      UsfTokenPluginTestService.getUsername().then(function(data) {
        //$window.alert(data);
        //$window.alert(JSON.stringify(data));
        $scope.username = data.username;
      });
    }]);
})(window, window.angular);