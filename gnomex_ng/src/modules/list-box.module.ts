import { NgModule }       from '@angular/core';
import { CommonModule }   from '@angular/common';

//import { jqxTreeComponent } from 'jqwidgets-framework';
import { jqxListBoxComponent } from '../assets/jqwidgets-ts/angular_jqxlistbox';

@NgModule({
    imports: [CommonModule],
    declarations: [jqxListBoxComponent],
    exports: [jqxListBoxComponent],
})
export class ListBoxModule { }
