/*
 * Copyright (c) 2016 Huntsman Cancer Institute at the University of Utah, Confidential and Proprietary
 */
import {NgModule} from "@angular/core";
import {EXPERIMENTS_ROUTING} from "./experiments.routes";
import {CommonModule} from "@angular/common";
import {BrowseExperimentsComponent} from "./browse-experiments.component";
import {ViewExperimentComponent} from "./view-experiment.component";

import { GridModule } from "hci-ng-grid/index";

/**
 * @author mbyrne
 * @since 12/19/16
 */
@NgModule({
    imports: [ EXPERIMENTS_ROUTING, CommonModule, GridModule ],
    declarations: [ BrowseExperimentsComponent, ViewExperimentComponent ]
})
export class ExperimentsModule {
}
