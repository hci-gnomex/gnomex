import { NgModule }       from '@angular/core';
import { CommonModule }   from '@angular/common';

import { jqxNotificationComponent } from '../assets/jqwidgets-ts/angular_jqxnotification';

@NgModule({
    imports: [CommonModule],
    declarations: [jqxNotificationComponent],
    exports: [jqxNotificationComponent],
})
export class NotificationModule { }

