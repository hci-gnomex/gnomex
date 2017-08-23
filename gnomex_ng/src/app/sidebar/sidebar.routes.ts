import {Routes, RouterModule} from "@angular/router";

import { SidebarDemoComponent } from "./sidebar-demo.component";
import { SidebarContent1Component } from "./sidebar-content1.component";
import { SidebarContent2Component } from "./sidebar-content2.component";
import { SidebarContent3Component } from "./sidebar-content3.component";
import {RouteGuardService} from "@hci/authentication";

const ROUTES: Routes = [{
  path: "", component: SidebarDemoComponent, canActivate: [RouteGuardService],
    children: [
      {path: "", redirectTo: "content1", pathMatch: "full"},
      {path: "content1", component: SidebarContent1Component},
      {path: "content2", component: SidebarContent2Component},
      {path: "content3", component: SidebarContent3Component}
    ]
}];

export const SIDEBAR_ROUTING = RouterModule.forChild(ROUTES);

