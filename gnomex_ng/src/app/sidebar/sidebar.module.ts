/*
 * Copyright (c) 2016 Huntsman Cancer Institute at the University of Utah, Confidential and Proprietary
 */
import {NgModule} from "@angular/core";
import {SIDEBAR_ROUTING} from "./sidebar.routes";
import {CommonModule} from "@angular/common";
import {SidebarService} from "./sidebar.service";
import {SidebarContent1Component} from "./sidebar-content1.component";
import {SidebarContent2Component} from "./sidebar-content2.component";
import {SidebarContent3Component} from "./sidebar-content3.component";
import {SidebarDemoComponent} from "./sidebar-demo.component";
import {SidebarResponsive} from "./sidebar-responsive.directive";
import {SidebarToggle} from "./sidebar-toggle.directive";

/**
 * @author brandony <brandon.youkstetter@hci.utah.edu>
 * @since 8/27/16
 */
@NgModule({
  imports: [SIDEBAR_ROUTING, CommonModule],
  providers: [SidebarService],
  declarations: [
    SidebarContent1Component,
    SidebarContent2Component,
    SidebarContent3Component,
    SidebarDemoComponent,
    SidebarResponsive,
    SidebarToggle
  ]
})
export class SidebarModule {
}
