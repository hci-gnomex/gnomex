/*
 * Copyright (c) 2016 Huntsman Cancer Institute at the University of Utah, Confidential and Proprietary
 */
import {Routes, RouterModule} from "@angular/router";
import {LoginComponent} from "@hci/user";

/**
 * A file defining and exporting the router configuration for the seed application.
 *
 * @author brandony <brandon.youkstetter@hci.utah.edu>
 * @since 7/10/16
 */
export const ROUTES: Routes = [
  {path: "", redirectTo: "home", pathMatch: "full"},
  {path: "login", component: LoginComponent},
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
