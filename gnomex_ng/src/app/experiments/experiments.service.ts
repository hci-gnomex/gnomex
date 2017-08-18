import {Inject, Injectable, OpaqueToken} from "@angular/core";
import {Http, Response, URLSearchParams} from "@angular/http";
import {Observable} from "rxjs/Observable";

export let BROWSE_EXPERIMENTS_ENDPOINT: OpaqueToken = new OpaqueToken("browse_experiments_url");
export let VIEW_EXPERIMENT_ENDPOINT: OpaqueToken = new OpaqueToken("view_experiment_url");

@Injectable()
export class ExperimentsService {

    constructor(private _http: Http, @Inject(BROWSE_EXPERIMENTS_ENDPOINT) private _browseExperimentsUrl: string) {}
    getExperiments() {
       //return this._http.get("/gnomex/GetProjectRequestList.gx?idLab=1500&showCategory='N'", {withCredentials: true}).map((response: Response) => {
         return this._http.get("/gnomex/GetProjectRequestList.gx?showEmptyProjectFolders=N&allExperiments=Y&showSamples=N&showCategory=N&idCoreFacility=3&showEmptyProjectFolders=N", {withCredentials: true}).map((response: Response) => {
            if (response.status === 200) {
                return response.json().Lab;
            } else {
                throw new Error("Error");
            }
        });
    }

    getExperiment(id: string): Observable<any> {
        return this._http.get("/gnomex/GetRequest.gx?requestNumber="+id, {withCredentials: true}).map((response: Response) => {
            if (response.status === 200) {
                return response.json();
            } else {
                throw new Error("Error");
            }
        });
    }

    getLab(params: URLSearchParams): Observable<any>{
        return this._http.get("/gnomex/GetLab.gx", {search: params}).map((response: Response) => {
            if (response.status === 200) {
                console.log("&&&&&&&&&&&&&&&&&& getLab "+response);
                return response.json().Lab;
            } else {
                throw new Error("Error");
            }
        });

    }

    saveProjectRequest(params: URLSearchParams):  Observable<any> {
        return this._http.get("/gnomex/SaveRequestProject.gx", {search: params}).map((response: Response) => {
            if (response.status === 200) {
                console.log("&&&&&&&&&&&&&&&&&& setProjectRequest "+response);
                return response;
            } else {
                throw new Error("Error");
            }
        });

    }

    getProject(params: URLSearchParams):  Observable<any> {
        return this._http.get("/gnomex/GetProject.gx", {search: params}).map((response: Response) => {
            if (response.status === 200) {
                console.log("&&&&&&&&&&&&&&&&&& getProject "+response);
                return response.json();
            } else {
                throw new Error("Error");
            }
        });

    }

    saveProject(params: URLSearchParams):  Observable<any> {
        return this._http.get("/gnomex/SaveProject.gx", {search: params}).map((response: Response) => {
            if (response.status === 200) {
                console.log("&&&&&&&&&&&&&&&&&& getProject "+response);
                return response;
            } else {
                throw new Error("Error");
            }
        });

    }

    deleteProject(params: URLSearchParams):  Observable<any> {
        return this._http.get("/gnomex/DeleteProject.gx", {search: params}).map((response: Response) => {
            if (response.status === 200) {
                console.log("&&&&&&&&&&&&&&&&&& deleteProject "+response);
                return response;
            } else {
                throw new Error("Error");
            }
        });

    }


    getProjectRequestList(params: URLSearchParams) {
        //return this._http.get("/gnomex/GetProjectRequestList.gx?idLab=1500&showCategory='N'", {withCredentials: true}).map((response: Response) => {
        return this._http.get("/gnomex/GetProjectRequestList.gx", {search: params, withCredentials: true}).map((response: Response) => {
            if (response.status === 200) {
                return response.json().Lab;
            } else {
                throw new Error("Error");
            }
        });
    }



}
