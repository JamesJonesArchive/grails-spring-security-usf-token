'use strict';

describe('Controller: GeteppaCtrl', function () {

  // load the controller's module
  beforeEach(module('usfTokenPluginTestApp'));

  var GeteppaCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    GeteppaCtrl = $controller('GeteppaCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
