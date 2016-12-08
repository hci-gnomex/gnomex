/*
 * Copyright (c) 2016 Huntsman Cancer Institute at the University of Utah, Confidential and Proprietary
 */
import {Routes, RouterModule} from "@angular/router";
import {AboutComponent} from "./about.component";
import {RouteGuardService} from "@hci/user";

/**
 * A file defining and exporting the router configuration for the about module.
 *
 * @author brandony <brandon.youkstetter@hci.utah.edu>
 * @since 7/10/16
 */
const ROUTES: Routes = [
  {path: "", component: AboutComponent, canActivate: [RouteGuardService]}
];

export const ABOUT_ROUTES = RouterModule.forChild(ROUTES);
