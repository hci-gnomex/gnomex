import { NgModule }       from '@angular/core';
import { CommonModule }   from '@angular/common';

import { jqxGridComponent } from "../assets/jqwidgets-ts/angular_jqxgrid";

@NgModule({
	imports: [CommonModule],
	declarations: [jqxGridComponent],
	exports: [jqxGridComponent],
})
export class JqxGridModule { }

