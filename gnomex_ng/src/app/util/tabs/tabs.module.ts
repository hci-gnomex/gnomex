import { NgModule } from '@angular/core'
import { CommonModule } from '@angular/common'
import { RouterModule } from '@angular/router'
import { FormsModule, ReactiveFormsModule } from '@angular/forms'

import {
    Tab,
    Tabs,
    PrimaryTab,
    TabContainer,
    TabChangeEvent,
    TabsStatusEvent
} from './index'



@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule
    ],

    declarations: [
        Tab,
        Tabs,
        PrimaryTab,
        TabContainer
    ],
    providers: [
    ],
    entryComponents: [Tab,Tabs,PrimaryTab],
    exports: [CommonModule,TabContainer,PrimaryTab]
})
export class TabsModule { }