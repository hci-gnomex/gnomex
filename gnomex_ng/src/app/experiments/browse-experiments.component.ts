/*
 * Copyright (c) 2016 Huntsman Cancer Institute at the University of Utah, Confidential and Proprietary
 */
import {Component, ElementRef, ViewChild, ViewEncapsulation} from "@angular/core";

import {ExperimentsService} from "./experiments.service";
import { jqxTreeComponent } from 'jqwidgets-framework';

@Component({
    selector: "experiments",
    templateUrl:'./experiments.component.html',
    styles: [`
        angularTree > div:first-child
        {
            height: 100%;
            border: none;
        }
    `],
    encapsulation: ViewEncapsulation.None

})
export class BrowseExperimentsComponent {
    @ViewChild('treeReference') tree: jqxTreeComponent;

    labs: any[];
    constructor(private experimentsService: ExperimentsService) {
        this.experimentsService.getExperiments().subscribe(response => {
            this.buildTree(response);
        })
        this.labs = [];

    }

    buildTree(response: any[]) {
        this.labs = [response];
        for( var l of this.labs) {
            l.icon = "assets/group.png"
            l.items = l.Project;
            for( var p of l.items) {
                p.icon = "assets/folder.png"
                p.items = [];
                if (!this.isArray(p.RequestCategory)) {
                    p.items = p.items.concat([p.RequestCategory.Request]);
                } else {
                    for (var r of p.RequestCategory) {
                        if( !this.isArray(r.Request)) {
                            p.items = p.items.concat([r.Request]);
                        } else {
                            p.items = p.items.concat(r.Request);
                        }
                    }
                }
            }
        }
    }

    isArray(what) {
        return Object.prototype.toString.call(what) === '[object Array]';
    }

    detailFn(): (keywords: string) => void {
        return (keywords) => {
            window.location.href = "http://localhost/gnomex/experiments/"+keywords;
        };
    }
}
