import {Component} from '@angular/core';
import {URLSearchParams} from "@angular/http";

import {LabListService} from "../services/lab-list.service";
import {GetLabService} from "../services/get-lab.service";
import {jqxComboBox} from "jqwidgets-framework";
import {jqxCalendar} from "jqwidgets-framework";

@Component({
    selector: 'browse-experiments-filter',
    templateUrl: "./browse-experiments-filter.component.html",
    styles: [require("./browse-experiments-filter.component.less").toString()]
})

export class BrowseExperimentsFilterComponent {
    private showMore: boolean;
    private isAdminState: boolean;
    private isGuestState: boolean;

    private ccNumberString: string;
    private showEmptyFoldersFlag: boolean;
    private allFlag: boolean;
    private externalExperimentsFlag: boolean;
    private radioString: string;
    private idLabString: string;
    private idAppUserString: string;
    private createDateFromString: string;
    private createDateToString: string;

    private labList: any[] = [];
    private ownerList: any[] = [];
    private coreFacilityList: any[] = null;

    constructor(private labListService: LabListService, private getLabService: GetLabService) {
        // TODO
        this.showMore = false;
        this.setDefaultTestingState();
        this.resetFields();
        if (this.isAdminState) {
            this.labListService.getLabList().subscribe((response: any[]) => {
                this.labList = response;
            })
        }
    }

    setDefaultTestingState(): void {
        // TODO Remove once testing is done
        this.isAdminState = true;
        this.isGuestState = false;
    }

    resetFields(): void {
        this.ccNumberString = "";
        this.showEmptyFoldersFlag = false;
        this.allFlag = false;
        this.externalExperimentsFlag = false;
        this.radioString = "myLab";
        this.idLabString = "";
        this.idAppUserString = "";
        this.createDateFromString = "";
        this.createDateToString = "";
    }

    toggleShowMore(): void {
        this.showMore = !this.showMore;
    }

    toggleAdminState(): void {
        // TODO Remove once testing is done
        this.isAdminState = !this.isAdminState;
        this.showMore = false;
        this.resetFields();
    }

    toggleGuestState(): void {
        // TODO Remove once testing is done
        this.isGuestState = !this.isGuestState;
        this.showMore = false;
        this.resetFields();
    }

    resetLab(): void {
        this.idLabString = "";
        this.idAppUserString = "";
        this.ownerList = [];
    }

    onLabSelect(event: any): void {
        if (event.args != undefined && event.args.item != null && event.args.item.value != null) {
            this.idLabString = event.args.item.value;
            this.getLabService.getLabByIdOnlyForHistoricalOwnersAndSubmitters(this.idLabString).subscribe((response: any) => {
                this.ownerList = response.Lab.historicalOwnersAndSubmitters;
            })
        } else {
            this.resetLab();
        }
    }

    onLabUnselect(event: any): void {
        this.resetLab();
    }

    onOwnerSelect(event: any): void {
        if (event.args != undefined && event.args.item != null && event.args.item.value != null) {
            this.idAppUserString = event.args.item.value;
        } else {
            this.idAppUserString = "";
        }
    }

    onOwnerUnselect(event: any): void {
        this.idAppUserString = "";
    }

    onCalendarChange(event: any): void {
        let selection = event.args.range;
        this.createDateFromString = selection.from.toLocaleDateString();
        this.createDateToString = selection.to.toLocaleDateString();
    }

    getBrowseParameters(): URLSearchParams {
        // TODO
        let params: URLSearchParams = new URLSearchParams();
        if (this.isAdminState) {
            if (this.allFlag) {
                params.set("allExperiments", "Y");
            } else {

            }
        } else if (this.isGuestState) {

        } else {

        }
        if (this.ccNumberString != "" && !this.isGuestState) {
            params.set("ccNumber", this.ccNumberString);
        }
        params.set("showEmptyProjectFolders", this.showEmptyFoldersFlag ? "Y" : "N");
        params.set("showSamples", "N");
        params.set("showCategory", "N");
        return params;

        /*
         var params:Object = new Object();

         if ( coreFacilityCombo != null && coreFacilityCombo.selectedItem != null ) {
         params.idCoreFacility = coreFacilityCombo.selectedItem.@value;
         }
         if ( requestCategoryComboBox != null && requestCategoryComboBox.selectedItem != null && requestCategoryComboBox.selectedLabel != ''){
         params.codeRequestCategory = requestCategoryComboBox.selectedItem.@codeRequestCategory;
         }
         if ( ccLookupText.text != '' ) {
         params.ccNumber = ccLookupText.text;
         }
         if (currentState == 'AdminState' && allExperimentsCheckbox.selected) {
         params.allExperiments = 'Y';
         } else {
         if (currentState == 'AdminState') {
            if (browseLabCombo.selectedItem != null) {
         params.idLab = browseLabCombo.selectedItem.@idLab;
         }
            if (browseUserCombo.selectedItem != null) {
         params.idAppUser = browseUserCombo.selectedItem.@idAppUser;
         }
         } else if (currentState != "GuestState") {
            if (myExperimentsCheckbox.selected) {
         params.idAppUser = parentApplication.getIdAppUser();
         }	else if (this.userAllExperimentsCheckbox.selected) {
         params.allExperiments = 'Y';
         }	else if (this.myCollaborationsCheckbox.selected) {
         params.allCollaborations = 'Y';
         params.idAppUser = parentApplication.getIdAppUser();
         } else if (this.myLabsExperimentsCheckbox.selected && memberCombo.selectedItem != null){
         params.idAppUser = memberCombo.selectedItem.@idAppUser;
         }
         }
         if (this.externalExperimentsOnlyCheckbox.selected) {
         params.isExternalOnly = 'Y';
         }

         if (publicExperimentsOtherGroupsCheckbox.selected) {
         params.publicExperimentsInOtherGroups = 'Y';
         } else {
         params.publicExperimentsInOtherGroups = 'N';
         }
         if (currentState == 'AdminState'  ) {
         params.showMyLabsAlways = 'N';
         } else {
         params.showMyLabsAlways = 'Y';
         }

         }

         if ( this.pickDateRangeButton.fromDate != null && this.pickDateRangeButton.toDate != null ) {
         params.createDateFrom = dateFormatter.format( this.pickDateRangeButton.fromDate );
         params.createDateTo = dateFormatter.format( this.pickDateRangeButton.toDate );
         }

         params.showEmptyProjectFolders = this.showEmptyProjectFolders.selected ? "Y" : "N";

         params.showSamples = 'N';
         params.showCategory = 'N';

         return params;
        */
    }

    find(): void {
        // TODO
        let params: URLSearchParams = this.getBrowseParameters();
    }
}