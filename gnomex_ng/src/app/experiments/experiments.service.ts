import { Inject, Injectable, OpaqueToken } from "@angular/core";
import { Http, Response } from "@angular/http";
import { Observable } from "rxjs/Observable";

export let BROWSE_EXPERIMENTS_ENDPOINT: OpaqueToken = new OpaqueToken("browse_experiments_url");

@Injectable()
export class ExperimentsService {

    constructor(private _http: Http, @Inject(BROWSE_EXPERIMENTS_ENDPOINT) private _browseExperimentsUrl: string) {}

    getExperiments(): Observable<Object> {
        console.log("getExperiments");
        return this._http.get("/gnomex/GetExperimentOverviewList.gx", {withCredentials: true}).map((response: Response) => {
            if (response.status === 200) {
                return response.json();
            } else {
                throw new Error("Error");
            }
        });
    }
}
