import {Inject, Injectable, OpaqueToken} from "@angular/core";
import {Http, Response, URLSearchParams} from "@angular/http";
import {Observable} from "rxjs/Observable";

export let BROWSE_EXPERIMENTS_ENDPOINT: OpaqueToken = new OpaqueToken("browse_experiments_url");
export let VIEW_EXPERIMENT_ENDPOINT: OpaqueToken = new OpaqueToken("view_experiment_url");

@Injectable()
export class ExperimentsService {

	private experimentOrders: any[];

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

	getExperimentOrders(params: URLSearchParams): Observable<any> {
		if (this.haveLoadedExperimentOrders && this.previousURLParams === params) {
			return Observable.of(this.experimentOrders);
		} else {
			this.haveLoadedExperimentOrders = true;
			this.previousURLParams = params;

			return this._http.get("/gnomex/GetRequestList.gx", {withCredentials: true, search: params}).map((response: Response) => {
				if (response.status === 200) {
					this.experimentOrders = response.json().Request;
					return response.json().Request;
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
