/*
 * Copyright (c) 2016 Huntsman Cancer Institute at the University of Utah, Confidential and Proprietary
 */
import {NgModule} from "@angular/core";
import {EXPERIMENTS_ROUTING} from "./experiments.routes";
import {CommonModule} from "@angular/common";
import {BrowseExperimentsComponent} from "./browse-experiments.component";
import {ViewExperimentComponent} from "./view-experiment.component";
import { TreeModule } from '../../modules/tree.module';
import { ExpanderModule } from '../../modules/expander.module';


import { GridModule } from "hci-ng-grid/index";
import { jqxGridComponent } from "../../assets/jqwidgets-ts/angular_jqxgrid";
import {ExperimentOrdersComponent} from "./experiment-orders.component";
import {FormsModule} from "@angular/forms";

/**
 * @author mbyrne
 * @since 12/19/16
 */
@NgModule({
    imports: [
        EXPERIMENTS_ROUTING,
        CommonModule,
        GridModule,
        TreeModule,
        ExpanderModule,
        FormsModule
    ],
    declarations: [
        BrowseExperimentsComponent,
        ViewExperimentComponent,
        ExperimentOrdersComponent,
        jqxGridComponent
    ]
})
export class ExperimentsModule {
}
