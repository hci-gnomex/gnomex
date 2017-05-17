/*
 * Copyright (c) 2016 Huntsman Cancer Institute at the University of Utah, Confidential and Proprietary
 */
import {NgModule} from "@angular/core";
import {HOME_ROUTING} from "./home.routes";
import {CommonModule} from "@angular/common";
import {HomeComponent} from "./home.component";

/**
 * @author brandony <brandon.youkstetter@hci.utah.edu>
 * @since 8/26/16
 */
@NgModule({
  imports: [HOME_ROUTING, CommonModule],
  declarations: [HomeComponent]
})
export class HomeModule {
}
