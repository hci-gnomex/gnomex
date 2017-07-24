import { NgModule }       from '@angular/core';
import { CommonModule }   from '@angular/common';

//import { jqxExpanderComponent } from 'jqwidgets-framework';
import { jqxExpanderComponent } from '../assets/jqwidgets-ts/angular_jqxexpander';

@NgModule({
    imports: [CommonModule],
    declarations: [jqxExpanderComponent],
    exports: [jqxExpanderComponent],
})
export class ExpanderModule { }

