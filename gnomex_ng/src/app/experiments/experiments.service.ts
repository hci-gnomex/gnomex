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

	private haveLoadedExperimentOrders: boolean = false;
	private previousURLParams: URLSearchParams = null;

	constructor(private _http: Http, @Inject(BROWSE_EXPERIMENTS_ENDPOINT) private _browseExperimentsUrl: string) { }

	getExperiments() {
		return this._http.get("/gnomex/GetProjectRequestList.gx?idLab=1500", {withCredentials: true}).map((response: Response) => {
			if (response.status === 200) {
				return response.json().Lab;
			} else {
				throw new Error("Error");
			}
		});
	}

	getExperimentOrdersObservable(): Observable<any> {
		return this.experimentOrdersSubject.asObservable();
	}

	private emitExperimentOrders(): void {
		this.experimentOrdersSubject.next(this.experimentOrders);
	}

	getExperimentOrders_fromBackend(params: URLSearchParams): void {
		if (this.haveLoadedExperimentOrders && this.previousURLParams === params) {
			// do nothing
			console.log("Experiment Orders already loaded");
			// return Observable.of(this.experimentOrders);
		} else {
			this.haveLoadedExperimentOrders = true;
			this.previousURLParams = params;

			this._http.get("/gnomex/GetRequestList.gx", {withCredentials: true, search: params}).subscribe((response: Response) => {
				console.log("GetRequestList called");

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

    getProjectRequestList(params: URLSearchParams): Observable<any> {
        return this._http.get("/gnomex/GetProjectRequestList.gx", {search: params}).map((response: Response) => {
            if (response.status === 200) {
                return response.json();
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
