import { NgModule }       from '@angular/core';
import { CommonModule }   from '@angular/common';

//import { jqxTreeComponent } from 'jqwidgets-framework';
import { jqxTreeComponent } from '../assets/jqwidgets-ts/angular_jqxtree';

@NgModule({
    imports: [CommonModule],
    declarations: [jqxTreeComponent],
    exports: [jqxTreeComponent],
})
export class TreeModule { }

