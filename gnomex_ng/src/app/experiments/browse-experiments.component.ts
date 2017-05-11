/*
 * Copyright (c) 2016 Huntsman Cancer Institute at the University of Utah, Confidential and Proprietary
 */
import {Component} from "@angular/core";

import {ExperimentsService} from "./experiments.service";

@Component({
    selector: "experiments",
    template: `
    <div>
        <hci-grid class="w-100"
                  [title]="'Browse Experiements'"
                  [inputData]="experiments"
                  [pageSize]="10">
            <column-def [name]="'Code'" [field]="'expCodeApp'"></column-def>
            <column-def [name]="'Create Date'" [field]="'expCreateDate'"></column-def>
            <column-def [name]="'Status'" [field]="'expStatus'"></column-def>
            <column-def [name]="'Run Type'" [field]="'expSeqRunType'"></column-def>
            <column-def [name]="'Lab Name'" [field]="'labFullName'"></column-def>
        </hci-grid>
    </div>
    `
})
export class BrowseExperimentsComponent {

    private experiments: Array<Object> = null;

    constructor(private experimentsService: ExperimentsService) {
        this.experimentsService.getExperiments().subscribe((response: Array<Object>) => {
            this.experiments = response;
        });
    }

}
