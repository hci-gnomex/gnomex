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
							<table>
									<tr>
											<td class="title">Orders</td>
                      <td>
                          <div class="vertical-aligned radioGroup">
															<div>
																	<input id="newRadioButton" value="new" type="radio" name="radioFilter"
																				 (change)="onRadioButtonClick()" [(ngModel)]="radioString_workflowState">
																	<label for="newRadioButton">New</label>
															</div>
															<div>
																	<input id="submittedRadioButton" value="submitted" type="radio" name="radioFilter"
																				 (change)="onRadioButtonClick()" [(ngModel)]="radioString_workflowState">
																	<label for="submittedRadioButton">Submitted</label>
															</div>
															<div>
																	<input id="processingRadioButton" value="processing" type="radio"
																				 name="radioFilter" (change)="onRadioButtonClick()"
																				 [(ngModel)]="radioString_workflowState">
																	<label for="processingRadioButton">Processing</label>
															</div>
															<div>
																	<input id="completeRadioButton" value="complete" type="radio" name="radioFilter"
																				 (change)="onRadioButtonClick()" [(ngModel)]="radioString_workflowState">
																	<label for="completeRadioButton">Complete</label>
															</div>
															<div>
																	<input id="FailedRadioButton" value="failed" type="radio" name="radioFilter"
																				 (change)="onRadioButtonClick()" [(ngModel)]="radioString_workflowState">
																	<label for="FailedRadioButton">Failed</label>
															</div>
                          </div>
                      </td>
                      <td>
													<span class="menu-spacer"></span>
                      </td>
                      <td>
													<div class="vertical-aligned">
															<input id="redosCheckbox" name="redos" type="checkbox" (change)="rebuildFilter()"
																		 [(ngModel)]="redosEnabled"/>
															<label style="margin-left: 0.3rem" for="redosCheckbox">Redos</label>
													</div>
                      </td>
                      <td>
													<span class="menu-spacer"></span>
                      </td>
                  </tr>
              </table>
          </div>
          <!--<div>-->
          <!--<h5>radioString_workflowState value :</h5>-->
          <!--<p>{{radioString_workflowState}}</p>-->
          <!--<h5>redosEnabled value :</h5>-->
          <!--<p>{{redosEnabled}}</p>-->
          <!--</div>-->
          <div class="lower-panel">
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
							
							<table class="status-change-control">
									<tr style="width: 100%">
											<td style="vertical-align: middle">
													<table>
															<tr>
																	<td>
                                      <div class="title">{{numberSelected}} selected</div>
																	</td>
                                  <td>
                                      <jqxDropDownList></jqxDropDownList>
                                  </td>
                                  <td>
                                      <button>
                                          <a>Go</a>
                                      </button>
                                  </td>
                                  <td>
                                      <button>
                                          <a>Delete</a>
                                      </button>
                                  </td>
                                  <td>
                                      <button>
                                          <a>Email</a>
                                      </button>
                                  </td>
															</tr>
													</table>
											</td>
                      <td style="text-align: right">
                          <div>({{rows}} orders)</div>
											</td>
									</tr>
							</table>
							
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
					position: relative;
					display: flex;
					flex-direction: column;
      }

      div.filter-bar {
          width: 100%;
          vertical-align: center;
          border: 1px solid darkgrey;
          background-color: white;
					padding: 0.3em 0.8em;
          margin-bottom: 0.3em;
      }

      div.filter-bar label {
          margin-bottom: 0;
      }
			
			div.vertical-aligned {
					display: table;
			}
			
			div.vertical-aligned div {
					display: table-cell;
					vertical-align: middle;
			}
			
      div.vertical-aligned button {
					margin-left: 0.7rem;
			}
			
      div.vertical-aligned input {
          display: table-cell;
          vertical-align: middle;
      }
			
			div.radioGroup input {
					margin-left: 0.7rem;
			}
			
      div.status-change-control {
					float: left;
          background-color: white;
          margin-top: 0.3em;
					bottom: 0;
      }

      div.status-change-control div {
          margin-left: 0.7rem;
      }

      td {
          font-size: small;
      }
			
			.title {
          font-size: medium;
          width: 12em;
      }

      div.radiogroup input {
          margin-left: 1.0em;
      }

      span.menu-spacer {
          display: block;
					float: left;
          width: 2px;
          height: 1.5em;
          background: lightgrey;
          margin-left: 0.7rem;
          margin-right: 0.7rem;
      }

      div.lower-panel{
          width: 100%;
          border: 1px solid darkgrey;
          background-color: white;
          padding: 0.3em;
					flex: 1;
					display: flex;
					flex-direction: column;
      }

      div.grid-container {
					flex: 1;
          margin-bottom: 0.4em;
			}

      button a {
					font-size: small;
			}
			
			div.container {
					display: block;
					background: white;
					overflow: hidden;
			}
      
			.jqx-grid-cell-alt {
					background-color: #EEEEEE;
			}

      table.status-change-control {
					width: 100%;
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
	private rows: number = 0;

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