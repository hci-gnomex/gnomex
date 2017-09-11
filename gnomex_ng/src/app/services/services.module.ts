import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {AppUserListService} from "./app-user-list.service";
import {CreateSecurityAdvisorService} from "./create-security-advisor.service";
import {GetLabService} from "./get-lab.service";
import {LabListService} from "./lab-list.service";
import {AnalysisService} from "./analysis.service";
import {DictionaryService} from "./dictionary.service";
import {DataTrackService} from "./data-track.service";

@NgModule({
    imports: [CommonModule],
    declarations: [],
    exports: [],
    providers: [
        AppUserListService,
        CreateSecurityAdvisorService,
        GetLabService,
        LabListService,
        AnalysisService,
        DictionaryService,
        DataTrackService
    ]
})
export class ServicesModule {
}