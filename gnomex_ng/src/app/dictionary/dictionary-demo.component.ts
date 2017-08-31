import {Component, AfterViewInit, ViewChild} from "@angular/core";
import {DictionaryService} from "./dictionary.service";
import {jqxComboBox} from "jqwidgets-framework";
import {jqxComboBoxComponent} from "jqwidgets-framework";

@Component({
    selector: "dictionary-demo",
    templateUrl: "./dictionary-demo.component.html"
})

export class DictionaryDemoComponent {

    private types: any[];
    private entries: any[];
    private coreFacilities: any[];

    @ViewChild("type") type: jqxComboBoxComponent;
    @ViewChild("entry") entry: jqxComboBoxComponent;
    @ViewChild("coreFacility") coreFacility: jqxComboBoxComponent;

    private selectedType = null;
    private selectedEntry = null;
    private selectedJson = "";

    constructor(private dictionaryService: DictionaryService) {
    }

    onSelectType(): void {
        let newSelectedType = this.getSelectedItem(this.type);
        if (newSelectedType != this.selectedType) {
            this.selectedType = newSelectedType;
            this.loadEntries();
        }
    }

    onSelectEntry(): void {
        this.selectedEntry = this.getSelectedItem(this.entry);
        this.selectedJson = JSON.stringify(this.selectedEntry);
    }

    private getSelectedItem(component: jqxComboBoxComponent) {
        if (component != null && component.getSelectedItem() != null) {
            return component.getSelectedItem().originalItem;
        } else {
            return null;
        }
    }

    clearTypes(): void {
        this.types = null;
        this.entries = null;
        this.selectedType = null;
        this.selectedEntry = null;
        this.selectedJson = "";
    }

    loadAllTypes(): void {
        this.clearTypes();
        this.dictionaryService.getAllDictionaries().subscribe((response) => {
            this.types = response;
        });
    }

    loadEditableTypes(): void {
        this.clearTypes();
        this.dictionaryService.getEditableDictionaries().subscribe((response) => {
            this.types = response;
        });
    }

    loadEntries(): void {
        this.entries = null;
        this.selectedEntry = null;
        this.selectedJson = "";
        if (this.selectedType) {
            this.dictionaryService.getEntries(this.selectedType.className).subscribe((response) => {
                this.entries = response;
            });
        }
    }

    loadCoreFacilities(): void {
        this.coreFacilities = null;
        this.dictionaryService.coreFacilities().subscribe((response) => {
            this.coreFacilities = response;
        });
    }

    loadCoreFacilitiesExcludeBlank(): void {
        this.coreFacilities = null;
        this.dictionaryService.getEntriesExcludeBlank(DictionaryService.CORE_FACILITY).subscribe((response) => {
            this.coreFacilities = response;
        });
    }

    invalidateCache(): void {
        this.dictionaryService.invalidateCache();
    }

}
