'use strict';

describe('Controller: GetusernameCtrl', function () {

  // load the controller's module
  beforeEach(module('usfTokenPluginTestApp'));

  var GetusernameCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    GetusernameCtrl = $controller('GetusernameCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
