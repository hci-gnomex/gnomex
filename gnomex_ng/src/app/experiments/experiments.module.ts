/*
 * Copyright (c) 2016 Huntsman Cancer Institute at the University of Utah, Confidential and Proprietary
 */
import {NgModule} from "@angular/core";
import {FormsModule} from "@angular/forms";
import {EXPERIMENTS_ROUTING} from "./experiments.routes";
import {CommonModule} from "@angular/common";
import {BrowseExperimentsComponent} from "./browse-experiments.component";
import {ViewExperimentComponent} from "./view-experiment.component";
import {BrowseExperimentsFilterComponent} from "./browse-experiments-filter.component";
import { TreeModule } from '../../modules/tree.module';
import { ExpanderModule } from '../../modules/expander.module';
import {ComboBoxModule} from '../../modules/combobox.module';
import {UtilModule} from "../util/util.module";


import { GridModule } from "hci-ng-grid/index";
import {CalendarModule} from "../../modules/calendar.module";

/**
 * @author mbyrne
 * @since 12/19/16
 */
@NgModule({
    imports: [ EXPERIMENTS_ROUTING, CommonModule, GridModule, TreeModule, ExpanderModule, ComboBoxModule, FormsModule, CalendarModule, UtilModule],
    declarations: [ BrowseExperimentsComponent, ViewExperimentComponent, BrowseExperimentsFilterComponent ]
})
export class ExperimentsModule {
}
