(function (window, angular, undefined) {
  'use strict';
  
  /**
   * @ngdoc function
   * @name usfTokenPluginTestApp.controller:GeteppaCtrl
   * @description
   * # GeteppaCtrl
   * Controller of the usfTokenPluginTestApp
   */
  angular.module('usfTokenPluginTestApp')
    .controller('GeteppaCtrl',['$scope','$window','UsfTokenPluginTestService', function ($scope,$window,UsfTokenPluginTestService) {
      UsfTokenPluginTestService.getEppa().then(function(data) {
        //$window.alert(data);
        //$window.alert(JSON.stringify(data));
        $scope.eppa = data.eppa;
      });
    }]);
})(window, window.angular);