import { Routes, RouterModule } from "@angular/router";
import { DictionaryDemoComponent } from "./dictionary-demo.component";

const ROUTES: Routes = [
    { path: "dictionary-demo", component: DictionaryDemoComponent }
];

export const DICTIONARY_DEMO_ROUTING = RouterModule.forChild(ROUTES);
