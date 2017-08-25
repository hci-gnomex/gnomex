import {Injectable} from "@angular/core";
import {Http, Response, URLSearchParams} from "@angular/http";
import {Observable} from "rxjs/Observable";

import 'rxjs/add/operator/map';

@Injectable()
export class AppUserListService {

    constructor(private http: Http) {
    }

    getAppUserList(params: URLSearchParams): Observable<any[]> {
        return this.http.get("/gnomex/GetAppUserList.gx", {search: params}).map((response: Response) => {
            if (response.status === 200) {
                return response.json();
            } else {
                throw new Error("Error");
            }
        });
    }

    getMembersOnly(): Observable<any[]> {
        let params: URLSearchParams = new URLSearchParams();
        params.set("membersOnly", "Y");
        return this.getAppUserList(params);
    }

}