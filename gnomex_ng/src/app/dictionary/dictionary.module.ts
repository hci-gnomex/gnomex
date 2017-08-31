import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {DictionaryDemoComponent} from "./dictionary-demo.component";
import {ComboBoxModule} from "../../modules/combobox.module";
import {DICTIONARY_DEMO_ROUTING} from "./dictionary-demo.routes";

@NgModule({
    imports: [CommonModule, ComboBoxModule, DICTIONARY_DEMO_ROUTING],
    declarations: [DictionaryDemoComponent]
})

export class DictionaryModule {
}
