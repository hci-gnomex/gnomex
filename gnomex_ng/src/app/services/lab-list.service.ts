import {Injectable} from "@angular/core";
import {Http, Response, URLSearchParams} from "@angular/http";
import {Observable} from "rxjs/Observable";

import 'rxjs/add/operator/map';
import 'rxjs/add/observable/of';

@Injectable()
export class LabListService {

    private labList: any[] = null;

    constructor(private http: Http) {
    }

    getLabList(): Observable<any[]> {
        if (this.labList != null) {
            return Observable.of(this.labList);
        } else {
            let params: URLSearchParams = new URLSearchParams();
            params.set("listKind", "UnboundedLabList");
            return this.http.get("/gnomex/GetLabList.gx", {search: params}).map((response: Response) => {
                if (response.status === 200) {
                    this.labList = response.json();
                    return response.json();
                } else {
                    throw new Error("Error");
                }
            });
        }
    }

    refresh(): Observable<any[]> {
        this.labList = null;
        return this.getLabList();
    }

}