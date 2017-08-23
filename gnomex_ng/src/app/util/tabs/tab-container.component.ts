import {
    OnInit,
    Directive,
    ChangeDetectorRef,
    ViewContainerRef,
    ComponentFactoryResolver,
    ComponentRef,
    OnDestroy,
    Input,
    Output,
    EventEmitter,
    Type,ViewRef
} from '@angular/core'
import {FormGroup,FormBuilder, Validators,AbstractControl } from '@angular/forms';
import { ActivatedRoute } from '@angular/router'
import { Tabs } from "./tabs.component"
import { Tab } from "./tab.component"
import { PrimaryTab } from './primary-tab.component'
import { TabChangeEvent } from './tab-change-event'
import { TabsStatusEvent } from './tab-status-event'
import {ComponentCommunicatorEvent} from './component-status-event'
import {PrepTab} from "../../experiments/experiment-detail/prep-tab.component";


@Directive({
    selector: 'tab-container, [tab-container]'})
export class TabContainer implements OnInit, OnDestroy {

    static readonly NEW: string = "new"
    static readonly VIEW: string = "view"
    static readonly EDIT: string = "edit"


    @Output() tabChanged: EventEmitter<TabChangeEvent> = new EventEmitter();
    @Output() tabStatusChanged: EventEmitter<TabsStatusEvent> = new EventEmitter();
    private tabRefList: Array<any> = []
    private tabsRef: ComponentRef<Tabs>
    private intialized: boolean = false
    private theForm:FormGroup;

    private _compName: Array<string>
    @Input() set componentNames(value: Array<string>) {
        this._compName = value;
    }
    get componentNames() {
        return this._compName
    }
    @Input() set state(value: string){
        this._state = value;
        if(this.intialized){
            this.tabsRef.instance.state = this._state;
            this.tabsRef.instance.tabs.forEach(tab =>{
                tab.getComp().setState(this._state);
                }
            )
        }
    }
    get state():string{
        return this._state;
    }
    private _state:string;



    constructor(private cdr: ChangeDetectorRef,
                private compFR: ComponentFactoryResolver,
                private viewContainer: ViewContainerRef,
                private route: ActivatedRoute,
                private fb: FormBuilder) {
        this.theForm = this.fb.group({
            childForms: this.fb.array([])
        });
    }

    private validIndexes(start: number, end: number, tabLength: number): boolean {
        if (start < 0) {
            return false;
        }
        if (end > tabLength) {
            return false;
        }
        return true;
    }

    private communicate(event: ComponentCommunicatorEvent, tabs: Tab[]): void {

        let tabId = -1;

        for (let i = 0; i < tabs.length; i++) {
            if (tabs[i].getComp() === event.component) {
                tabId = i;
                break;
            }
        }
        // this is a callback function
        const changeStatusByIndex = (status: boolean, start?: number, end?: number): void => {

            if (start != undefined && end != undefined) { // if they provide 0 or 1 for indexes 'if' sees that as true or false
                    if (this.validIndexes(start, end, tabs.length)) {
                        for (let i = start; i < end; i++) {
                            if(status){
                                tabs[i].enable = true;
                            }
                            else{
                                tabs[i].enable = false;
                            }

                        }
                    }
            }
            else { // no range provided just change status of next tab
                let nextId = this.activeId + 1 < tabs.length? this.activeId + 1 : -1;
                tabs[nextId].enable = status;
            }

        };


            changeStatusByIndex(event.status);

        // will change status, but handler that executes from the tab-container externally has last say
        this.tabStatusChanged.emit({currentStatus:event.status, statusOfTabs: changeStatusByIndex, tabId: tabId, tabLength: tabs.length});
    }


    private tabCommunication(tabs: Tab[]): void {
        tabs.map(tab => {
            tab.getComp().changeStatus.subscribe(event => this.communicate(event,tabs));
        });

    }

    private initTab(): void {

        let tabs: Tab[] = this.tabsRef.instance.tabs;

        let factories = <Array<Function>>Array.from(this.compFR['_factories'].keys()); //Getting a factories made from EntryComponent
        let compFactoryArray: Array<Type<PrimaryTab>> = []

        for (let i = 0; i < this.componentNames.length; i++) {
            compFactoryArray.push(<Type<PrimaryTab>>factories.find((x: any) => x.name === this.componentNames[i]))
        }

        compFactoryArray.forEach((tabComponent) => {
            this.tabsRef.instance.insertTab(tabComponent,this.theForm,this.state);
        });

        this.tabsRef.instance.state = this.state;
        this.tabsRef.instance.initContent(tabs);

        this.tabCommunication(tabs);
        this.tabsRef.instance.tabChange.subscribe(event => {
            this.tabChanged.emit(event);
        });

        /*if(!this.tabsRef){
            const tabsFactory = this.compFR.resolveComponentFactory(Tabs);
            this.tabsRef = this.viewContainer.createComponent(tabsFactory, 0, undefined, [this.tabRefList]);
            this.tabsRef.instance.state = this.state; // giving tabs state before init(important for 'new' state)
            this.tabsRef.instance.initContent(tabs);*/
    }
    open():void{
        this.tabsRef.instance.insertTab(PrepTab,this.theForm,this.state);
        this.initTab();
    }

    get activeId():number{
        return this.tabsRef.instance.activeTabId;
    }
    set activeId(activeId:number){
        this.tabsRef.instance.activeTabId = activeId;
    }
    public select(id:number){
        this.tabsRef.instance.selectTabById(id);
    }



    ngOnInit() {
        let tabsFactory = this.compFR.resolveComponentFactory(Tabs);
        this.tabsRef = this.viewContainer.createComponent(tabsFactory);
        this.tabsRef.instance.tabs = []

        if(this.state == TabContainer.NEW){
            this.open();
        }else{
            this.initTab();
        }

        this.intialized = true;
        //this.addTab()

    }
    ngOnChange() {
        if (this.intialized) {
            this.initTab();
        }

    }



    ngOnDestroy() {
        this.tabsRef.destroy();
    }



}