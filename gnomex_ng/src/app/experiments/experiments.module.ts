/*
 * Copyright (c) 2016 Huntsman Cancer Institute at the University of Utah, Confidential and Proprietary
 */
import {NgModule} from "@angular/core";
import {EXPERIMENTS_ROUTING} from "./experiments.routes";
import {CommonModule} from "@angular/common";
import {BrowseExperimentsComponent} from "./browse-experiments.component";
import {ViewExperimentComponent} from "./view-experiment.component";
import { LabUsersComponent} from "./getLabUsers.component"
import { jqxTreeComponent } from "../../assets/jqwidgets-ts/angular_jqxtree";
import { jqxExpanderComponent } from "../../assets/jqwidgets-ts/angular_jqxexpander";

import { jqxWindowComponent } from "../../assets/jqwidgets-ts/angular_jqxwindow";
import { jqxButtonComponent } from "../../assets/jqwidgets-ts/angular_jqxbuttons";
import { jqxComboBoxComponent } from "../../assets/jqwidgets-ts/angular_jqxcombobox";
import { jqxNotificationComponent } from "../../assets/jqwidgets-ts/angular_jqxnotification";
import { jqxCheckBoxComponent } from "../../assets/jqwidgets-ts/angular_jqxcheckbox";
import { jqxInputComponent } from "../../assets/jqwidgets-ts/angular_jqxinput";
import { jqxToggleButtonComponent } from "../../assets/jqwidgets-ts/angular_jqxtogglebutton";
import { jqxTextAreaComponent } from "../../assets/jqwidgets-ts/angular_jqxtextarea";
import { jqxLoaderComponent } from "../../assets/jqwidgets-ts/angular_jqxloader";


import { GridModule } from "hci-ng-grid/index";
import {FormsModule} from "@angular/forms";

/**
 * @author mbyrne
 * @since 12/19/16
 */
@NgModule({
    imports: [ EXPERIMENTS_ROUTING, CommonModule, GridModule, FormsModule],
    declarations: [ BrowseExperimentsComponent,
                    ViewExperimentComponent,
                    LabUsersComponent,
                    jqxTreeComponent,
                    jqxExpanderComponent,
                    jqxWindowComponent,
                    jqxButtonComponent,
                    jqxComboBoxComponent,
                    jqxNotificationComponent,
                    jqxCheckBoxComponent,
                    jqxInputComponent,
                    jqxToggleButtonComponent,
                    jqxTextAreaComponent,
                    jqxLoaderComponent
                    ]
})
export class ExperimentsModule {
}
