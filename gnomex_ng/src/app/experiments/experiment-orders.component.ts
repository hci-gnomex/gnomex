import {Component, OnDestroy, OnInit} from "@angular/core";

import {ExperimentsService} from "./experiments.service";
import {Subscription} from "rxjs/Subscription";
/**
 *
 * @author u0556399
 * @since 7/20/2017.
 */
@Component({
	selector: "ExperimentOrders",
	template: `
      <!--<div>-->
      <!--<button (click)="onButton()">My refresh button!</button>-->
      <!--</div>-->
      <div>
          <h2>Hello!</h2>
          <hci-grid class="w-100"
                    [title]="'Orders'"
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
      </div>
	`
})
export class ExperimentOrdersComponent implements OnInit, OnDestroy {

	private orders: Array<any>;
	private subscription: Subscription;

	constructor(private experimentsService: ExperimentsService) {
	}

	// onButton(): void {
	//     // this.experimentsService.refreshExperimentOrders();
	//
	//     this.subscription.unsubscribe();
	// }

	ngOnInit(): void {
		this.subscription = this.experimentsService.getExperimentOrders()
				.subscribe(
						(response) => {
							this.orders = response;
						}
				);
	}

	ngOnDestroy(): void {
		this.subscription.unsubscribe();
	}
}