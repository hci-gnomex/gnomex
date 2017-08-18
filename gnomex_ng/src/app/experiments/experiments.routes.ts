/*
 * Copyright (c) 2016 Huntsman Cancer Institute at the University of Utah, Confidential and Proprietary
 */
import { Routes, RouterModule } from "@angular/router";
import { BrowseExperimentsComponent } from "./browse-experiments.component";
import { ViewExperimentComponent } from "./view-experiment.component";
import {LabUsersComponent} from "./getLabUsers.component";

/**
 * A file defining and exporting the router configuration for the experiments module.
 *
 * @author mbyrne
 * @since 12/19/16
 */
const ROUTES: Routes = [
    { path: "experiments", component: BrowseExperimentsComponent },
    { path: "experiments/:id", component:ViewExperimentComponent}
];

export const EXPERIMENTS_ROUTING = RouterModule.forChild(ROUTES);
