/*
 * Copyright (c) 2016 Huntsman Cancer Institute at the University of Utah, Confidential and Proprietary
 */
import { Component, OnInit } from "@angular/core";

import { ExperimentsService } from "./experiments.service";
import { Column, LabelCell } from "hci-ng-grid/index";

@Component({
    selector: "experiments",
    template: `
    <div>
        <hci-grid [title]="'Browse Experiements'"
                  [inputData]="experiments"
                  [columnDefinitions]="experimentsColumns"
                  [pageSize]="10">
        </hci-grid>
    </div>
    `
})
export class BrowseExperimentsComponent implements OnInit {

    experimentsColumns: Column[] = [
        new Column({ field: "expCodeApp", name: "Code", template: LabelCell }),
        new Column({ field: "expCreateDate", name: "Create Date", template: LabelCell }),
        new Column({ field: "expStatus", name: "Status", template: LabelCell }),
        new Column({ field: "expSeqRunType", name: "Run Type", template: LabelCell }),
        new Column({ field: "labFullName", name: "Lab Name", template: LabelCell })
    ];

    private experiments: Array<Object> = null;

    constructor(private experimentsService: ExperimentsService) {
        //
    }

    ngOnInit() {
        console.log("BrowseExperimentsComponent.ngOnInit");
        this.experimentsService.getExperiments().subscribe((response: Array<Object>) => {
            this.experiments = response;
        });
    }

}
