import {Component, OnDestroy, OnInit} from "@angular/core";

import {ExperimentsService} from "./experiments.service";
import {Subscription} from "rxjs/Subscription";
import {NgModel} from "@angular/forms"
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
                  <input id="newRadioButton" value="new" type="radio" name="radioFilter"
                         (change)="onRadioButtonClick()" [(ngModel)]="radioString_workflowState">
                  <label for="newRadioButton">New</label>
                  <input id="submittedRadioButton" value="submitted" type="radio" name="radioFilter"
                         (change)="onRadioButtonClick()" [(ngModel)]="radioString_workflowState">
                  <label for="submittedRadioButton">Submitted</label>
                  <input id="processingRadioButton" value="processing" type="radio"
                         name="radioFilter" (change)="onRadioButtonClick()"
                         [(ngModel)]="radioString_workflowState">
                  <label for="processingRadioButton">Processing</label>
                  <input id="completeRadioButton" value="complete" type="radio" name="radioFilter"
                         (change)="onRadioButtonClick()" [(ngModel)]="radioString_workflowState">
                  <label for="completeRadioButton">Complete</label>
                  <input id="FailedRadioButton" value="failed" type="radio" name="radioFilter"
                         (change)="onRadioButtonClick()" [(ngModel)]="radioString_workflowState">
                  <label for="FailedRadioButton">Failed</label>
              </div>
              <span class="menu-spacer"></span>
              <div>
                  <input id="redosCheckbox" name="redos" type="checkbox" (change)="rebuildFilter()"
                         [(ngModel)]="redosEnabled"/>
                  <label for="redosCheckbox">Redos</label>
              </div>
              <span class="menu-spacer"></span>
          </div>
          <!--<div>-->
          <!--<h5>radioString_workflowState value :</h5>-->
          <!--<p>{{radioString_workflowState}}</p>-->
          <!--<h5>redosEnabled value :</h5>-->
          <!--<p>{{redosEnabled}}</p>-->
          <!--</div>-->
          <div class="grid-container">
							<jqxGrid 
											[width]="'100%'"
											[height]="'100%'"
											[source]="dataAdapter"
											[pageable]="false"
											[autoheight]="false"
											[editable]="true"
											[sortable]="true"
											[columns]="columns"
											[selectionmode]='"multiplecellsadvanced"'
											#gridReference>
							</jqxGrid>
          </div>
          <div class="status-change-control">
							<div class="label">{{numberSelected}} selected</div>
							<div>
									<jqxDropDownList></jqxDropDownList>
              </div>
							<div>
									<button>
											<a>Go</a>
									</button>
              </div>
							<div>
									<button>
											<a>Delete</a>
									</button>
              </div>
          		<div>
									<button>
											<a>Email</a>
									</button>
              </div>
					</div>
      </div>
	`,
	styles: [`
      div.background {
          width: 100%;
          height: 100%;
          background-color: #EEEEEE;
          padding: 0.3em;
          border-radius: 0.3em;
          border: 1px solid darkgrey;
					display: flex;
					flex-direction: column;
      }

      div.filter-bar {
          width: 100%;
          overflow: hidden;
          vertical-align: center;
          border: 1px solid darkgrey;
          background-color: white;
          padding-left: 0.8em;
          margin-bottom: 0.3em;
      }

      div.filter-bar div {
          float: left;
          vertical-align: center;
          margin-top: 0.5rem;
          margin-bottom: 0.5rem;
      }

      div.filter-bar span {
          float: left;
          vertical-align: center;
          margin-top: 0.5rem;
          margin-bottom: 0.5rem;
      }

      div.filter-bar label {
          margin-bottom: 0;
      }
			
      div.status-change-control {
          width: 100%;
          overflow: hidden;
          vertical-align: center;
          border: 1px solid darkgrey;
          background-color: white;
          padding-left: 0.8em;
          margin-top: 0.3em;
					bottom: 0;
      }
			
      div.status-change-control div {
          float: left;
          vertical-align: center;
					margin-left: 1.0rem;
          margin-top: 0.5rem;
          margin-bottom: 0.5rem;
      }
			
      div.status-change-control span {
          float: left;
          vertical-align: center;
          margin-top: 0.5rem;
          margin-bottom: 0.5rem;
      }

      div.status-change-control label {
          margin-bottom: 0;
      }
      
			div.title {
          font-size: large;
          width: 16%;
      }

      div.radiogroup input {
          margin-left: 1.0em;
      }

      span.menu-spacer {
          display: block;
          width: 2px;
          height: 1.5em;
          background: lightgrey;
          margin-left: 1.5em;
          margin-right: 1.5em;
      }

      div.grid-container {
          width: 100%;
          flex:1;
          border: 1px solid darkgrey;
          background-color: white;
          padding: 0.3em;
      }
			
			button a {
					font-size: small;
			}
	`]
})
export class ExperimentOrdersComponent implements OnInit, OnDestroy {

	private orders: Array<any>;
	private columns: any[] = [
		{text: "Checkbox"},
		{text: "Experiment Name", datafield: "name"},
		{text: "Request Number", datafield: "requestNumber"},
		{text: "Request Status", datafield: "codeRequestStatus"}
	];
	private columnGroups: any[] = [
		{text: "Group 1", name: "Request Stuff"}
	];

	private source = {
		datatype: "json",
		localdata: [
			{name: "Hello", requestNumber: "World", codeRequestStatus: "Good to see you!"}
		],
		datafields: [
			{name: "name", type: "string"},
			{name: "requestNumber", type: "string"},
			{name: "codeRequestStatus", type: "string"}
		]
	};

	private dataAdapter: any = new jqx.dataAdapter(this.source);

	private subscription: Subscription;

	private radioString_workflowState: String = 'submitted';
	private redosEnabled: boolean = false;

	private numberSelected: number = 0;


	constructor(private experimentsService: ExperimentsService) {
	}

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