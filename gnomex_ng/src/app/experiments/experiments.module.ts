/*
 * Copyright (c) 2016 Huntsman Cancer Institute at the University of Utah, Confidential and Proprietary
 */
import {NgModule} from "@angular/core";
import {EXPERIMENTS_ROUTING} from "./experiments.routes";
import {CommonModule} from "@angular/common";
import {BrowseExperimentsComponent} from "./browse-experiments.component";
import {ViewExperimentComponent} from "./view-experiment.component";
import { WindowModule } from "../../modules/window.module"
import { GridModule } from "hci-ng-grid/index";
import {FormsModule} from "@angular/forms";
import { TreeModule } from 'angular-tree-component';
import { ButtonModule } from "../../modules/button.module";
import { NotificationModule} from "../../modules/notification.module";
import { CheckBoxModule} from "../../modules/checkbox.module";
import { InputModule } from "../../modules/input.module";
import { ToggleButtonModule } from "../../modules/togglebutton.module";
import { TextAreaModule } from "../../modules/textarea.module";
import { LoaderModule } from "../../modules/loader.module";
import { ComboBoxModule } from "../../modules/combobox.module";

/**
 * @author mbyrne
 * @since 12/19/16
 */
@NgModule({
    imports: [ EXPERIMENTS_ROUTING, CommonModule, GridModule, FormsModule, TreeModule, WindowModule, ButtonModule,
                NotificationModule, CheckBoxModule, InputModule, ToggleButtonModule, TextAreaModule, LoaderModule,
                ComboBoxModule],
    declarations: [ BrowseExperimentsComponent,
                    ViewExperimentComponent,
                    ]
})
export class ExperimentsModule {
}
