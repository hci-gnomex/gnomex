import {Component, Input, OnInit} from '@angular/core';
import {URLSearchParams} from "@angular/http";

import {LabListService} from "../services/lab-list.service";
import {GetLabService} from "../services/get-lab.service";
import {jqxComboBox} from "jqwidgets-framework";
import {jqxCalendar} from "jqwidgets-framework";
import {AppUserListService} from "../services/app-user-list.service";
import {CreateSecurityAdvisorService} from "../services/create-security-advisor.service";
import {ExperimentsService} from "../experiments/experiments.service";
import {AnalysisService} from "../services/analysis.service";
import {DictionaryService} from "../services/dictionary.service";
import {DataTrackService} from "../services/data-track.service";

@Component({
    selector: 'browse-filter',
    templateUrl: "./browse-filter.component.html",
    styles: [require("./browse-filter.component.less").toString()]
})

export class BrowseFilterComponent implements OnInit {
    @Input() private mode: string = "";

    private showLabelAndIcon: boolean = true;
    @Input() private label: string = "";
    @Input() private iconSource: string = "";
    @Input() private iconAlt: string = "icon";

    private showAllCheckbox: boolean = false;
    private allFlag: boolean;

    private showDateRangePicker: boolean = false;
    private dateFromString: string;
    private dateToString: string;

    private showSearchTextInput: boolean = false;
    private searchText: string;

    private showMoreSwitch: boolean = false;
    private showMore: boolean;

    private showExternalExperimentsCheckbox: boolean = false;
    private externalExperimentsFlag: boolean;

    private showPublicCheckbox: boolean = false;
    private publicFlag: boolean;

    private showCoreFacilityComboBox: boolean = false;
    private coreFacilityList: any[] = [];
    private idCoreFacilityString: string;

    private showRequestCategoryComboBox: boolean = false;
    private codeRequestCategoryString: string;
    private requestCategoryList: any[] = [];

    private showOrganismComboBox: boolean = false;
    private idOrganismString: string;
    private organismList: any[] = [];

    private showGenomeBuildComboBox: boolean = false;
    private idGenomeBuildString: string;
    private genomeBuildList: any[] = [];

    private showCCNumberInput: boolean = false;
    private ccNumberString: string;

    private showExperimentsRadioGroup: boolean = false;
    private experimentsRadioString: string;

    private showAnalysesRadioGroup: boolean = false;
    private analysesRadioString: string;

    private showWorkflowStateRadioGroup: boolean = false;
    private workflowStateString: string;

    private showRedosCheckbox: boolean = false;
    private redosFlag: boolean;

    private showOrderNumberInput: boolean = false;
    private orderNumberString: string;

    private showVisibilityCheckboxes: boolean = false;
    private visibilityOwnerFlag: boolean;
    private visibilityInstitutionFlag: boolean;
    private visibilityAllLabMembersFlag: boolean;
    private visibilityPublicFlag: boolean;

    private showLabComboBox: boolean = false;
    private showLabMultiSelectComboBox: boolean = false;
    private multiSelectIdLabs: Set<string> = new Set<string>();
    private labList: any[] = [];
    private idLabString: string;
    private ownerList: any[] = [];
    private showOwnerComboBox: boolean = false;
    private showLabMembersComboBox: boolean = false;
    private labMembersList: any[] = [];
    private idAppUserString: string;

    private showEmptyFoldersCheckbox: boolean = false;
    private showEmptyFoldersFlag: boolean;

    constructor(private labListService: LabListService, private getLabService: GetLabService,
                private appUserListService: AppUserListService, private createSecurityAdvisorService: CreateSecurityAdvisorService,
                private experimentsService: ExperimentsService, private analysisService: AnalysisService, private dataTrackService: DataTrackService,
                private dictionaryService: DictionaryService) {
        this.showMore = false;
        this.resetFields();
    }

    ngOnInit() {
        let isAdminState: boolean = this.createSecurityAdvisorService.isSuperAdmin || this.createSecurityAdvisorService.isAdmin;
        let isGuestState: boolean = this.createSecurityAdvisorService.isGuest;
        if (this.mode === "experimentBrowse") {
            if (isAdminState) {
                this.showAllCheckbox = true;
                this.showLabComboBox = true;
                this.showOwnerComboBox = true;
                this.showDateRangePicker = true;
                this.showMoreSwitch = true;
                this.showExternalExperimentsCheckbox = true;
                this.showCoreFacilityComboBox = true;
                this.showRequestCategoryComboBox = true;
                this.showCCNumberInput = true;
                this.showEmptyFoldersCheckbox = true;

                this.labListService.getLabList().subscribe((response: any[]) => {
                    this.labList = response;
                });
            } else if (isGuestState) {
                this.showEmptyFoldersCheckbox = true;
            } else {
                this.showExperimentsRadioGroup = true;
                this.showDateRangePicker = true;
                this.showMoreSwitch = true;
                this.showExternalExperimentsCheckbox = true;
                this.showLabMembersComboBox = true;
                this.showCoreFacilityComboBox = true;
                this.showRequestCategoryComboBox = true;
                this.showCCNumberInput = true;
                this.showEmptyFoldersCheckbox = true;

                this.appUserListService.getMembersOnly().subscribe((response: any[]) => {
                    this.labMembersList = response;
                });
            }
            this.coreFacilityList = this.createSecurityAdvisorService.myCoreFacilities;
        } else if (this.mode === "orderBrowse") {
            if (isAdminState) {
                this.showWorkflowStateRadioGroup = true;
                this.showRedosCheckbox = true;
                this.showDateRangePicker = true;
                this.showOrderNumberInput = true;
                this.showMoreSwitch = true;
                this.showCoreFacilityComboBox = true;
                this.showRequestCategoryComboBox = true;

                this.showMore = true;

                this.coreFacilityList = this.createSecurityAdvisorService.myCoreFacilities;
            }
        } else if (this.mode === "analysisBrowse") {
            if (isAdminState) {
                this.showMoreSwitch = true;
                this.showAllCheckbox = true;
                this.showDateRangePicker = true;
                this.showSearchTextInput = true;
                this.showPublicCheckbox = true;
                this.showLabMultiSelectComboBox = true;
                this.showOrganismComboBox = true;
                this.showGenomeBuildComboBox = true;

                this.showMore = true;

                this.labListService.getLabList().subscribe((response: any[]) => {
                    this.labList = response;
                });
            } else if (isGuestState) {
                this.showDateRangePicker = true;
                this.showSearchTextInput = true;
                this.showOrganismComboBox = true;
                this.showGenomeBuildComboBox = true;
            } else {
                this.showDateRangePicker = true;
                this.showSearchTextInput = true;
                this.showAnalysesRadioGroup = true;
                this.showOrganismComboBox = true;
                this.showGenomeBuildComboBox = true;
                this.showMoreSwitch = true;

                this.showMore = true;
            }

            this.dictionaryService.getEntriesExcludeBlank("hci.gnomex.model.OrganismLite").subscribe((response) => {
                this.organismList = response;
            });
        } else if (this.mode === "dataTrackBrowse") {
            if (isAdminState) {
                this.showOrganismComboBox = true;
                this.showGenomeBuildComboBox = true;
                this.showLabComboBox = true;
                this.showVisibilityCheckboxes = true;

                this.labListService.getLabList().subscribe((response: any[]) => {
                    this.labList = response;
                });
            } else if (isGuestState) {
                this.showOrganismComboBox = true;
                this.showGenomeBuildComboBox = true;
            } else {
                this.showOrganismComboBox = true;
                this.showGenomeBuildComboBox = true;
                this.showVisibilityCheckboxes = true;
            }

            this.dictionaryService.getEntriesExcludeBlank("hci.gnomex.model.OrganismLite").subscribe((response) => {
                this.organismList = response;
            });
        }
    }

    resetFields(): void {
        this.allFlag = false;
        this.experimentsRadioString = "myLab";
        this.analysesRadioString = "myLab";
        this.workflowStateString = "SUBMITTED";
        this.redosFlag = false;
        this.orderNumberString = "";
        this.idLabString = "";
        this.multiSelectIdLabs.clear();
        this.ownerList = [];
        this.idAppUserString = "";
        this.dateFromString = "";
        this.dateToString = "";
        this.searchText = "";
        this.externalExperimentsFlag = false;
        this.publicFlag = false;
        this.idCoreFacilityString = "";
        this.coreFacilityList = [];
        this.codeRequestCategoryString = "";
        this.idOrganismString = "";
        this.idGenomeBuildString = "";
        this.ccNumberString = "";
        this.showEmptyFoldersFlag = false;
        this.visibilityOwnerFlag = true;
        this.visibilityInstitutionFlag = true;
        this.visibilityAllLabMembersFlag = true;
        this.visibilityPublicFlag = true;
    }

    toggleShowMore(): void {
        this.showMore = !this.showMore;
        if (!this.showMore) {
            this.multiSelectIdLabs.clear();
            this.resetCoreFacilitySelection();
            if (this.showLabMembersComboBox) {
                this.idAppUserString = "";
            }
        }
    }

    resetLabSelection(): void {
        this.idLabString = "";
        this.idAppUserString = "";
        this.ownerList = [];
    }

    onLabSelect(event: any): void {
        if (event.args != undefined && event.args.item != null && event.args.item.value != null) {
            this.idLabString = event.args.item.value;
            if (this.showOwnerComboBox) {
                this.getLabService.getLabByIdOnlyForHistoricalOwnersAndSubmitters(this.idLabString).subscribe((response: any) => {
                    this.ownerList = response.Lab.historicalOwnersAndSubmitters;
                });
            }
        } else {
            this.resetLabSelection();
        }
    }

    onLabUnselect(): void {
        this.resetLabSelection();
    }

    onMultiLabSelect(event: any): void {
        if (event.args != undefined && event.args.item != null && event.args.item.value != null) {
            this.multiSelectIdLabs.add(event.args.item.value);
        }
    }

    onMultiLabUnselect(event: any): void {
        if (event.args != undefined && event.args.item != null && event.args.item.value != null) {
            this.multiSelectIdLabs.delete(event.args.item.value);
        }
    }

    onAppUserSelect(event: any): void {
        if (event.args != undefined && event.args.item != null && event.args.item.value != null) {
            this.idAppUserString = event.args.item.value;
        } else {
            this.idAppUserString = "";
        }
    }

    onAppUserUnselect(): void {
        this.idAppUserString = "";
    }

    onCoreFacilitySelect(event: any): void {
        if (event.args != undefined && event.args.item != null && event.args.item.value != null) {
            this.idCoreFacilityString = event.args.item.value;
            if (this.showRequestCategoryComboBox) {
                this.dictionaryService.getEntriesExcludeBlank("hci.gnomex.model.RequestCategory").subscribe((response) => {
                    this.requestCategoryList = response.filter(cat => {
                        if (cat.isActive === "Y" && !(cat.value === "") && cat.isInternal === "Y") {
                            return cat.idCoreFacility === this.idCoreFacilityString;
                        }
                        return false;
                    });
                });
            }
        } else {
            this.resetCoreFacilitySelection();
        }
    }

    onCoreFacilityUnselect(): void {
        this.resetCoreFacilitySelection();
    }

    resetCoreFacilitySelection(): void {
        this.idCoreFacilityString = "";
        this.codeRequestCategoryString = "";
        this.requestCategoryList = [];
    }

    onOrganismSelect(event: any): void {
        if (event.args != undefined && event.args.item != null && event.args.item.value != null) {
            this.idOrganismString = event.args.item.value;
            if (this.showGenomeBuildComboBox) {
                this.dictionaryService.getEntriesExcludeBlank("hci.gnomex.model.GenomeBuildLite").subscribe((response) => {
                    this.genomeBuildList = response.filter(gen => {
                        if (gen.isActive === "Y" && !(gen.value === "")) {
                            return gen.idOrganism === this.idOrganismString;
                        }
                        return false;
                    });
                });
            }
        } else {
            this.resetOrganismSelection();
        }
    }

    onOrganismUnselect(): void {
        this.resetOrganismSelection();
    }

    resetOrganismSelection(): void {
        this.idOrganismString = "";
        this.idGenomeBuildString = "";
        this.genomeBuildList = [];
    }

    onGenomeBuildSelect(event: any): void {
        if (event.args != undefined && event.args.item != null && event.args.item.value != null) {
            this.idGenomeBuildString = event.args.item.value;
        } else {
            this.idGenomeBuildString = "";
        }
    }

    onGenomeBuildUnselect(): void {
        this.idGenomeBuildString = "";
    }

    onRequestCategorySelect(event: any): void {
        if (event.args != undefined && event.args.item != null && event.args.item.value != null) {
            this.codeRequestCategoryString = event.args.item.value;
        } else {
            this.codeRequestCategoryString = "";
        }
    }

    onRequestCategoryUnselect(): void {
        this.codeRequestCategoryString = "";
    }

    onExperimentsRadioGroupChange(): void {
        if (this.showExperimentsRadioGroup && this.showLabMembersComboBox && !(this.experimentsRadioString === "myLab")) {
            this.idAppUserString = "";
        }
    }

    getExperimentBrowseParameters(): URLSearchParams {
        let params: URLSearchParams = new URLSearchParams();

        if (this.showCoreFacilityComboBox && !(this.idCoreFacilityString === "")) {
            params.set("idCoreFacility", this.idCoreFacilityString);

            if (this.showRequestCategoryComboBox && !(this.codeRequestCategoryString === "")) {
                params.set("codeRequestCategory", this.codeRequestCategoryString);
            }
        }

        if (this.showCCNumberInput && !(this.ccNumberString === "")) {
            params.set("ccNumber", this.ccNumberString);
        }

        if (this.showAllCheckbox && this.allFlag) {
            params.set("allExperiments", "Y");
        } else {
            if (this.showLabComboBox && !(this.idLabString === "")) {
                params.set("idLab", this.idLabString);
            }
            if (this.showOwnerComboBox && !(this.idAppUserString === "")) {
                params.set("idAppUser", this.idAppUserString);
            }
            if (this.showExperimentsRadioGroup) {
                if (this.experimentsRadioString === "myExperiments") {
                    params.set("idAppUser", this.createSecurityAdvisorService.idAppUser.toString());
                } else if (this.experimentsRadioString === "myLab" && this.showLabMembersComboBox && !(this.idAppUserString === "")) {
                    params.set("idAppUser", this.idAppUserString);
                } else if (this.experimentsRadioString === "myCollaborations") {
                    params.set("allCollaborations", "Y");
                    params.set("idAppUser", this.createSecurityAdvisorService.idAppUser.toString());
                } else if (this.experimentsRadioString === "all") {
                    params.set("allExperiments", "Y");
                }
                params.set("publicExperimentsInOtherGroups", this.experimentsRadioString === "publicData" ? "Y" : "N");
            }
            if (this.showExternalExperimentsCheckbox && this.externalExperimentsFlag) {
                params.set("isExternalOnly", "Y");
            }
            params.set("showMyLabsAlways", this.createSecurityAdvisorService.isSuperAdmin || this.createSecurityAdvisorService.isAdmin ? "N" : "Y");
        }

        if (this.showDateRangePicker && !(this.dateFromString === "") && !(this.dateToString === "")) {
            params.set("createDateFrom", this.dateFromString);
            params.set("createDateTo", this.dateToString);
        }

        if (this.showEmptyFoldersCheckbox && this.showEmptyFoldersFlag) {
            params.set("showEmptyProjectFolders", "Y");
        } else {
            params.set("showEmptyProjectFolders", "N");
        }

        params.set("showSamples", "N");
        params.set("showCategory", "N");

        return params;
    }

    getOrderBrowseParameters(): URLSearchParams {
        let params: URLSearchParams = new URLSearchParams();

        params.set("includeSampleInfo", "Y");

        if (this.showDateRangePicker && !(this.dateFromString === "") && !(this.dateToString === "")) {
            params.set("createDateFrom", this.dateFromString);
            params.set("createDateTo", this.dateToString);
        }

        if (this.showOrderNumberInput && !(this.orderNumberString === "")) {
            params.set("number", this.orderNumberString);
            this.workflowStateString = "";
        }

        if (this.showWorkflowStateRadioGroup && !(this.workflowStateString === "")) {
            params.set("status", this.workflowStateString);
        }

        if (this.showRedosCheckbox && this.redosFlag) {
            params.set("hasRedo", "Y");
        }

        if (this.showCoreFacilityComboBox && !(this.idCoreFacilityString === "")) {
            params.set("idCoreFacility", this.idCoreFacilityString);

            if (this.showRequestCategoryComboBox && !(this.codeRequestCategoryString === "")) {
                params.set("codeRequestCategory", this.codeRequestCategoryString);
            }
        }

        return params;
    }

    getAnalysisBrowseParameters(): URLSearchParams {
        let params: URLSearchParams = new URLSearchParams();

        if (this.showAllCheckbox && this.allFlag) {
            params.set("allAnalysis", "Y");
        } else {
            if (this.showAnalysesRadioGroup) {
                if (this.analysesRadioString === "all") {
                    params.set("allAnalysis", "Y");
                } else if (this.analysesRadioString === "myAnalyses") {
                    params.set("idAppUser", this.createSecurityAdvisorService.idAppUser.toString());
                } else if (this.analysesRadioString === "otherLabs") {
                    params.set("publicAnalysisOtherGroups", "Y");
                }
            }

            params.set("showMyLabsAlways", this.createSecurityAdvisorService.isSuperAdmin || this.createSecurityAdvisorService.isAdmin ? "N" : "Y");

            if (this.createSecurityAdvisorService.isGuest || (this.showPublicCheckbox && this.publicFlag)) {
                params.set("publicProjects", "Y");
            }

            if (this.showDateRangePicker && !(this.dateFromString === "") && !(this.dateToString === "")) {
                params.set("createDateFrom", this.dateFromString);
                params.set("createDateTo", this.dateToString);
            }

            if (this.showSearchTextInput && !(this.searchText === "")) {
                params.set("searchText", this.searchText);
            }

            if (this.showLabMultiSelectComboBox && this.multiSelectIdLabs.size > 0) {
                let labKeys: string = "";
                this.multiSelectIdLabs.forEach(function (lab: string) {
                    if (labKeys === "") {
                        labKeys = labKeys.concat(lab);
                    } else {
                        labKeys = labKeys.concat(":", lab);
                    }
                }, this);
                params.set("labKeys", labKeys);
            }

            if (this.showOrganismComboBox && !(this.idOrganismString === "")) {
                params.set("idOrganism", this.idOrganismString);
                if (this.showGenomeBuildComboBox && !(this.idGenomeBuildString === "")) {
                    params.set("idGenomeBuild", this.idGenomeBuildString);
                }
            }
        }

        return params;
    }

    getDataTrackBrowseParameters(): URLSearchParams {
        let params: URLSearchParams = new URLSearchParams();

        if (this.showLabComboBox && !(this.idLabString === "")) {
            params.set("idLab", this.idLabString);
        }

        if (this.showOrganismComboBox && !(this.idOrganismString === "")) {
            params.set("idOrganism", this.idOrganismString);
            if (this.showGenomeBuildComboBox && !(this.idGenomeBuildString === "")) {
                params.set("idGenomeBuild", this.idGenomeBuildString);
            }
        }

        if (this.showVisibilityCheckboxes) {
            params.set("isVisibilityPublic", this.visibilityPublicFlag ? "Y" : "N");
            params.set("isVisibilityOwner", this.visibilityOwnerFlag ? "Y" : "N");
            params.set("isVisibilityMembers", this.visibilityAllLabMembersFlag ? "Y" : "N");
            params.set("isVisibilityInstitute", this.visibilityInstitutionFlag ? "Y" : "N");
        }

        return params;
    }

    search(): void {
        if (this.mode === "experimentBrowse") {
            let params: URLSearchParams = this.getExperimentBrowseParameters();
            this.experimentsService.getProjectRequestList(params).subscribe((response: any) => {
                console.log("GetProjectRequestList called");
            });
        } else if (this.mode === "orderBrowse") {
            let params: URLSearchParams = this.getOrderBrowseParameters();
            this.experimentsService.getRequestList(params).subscribe((response: any) => {
                console.log("GetRequestList called");
            });
        } else if (this.mode === "analysisBrowse") {
            let params: URLSearchParams = this.getAnalysisBrowseParameters();
            this.analysisService.getAnalysisGroupList(params).subscribe((response: any) => {
                console.log("GetAnalysisGroupList called");
            });
        } else if (this.mode === "dataTrackBrowse") {
            let params: URLSearchParams = this.getDataTrackBrowseParameters();
            this.dataTrackService.getDataTrackList(params).subscribe((response: any) => {
                console.log("GetDataTrackList called");
            });
        }
    }
}