/*
 * Copyright (c) 2016 Huntsman Cancer Institute at the University of Utah, Confidential and Proprietary
 */
import {NgModule} from "@angular/core";
import {EXPERIMENTS_ROUTING} from "./experiments.routes";
import {CommonModule} from "@angular/common";
import {FormsModule,ReactiveFormsModule} from "@angular/forms"
import {BrowseExperimentsComponent} from "./browse-experiments.component";
import {ViewExperimentComponent} from "./view-experiment.component";
import { TreeModule,
        ExpanderModule,
        RichEditorModule,
        DropDownModule
} from '../../modules/index';
import {UtilModule} from "../util/util.module";
import {TestComponent,DescriptionTab,PrepTab,ExperimentDetail} from './experiment-detail/index'


import { GridModule } from "hci-ng-grid/index";
import {ServicesModule} from "../services/services.module";

/**
 * @author mbyrne
 * @since 12/19/16
 */


export const componentFactories = [TestComponent,DescriptionTab,PrepTab]; // need add components that will be tabs here
                                                                          // could be put in gnomexFlex as w
@NgModule({
    imports: [ EXPERIMENTS_ROUTING,
               CommonModule,
               ReactiveFormsModule,
               FormsModule,
               GridModule,
               TreeModule,
               ExpanderModule,
               UtilModule,
               ServicesModule,
               RichEditorModule,
               DropDownModule
            ],
    declarations: [ BrowseExperimentsComponent,
                    ViewExperimentComponent,TestComponent,DescriptionTab,ExperimentDetail,PrepTab ],
    entryComponents:[...componentFactories]
})
export class ExperimentsModule {
}
