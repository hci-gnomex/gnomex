/*
 * Copyright (c) 2016 Huntsman Cancer Institute at the University of Utah, Confidential and Proprietary
 */
import {Component, ViewChild, ViewEncapsulation} from "@angular/core";

import { URLSearchParams } from "@angular/http";

import {ExperimentsService} from "./experiments.service";
import { jqxWindowComponent } from "jqwidgets-framework";
import { jqxButtonComponent } from "jqwidgets-framework";
import { jqxComboBoxComponent } from "jqwidgets-framework";
import { jqxNotificationComponent  } from "jqwidgets-framework";
import { jqxCheckBoxComponent } from "jqwidgets-framework";
import {jqxLoaderComponent} from "jqwidgets-framework";
import {TreeComponent, ITreeOptions, TreeNode, TreeModel} from "angular-tree-component";

import * as _ from "lodash";

@Component({
    selector: "experiments",
    templateUrl: "./experiments.component.html",
    styles: [`
        .inlineComboBox {
            display: inline-block;
        }

        .hintLink
        {
            fontSize: 9;
            paddingLeft: 1;
            paddingRight: 1;
            paddingBottom: 1;
            paddingTop: 1;
        }

        .sidebar {
            width: 25%;
            position: relative;
            left: 0;
            background-color: #ccc;
            transition: all .25s;
        }

        .container {
            display: flex;
            min-height:100px;
        }

        .t {
            display: table;
            width: 100%;
        }

        .tr {
            display: table-row;
            width: 100%;
        }

        .td {
            display: table-cell;
        }

        .jqx-tree {
            height: 100%;
        }

        .jqx-notification {
            margin-top: 30em;
            margin-left: 20em;
        }

        //.jqx-notification {
          //  height: 10em;
          //  position: relative }
        //.jqx-notification p {
          //  margin: 0;
          //  background: yellow;
          //  position: absolute;
          //  top: 50%;
          //  left: 50%;
          //  margin-right: -50%;
          //  transform: translate(-50%, -50%) }

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
    `]
})
export class BrowseExperimentsComponent {

    @ViewChild("tree") treeComponent: TreeComponent;
    @ViewChild("reassignWindow") reassignWindow: jqxWindowComponent;
    @ViewChild("responseMsgWindow") responseMsgWindow: jqxWindowComponent;
    @ViewChild("msgSelectOwner") msgSelectOwner: jqxNotificationComponent;
    @ViewChild("msgSelectBilling") msgSelectBilling: jqxNotificationComponent;
    @ViewChild("msgNoAuthUsersForLab") msgNoAuthUsersForLab: jqxNotificationComponent;
    @ViewChild("msgEnterProjectName") msgEnterProjectName: jqxNotificationComponent;
    @ViewChild("msgDragDropHint") msgDragDropHint: jqxNotificationComponent;
    @ViewChild("msgEnterLab") msgEnterLab: jqxNotificationComponent;
    @ViewChild("newProjectWindow") newProjectWindow: jqxWindowComponent;
    @ViewChild("deleteProjectWindow") deleteProjectWindow: jqxWindowComponent;
    @ViewChild("toggleButton") toggleButton: jqxButtonComponent;
    @ViewChild("showEmptyCheckBox") showEmptyCheckBox: jqxCheckBoxComponent;

    @ViewChild("deleteProject") deleteProject: jqxButtonComponent;
    @ViewChild("newProject") newProject: jqxButtonComponent;
    @ViewChild("labComboBox") labComboBox: jqxComboBoxComponent;
    @ViewChild("yesButtonDeleteProject") yesButtonDeleteProject: jqxButtonComponent;
    @ViewChild("deleteProjectNoButtonClicked") deleteProjectNoButton: jqxButtonComponent;
    @ViewChild("jqxLoader") jqxLoader: jqxLoaderComponent;
    @ViewChild("jqxConstructorLoader") jqxConstructorLoader: jqxLoaderComponent;

    private treeModel: TreeModel;
    /*
    angular2-tree options
     */
    private options: ITreeOptions = {
        displayField: "label",
        childrenField: "items",
        useVirtualScroll: true,
        nodeHeight: 22,
        nodeClass: (node: TreeNode) => {
            return "icon-" + node.data.icon;
        },
        allowDrop: (element, { parent, index }) => {
            this.dragEndItems = _.cloneDeep(this.items);
            if (parent.data.labName) {
                return false;
            } else {
                return true;
            }
        },

        allowDrag: (node) => node.isLeaf,
    };

    private items: any;
    private labs: any;
    private isClose = true;
    private responseMsg: string = "";
    private currentItem: any;
    private targetItem: any;
    private projectDescription: string = "";
    private projectName: string = "";
//    private projectLabName: string = "";
    private experimentService: ExperimentsService;
    private labMembers: any;
    private billingAccounts: any;
    private dragEndItems: any;
    private selectedItem: any;
    private selectedIndex: number = -1;
    private selectedBillingItem: string;
    private selectedBillingIndex: number = -1;
    private selectedProjectLabItem: any;
    private selectedProjectLabIndex: number = -1;
    private idCoreFacility: string = "3";
    private showBillingCombo: boolean = false;
    private experimentCount: number;

    ngOnInit() {
        this.treeModel = this.treeComponent.treeModel;
    }
    constructor(private experimentsService: ExperimentsService) {
        this.experimentService = experimentsService;

        this.experimentsService.getExperiments().subscribe(response => {
            this.buildTree(response);
            //this.thisResponse = response;
        });
        this.items = [];
        this.dragEndItems = [];
        this.labMembers = [];
        this.billingAccounts = [];
        this.labs = [];

    }

    go(event: any) {
        console.log("event " + event);
    }
    /*
    Build the tree data
    @param
        what
     */
    buildTree(response: any[]) {
        this.experimentCount = 0;
        if (!this.isArray(response)) {
            this.items = [response];
        } else {
            this.items = response;
        }
        this.labs = this.labs.concat(this.items);
        for( var l of this.items) {
            if (!this.isArray(l.Project)) {
                l.items = [l.Project];
            } else {
                l.items = l.Project;
            }
            l.id = l.idLab;
            l.parentid = -1;

            l.icon = "assets/group.png";
            for( var p of l.items) {
                p.icon = "assets/folder.png";
                p.labId = l.labId;
                p.id = p.idProject;
                p.parentid = l.id;
                if (p.Request) {
                    if (!this.isArray(p.Request)) {
                        p.items = [p.Request];
                    } else {
                        p.items = p.Request;
                    }
                    for (var r of p.items) {
                        if (r) {
                            if (r.label) {
                                var shortLabel = r.label.substring(0, (r.label.lastIndexOf("-")));
                                var shorterLabel = shortLabel.substring(0, shortLabel.lastIndexOf("-"));
                                r.label = shorterLabel;
                                this.experimentCount++;
                                r.id = r.idRequest;
                                r.parentid = p.id;
                                if (this.experimentCount % 100 === 0) {
                                    console.log("experiment count " + this.experimentCount);
                                }
                            } else {
                                console.log("label not defined");
                            }
                        } else {
                            console.log("r is undefined");
                        }
                    }
                } else {
                    console.log("");
                }
            }
        }
        this.jqxLoader.close();
        this.jqxConstructorLoader.close();
    };

    /*

    Start of Ng2 tree
     */

    onMoveNode($event) {
        console.log(
            "Moved",
            $event.node.name,
            "to",
            $event.to.parent.name,
            "at index",
            $event.to.index);
        this.currentItem = $event.node;
        this.targetItem = $event.to.parent;
        this.getLabUsers($event);
    }

    onActiveChangedEvent($event) {
        console.log("event is " + event);

    }

    onDragEnd1($event) {
        console.log("event is " + event);
    }

    /*
        Determine if the object is an array
        @param what
     */
    isArray(what) {
        return Object.prototype.toString.call(what) === "[object Array]";
    };

    detailFn(): (keywords: string) => void {
        return (keywords) => {
            window.location.href = "http://localhost/gnomex/experiments/" + keywords;
        };
    }

    showReassignWindow() {
        this.reassignWindow.open();
    }

    /**
     * Get the target lab users. Set showBillingCombo.
     * @param event
     */
    getLabUsers(event: any) {
        if (event.node.isExternal === "N" && event.node.idLab === event.to.parent.idLab) {
            this.showBillingCombo = false;
        } else {
            this.showBillingCombo = true;
        }
        let params: URLSearchParams = new URLSearchParams();
        params.set("idLab", event.to.parent.idLab);

        var lPromise = this.experimentService.getLab(params).toPromise();
        lPromise.then(response => {
            this.buildLabMembers(response, event);
        });

    }

    /**
     * Build the users that are in the reassign Labs.
     * @param response
     * @param event
     */
    buildLabMembers(response: any, event: any) {
        this.labMembers = [];
        this.billingAccounts = [];
        var i: number = 0;
        for (let u of response.members) {
            if (u.isActive === "Y") {
                this.labMembers[i] = u;
                u.label = u.firstLastDisplayName;
                i++;
            }
        }

        for (let b of response.authorizedBillingAccounts) {
            if (b.idCoreFacility === this.idCoreFacility &&
                b.isApproved === "Y" && b.isActive === "Y") {
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
                if(u.isActive === "Y") {
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

    /**
     * Reset the tree to the initial state.
     */
    resetTree() {
        this.items = this.dragEndItems;
    }

    /**
     * Save the project request created in the reassign dialog
     * @param {URLSearchParams} params
     */
    saveRequestProject(params: URLSearchParams) {
        var lPromise = this.experimentService.saveRequestProject(params).toPromise();
        lPromise.then(response => {
            // if (response.text().indexOf("billingAccountMessage") !== -1) {
            //     this.responseMsg = response.text().substring(response.text().indexOf("billingAccountMessage")+"billingAccountMessage".length+4, response.text().indexOf("}")-2);
            //     this.responseMsgWindow.show();
            // }
            console.log("saveprojectrequest " + response);
            this.resetComboBoxes();

        });

    }

    /**
     * Yes button clicked on the reassign.
     */
    yesButtonClicked(): void {
        if (this.selectedIndex === -1) {
            this.msgSelectOwner.open();
            this.reassignWindow.close();
        } else if (this.selectedBillingIndex === -1 && this.showBillingCombo) {
            this.msgSelectBilling.open();
            this.reassignWindow.close();
        } else {
            this.isClose = false;
            this.selectedIndex = -1;
            let params: URLSearchParams = new URLSearchParams();
            params.set("idRequest", this.currentItem.id);
            params.set("idProject", this.targetItem.id);
            var appUserId = this.getAppUserId(this.selectedItem);
            params.set("idAppUser", appUserId);
            var idBillingAccount = this.getBillingAccountId(this.selectedBillingItem);
            params.set("idBillingAccount", idBillingAccount);
            this.saveRequestProject(params);
        }
    }

    /**
     * Reset the data for the reassign window.
     */
    resetComboBoxes () {
        this.labMembers = [];
        this.billingAccounts = [];
        //this.treeModel.update();
        //this.refreshProjectRequestList();
        this.reassignWindow.close();
    }

    /**
     * The no button was selected in the reassign dialog.
     */
    noButtonClicked(): void {
        this.isClose = true;
        // this.resetTree();
        this.reassignWindow.close();

    }

    /**
     * Return the idAppUser
     * @param {string} userName
     * @returns {any}
     */
    getAppUserId(userName: string): any {
        for (var l of this.labMembers) {
            if (l.firstLastDisplayName === userName) {
                return l.idAppUser;
            }
            console.log("user " + l);
        }
        return null;
    }

    /**
     * Return the billing account id.
     * @param {string} accountName
     * @returns {any}
     */
    getBillingAccountId(accountName: string): any {
        for (var b of this.billingAccounts) {
            if (b.accountName === accountName) {
                return b.idBillingAccount;
            }
        }
        return null;
    }

    /**
     * On select of the owner combo box of the reassign window.
     * @param event
     */
    onOwnerSelect(event: any): void {
        let args = event.args;
        if (args !== undefined && event.args.item) {
            this.selectedItem = event.args.item.value;
            this.selectedIndex = event.args.index;
        }
    }

    /**
     * Reassign window close event. If the 'x' reset the tree.
     * @param event
     */
    reassignWindowClose(event: any): void {
        if (this.isClose) {
            this.resetTree();
        }
        this.isClose = true;
    }

    /**
     * Start over. Data is missing.
     * @param event
     */
    resetReassign(event: any) {
        this.isClose = true;
        this.resetTree();
        this.reassignWindow.open();
    }

    /**
     * When the ShowEmptyFolders checkbox is selected.
     * @param event
     */
    showEmptyFoldersChange(event: any): void {
        this.jqxLoader.open();

        let checked = event.args.checked;
        let params: URLSearchParams = new URLSearchParams();
        //TODO
        // When merged with the filter this will change
        params.set("showCategory", "N");
        params.set("allExperiments", "Y");
        params.set("showSamples", "N");
        params.set("idCoreFacility", "3");

        if (checked) {
            params.set("showEmptyProjectFolders", "Y");

            let lPromise = this.experimentService.getProjectRequestList(params).toPromise();

            lPromise.then(response => {
                this.buildTree(response);            });
        } else {
            params.set("showEmptyProjectFolders", "N");

            let lPromise = this.experimentService.getProjectRequestList(params).toPromise();

            lPromise.then(response => {
                this.buildTree(response);            });
        }
        console.log("end");
    }

    /**
     * The new project link is selected.
     * @param event
     */
    newProjectClicked(event: any) {
        this.setLabName(event);
        this.newProjectWindow.open();
    }

    /**
     * Select the lab in the new project window.
     * @param event
     */
    setLabName(event: any) {
        // Lab
        if (this.selectedItem.level === 1) {
            this.labComboBox.selectItem(this.selectedItem.data.label);
            // Project
        } else if (this.selectedItem.level === 2) {
            this.labComboBox.selectItem(this.selectedItem.parent.data.label);
        }
    }

    /**
     * The no button was select in the new project window.
     */
    noProjectButtonClicked() {
        this.newProjectWindow.close();
    }

    /**
     * The delete project link was selected.
     * @param event
     */
    deleteProjectClicked(event: any) {
        this.deleteProjectWindow.open();
    }

    /**
     * The no button was selected in the delete project window.
     */
    deleteProjectNoButtonClicked() {
        this.deleteProjectWindow.close();
    }

    /**
     * The yes button was selected in the delete project window.
     */
    deleteProjectYesButtonClicked() {
        let params: URLSearchParams = new URLSearchParams();
        params.set("idProject", this.selectedItem.id);

        var lPromise = this.experimentsService.deleteProject(params).toPromise();
        lPromise.then( response => {
            this.deleteProjectWindow.close();
            this.refreshProjectRequestList();
        });
    }

    /**
     * Refresh the tree.
     */
    refreshProjectRequestList() {

        var lPromise = this.experimentsService.getExperiments().toPromise();
        this.showEmptyCheckBox.checked(false);
        lPromise.then( response => {
            this.buildTree(response);
        });
    }

    /**
     * Save the new project.
     * @param project
     */
    saveProject(project: any) {
        let params: URLSearchParams = new URLSearchParams();


        project.name = this.projectName;
        project.projectDescription = this.projectDescription;
        //TODO
        // Need to get idAppUser. Flex did this like: parentApplication.getIdAppUser();
        params.set("projectXMLString", project);
        params.set("parseEntries", "Y");
        if (!this.projectName) {
            this.msgEnterProjectName.open();
        } else {

            var lPromise = this.experimentService.saveProject(params).toPromise();
            lPromise.then(response => {
                this.refreshProjectRequestList();
            });
        }
        this.newProjectWindow.close();

    }

    /**
     * Get the project.
     */
    getProject() {
        let idProject: any = 0;

        let params: URLSearchParams = new URLSearchParams();
        if (!this.selectedProjectLabItem) {
            this.msgEnterLab.open();
        } else {
//            let mylab = this.projectLabName;
            params.set("idLab", this.selectedProjectLabItem.idLab);
            params.set("idProject", idProject);

            let lPromise = this.experimentService.getProject(params).toPromise();
            lPromise.then(response => {
                this.saveProject(response.Project);
            });
        }
    }

    /**
     * Initiate the save project from the selection of the save button selected
     * in the new project window.
     */
    saveProjectButtonClicked() {
        this.getProject();
    }

    /**
     * On selection of the billing account combobox in the reassign window.
     * @param event
     */
    onBillingSelect(event: any): void {
        let args = event.args;
        if (args !== undefined && event.args.item) {
            this.selectedBillingItem = event.args.item.value;
            this.selectedBillingIndex = event.args.index;
        }
    }

    onProjectLabSelect(event: any): void {
        let args = event.args;
        if (args !== undefined) {
            this.selectedProjectLabItem = event.args.item.originalItem;
            this.selectedProjectLabIndex = event.args.index;
        }
    }

    /**
     * A node is selected in the tree.
     * @param event
     */
    treeOnSelect(event: any) {
        console.log("event");
//        let args = event.args;
        this.selectedItem = event.node;

        //Lab
        if (this.selectedItem.level === 1) {
            this.newProject.disabled(false);
            this.deleteProject.disabled(true);
            //Project
        } else if (this.selectedItem.level === 2) {
            this.newProject.disabled(false);
            this.deleteProject.disabled(false);
            //Experiment
        } else {
            this.newProject.disabled(true);
            this.deleteProject.disabled(true);
        }
    }

    /**
     * The expand collapse toggle is selected.
     */
    expandCollapseClicked(): void {
        setTimeout(_ => {
            let toggled = this.toggleButton.toggled();

            if (!toggled) {
                this.toggleButton.val("Expand Projects");
                this.treeModel.collapseAll();
            } else {
                this.toggleButton.val("Collapse Projects");
                this.treeModel.expandAll();
            }
        });
    };

    /**
     * Show the drag-drop hint.
     */
    dragDropHintClicked() {
        this.msgDragDropHint.open();
    }

    /**
     * Show the response from the back end.
     */
    responseMsgNoButtonClicked() {
        this.responseMsg = "";
        this.responseMsgWindow.close();
    }
}
