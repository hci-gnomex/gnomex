import {Injectable} from "@angular/core";
import {Http, Response, URLSearchParams} from "@angular/http";
import {Observable} from "rxjs/Observable";

import 'rxjs/add/operator/map';

@Injectable()
export class AnalysisService {

    constructor(private http: Http) {
    }

    getAnalysisGroupList(params: URLSearchParams): Observable<any> {
        return this.http.get("/gnomex/GetAnalysisGroupList.gx", {search: params}).map((response: Response) => {
            if (response.status === 200) {
                return response.json();
            } else {
                throw new Error("Error");
            }
        });
    }

    getAnalysisLabList(): Observable<any> {
        return this.http.get("/gnomex/GetAnalysisGroupList.gx").map((response: Response) => {
            if (response.status === 200) {
                return response.json();
            } else {
                throw new Error("Error");
            }
        });
    }

}