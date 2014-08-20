'use strict';

describe('Service: UsfTokenPluginTestService', function () {

  // load the service's module
  beforeEach(module('usfTokenPluginTestApp'));

  // instantiate service
  var UsfTokenPluginTestService;
  beforeEach(inject(function (_UsfTokenPluginTestService_) {
    UsfTokenPluginTestService = _UsfTokenPluginTestService_;
  }));

  it('should do something', function () {
    expect(!!UsfTokenPluginTestService).toBe(true);
  });

});
