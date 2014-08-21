(function (window, angular, undefined) {
  'use strict';
  
  /**
   * @ngdoc function
   * @name usfTokenPluginTestApp.controller:GetattributesCtrl
   * @description
   * # GetattributesCtrl
   * Controller of the usfTokenPluginTestApp
   */
  angular.module('usfTokenPluginTestApp')
    .controller('GetattributesCtrl',['$scope','$window','UsfTokenPluginTestService', function ($scope,$window,UsfTokenPluginTestService) {
      UsfTokenPluginTestService.getAttributes().then(function(data) {
        //$window.alert(data);
        //$window.alert(JSON.stringify(data));
        $scope.attributes = data.attributes;
      });
    }]);
})(window, window.angular);