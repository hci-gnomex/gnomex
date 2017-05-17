/*
 * Copyright (c) 2016 Huntsman Cancer Institute at the University of Utah, Confidential and Proprietary
 */
import {NgModule} from "@angular/core";
import {ABOUT_ROUTES} from "./about.routes";
import {CommonModule} from "@angular/common";
import {AboutComponent} from "./about.component";

/**
 * @author brandony <brandon.youkstetter@hci.utah.edu>
 * @since 8/28/16
 */
@NgModule({
  imports: [ABOUT_ROUTES, CommonModule],
  declarations: [AboutComponent]
})
export class AboutModule {
}
