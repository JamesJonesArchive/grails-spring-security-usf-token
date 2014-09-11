'use strict';

describe('Controller: GetattributesCtrl', function () {

  // load the controller's module
  beforeEach(module('usfTokenPluginTestApp'));

  var GetattributesCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    GetattributesCtrl = $controller('GetattributesCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
