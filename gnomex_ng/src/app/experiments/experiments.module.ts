/*
 * Copyright (c) 2016 Huntsman Cancer Institute at the University of Utah, Confidential and Proprietary
 */
import {NgModule} from "@angular/core";
import {EXPERIMENTS_ROUTING} from "./experiments.routes";
import {CommonModule} from "@angular/common";
import {BrowseExperimentsComponent} from "./browse-experiments.component";

/**
 * @author mbyrne
 * @since 12/19/16
 */
@NgModule({
    imports: [EXPERIMENTS_ROUTING, CommonModule],
    declarations: [BrowseExperimentsComponent]
})
export class ExperimentsModule {
}
