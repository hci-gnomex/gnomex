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
    private label: string;

    private fromValue: string;
    private toValue: string;

    @Output() fromChange = new EventEmitter();
    @Output() toChange = new EventEmitter();

    private fromDate: any;
    private toDate: any;

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
        this.determineLabel();
    }

    reset(): void {
        this.from = "";
        this.fromDate = null;
        this.to = "";
        this.toDate = null;
    }

    onCalendarChange(event: any): void {
        let range = event.args.range;
        if (range.from != null && range.to != null) {
            this.from = range.from.toLocaleDateString();
            this.fromDate = range.from;
            this.to = range.to.toLocaleDateString();
            this.toDate = range.to;
        } else {
            this.reset();
        }
    }

    clear(): void {
        this.calendar.clear();
    }

    toggleShowCalendar(): void {
        this.showCalendar = !this.showCalendar;
        this.determineLabel();
        if (this.showCalendar && !(this.fromDate === null) && !(this.toDate === null)) {
            setTimeout(() => {
                this.calendar.setRange(this.fromDate, this.toDate);
            });
        }
    }

    determineLabel(): void {
        if (!(this.from === "") && !(this.to === "")) {
            this.label = this.from + " - " + this.to;
        } else {
            this.label = "Filter by date";
        }
    }

}