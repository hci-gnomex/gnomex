/**
 * Created by u6008750 on 5/12/2017.
 */
import 'rxjs/add/operator/switchMap';
import {Component, OnInit} from "@angular/core";
import { ActivatedRoute, Params } from '@angular/router';
import {ExperimentsService} from "./experiments.service";
import {Request} from "@angular/http";

@Component({
    selector: "experiment",
    template: `
        <div *ngIf="experiment" class="container-fluid" style="padding-top: 10px">
        
            <div class="row">
                    <table class="table">
                        <tbody>
                            <tr>
                                <td width="15%">Name</td>
                                <td width="30%">{{this.experiment.name}}</td>
                                <td width="15%">Number</td>
                                <td width="30%">{{experiment.number}}</td>
                            </tr>
                            <tr>
                                <td width="15%">Experiment</td>
                                <td width="30%">{{experiment.project}}</td>
                                <td width="15%">Email</td>
                                <td width="30%">{{experiment.requestor}}</td>
                            </tr>
                        </tbody>
                    </table>
            </div> <!-- end row -->
        
        
        </div> <!--  end container -->
    `
})

export class ViewExperimentComponent implements OnInit {

    experiment: any;


    constructor(private experimentsService: ExperimentsService,
                private route: ActivatedRoute) {
    }

    // ngOnInit(): void {
    //     this.experimentsService
    //         .getExperiment(this.route.snapshot.paramMap.get('id'))
    //         .subscribe((response:any) => {
    //             this.experiment = response.Request;
    //             console.log("in init "+this.experiment.number);
    //         })
    //
    // }

    // ngOnInit(): void {
    //     this.route.params
    //         .switchMap((params: Params) => this.experimentsService.getExperiment(params['id']))
    //         .subscribe((response: Object) => {
    //             this.experiment = response;
    //             console.log("in experiment");
    //         })
    // }
    //
    ngOnInit(): void {
        this.experimentsService
            .getExperiment(this.route.snapshot.paramMap.get('id'))
            .subscribe((response) => {
                this.experiment = response.Request;
                console.log("in init "+this.experiment.number);
            })

    }
}
