/*
 * Copyright (c) 2016 Huntsman Cancer Institute at the University of Utah, Confidential and Proprietary
 */
import { Router } from "@angular/router";
import { Http } from "@angular/http";
import { GnomexAppComponent } from "./gnomex-app.component";
import {UserService} from "@hci/user";
import {AuthenticationService} from "@hci/authentication";

/**
 * Unit tests for the CoreAppComponent.
 *
 * @author brandony <brandon.youkstetter@hci.utah.edu>
 * @since 7/19/16
 */
describe("SeedAppComponent Tests", () => {
  beforeEach(() => {
    this.gnomexAppComponent = new GnomexAppComponent(<UserService> null, <AuthenticationService> null, <Router> null, <Http> null);
  });

  it("Should have isCollapsed set to 'true' initially", () => {
    expect(this.gnomexAppComponent.isCollapsed).toBeTruthy();
  });

  it("Should have status.isopen set to 'false' initially", () => {
    expect(this.gnomexAppComponent.status.isopen).toBeFalsy();
  });
});
