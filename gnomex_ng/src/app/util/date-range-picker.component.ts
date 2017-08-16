import {Component, EventEmitter, Input, Output, ViewChild} from '@angular/core';
import {jqxCalendar} from "jqwidgets-framework";
import {jqxCalendarComponent} from "../../assets/jqwidgets-ts/angular_jqxcalendar";

@Component({
    selector: 'date-range-picker-custom',
    templateUrl: "./date-range-picker.component.html",
    styles: [require("./date-range-picker.component.less").toString()]
})

export class DateRangePickerComponent {
    @ViewChild('calendar') calendar: jqxCalendarComponent;

    private showCalendar: boolean;

    private fromValue: string;
    private toValue: string;

    @Output() fromChange = new EventEmitter();
    @Output() toChange = new EventEmitter();

    @Input()
    get from(): string {
        return this.fromValue;
    }

    set from(value: string) {
        this.fromValue = value;
        this.fromChange.emit(this.fromValue);
    }

    @Input()
    get to(): string {
        return this.toValue;
    }

    set to(value: string) {
        this.toValue = value;
        this.toChange.emit(this.toValue);
    }

    constructor() {
        this.showCalendar = false;
        this.reset();
    }

    reset(): void {
        this.from = "";
        this.to = "";
    }

    onCalendarChange(event: any): void {
        let range = event.args.range;
        if (range.from != null && range.to != null) {
            this.from = range.from.toLocaleDateString();
            this.to = range.to.toLocaleDateString();
        } else {
            this.reset();
        }
    }

    clear(): void {
        this.calendar.clear();
    }

    toggleShowCalendar(): void {
        this.showCalendar = !this.showCalendar;
        if (!this.showCalendar) {
            this.reset();
        }
    }

}