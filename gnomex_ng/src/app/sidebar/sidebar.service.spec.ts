/*
 * Copyright (c) 2016 Huntsman Cancer Institute at the University of Utah, Confidential and Proprietary
 */
import { SidebarService } from "./sidebar.service";

/**
 * Unit tests for the SidebarService.
 *
 * @author brandony <brandon.youkstetter@hci.utah.edu>
 * @since 7/18/16
 */
describe("SidebarService Tests", () => {
  let service: SidebarService = new SidebarService();

  it("Should change the currentSidebarState to '2' if currentManual and currentWindow are also equal to '2'.", () => {
    let currentSidebarStateValue: number = 0;

    service.setManual(2);
    service.setWindow(2);
    service.addSidebarStateObserver((sidebarStateValue) => {
      currentSidebarStateValue = sidebarStateValue;
    });

    service.setSidebarState();

    expect(currentSidebarStateValue).toEqual(2);
  });

  it("Should change the currentSidebarState to '1' if either currentManual and currentWindow are not equal to '2'.", () => {
    let currentSidebarStateValue: number = 0;

    service.addSidebarStateObserver((sidebarStateValue) => {
      currentSidebarStateValue = sidebarStateValue;
    });

    service.setManual(1);
    service.setWindow(2);
    service.setSidebarState();
    expect(currentSidebarStateValue).toEqual(1);

    service.setManual(2);
    service.setWindow(0);
    service.setSidebarState();
    expect(currentSidebarStateValue).toEqual(1);

    service.setManual(3);
    service.setWindow(4);
    service.setSidebarState();
    expect(currentSidebarStateValue).toEqual(1);
  });

  it("Should toggle internal manual state resulting in a change to currentSidebarState", () => {
    let currentSidebarStateValue: number = 0;

    service.addSidebarStateObserver((sidebarStateValue) => {
      currentSidebarStateValue = sidebarStateValue;
    });

    service.setManual(2);
    service.setWindow(2);
    service.setSidebarState();
    expect(currentSidebarStateValue).toEqual(2);

    service.negateManual();
    expect(currentSidebarStateValue).toEqual(1);

    service.negateManual();
    expect(currentSidebarStateValue).toEqual(2);
  });
});
