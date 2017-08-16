import {Injectable} from "@angular/core";
import {Http, Response} from "@angular/http";
import {Observable} from "rxjs/Observable";

import 'rxjs/add/operator/map';
import {LabListService} from "./lab-list.service";

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

    private myCoreFacilitiesValue: any[];
    public get myCoreFacilities(): any[] {
        if (this.myCoreFacilitiesValue === null) {
            return [];
        }
        return this.myCoreFacilitiesValue;
    }

    private coreFacilitiesICanManageValue: any[];
    public get coreFacilitiesICanManage(): any[] {
        if (this.coreFacilitiesICanManageValue === null) {
            return [];
        }
        return this.coreFacilitiesICanManageValue;
    }

    private coreFacilitiesICanSubmitToValue: any[];
    public get coreFacilitiesICanSubmitTo(): any[] {
        if (this.coreFacilitiesICanSubmitToValue === null) {
            return [];
        }
        return this.coreFacilitiesICanSubmitToValue;
    }

    constructor(private http: Http,
                private labListService: LabListService) {
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
        if (this.isGuest) {
            this.myCoreFacilitiesValue = null;
            this.coreFacilitiesICanManageValue = null;
            this.coreFacilitiesICanSubmitToValue = null;
        } else if (this.isSuperAdmin) {
            // TODO
            this.myCoreFacilitiesValue = null;
            this.coreFacilitiesICanManageValue = null;
            this.coreFacilitiesICanSubmitToValue = null;
            //myCoreFacilities = dictionaryManager.getEntriesExcludeBlank("hci.gnomex.model.CoreFacility");
            //coreFacilitiesICanManage = dictionaryManager.getEntriesExcludeBlank("hci.gnomex.model.CoreFacility");
            //coreFacilitiesICanSubmitTo = dictionaryManager.getEntriesExcludeBlank("hci.gnomex.model.CoreFacility");
        } else {
            // TODO
            this.myCoreFacilitiesValue = null;
            this.coreFacilitiesICanManageValue = null;
            this.coreFacilitiesICanSubmitToValue = null;
            /*
            myCoreFacilities = new XMLList();
				for each (var core:Object in createSecurityAdvisor.lastResult..CoreFacility) {

					for each (var dcf:Object in coreFacilities) {
						if (dcf.@idCoreFacility == core.@idCoreFacility) {
							var found:Boolean = false;
							for (var i:int = 0; i < myCoreFacilities.length(); i++) {
								var core2:Object = myCoreFacilities[i];
								if (core.@idCoreFacility == '') {
									found = true;
									break;
								} else if (core.@idCoreFacility == core2.@idCoreFacility) {
									found = true;
									break;
								}
							}
							if (!found) {
								myCoreFacilities[myCoreFacilities.length()] = dcf;
							}
						}
					}
				}
				//myCoreFacilities = new XMLList(temp.toXMLString());
				//myCoreFacilities = createSecurityAdvisor.lastResult..CoreFacility;
				coreFacilitiesICanManage = new XMLList();
				for each(var entry:Object in dictionaryManager.getEntriesExcludeBlank("hci.gnomex.model.CoreFacility")) {
					for each (var entry1:Object in createSecurityAdvisor.lastResult.coreFacilitiesIManage.CoreFacility) {
						if (entry.@value == entry1.@value) {
							coreFacilitiesICanManage[coreFacilitiesICanManage.length()] = entry;
							break;
						}
					}
				}

				coreFacilitiesICanSubmitTo = new XMLList();
				for each(var entry2:Object in dictionaryManager.getEntriesExcludeBlank("hci.gnomex.model.CoreFacility")) {
					for each (var entry3:Object in createSecurityAdvisor.lastResult.coreFacilitiesICanSubmitTo.CoreFacility) {
						if (entry2.@value == entry3.@value) {
							coreFacilitiesICanSubmitTo[coreFacilitiesICanSubmitTo.length()] = entry2;
							break;
						}
					}
				}
             */
        }
    }

}