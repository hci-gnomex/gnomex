/**
 * Created by u6008750 on 5/12/2017.
 */
import 'rxjs/add/operator/switchMap';
import {Component, OnInit} from "@angular/core";
import { ActivatedRoute, Params } from '@angular/router';
import {ExperimentsService} from "./experiments.service";

@Component({
    selector: "experiments",
    template: `
    <div>
        <hci-grid class="w-100"
                  [title]="'Experiement'"
                  [inputData]="experiment"
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

export class ViewExperimentComponent implements OnInit{

    private experiment: Array<Object> = null;

    constructor(private experimentsService: ExperimentsService,
                private route:ActivatedRoute
    ) {}

    ngOnInit(): void {
        this.route.params
            .switchMap((params: Params) => this.experimentsService.getExperiment(params['id']))
            .subscribe((response: Array<Object>) => {
                this.experiment = response;
            })
    }

    // ngOnInit(): void {
    //     this.experimentsService
    //         .getExperiment(this.route.snapshot.paramMap.get('id'))
    //         .subscribe((response: Array<Object>) => {
    //             this.experiment = response;
    //         })
    //
    // }
}

