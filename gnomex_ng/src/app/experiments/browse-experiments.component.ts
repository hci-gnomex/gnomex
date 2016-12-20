/*
 * Copyright (c) 2016 Huntsman Cancer Institute at the University of Utah, Confidential and Proprietary
 */
import { Component, OnInit } from "@angular/core";

import { ExperimentsService } from "./experiments.service";

@Component({
    selector: "experiments",
    template: `
    <div>
      Browse Experiments
    </div>
    `,
    styles: [],
    providers: []
})
export class BrowseExperimentsComponent implements OnInit {

    constructor(private experimentsService: ExperimentsService) {
        //
    }

    ngOnInit() {
        console.log("BrowseExperimentsComponent.ngOnInit");
        this.experimentsService.getExperiments().subscribe(response => {
            console.log("subscribe response");
            console.log(response);
        });
    }

}
