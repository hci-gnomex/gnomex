import {Injectable} from "@angular/core";
import {Http, Response, URLSearchParams} from "@angular/http";
import {Observable} from "rxjs/Observable";

import 'rxjs/add/operator/map';

@Injectable()
export class GetLabService {

    constructor(private http: Http) {
    }

    getLab(params: URLSearchParams): Observable<any> {
        return this.http.get("/gnomex/GetLab.gx", {search: params}).map((response: Response) => {
            if (response.status === 200) {
                return response.json();
            } else {
                throw new Error("Error");
            }
        });
    }

    getLabById(idLab: string): Observable<any> {
        let params: URLSearchParams = new URLSearchParams();
        params.set("idLab", idLab);
        return this.getLab(params);
    }

    getLabByIdOnlyForHistoricalOwnersAndSubmitters(idLab: string): Observable<any> {
        let params: URLSearchParams = new URLSearchParams();
        params.set("idLab", idLab);
        params.set("includeBillingAccounts", "N");
        params.set("includeProductCounts", "N");
        params.set("includeProjects", "N");
        params.set("includeCoreFacilities", "N");
        params.set("includeHistoricalOwnersAndSubmitters", "Y");
        params.set("includeInstitutions", "N");
        params.set("includeSubmitters", "N");
        params.set("includeMoreCollaboratorInfo", "N");
        return this.getLab(params);
    }

}