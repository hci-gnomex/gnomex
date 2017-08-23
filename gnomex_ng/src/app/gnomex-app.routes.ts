/*
 * Copyright (c) 2016 Huntsman Cancer Institute at the University of Utah, Confidential and Proprietary
 */
import {Routes, RouterModule} from "@angular/router";

import {DirectLoginComponent, RouteGuardService} from "@hci/authentication";

import {HomeComponent} from "./home/home.component";

/**
 * A file defining and exporting the router configuration for the seed application.
 *
 * @since 1.0.0
 */
export const ROUTES: Routes = [
  {path: "", redirectTo: "home", pathMatch: "full"},
  {path: "authenticate", component: DirectLoginComponent},
  {path: "home", component: HomeComponent, canActivate: [RouteGuardService]},
  {
    path: "about",
    loadChildren: () => new Promise(resolve => {
      (require as any).ensure([], (require: any) => {
        resolve(require("./about/about.module").AboutModule);
      });
    })
  },
  {
    path: "sidebar",
    loadChildren: () => new Promise(resolve => {
      (require as any).ensure([], (require: any) => {
        resolve(require("./sidebar/sidebar.module").SidebarModule);
      });
    })
  }
];

export const APP_ROUTING = RouterModule.forRoot(ROUTES);
