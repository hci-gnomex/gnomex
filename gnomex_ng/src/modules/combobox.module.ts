import { NgModule }       from '@angular/core';
import { CommonModule }   from '@angular/common';

import { jqxListBoxComponent }  from '../assets/jqwidgets-ts/angular_jqxlistbox';
import { jqxComboBoxComponent } from '../assets/jqwidgets-ts/angular_jqxcombobox';

@NgModule({
    imports: [CommonModule],
    declarations: [jqxListBoxComponent, jqxComboBoxComponent],
    exports: [jqxListBoxComponent, jqxComboBoxComponent],
})
export class ComboBoxModule { }