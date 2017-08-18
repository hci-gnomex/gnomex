/*
 * Copyright (c) 2016 Huntsman Cancer Institute at the University of Utah, Confidential and Proprietary
 */
import {Component, ElementRef, ViewChild, ViewEncapsulation} from "@angular/core";

import { URLSearchParams } from "@angular/http";
import { Routes, RouterModule, Router } from "@angular/router";

import {ExperimentsService} from "./experiments.service";
import { jqxTreeComponent } from 'jqwidgets-framework';
import { jqxWindowComponent } from 'jqwidgets-framework';
import { jqxButtonComponent } from 'jqwidgets-framework';
import { jqxComboBoxComponent } from 'jqwidgets-framework';
import { jqxNotificationComponent  } from 'jqwidgets-framework';
import { jqxCheckBoxComponent } from 'jqwidgets-framework';
import { jqxInputComponent } from 'jqwidgets-framework';
import { jqxToggleButtonComponent } from 'jqwidgets-framework';
import {jqxPanelComponent} from "jqwidgets-framework";
import {jqxLoaderComponent} from "jqwidgets-framework";

import * as _ from "lodash";
import * as $ from "jquery";

@Component({
    selector: "experiments",
    templateUrl:'./experiments.component.html',
    styles: [`
        div.background {
            width: 100%;
            height: 100%;
            background-color: #EEEEEE;
            padding: 0.3em;
            border-radius: 0.3em;
            border: 1px solid darkgrey;
            display: block;
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
`, require("./browse-experiments-component.less").toString()],
    encapsulation: ViewEncapsulation.None

})
export class BrowseExperimentsComponent {

    @ViewChild('treeReference') tree: jqxTreeComponent;
    @ViewChild('jqxWidget') jqxWidget: ElementRef;
    @ViewChild('reassignWindow') reassignWindow: jqxWindowComponent;
    @ViewChild('reassignComboBox') reassignComboBox: jqxComboBoxComponent;
    @ViewChild('billingComboBox') billingComboBox: jqxComboBoxComponent;
    @ViewChild('msgSelectOwner') msgSelectOwner: jqxNotificationComponent;
    @ViewChild('msgSelectBilling') msgSelectBilling: jqxNotificationComponent;
    @ViewChild('msgNoAuthUsersForLab') msgNoAuthUsersForLab: jqxNotificationComponent;
    @ViewChild('msgEnterProjectName') msgEnterProjectName: jqxNotificationComponent;
    @ViewChild('msgDragDropHint') msgDragDropHint: jqxNotificationComponent;
    @ViewChild('msgLoading') msgLoading: jqxNotificationComponent;
    @ViewChild('newProjectWindow') newProjectWindow: jqxWindowComponent;
    @ViewChild('deleteProjectWindow') deleteProjectWindow: jqxWindowComponent;
    @ViewChild('toggleButton') toggleButton: jqxButtonComponent;
    @ViewChild('showEmptyCheckBox') showEmptyCheckBox: jqxCheckBoxComponent;

    @ViewChild('deleteProject') deleteProject: jqxButtonComponent;
    @ViewChild('newProject') newProject: jqxButtonComponent;
    @ViewChild('labComboBox') labComboBox: jqxComboBoxComponent;
    @ViewChild('yesButtonDeleteProject') yesButtonDeleteProject: jqxButtonComponent;
    @ViewChild('deleteProjectNoButtonClicked') deleteProjectNoButton: jqxButtonComponent;
    @ViewChild('jqxLoader') jqxLoader: jqxLoaderComponent;

    currentItem: any;
    targetItem: any;
    dropItem: any;
    it: any;
    projectDescription: string;
    projectName: string;
    source: any;
    source1: any;
    experimentService: ExperimentsService;
    users: any[];
    labMembers: any;
    billingAccounts: any;
    dragEndItems: any;
    sourceLab: any;
    selectedItem: any;
    selectedIndex: number = -1;
    selectedBillingItem: string;
    selectedBillingIndex: number = -1;
    selectedProjectLabItem: any;
    selectedProjectLabIndex: number = -1;
    parentNode: any;
    dragEndLab: any;
    idCoreFacility: string = "3";
    icon: string;
    response: any;
    showBillingCombo: boolean = false;
    dataAdapter: any;
    records: any;
    items2: any;
    items: any;
    labs: any;
    experimentCount: number;

    constructor(private experimentsService: ExperimentsService) {
        this.experimentService = experimentsService;

        this.experimentsService.getExperiments().subscribe(response => {
            // this.buildTree(response);
            this.response = response;
            this.buildTree(response);
        })
        const folder = "assets/folder.png"

        this.users = [];
        this.items2 = [];
        this.items = [];
        this.dragEndItems = [];
        this.labMembers = [];
        this.billingAccounts = [];
        this.records = [];
        this.labs = [];
    }
    /*
        Build tree from getExperiments.
        @param response
    */
    buildTree(response: any[]) {
        this.jqxLoader.open();
        this.experimentCount = 0;
        this.items2 = [];
        if (!this.isArray(response)) {
            this.items = [response];
        } else {
            this.items = response;
        }
        this.labs = this.labs.concat(this.items);
        this.items.label = "root";
        //this.items2 = this.items2.concat(this.items);
        for( var l of this.items) {
            if (!this.isArray(l.Project)) {
                l.items = [l.Project];
            } else {
                l.items = l.Project;
            }
            l.id = l.idLab;
            l.parentid = -1;

            l.icon = "assets/group.png"
            this.items2 = this.items2.concat(l)
            for( var p of l.items) {
                p.icon = "assets/folder.png"
                p.labId = l.labId;
                p.id = p.idProject;
                p.parentid = l.id;
                if (!this.isArray(p.Request)) {
                    p.items = [p.Request];
                } else {
                    p.items = p.Request;
                }
                this.items2 = this.items2.concat(p);
                for (var r of p.items) {
                    if (r) {
                        this.experimentCount ++;
                        r.id = r.idRequest;
                        r.parentid = p.id;
                        this.items2 = this.items2.concat(r);
                    }
                }

            }
        }
        this.source1 = {
            datatype: 'json',
            datafields: [
                { name: 'id' },
                { name: 'parentid' },
                { name: 'label' },
                { name: 'icon' },
                { name: 'idAppUser'},
                { name: 'isExternal'}
            ],
            id: 'id',
            localdata: this.items2
        };
        this.dataAdapter = new $.jqx.dataAdapter(this.source1, { autoBind: true });
        this.records = this.dataAdapter.getRecordsHierarchy('id', 'parentid', 'items');
        this.tree.expandAll();
        this.jqxLoader.close();
    };

    /*
        Determine if the object is an array
        @param what
     */
    isArray(what) {
        return Object.prototype.toString.call(what) === '[object Array]';
    };

    detailFn(): (keywords: string) => void {
        return (keywords) => {
            window.location.href = "http://localhost/gnomex/experiments/"+keywords;
        };
    }

    onDragStart = (event) => {
        console.log("ondragstart");
    };

    onDragEnd = (event) => {

        this.dragEndItems = _.cloneDeep(this.records);

        //this.dragEndItems = Object.assign(this.dragEndItems, this.items);
        if (event.args.label) {
            this.currentItem = this.tree.getItem(event.owner.it.element);

            this.targetItem = this.tree.getItem(event.owner.dropItem.element);
            if (this.targetItem.level == 0) {
                this.resetTree();
            } else {
                this.getSourceLab();

                if (this.targetItem && this.targetItem.level != 0) {
                    if (this.targetItem.level == 2) {
                        this.targetItem = this.tree.getItem(this.targetItem.parentElement);
                        // this.currentItem.parentid = this.targetItem.id;
                        // this.currentItem.parentElement = this.targetItem.element;
                    }
                    this.currentItem.parentid = this.targetItem.id;
                    this.getRecord(this.currentItem.id);

                    this.dragEndLab = this.tree.getItem(this.targetItem.parentElement);
                    // let temp = _.cloneDeep(this.records);
                    // this.records = temp;
                    // var root: any = this;
                    // this.tree.refresh();
                    this.getLabUsers();
                }
            }
        }
    };

    getRecord(id: any): any{
        for (var item of this.items2) {
            if (item.id == id && item.idRequest == id) {
                item.parentid = this.targetItem.id;
                this.source1.localdata = this.items2;
                this.dataAdapter.dataBind();
                this.records = this.dataAdapter.getRecordsHierarchy('id', 'parentid', 'items');
                break;
            }
        }
        return item;
    }

    getSourceLab() {
        var folderElement = this.tree.getItem(this.currentItem.parentElement);
        this.sourceLab = this.tree.getItem(folderElement.parentElement);
    }

    showReassignWindow() {
        //this.reassignComboBox.source(this.labMembers);
        //this.billingComboBox.source(this.billingAccounts);
        if (this.targetItem) {
            if (this.targetItem.isExternal == "N" || this.sourceLab.id == this.dragEndLab.id) {
                this.showBillingCombo = false;
            } else {
                this.showBillingCombo = true;
            }
        }
        this.reassignWindow.open();

    }

    getLabUsers() {
        let params: URLSearchParams = new URLSearchParams();
        params.set("idLab", this.dragEndLab.id);

        var lPromise = this.experimentService.getLab(params).toPromise();
        lPromise.then(response => {
            this.buildLabMembers(response);
        })

    }

    buildLabMembers(response: any) {
        //this.isNewLab = this.dragEndLab.label != this.dragStartLab.label;
        this.labMembers = [];
        this.billingAccounts = [];
        var i: number = 0;
        for (let u of response.members) {
            if (u.isActive == 'Y') {
                this.labMembers[i] = u;
                u.label = u.firstLastDisplayName;
                i++;
            }
        }

        for (let b of response.authorizedBillingAccounts) {
            if (b.idCoreFacility === this.idCoreFacility &&
                b.isApproved == "Y" && b.isActive=="Y") {
                b.label = b.accountName;
                this.billingAccounts.push(b);
            }
        }

        for (let u of response.managers) {
            var found = false;

            for (let fl of this.labMembers) {
                if (u.firstLastDisplayName.indexOf(fl.firstLastDisplayName) > 0 ) {
                    found = true;
                    break;
                }

            }
            if (!found) {
                if(u.isActive=='Y') {
                    u.label = u.firstLastDisplayName;
                    this.labMembers.push(u);
                }
            }
        }
        if (this.labMembers.length < 1) {
            this.resetTree();
            this.msgNoAuthUsersForLab.open();
        } else {
            this.showReassignWindow();
        }

    }

    resetTree() {
        this.records = this.dragEndItems;
    }

    saveProjectRequest(params: URLSearchParams) {
        var lPromise = this.experimentService.saveProjectRequest(params).toPromise();
        lPromise.then(response => {
            console.log("saveprojectrequest "+response);
            this.resetComboBoxes();
            this.reassignWindow.close();
        })

    }

    dragStartTree(event: any): void {
        this.onDragStart(event);
    };

    dragEndTree(event: any): void {
        this.onDragEnd(event);
    };

    dragEnd(item, dropItem, args, dropPosition, tree): boolean {
        if (dropItem ) {
            this.dropItem = dropItem;
            this.it = item;
        }
        return true;
    };


    dragStart(item): boolean {
        console.log("item "+item);
        return true;
    };

    yesButtonClicked(): void {
        if (this.selectedIndex === -1) {
            this.msgSelectOwner.open();
            this.reassignWindow.close();
        } else if (this.selectedBillingIndex == -1 && this.showBillingCombo) {
            this.msgSelectBilling.open();
            this.reassignWindow.close();
        }
        else {
            this.selectedIndex = -1;
            let params: URLSearchParams = new URLSearchParams();
            params.set("idRequest", this.currentItem.id);
            params.set("idProject", this.targetItem.id);
            var appUserId = this.getAppUserId(this.selectedItem);
            params.set("idAppUser", appUserId);
            var idBillingAccount = this.getBillingAccountId(this.selectedBillingItem);
            params.set("idBillingAccount", idBillingAccount);
            this.saveProjectRequest(params);
        }
    }

    resetComboBoxes () {
        this.labMembers = [];
        this.billingAccounts = [];
    }

    noButtonClicked(): void {
        this.records = this.dragEndItems;
        this.reassignWindow.close();

    }

    getAppUserId(userName: string): any {
        for (var l of this.labMembers) {
            if (l.firstLastDisplayName === userName) {
                return l.idAppUser;
            }
            console.log("user "+l);
        }
        return null;
    }

    getBillingAccountId(accountName: string): any {
        for (var b of this.billingAccounts) {
            if (b.accountName === accountName) {
                return b.idBillingAccount;
            }
        }
        return null;
    }

    onOwnerSelect(event: any): void {
        let args = event.args;
        if (args != undefined) {
            this.selectedItem = event.args.item.value;
            this.selectedIndex = event.args.index;
        }
    }

    eventWindowClose(event: any): void {
        console.log("donne");

    }

    eventWindowOpen(event: any): void {
        console.log("donne");

    }

    clickReassign(event: any) {
        this.items = this.dragEndItems;
        this.reassignWindow.open();

    }

    clickNoProject(event: any) {
        this.newProjectWindow.show();
    }

    clickNewProject(event: any) {
        this.items = this.dragEndItems;
        this.newProjectWindow.open();
    }

    showEmptyFoldersChange(event: any): void {
        let checked = event.args.checked;
        let params: URLSearchParams = new URLSearchParams();

        if (checked) {
            params.set("showCategory", 'N');
            params.set("showEmptyProjectFolders", 'Y');
            params.set("allExperiments", 'Y');
            params.set("showSamples", 'N');
            params.set("idCoreFacility", '3');

            var lPromise = this.experimentService.getProjectRequestList(params).toPromise();

            lPromise.then(response => {
                this.buildTree(response);            })
        }
        else {
            params.set("showCategory", 'N');
            params.set("showEmptyProjectFolders", 'N');
            params.set("allExperiments", 'Y');
            params.set("showSamples", 'N');
            params.set("idCoreFacility", '3');

            var lPromise = this.experimentService.getProjectRequestList(params).toPromise();

            lPromise.then(response => {
                this.buildTree(response);            })
        }
    }


    newProjectClicked(event: any) {
        this.newProjectWindow.open();
    }

    noProjectButtonClicked() {
        this.newProjectWindow.close();
    }

    deleteProjectClicked(event: any) {
        this.deleteProjectWindow.open();

    }

    deleteProjectNoButtonClicked() {
        this.deleteProjectWindow.close();
    }

    deleteProjectYesButtonClicked() {
        let params: URLSearchParams = new URLSearchParams();
        params.set("idProject", this.selectedItem.id);

        var lPromise = this.experimentsService.deleteProject(params).toPromise();
        lPromise.then( response => {
            this.deleteProjectWindow.close();
            this.refreshProjectRequestList();
        })


    }

    refreshProjectRequestList() {

        var lPromise = this.experimentsService.getExperiments().toPromise();
        this.showEmptyCheckBox.checked(false);
        lPromise.then( response => {
            this.buildTree(response);
        })
    }

    saveProject(project: any) {
        let params: URLSearchParams = new URLSearchParams();


        project.name = this.projectName;
        project.projectDescription = this.projectDescription;

        params.set("projectXMLString", project);
        params.set("parseEntries", "Y");
        if (!this.projectName) {
            this.msgEnterProjectName.open();
        } else {

            var lPromise = this.experimentService.saveProject(params).toPromise();
            lPromise.then(response => {
                this.refreshProjectRequestList();
            })
        }
        this.newProjectWindow.close();

    }

    getProject() {
        let projectId:any = 0;
        let item: any;
        let project: any = 0;

        let params: URLSearchParams = new URLSearchParams();
        params.set("idLab", this.selectedProjectLabItem.idLab);
        params.set("idProject", projectId);

        var lPromise = this.experimentService.getProject(params).toPromise();
        lPromise.then(response => {
            this.saveProject(response.Project);
        })


    }


    saveProjectButtonClicked() {

        this.getProject();
    }

    onBillingSelect(event: any): void {
        let args = event.args;
        if (args != undefined) {
            this.selectedBillingItem = event.args.item.value;
            this.selectedBillingIndex = event.args.index;
        }
    }

    onProjectLabSelect(event: any): void {
        let args = event.args;
        if (args != undefined) {
            this.selectedProjectLabItem= event.args.item.originalItem;
            this.selectedProjectLabIndex = event.args.index;
        }
    }


    treeOnSelect(event: any) {
        console.log("event");
        let args = event.args;
        this.selectedItem = this.tree.getItem(args.element);

        if (this.selectedItem.level == 0) {
            this.newProject.disabled(true);
            this.deleteProject.disabled(true);
        } else if (this.selectedItem.level == 1) {
            this.newProject.disabled(false);
            this.deleteProject.disabled(false);
        } else {
            this.newProject.disabled(true);
            this.deleteProject.disabled(true);
        }
    }

    expandCollapseClicked(): void {
        setTimeout(_ => {
            let toggled = this.toggleButton.toggled();

            if (toggled) {
                this.toggleButton.val('Expand');
                this.tree.collapseAll();
            }
            else {
                this.toggleButton.val('Collapse');
                this.tree.expandAll();
            }
        });
    };

    dragDropHintClicked() {
        this.msgDragDropHint.open();
    }

    treeOnInitialized() {
        // this.tree.expandAll();
        // this.deleteProject.disabled(true);
        // this.newProject.disabled(true);
        //this.msgLoading.open();
    }
}