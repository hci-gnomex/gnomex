import {NgModule} from "@angular/core";
import {FormsModule} from "@angular/forms";
import {CommonModule} from "@angular/common";
import {DateRangePickerComponent} from "./date-range-picker.component";
import {BrowseFilterComponent} from "./browse-filter.component";
import {ComboBoxModule} from '../../modules/combobox.module';
import {CalendarModule} from "../../modules/calendar.module";

@NgModule({
    imports: [CommonModule, CalendarModule, ComboBoxModule, FormsModule],
    declarations: [DateRangePickerComponent, BrowseFilterComponent],
    exports: [DateRangePickerComponent, BrowseFilterComponent]
})
export class UtilModule {
}