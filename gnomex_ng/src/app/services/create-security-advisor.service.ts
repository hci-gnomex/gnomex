import {Injectable} from "@angular/core";
import {Http, Response} from "@angular/http";
import {Observable} from "rxjs/Observable";

import 'rxjs/add/operator/map';
import {LabListService} from "./lab-list.service";
import {DictionaryService} from "./dictionary.service";

@Injectable()
export class CreateSecurityAdvisorService {
    private result: any;

    private idAppUserValue: number;
    public get idAppUser(): number {
        return this.idAppUserValue;
    }

    private isGuestValue: boolean;
    public get isGuest(): boolean {
        return this.isGuestValue;
    }

    private isAdminValue: boolean;
    public get isAdmin(): boolean {
        return this.isAdminValue;
    }

    private isSuperAdminValue: boolean;
    public get isSuperAdmin(): boolean {
        return this.isSuperAdminValue;
    }

    private isBillingAdminValue: boolean;
    public get isBillingAdmin(): boolean {
        return this.isBillingAdminValue;
    }

    private userNameValue: string;
    public get userName(): string {
        return this.userNameValue;
    }

    private loginDateTimeValue: string;
    public get loginDateTime(): string {
        return this.loginDateTimeValue;
    }

    private isUniversityOnlyUserValue: boolean;
    public get isUniversityOnlyUser(): boolean {
        return this.isUniversityOnlyUserValue;
    }

    private isUserActiveValue: boolean;
    public get isUserActive(): boolean {
        return this.isUserActiveValue;
    }

    private isExternalUserValue: boolean;
    public get isExternalUser(): boolean {
        return this.isExternalUserValue;
    }

    private versionValue: string;
    public get version(): string {
        return this.versionValue;
    }

    private myCoreFacilitiesValue: string[];
    public get myCoreFacilities(): any[] {
        if (this.myCoreFacilitiesValue === null) {
            return [];
        }
        let index: number;
        let result: any[] = [];
        for (index = 0; index < this.myCoreFacilitiesValue.length; index++) {
            result.push(JSON.parse(this.myCoreFacilitiesValue[index]));
        }
        return result;
    }

    private coreFacilitiesICanManageValue: string[];
    public get coreFacilitiesICanManage(): any[] {
        if (this.coreFacilitiesICanManageValue === null) {
            return [];
        }
        let index: number;
        let result: any[] = [];
        for (index = 0; index < this.coreFacilitiesICanManageValue.length; index++) {
            result.push(JSON.parse(this.coreFacilitiesICanManageValue[index]));
        }
        return result;
    }

    private coreFacilitiesICanSubmitToValue: string[];
    public get coreFacilitiesICanSubmitTo(): any[] {
        if (this.coreFacilitiesICanSubmitToValue === null) {
            return [];
        }
        let index: number;
        let result: any[] = [];
        for (index = 0; index < this.coreFacilitiesICanSubmitToValue.length; index++) {
            result.push(JSON.parse(this.coreFacilitiesICanSubmitToValue[index]));
        }
        return result;
    }

    constructor(private http: Http,
                private labListService: LabListService, private dictionaryService: DictionaryService) {
    }

    public hasPermission(permission: string): boolean {
        if (this.isGuest) {
            return false;
        }

        let index: number;
        let length: number = this.result.globalPermissions.length;
        for (index = 0; index < length; index++) {
            if (this.result.globalPermissions[index].name === permission) {
                return true;
            }
        }
        return false;
    }

    createSecurityAdvisor(): Observable<any> {
        console.log("createSecurityAdvisor new");
        return this.http.get("/gnomex/CreateSecurityAdvisor.gx", {withCredentials: true}).map((response: Response) => {
            console.log("return createSecurityAdvisor");
            if (response.status === 200) {
                this.result = response.json();

                this.idAppUserValue = Number(this.result.idAppUser);
                this.isGuestValue = this.result.isGuest == "Y";
                this.userNameValue = this.result.userFirstName + " " + this.result.userLastName;
                this.loginDateTimeValue = this.result.loginDateTime;
                this.isUniversityOnlyUserValue = this.result.isUniversityOnlyUser == "Y";
                this.isUserActiveValue = this.result.isUserActive == "Y";
                this.isExternalUserValue = this.result.isExternalUser == "Y";
                this.versionValue = this.result.version;

                this.isSuperAdminValue = this.hasPermission("canAdministerAllCoreFacilities");

                if (this.hasPermission("canAccessAnyObject")) {
                    if (this.hasPermission("canWriteAnyObject")) {
                        this.isAdminValue = true;
                    } else {
                        this.isBillingAdminValue = true;
                    }
                } else {
                    this.isAdminValue = false;
                    this.isBillingAdminValue = false;
                }

                this.determineUsersCoreFacilities();

                this.labListService.getLabList().subscribe((response: any[]) => {
                    console.log("Lab List Loaded");
                });

                return this.result;
            } else {
                throw new Error("Error");
            }
        }).flatMap(() => this.http.get("/gnomex/ManageDictionaries.gx?action=load", {withCredentials: true}).map((response: Response) => {
            console.log("return getDictionaries");
        }));
    }

    determineUsersCoreFacilities(): void {
        this.myCoreFacilitiesValue = [];
        this.coreFacilitiesICanManageValue = [];
        this.coreFacilitiesICanSubmitToValue = [];

        if (this.isSuperAdmin) {
            this.dictionaryService.getEntriesExcludeBlank("hci.gnomex.model.CoreFacility").subscribe((response) => {
                let index: number;
                for (index = 0; index < response.length; index++) {
                    this.myCoreFacilitiesValue.push(JSON.stringify(response[index]));
                    this.coreFacilitiesICanManageValue.push(JSON.stringify(response[index]));
                    this.coreFacilitiesICanSubmitToValue.push(JSON.stringify(response[index]));
                }
            });
        } else if (!this.isGuest) {
            this.dictionaryService.getEntriesExcludeBlank("hci.gnomex.model.CoreFacility").subscribe((response) => {
                let myCoreFacilitiesSet: Set<string> = new Set<string>();
                let coreFacilitiesICanManageSet: Set<string> = new Set<string>();
                let coreFacilitiesICanSubmitToSet: Set<string> = new Set<string>();
                let index: number;
                for (index = 0; index < this.result.coreFacilitiesIManage.length; index++) {
                    coreFacilitiesICanManageSet.add(this.result.coreFacilitiesIManage[index].idCoreFacility);
                    myCoreFacilitiesSet.add(this.result.coreFacilitiesIManage[index].idCoreFacility);
                }
                for (index = 0; index < this.result.coreFacilitiesICanSubmitTo.length; index++) {
                    coreFacilitiesICanSubmitToSet.add(this.result.coreFacilitiesICanSubmitTo[index].idCoreFacility);
                    myCoreFacilitiesSet.add(this.result.coreFacilitiesICanSubmitTo[index].idCoreFacility);
                }
                for (index = 0; index < this.result.coreFacilitiesForMyLab.length; index++) {
                    myCoreFacilitiesSet.add(this.result.coreFacilitiesForMyLab[index].idCoreFacility);
                }

                for (index = 0; index < response.length; index++) {
                    if (myCoreFacilitiesSet.has(response[index].idCoreFacility)) {
                        this.myCoreFacilitiesValue.push(JSON.stringify(response[index]));
                    }
                    if (coreFacilitiesICanManageSet.has(response[index].idCoreFacility)) {
                        this.coreFacilitiesICanManageValue.push(JSON.stringify(response[index]));
                    }
                    if (coreFacilitiesICanSubmitToSet.has(response[index].idCoreFacility)) {
                        this.coreFacilitiesICanSubmitToValue.push(JSON.stringify(response[index]));
                    }
                }
            });
        }
    }

}