import { Component,ComponentRef, Input, ContentChild, AfterContentInit, } from '@angular/core';
import {PrimaryTab} from './primary-tab.component'
// Probably in real app would move to a shared folder visible in app.module
@Component({
    selector: 'tab',
    styles: [`
    .pane{
      padding: 1em;
    }
  `],
    template: `
    <div [hidden]="!active" class="pane">
     <ng-content></ng-content>
    </div>
  `
})
export class Tab {
    @Input('tabTitle') title: string;
    @Input() active:boolean = false;
    @Input() enable:boolean = true;
    private theComponent:ComponentRef<PrimaryTab>;

    initComp(compRef:ComponentRef<PrimaryTab>):void{
        this.theComponent = compRef;
    }
    getComp():PrimaryTab{
        return this.theComponent.instance;
    }


}