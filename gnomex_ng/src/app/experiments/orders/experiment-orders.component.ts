import {Component, OnDestroy, OnInit, ViewChild} from "@angular/core";

import { ExperimentsService } from "../experiments.service";
import { Subscription } from "rxjs/Subscription";
import { NgModel } from "@angular/forms"
import { URLSearchParams } from "@angular/http";
import { BrowseFilterComponent } from "../../util/browse-filter.component";
/**
 *
 * @author u0556399
 * @since 7/20/2017.
 */
@Component({
	selector: "ExperimentOrders",
	template: `
      <div class="background">
					<div class="t" style="height: 100%; width: 100%;">
							<div class="tr" style="width: 100%;">
									<div class="td" style="width: 100%;">
											<div style="margin-bottom: 0.3em;">
                          <browse-filter [label]="'Orders'" [iconSource]="'assets/review.png'" [mode]="'orderBrowse'"></browse-filter>
											</div>
									</div>
							</div>
							<div class="tr" style="width: 100%;">
									<div class="td" style="width: 100%; height: 100%">
											<div class="lower-panel">
                          <div class="t" style="height: 100%; width: 100%;">
															<div class="tr" style="width: 100%;">
																	<div class="td" style="width: 100%; height: 100%;">
																			<div style="display: block; width: 100%; height: 100%; padding: 0.1em;">
                                          <jqxGrid
                                                  [width]="'100%'"
                                                  [height]="'100%'"
                                                  [source]="dataAdapter"
                                                  [pageable]="false"
                                                  [autoheight]="false"
                                                  [editable]="true"
                                                  [sortable]="true"
                                                  [columns]="columns"
                                                  [altrows]="true"
                                                  [selectionmode]='"checkbox"'
																									[columnsresize]="true"
                                                  #gridReference>
                                          </jqxGrid>
																			</div>
																	</div>
															</div>
															<div class="tr" style="width:100%">
																	<div class="td" style="width: 100%">
                                      <div class="grid-footer">
                                          <div class="t" style="width: 100%">
                                              <div class="tr" style="width:100%">
                                                  <div class="td">
                                                      <div class="t">
                                                          <div class="tr">
                                                              <div class="td">
                                                                  <div class="title">{{numberSelected}} selected</div>
                                                              </div>
                                                              <div class="td">
                                                                  <jqxComboBox></jqxComboBox>
                                                              </div>
                                                              <div class="td">
                                                                  <jqxButton 
																																					[template]="'link'"
																																					[imgSrc]="'assets/arrow_right.gif'"
																																					[imgPosition]="'center'"
																																					[textImageRelation]="'imageBeforeText'"
																																					(onClick)="goButtonClicked()">Go</jqxButton>
                                                              </div>
                                                              <div class="td">
                                                                  <jqxButton
                                                                          [template]="'link'"
                                                                          [imgSrc]="'assets/delete.png'"
                                                                          [imgPosition]="'center'"
                                                                          [textImageRelation]="'imageBeforeText'"
                                                                          (onClick)="deleteButtonClicked()">Delete</jqxButton>
                                                              </div>
                                                              <div class="td">
                                                                  <jqxButton
                                                                          [template]="'link'"
                                                                          [imgSrc]="'assets/email_go.png'"
                                                                          [imgPosition]="'center'"
                                                                          [textImageRelation]="'imageBeforeText'"
                                                                          (onClick)="emailButtonClicked()">Email</jqxButton>
                                                              </div>
                                                          </div>
                                                      </div>
                                                  </div>
                                                  <td style="text-align: right">
                                                      <div>({{(source.localdata.length === null) ? 0 : source.localdata.length}} orders)</div>
                                                  </td>
                                              </div>
                                          </div>
																			</div>
																	</div>
															</div>
													</div>
                      </div>
									</div>
							</div>
					</div>
      </div>
	`,
	styles: [`
      div.t {
          display: table;
      }

      div.tr {
          display: table-row;
          vertical-align: middle;
      }

      div.td {
          display: table-cell;
          vertical-align: middle;
      }
			
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
          border: 1px solid darkgrey;
          background-color: white;
					padding: 0.3em 0.8em;
          margin-bottom: 0.3em;
      }
			
      .title {
          text-align: left;
          font-size: medium;
          width: 12em;
      }

      div.filter-bar label {
          margin-bottom: 0;
      }
			
			div.radioGroup input {
					margin-left: 0.7rem;
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
          height: 100%;
          width: 100%;
          border: 1px solid darkgrey;
          background-color: white;
          padding: 0.3em;
          display: block;
      }
			
      .jqx-grid-cell-alt {
          background-color: #cccccc;
      }

      .jqx-fill-state-hover {
					background-color: deepskyblue;
			}
			
      div.grid-footer {
					display: block;
					width: 100%;
          margin-top: 0.3em;
					padding: 0em 0.8em;
      }
			
      button a {
					font-size: small;
			}
      
	`]
})
export class ExperimentOrdersComponent implements OnInit, OnDestroy {

	private orders: Array<any>;

	private editViewRenderer = (row:number, column: any, value: any): any => {
		return `<div style="display: inline-block; width: 100%; height:100%; text-align: center;">
							<a style="padding: 0em 1em">Edit!</a>
							<a style="padding: 0em 1em">View!</a>
						</div>`;
	};

	private columns: any[] = [
		{ text: "# ", datafield: "requestNumber", width: "4%" },
		{ text: "Name", datafield: "name", width: "14%" },
		{ text: "Action", width: "9%", cellsrenderer: this.editViewRenderer },
		{ text: "Samples", datafield: "numberOfSamples", width: "4%"},
		{ text: "Status", datafield: "requestStatus", width: "6%" },
		{ text: "Type", width: "8%" },
		{ text: "Submitted on", datafield: "createDate", width: "10%" },
		{ text: "Container", datafield: "container", width: "6%" },
		{ text: "Submitter", datafield: "ownerName", width: "13%" },
		{ text: "Lab", datafield: "labName" }
	];

	private source = {
		datatype: "json",
		localdata: [
			{ name: "Hello",
				requestNumber: "World",
				requestStatus: "Good to see you!",
				isSelected: "N"
			}
		],
		datafields: [
			{ name: "name", type: "string"},
			{ name: "requestNumber", type: "string"},
			{ name: "requestStatus", type: "string"},
			{ name: "container", type: "string" },
			{ name: "ownerName", type: "string" },
			{ name: "labName", type: "string" },
			{ name: "createDate", type: "string" },
			{ name: "numberOfSamples", type: "string" }
		]
	};

	private dataAdapter: any = new jqx.dataAdapter(this.source);

	private subscription: Subscription;

	private radioString_workflowState: String = 'submitted';
	private redosEnabled: boolean = false;

	private numberSelected: number = 0;

	private params: URLSearchParams = null;

	@ViewChild(BrowseFilterComponent)
	private _browseFilterComponent: BrowseFilterComponent;

	constructor(private experimentsService: ExperimentsService) {
	}

	onRadioButtonClick(): void {
		// this.experimentsService.refreshExperimentOrders();

		this.subscription.unsubscribe();
	}

	goButtonClicked(): void {
		console.log("You clicked \"Go\"!");
	}

	deleteButtonClicked(): void {
		console.log("You clicked \"Delete\"!");
	}

	emailButtonClicked(): void {
		console.log("You clicked \"Email\"!");
	}

	updateGridData(data: Array<any>) {
		this.source.localdata = data;
		this.dataAdapter = new jqx.dataAdapter(this.source);
	}

	ngOnInit(): void {
		this.params = new URLSearchParams();
		this.params.append("status", "SUBMITTED");

		this.subscription = this.experimentsService.getExperimentOrdersObservable()
				.subscribe((response) => {
					this.orders = response;
					this.updateGridData(response);
				});

		this.experimentsService.getExperimentOrders_fromBackend(this.params);
	}

	ngOnDestroy(): void {
		this.subscription.unsubscribe();
	}
}