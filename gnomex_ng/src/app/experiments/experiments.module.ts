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
import {UtilModule} from "../util/util.module";

import { ComboBoxModule } from "../../modules/combobox.module";

import { jqxGridComponent } from "../../assets/jqwidgets-ts/angular_jqxgrid";
import { ExperimentOrdersComponent } from "./orders/experiment-orders.component";
import {FormsModule} from "@angular/forms";
import {ServicesModule} from "../services/services.module";

/**
 * @author mbyrne
 * @since 12/19/16
 */
@NgModule({
    imports: [
        EXPERIMENTS_ROUTING,
        ComboBoxModule,
        CommonModule,
        TreeModule,
        ExpanderModule,
        FormsModule,
        UtilModule,
        ServicesModule
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
