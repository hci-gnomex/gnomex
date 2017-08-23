import {
    Component, EventEmitter, Output, Input, ViewChild, ViewContainerRef, ComponentFactory, ComponentRef,
    ComponentFactoryResolver, Type
} from '@angular/core';
import { Tab } from './tab.component';
import { TabChangeEvent } from './tab-change-event'
import { TabContainer } from './tab-container.component'
import {PrimaryTab} from "./primary-tab.component";
import {PrepTab} from "../../experiments/experiment-detail/prep-tab.component";
import {FormGroup} from "@angular/forms";

@Component({
    selector: 'tabs',

    template: `
            
            <ul #container class="nav nav-tabs">
                <li class="nav-item" *ngFor="let tab of tabs"  [class.active]="tab.active" [class.disabled]="!tab.enable" >
                    <a class="nav-link" (click)="selectTab(tab)" role="tab" [ngClass]="{selecting:true, active:isActive(tab)}" 
                       [class.active]="tab.enable" [class.disabled]="!tab.enable">
                        {{tab.title}}</a>
                </li>
            </ul>
            <ng-content></ng-content>
  `

})
export class Tabs {

    tabs: Tab[];
    activeTabId: number;
    state:string;
    private projectableNodeList:Array<any> = [];
    @ViewChild('container', {read: ViewContainerRef}) tabsContainer:ViewContainerRef

    @Output() tabChange = new EventEmitter<TabChangeEvent>();

    constructor(private compFR:ComponentFactoryResolver){

    }

    initContent(tabs: Tab[]) {
        this.tabs = tabs;
        // get all active tabs
        let activeTabs = this.tabs.filter((tab) => tab.active);
        if(this.state === TabContainer.NEW){
            for(let i = 0; i < tabs.length; i++ ){
                if(i === 0){
                    tabs[i].enable = true;
                }
                else{
                    tabs[i].enable = false;
                }

            }
        }

        // if there is no active tab set, activate the first
        if (activeTabs.length === 0) {
            this.selectTab(this.tabs[0]);
        }

    }
    insertTab(component: Type<PrimaryTab>,parentForm:FormGroup,state:string){
        let compFactory =this.compFR.resolveComponentFactory(component);
        let compRef = this.tabsContainer.createComponent(compFactory);
        compRef.instance.theForm = parentForm;
        compRef.instance.setState(state)

        let tabFactory = this.compFR.resolveComponentFactory(Tab);
        let tabRef = this.tabsContainer.createComponent(tabFactory, 0,undefined,[[compRef.location.nativeElement]]);
        tabRef.instance.title = compRef.instance.name;
        tabRef.instance.initComp(compRef);

        this.tabs.push(tabRef.instance);
        tabRef.changeDetectorRef.detectChanges();

    }

    addTab(tab: Tab) {
        this.tabs.push(tab)
    }

    selectTabById(id: number) {
        // need try catch here
        if (id < this.tabs.length && id > -1) {
            this.selectTab(this.tabs[id]);
        }

    }
    selectTab(tab: Tab) {
        // deactivate all tabs
        let selectedTabIndex: number = -1;

        for (let i = 0; i < this.tabs.length; i++) {
            this.tabs[i].active = false;
            this.tabs[i].getComp().tabIsActive = false;
            if (tab == this.tabs[i]) {
                selectedTabIndex = i;
            }
        }


        if (selectedTabIndex != -1 && this.tabs[selectedTabIndex].enable &&
            this.activeTabId != selectedTabIndex) {

            let defaultPrevented = false;

            this.tabChange.emit(
                { activeTabId: this.activeTabId, nextId: selectedTabIndex, preventDefault: () => { defaultPrevented = true; } });

            if (!defaultPrevented) {
                this.activeTabId = selectedTabIndex;
                tab.active = true;
                tab.getComp().tabIsActive= true;
                tab.getComp().setState(this.state); // need to run state actions like disable components when tab becomes active
            }

        }
        else{ // tab did not change
            this.tabs[this.activeTabId].active = true;
            this.tabs[this.activeTabId].getComp().tabIsActive= true;

        }

        console.log(this.tabs);
        // activate the tab the user has clicked on.

    }
    isActive(tab:Tab):boolean{
        if(tab.active){
            return true
        }
        else{
            return false;
        }
    }

}

