import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {DateRangePickerComponent} from "./date-range-picker.component";
import {CalendarModule} from "../../modules/calendar.module";

@NgModule({
    imports: [CommonModule, CalendarModule],
    declarations: [DateRangePickerComponent],
    exports: [DateRangePickerComponent]
})
export class UtilModule {
}