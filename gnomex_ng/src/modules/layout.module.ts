import { NgModule }       from '@angular/core';
import { CommonModule }   from '@angular/common';

//import { jqxTreeComponent } from 'jqwidgets-framework';
import {jqxLayoutComponent } from '../assets/jqwidgets-ts/angular_jqxlayout';

@NgModule({
    imports: [CommonModule],
    declarations: [jqxLayoutComponent],
    exports: [jqxLayoutComponent],
})
export class LayoutModule { }

