import {Component, OnDestroy, OnInit} from "@angular/core";

import {ExperimentsService} from "./experiments.service";
import {Subscription} from "rxjs/Subscription";
import { NgModel } from "@angular/forms"
/**
 *
 * @author u0556399
 * @since 7/20/2017.
 */
@Component({
	selector: "ExperimentOrders",
	template: `
      <div class="background">
          <div class="filter-bar">
							<div class="title">Orders</div>
							<div class="radiogroup">
									<input id="newRadioButton" 				value="new" 				type="radio" name="radioFilter" (change)="onRadioButtonClick()" [(ngModel)]="radioString_workflowState">
									<label for="newRadioButton">New</label>
                  <input id="submittedRadioButton" 	value="submitted" 	type="radio" name="radioFilter" (change)="onRadioButtonClick()" [(ngModel)]="radioString_workflowState">
                  <label for="submittedRadioButton">Submitted</label>
                  <input id="processingRadioButton" value="processing" 	type="radio" name="radioFilter" (change)="onRadioButtonClick()" [(ngModel)]="radioString_workflowState">
                  <label for="processingRadioButton">Processing</label>
                  <input id="completeRadioButton" 	value="complete" 		type="radio" name="radioFilter" (change)="onRadioButtonClick()" [(ngModel)]="radioString_workflowState">
                  <label for="completeRadioButton">Complete</label>
                  <input id="FailedRadioButton" 		value="failed" 			type="radio" name="radioFilter" (change)="onRadioButtonClick()" [(ngModel)]="radioString_workflowState">
                  <label for="FailedRadioButton">Failed</label>
              </div>
							<span class="menu-spacer"></span>
							<div>
									<input id="redosCheckbox" name="redos" type="checkbox" (change)="rebuildFilter()" [(ngModel)]="redosEnabled"/>
									<label for="redosCheckbox">Redos</label>
							</div>
							<span class="menu-spacer"></span>
					</div>
					<div>
							<h5>radioString_workflowState value :</h5>
							<p>{{radioString_workflowState}}</p>
              <h5>redosEnabled value :</h5>
              <p>{{redosEnabled}}</p>
					</div>
          <div>
              <hci-grid class="w-100"
                        [inputData]="orders">
                  <column-def [name]="'#'" [field]="'requestNumber'"></column-def>
                  <column-def [name]="'Name'" [field]="'name'"></column-def>
                  <column-def [name]="'Action'" [field]="''"></column-def>
                  <column-def [name]="'Samples'" [field]="'numberOfSamples'"></column-def>
                  <column-def [name]="'Status'" [field]="'requestStatus'"></column-def>
                  <column-def [name]="'Type'" [field]="'codeRequestCategory'"></column-def>
                  <column-def [name]="'Submitted on'" [field]="'createDate'"></column-def>
                  <column-def [name]="'Container'" [field]="'container'"></column-def>
                  <column-def [name]="'Submitter'" [field]="'ownerName'"></column-def>
                  <column-def [name]="'Lab'" [field]="'labName'"></column-def>
              </hci-grid>
							<jqxGrid 
											[width]="900"
											[source]="dataAdapter"
											[pageable]="true"
											[autoheight]="true"
											[editable]="true"
											[sortable]="true"
											[columns]="columns"
											[selectionmode]='"multiplecellsadvanced"'
											#gridReference >
							</jqxGrid>
          </div>
      </div>
	`,
	styles: [`
			div.background {
					background-color:#EEEEEE;
			}
			div.filter-bar {
					float: left;
					width: 100%;
					vertical-align:center;
					border: 1px;
					border-color: grey;
					background-color: white;
			}
			div.filter-bar div {
					float: left;
          vertical-align:center;
          margin-top:0.5rem;
          margin-bottom:0.5rem;
			}
      div.filter-bar span {
          float: left;
          vertical-align:center;
          margin-top:0.5rem;
          margin-bottom:0.5rem;
      }
			div.filter-bar label {
					margin-bottom:0;				
			}
			div.title {
					font-size:large;
					width:16%;
			}
			div.radiogroup {
					
			}
			div.radiogroup input {
					margin-left: 1.0em;
			}
			div.radiogroup label {
					
			}
			span.menu-spacer {
					display: block;
					width:	2px;
					height: 1.5em;
					background: lightgrey;
					margin-left: 1.5em;
          margin-right: 1.5em;
			}
	`]
})
export class ExperimentOrdersComponent implements OnInit, OnDestroy {

	private orders: Array<any>;
	private columns: any[] = [
		{ text: "Checkbox" },
		{ text: "Experiment Name", datafield: "name" },
		{ text: "Request Number", datafield: "requestNumber" },
		{ text: "Request Status", datafield: "codeRequestStatus" }
	];
	private columnGroups: any[] = [
		{ text: "Group 1", name: "Request Stuff"}
	];

	private source = {
		datatype: "json",
		localdata: [
					{ name: "Hello", requestNumber: "World", codeRequestStatus: "Good to see you!" }
		],
		datafields: [
			{ name: "name", 							type: "string" },
			{ name: "requestNumber",	 		type: "string" },
			{ name: "codeRequestStatus", 	type: "string" }
		]
	};

	private dataAdapter: any = new jqx.dataAdapter(this.source);

	private subscription: Subscription;

	private radioString_workflowState: String = 'submitted';
	private redosEnabled: boolean = false;

	constructor(private experimentsService: ExperimentsService) { }

	onRadioButtonClick(): void {
	    // this.experimentsService.refreshExperimentOrders();

	    this.subscription.unsubscribe();
	}

	rebuildFilter(): void {

	}

	updateGridData(data: Array<any>) {
		this.source.localdata = data;
		this.dataAdapter = new jqx.dataAdapter(this.source);
	}

	ngOnInit(): void {
		this.subscription = this.experimentsService.getExperimentOrders(null)
				.subscribe((response) => {
			this.orders = response;
			this.updateGridData(response);
		});
	}

	ngOnDestroy(): void {
		this.subscription.unsubscribe();
	}
}