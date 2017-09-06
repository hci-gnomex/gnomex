import {Inject, Injectable, OpaqueToken} from "@angular/core";
import {Http, Response, URLSearchParams} from "@angular/http";
import {Observable} from "rxjs/Observable";
import {Subject} from "rxjs/Subject";
import {Observer} from "rxjs/Observer";

export let BROWSE_EXPERIMENTS_ENDPOINT: OpaqueToken = new OpaqueToken("browse_experiments_url");
export let VIEW_EXPERIMENT_ENDPOINT: OpaqueToken = new OpaqueToken("view_experiment_url");

@Injectable()
export class ExperimentsService {

	private experimentOrders: any[];
	private experimentOrdersSubject: Subject<any[]> = new Subject();

	private changeStatusSubject: Subject<any> = new Subject();

	private haveLoadedExperimentOrders: boolean = false;
	private previousURLParams: URLSearchParams = null;

	constructor(private _http: Http, @Inject(BROWSE_EXPERIMENTS_ENDPOINT) private _browseExperimentsUrl: string) { }

	getExperiments() {
		//return this._http.get("/gnomex/GetProjectRequestList.gx?idLab=1500&showCategory='N'", {withCredentials: true}).map((response: Response) => {
		//return this._http.get("/gnomex/GetProjectRequestList.gx?showEmptyProjectFolders=N&allExperiments=Y&showSamples=N&showCategory=N", {withCredentials: true}).map((response: Response) => {

		return this._http.get("/gnomex/GetProjectRequestList.gx?showEmptyProjectFolders=N&allExperiments=Y&showSamples=N&showCategory=N&idCoreFacility=3&showEmptyProjectFolders=N", {withCredentials: true}).map((response: Response) => {
			if (response.status === 200) {
				return response.json().Lab;
			} else {
				throw new Error("Error");
			}
		});
	}


	getExperimentsObservable(): Observable<any> {
		return this.experimentOrdersSubject.asObservable();
	}

	getExperiments_fromBackend(parameters: URLSearchParams): void {
		if (this.haveLoadedExperimentOrders && this.previousURLParams === parameters) {
			// do nothing
			console.log("Experiment Orders already loaded");
			// return Observable.of(this.experimentOrders);
		} else {
			this.haveLoadedExperimentOrders = true;
			this.previousURLParams = parameters;

			this._http.get("/gnomex/GetRequestList.gx", {withCredentials: true, search: parameters}).subscribe((response: Response) => {
				// console.log("GetRequestList called");

				if (response.status === 200) {
					this.experimentOrders = response.json().Request;
					this.emitExperimentOrders();
					//return response.json().Request;
				} else {
					throw new Error("Error");
				}
			});
		}
	}

	repeatGetExperiments_fromBackend(): void {
		this.haveLoadedExperimentOrders = false;
		this.getExperiments_fromBackend(this.previousURLParams);
	}

	getChangeExperimentStatusObservable(): Observable<any> {
		return this.changeStatusSubject.asObservable();
	}

	changeExperimentStatus(idRequest: string, codeRequestStatus: string): void {

		let parameters: URLSearchParams = new URLSearchParams;
		parameters.set("idRequest", idRequest);
		parameters.set("codeRequestStatus", codeRequestStatus);

		// console.log("Changing Experiment numbers: " + parameters.get("idRequest") + " status to " + parameters.get("codeRequestStatus"));

		this._http.get("/gnomex/ChangeRequestStatus.gx", {withCredentials: true, search: parameters}).subscribe((response: Response) => {
			if (response.status === 200) {
				this.changeStatusSubject.next(response.json());
				//return response.json().Request;
			} else {
				throw new Error("Error");
			}
		});
	}

	private emitExperimentOrders(): void {
		this.experimentOrdersSubject.next(this.experimentOrders);
	}

	// refreshExperimentOrders(): void {
	// 	this._http.get("/gnomex/GetRequestList.gx", {withCredentials: true}).map((response: Response) => {
	// 		if (response.status === 200) {
	// 			this.experimentOrders2.next({requests: response.json().Request});
	// 		} else {
	// 			throw new Error("Error");
	// 		}
	// 	});
	// }

	getExperiment(id: string): Observable<any> {
		return this._http.get("/gnomex/GetRequest.gx?requestNumber=" + id, {withCredentials: true}).map((response: Response) => {
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
                return response.json().Lab;
            } else {
                throw new Error("Error");
            }
        });

    }

    saveRequestProject(params: URLSearchParams):  Observable<any> {
        return this._http.get("/gnomex/SaveRequestProject.gx", {search: params}).map((response: Response) => {
            if (response.status === 200) {
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
                return response;
            } else {
                throw new Error("Error");
            }
        });

    }

    deleteProject(params: URLSearchParams):  Observable<any> {
        return this._http.get("/gnomex/DeleteProject.gx", {search: params}).map((response: Response) => {
            if (response.status === 200) {
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

    getRequestList(params: URLSearchParams): Observable<any> {
        return this._http.get("/gnomex/GetRequestList.gx", {search: params}).map((response: Response) => {
            if (response.status === 200) {
                return response.json();
            } else {
                throw new Error("Error");
            }
        });
    }

}
