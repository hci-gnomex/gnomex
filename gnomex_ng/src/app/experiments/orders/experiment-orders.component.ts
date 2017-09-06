import {Component, OnDestroy, OnInit, ViewChild} from "@angular/core";

import {ExperimentsService} from "../experiments.service";
import {Subscription} from "rxjs/Subscription";
import {NgModel} from "@angular/forms"
import {URLSearchParams} from "@angular/http";
import {BrowseFilterComponent} from "../../util/browse-filter.component";
import {jqxGridComponent} from "../../../assets/jqwidgets-ts/angular_jqxgrid";
import {jqxComboBoxComponent} from "../../../assets/jqwidgets-ts/angular_jqxcombobox";
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
						<browse-filter [label]="'Orders'" [iconSource]="'assets/review.png'"
													 [mode]="'orderBrowse'"></browse-filter>
					</div>
				</div>
				<div class="tr" style="height:0.3em; width:0;">
				</div>
				<div class="tr" style="width: 100%;">
					<div class="td" style="width: 100%; height: 100%">
						<div class="lower-panel">
							<div class="t" style="height: 100%; width: 100%;">
								<div class="tr" style="width: 100%;">
									<div class="td" style="width: 100%; height: 100%;">
										<div style="display: block; width: 100%; height: 100%; padding: 0.1em;">
											<jqxGrid #myGrid
															 [width]="'100%'"
															 [height]="'100%'"
															 [source]="dataAdapter"
															 [pageable]="false"
															 [autoheight]="false"
															 [editable]="false"
															 [sortable]="true"
															 [columns]="columns"
															 [altrows]="true"
															 [selectionmode]="'checkbox'"
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
																	<div class="title">{{myGrid.getselectedrowindexes().length}}
																		selected
																	</div>
																</div>
																<div class="td">
																	<jqxComboBox #statusComboBox
																							 [source]="dropdownChoices"
																							 [placeHolder]="'- Change Status -'"
																							 [dropDownVerticalAlignment]="'top'"
																							 [autoDropDownHeight]="true"></jqxComboBox>
																</div>
																<div class="td button-container">
																	<jqxButton
																			[disabled]="myGrid.getselectedrowindexes().length === 0 || (this.statusCombobox.getSelectedItem() === null || this.statusCombobox.getSelectedItem().value === '')"
																			[template]="'link'"
																			(onClick)="goButtonClicked()">
																		<img
																				*ngIf="myGrid.getselectedrowindexes().length  != 0 && (this.statusCombobox.getSelectedItem()  != null && this.statusCombobox.getSelectedItem().value  != '')"
																				src="assets/bullet_go.png" alt=""
																				style="margin-right:0.2em;"/>
																		<img
																				*ngIf="myGrid.getselectedrowindexes().length === 0 || (this.statusCombobox.getSelectedItem() === null || this.statusCombobox.getSelectedItem().value === '')"
																				src="assets/bullet_go_disable.png" alt=""
																				style="margin-right:0.2em;"/>
																		Go
																	</jqxButton>
																</div>
																<div class="td button-container">
																	<jqxButton
																			[disabled]="myGrid.getselectedrowindexes().length === 0"
																			[template]="'link'"
																			(onClick)="deleteButtonClicked()">
																		<img *ngIf="myGrid.getselectedrowindexes().length != 0"
																				 src="assets/delete.png" alt=""
																				 style="margin-right:0.2em;"/>
																		<img *ngIf="myGrid.getselectedrowindexes().length === 0"
																				 src="assets/delete_disable.png" alt=""
																				 style="margin-right:0.2em;"/>
																		Delete
																	</jqxButton>
																</div>
																<div class="td button-container">
																	<jqxButton
																			[disabled]="myGrid.getselectedrowindexes().length === 0"
																			[template]="'link'"
																			(onClick)="emailButtonClicked()">
																		<img *ngIf="myGrid.getselectedrowindexes().length != 0"
																				 src="assets/email_go.png" alt=""
																				 style="margin-right:0.2em;"/>
																		<img *ngIf="myGrid.getselectedrowindexes().length === 0"
																				 src="assets/email_go_disable.png" alt=""
																				 style="margin-right:0.2em;"/>
																		Email
																	</jqxButton>
																</div>
															</div>
														</div>
													</div>
													<td style="text-align: right">
														<div>({{(source.localdata.length === null) ? 0 : source.localdata.length
																																						 + (source.localdata.length
																																								!= 1 ? " orders"
																																					 : " order")}})
														</div>
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

      div.lower-panel {
          height: 100%;
          width: 100%;
          border: 1px solid darkgrey;
          background-color: white;
          padding: 0.3em;
          display: block;
      }

      div.grid-footer {
          display: block;
          width: 100%;
          margin-top: 0.3em;
          padding: 0em 0.8em;
      }

      div.button-container {
          padding: 0.2em 0em 0.2em 0.6em;
      }
	`]
})
export class ExperimentOrdersComponent implements OnInit, OnDestroy {

	@ViewChild('myGrid') myGrid: jqxGridComponent;
	@ViewChild('statusComboBox') statusCombobox: jqxComboBoxComponent;

	private orders: Array<any>;

	private editViewRenderer = (row: number, column: any, value: any): any => {
		return `<div style="display: table; width: 100%; height:100%; text-align: center;">
							<a style="display: table-cell; padding: 0em 0.5em 0em 1em; width: 50%; height: 100%; vertical-align: middle;">Edit!</a>
							<a style="display: table-cell; padding: 0em 1em 0em 0.5em; width: 50%; height: 100%; vertical-align: middle;">View!</a>
						</div>`;
	};

	private dropdownChoices: any[] = [
		{value: "", label: ""},
		{value: "COMPLETE", label: "COMPLETE"},
		{value: "FAILED", label: "FAILED"},
		{value: "NEW", label: "NEW"},
		{value: "PROCESSING", label: "PROCESSING"},
		{value: "SUBMITTED", label: "SUBMITTED"}
	];

	private columns: any[] = [
		{text: "# ", datafield: "requestNumber", width: "4%"},
		{text: "Name", datafield: "name", width: "14%"},
		{text: "Action", width: "9%", cellsrenderer: this.editViewRenderer},
		{text: "Samples", datafield: "numberOfSamples", width: "4%"},
		{text: "Status", datafield: "requestStatus", width: "6%"},
		{text: "Type", width: "8%"},
		{text: "Submitted on", datafield: "createDate", width: "10%"},
		{text: "Container", datafield: "container", width: "6%"},
		{text: "Submitter", datafield: "ownerName", width: "13%"},
		{text: "Lab", datafield: "labName"}
	];

	private source = {
		datatype: "json",
		localdata: [],
		datafields: [
			{name: "name", type: "string"},
			{name: "requestNumber", type: "string"},
			{name: "requestStatus", type: "string"},
			{name: "container", type: "string"},
			{name: "ownerName", type: "string"},
			{name: "labName", type: "string"},
			{name: "createDate", type: "string"},
			{name: "numberOfSamples", type: "string"}
		]
	};

	private dataAdapter: any = new jqx.dataAdapter(this.source);

	private experimentsSubscription: Subscription;
	private statusChangeSubscription: Subscription;

	private radioString_workflowState: String = 'submitted';
	private redosEnabled: boolean = false;

	private numberSelected: number = 0;

	@ViewChild(BrowseFilterComponent)
	private _browseFilterComponent: BrowseFilterComponent;

	private selectedRequestNumbers: string[];
	private changeStatusResponsesRecieved: number;

	constructor(private experimentsService: ExperimentsService) {
	}

	goButtonClicked(): void {

		if (this.statusCombobox.getSelectedItem().value === "") {
			return;
		}

		// console.log("You clicked \"Go\"!");
		let gridSelectedIndexes: Array<Number> = this.myGrid.getselectedrowindexes();
		let statusSelectedIndex: number = this.statusCombobox.getSelectedIndex();

		this.selectedRequestNumbers = [];
		this.changeStatusResponsesRecieved = 0;

		for (let i: number = 0; i < gridSelectedIndexes.length; i++) {
			//	console.log("Changing Experiment numbers: " + this.myGrid.getcell(gridSelectedIndexes[i].valueOf(), "requestNumber").value + " status to " + this.statusCombobox.getSelectedItem().value);
			let idRequest: string = "" + this.myGrid.getcell(gridSelectedIndexes[i].valueOf(), "requestNumber").value;
			let cleanedIdRequest: string = idRequest.slice(0, idRequest.indexOf("R") >= 0 ? idRequest.indexOf("R") : idRequest.length);
			this.selectedRequestNumbers.push(cleanedIdRequest);

			this.experimentsService.changeExperimentStatus(cleanedIdRequest, this.statusCombobox.getSelectedItem().value)
		}
	}

	deleteButtonClicked(): void {
		console.log("You clicked \"Delete\"!");
	}

	emailButtonClicked(): void {
		console.log("You clicked \"Email\"!");
	}

	updateGridData(data: Array<any>) {
		this.source.localdata = Array.isArray(data) ? data : [data];
		this.dataAdapter = new jqx.dataAdapter(this.source);
		this.myGrid.selectedrowindexes([]);
	}

	ngOnInit(): void {
		this.experimentsSubscription = this.experimentsService.getExperimentsObservable()
				.subscribe((response) => {
					this.orders = response;
					this.updateGridData(response);
				});

		this.statusChangeSubscription = this.experimentsService.getChangeExperimentStatusObservable().subscribe((response) => {
			for (let i: number = 0; i < this.selectedRequestNumbers.length; i++) {
				// console.log("SelectedGridValues: " + this.selectedRequestNumbers[i] + "    idRequest: " + response.idRequest);

				if (this.selectedRequestNumbers[i] === response.idRequest) {
					this.changeStatusResponsesRecieved++;

					if (this.changeStatusResponsesRecieved === this.selectedRequestNumbers.length) {
						this.experimentsService.repeatGetExperiments_fromBackend();
					}

					break;
				}
			}
		});

		let params: URLSearchParams = new URLSearchParams();
		params.append("status", "SUBMITTED");

		this.experimentsService.getExperiments_fromBackend(params);
	}

	ngOnDestroy(): void {
		this.experimentsSubscription.unsubscribe();
		this.statusChangeSubscription.unsubscribe();
	}
}