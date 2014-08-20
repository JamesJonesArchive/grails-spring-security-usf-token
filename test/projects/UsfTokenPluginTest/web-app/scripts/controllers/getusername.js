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
      //voterContactsInit.getStorage().counties = data.counties;
      $scope.username = data.username;
      //$window.alert(JSON.stringify($scope.counties));
    });
    $scope.awesomeThings = [
      'HTML5 Boilerplate',
      'AngularJS',
      'Karma'
    ];
  }]);
