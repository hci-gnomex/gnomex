import {Injectable} from "@angular/core";
import {Http, Response} from "@angular/http";
import {Observable} from "rxjs/Observable";

@Injectable()
export class DictionaryService {

    public static CORE_FACILITY: string = "hci.gnomex.model.CoreFacility";

    private cachedDictionaryString: any;
    private cacheExpirationTime: number;
    private CACHE_EXPIRATION_MILLIS = 600000;   // ten minutes

    constructor(private _http: Http) {}

    private reloadDictionaries(): Observable<any> {
        return this._http.get("/gnomex/ManageDictionaries.gx?action=load", {withCredentials: true}).map((response: Response) => {
            if (response.status === 200) {
                this.cachedDictionaryString = JSON.stringify(response.json());
                this.cacheExpirationTime = Date.now() + this.CACHE_EXPIRATION_MILLIS;
                return JSON.parse(this.cachedDictionaryString);
            } else {
                throw new Error("Error");
            }
        });
    }

    private getDictionaries(): Observable<any> {
        if (this.cachedDictionaryString != null && Date.now() < this.cacheExpirationTime) {
            return Observable.of(JSON.parse(this.cachedDictionaryString));
        } else {
            return this.reloadDictionaries();
        }
    }

    /**
     * Invalidates the dictionary cache, forcing a full reload the next time dictionary information is queried
     */
    invalidateCache(): void {
        this.cachedDictionaryString = null;
    }

    /**
     * Get an array of all dictionary objects (no entries), sorted by displayName
     * @returns {Observable<any[]>}
     */
    getAllDictionaries(): Observable<any[]> {
        return this.getDictionaries().flatMap((response) => {
            let dictionaries: any[] = response;
            for (let dictionary of dictionaries) {
                delete dictionary.DictionaryEntry;
            }
            dictionaries = dictionaries.sort((o1, o2) => {
                if (o1.displayName < o2.displayName) {
                    return -1;
                } else if (o1.displayName > o2.displayName) {
                    return 1;
                } else {
                    return 0;
                }
            });
            return Observable.of(dictionaries);
        });
    }

    /**
     * Get an array of all editable dictionary objects (no entries), sorted by displayName
     * @returns {Observable<any[]>}
     */
    getEditableDictionaries(): Observable<any[]> {
        return this.getAllDictionaries().flatMap((response) => {
            let dictionaries: any[] = response;
            let editableDictionaries: any[] = dictionaries.filter((value) => (value.canWrite == "Y"));
            return Observable.of(editableDictionaries);
        });
    }

    /**
     * Get the dictionary object (no entries) for a specific className
     * @param {string} className
     * @returns {Observable<any>}
     */
    getDictionary(className: string): Observable<any> {
        return this.getAllDictionaries().flatMap((response) => {
            let dictionaries: any[] = response;
            let matches: any[] = dictionaries.filter((value) => (value.className == className));
            let match = matches.length > 0 ? matches[0] : null;
            return Observable.of(match);
        });
    }

    /**
     * Get all dictionary entries for a specific className, including blank entries, sorted by display
     * @param {string} className
     * @returns {Observable<any[]>}
     */
    getEntries(className: string): Observable<any[]> {
        return this.getDictionaries().flatMap((response) => {
            let dictionaries: any[] = response;
            let entries = [];
            for (let dictionary of dictionaries) {
                if (dictionary.className == className) {
                    entries = dictionary.DictionaryEntry;
                }
            }
            entries = entries.sort((o1, o2) => {
                if (o1.display < o2.display) {
                    return -1;
                } else if (o1.display > o2.display) {
                    return 1;
                } else {
                    return 0;
                }
            });
            return Observable.of(entries);
        });
    }

    /**
     * Get all dictionary entries for a specific className, excluding blank entries, sorted by display
     * @param {string} className
     * @returns {Observable<any[]>}
     */
    getEntriesExcludeBlank(className: string): Observable<any[]> {
        return this.getEntries(className).flatMap((response) => {
            let entries: any[] = response;
            entries = entries.filter((value) => value.value != "");
            return Observable.of(entries);
        });
    }

    /**
     * Get all core facilities
     * @returns {Observable<any[]>}
     */
    coreFacilities(): Observable<any[]> {
        return this.getEntries(DictionaryService.CORE_FACILITY);
    }

}