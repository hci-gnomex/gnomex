import {Injectable} from "@angular/core";
import {Subject} from "rxjs/Rx";

/**
 * Maintains the state of the responsive sidebar.
 *
 * This service should be placed on a provider of a component that uses the sidebar.
 */
@Injectable()
export class SidebarService {
  private currentSidebarState: number = 0;
  private sidebarState = new Subject<number>();
  private sidebarStateObservable = this.sidebarState.asObservable();
  private currentWindow: number = 0;
  private window = new Subject<number>();
  private currentManual: number = 0;
  private manual = new Subject<number>();

  /**
   * User control to toggle between iconized and expanded sidebar.
   * Should be called by the sidebar-toggle directive.
   */
  negateManual() {
    if (this.currentManual === 1) {
      this.setManual(2);
    } else {
      this.setManual(1);
    }

    this.setSidebarState();
  }

  /**
   * Sets the sidebar state based upon the window size and the user preference.
   */
  setSidebarState() {
    if (this.currentManual === 2 && this.currentWindow === 2) {
      this.currentSidebarState = 2;
    } else {
      this.currentSidebarState = 1;
    }

    this.sidebarState.next(this.currentSidebarState);
  }

  /**
   * Sets the user window size state and updates the sidebar state.
   *
   * @param value
   */
  setWindow(value: number) {
    if (this.currentWindow === value) {
      return;
    }

    this.currentWindow = value;
    this.window.next(value);

    this.setSidebarState();
  }

  /**
   * Sets the user preference state and updates the sidebar state.
   *
   * @param value
   */
  setManual(value: number) {
    if (this.currentManual === value) {
      return;
    }

    this.currentManual = value;
    this.manual.next(value);

    this.setSidebarState();
  }

  /**
   * Adds an observer to the sidebar state.
   *
   * @param observer a callback to respond to state change events
   */
  addSidebarStateObserver(observer: (value: any) => void) {
    this.sidebarStateObservable.subscribe(observer);
  }
}
