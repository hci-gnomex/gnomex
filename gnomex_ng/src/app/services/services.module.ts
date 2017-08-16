import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {AppUserListService} from "./app-user-list.service";
import {CreateSecurityAdvisorService} from "./create-security-advisor.service";
import {GetLabService} from "./get-lab.service";
import {LabListService} from "./lab-list.service";

@NgModule({
    imports: [CommonModule],
    declarations: [],
    exports: [],
    providers: [
        AppUserListService,
        CreateSecurityAdvisorService,
        GetLabService,
        LabListService
    ]
})
export class ServicesModule {
}