/*
 * Copyright (c) 2016 Huntsman Cancer Institute at the University of Utah, Confidential and Proprietary
 */
import {Routes, RouterModule} from "@angular/router";
import {HomeComponent} from "./home.component";
import {RouteGuardService} from "@hci/user";

/**
 * A file defining and exporting the router configuration for the home module.
 *
 * @author brandony <brandon.youkstetter@hci.utah.edu>
 * @since 7/10/16
 */
const ROUTES: Routes = [
  {path: "home", component: HomeComponent, canActivate: [RouteGuardService]}
];

export const HOME_ROUTING = RouterModule.forChild(ROUTES);
