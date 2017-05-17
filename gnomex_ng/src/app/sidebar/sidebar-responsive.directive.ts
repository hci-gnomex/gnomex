import { Input, Directive, ElementRef, } from "@angular/core";
import { Router, ActivatedRoute } from "@angular/router";
import { Subscription } from "rxjs/Rx";

import { SidebarService } from "./sidebar.service";

/**
 * This directive is designed to provide sidebar responsiveness to a component.
 *
 * Requirements:
 * The parent component is required to have an element with a ".sidebar" class and
 * an element with a ".main" class.
 *
 * Example "LayoutComponent" ("/route-segment" should represent the route segment of this component):
 * <div class="container-fluid" sidebar-responsive="/route-segment">
 *     <div class="col-sm-3 col-md-2 sidebar">
 *         <ul class="nav nav-sidebar">
 *             <li></li>
 *         </ul>
 *     </div>
 *     <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main"></div>
 * </div>
 */
@Directive({
  selector: "[sidebar-responsive]"
})
export class SidebarResponsive {

  // The input is the current route.
  @Input("sidebar-responsive") baseRouteSegment: string;

  // Allow the modifier to be anything (doesn"t lock ourselves into font-awesome).
  @Input("iconized-modifier") iconizedModifier: string = "fa-2x";

  mainElement: HTMLElement;
  sidebarElement: HTMLElement;
  iconElements: HTMLElement[];

  activeRoute: string;
  routerSubscription: Subscription;

  constructor(private elementRef: ElementRef,
              private sidebarService: SidebarService,
              private router: Router,
              private route: ActivatedRoute) {

  }

  ngOnInit() {
    try {
      this.mainElement = this.elementRef.nativeElement.getElementsByClassName("main")[0];
    } catch (e) {
      console.log("SidebarResponsive.ngOnInit: Exception: Failed to find element with .main class.");
    }

    try {
      this.sidebarElement = this.elementRef.nativeElement.getElementsByClassName("sidebar")[0];
    } catch (e) {
      console.log("SidebarResponsive.ngOnInit: Exception: Failed to find element with .sidebar class.");
    }
    try {
      this.iconElements = this.elementRef.nativeElement.getElementsByClassName("icon-resize");
    } catch (e) {
      console.log("SidebarResponsive.ngOnInit: Exception: Failed to find elements with .icon-resize class.");
    }

    /* Listen for route changes.  Change active li. */
    this.routerSubscription = this.router.events.subscribe(o => {
      console.log("SidebarResponsive.routerSubscription: RouteChange: " + this.router.url);

      this.findActiveRoute();
    });

    /* Listen for changes to the sidebar state.  1 for iconized and 2 for full size. */
    this.sidebarService.addSidebarStateObserver((value) => {
      if (value === 2) {
        this.setSidebarCss("sidebar-large");
        this.setMainCss("sidebar-large");
        this.setIconCss(null);
      } else {
        this.setSidebarCss("sidebar-small");
        this.setMainCss("sidebar-small");
        this.setIconCss(this.iconizedModifier);
      }
    });

    window.onresize = (e) => {
      this.onResize(e);
    };
  }

  /**
   * Upon creation of the DOM, trigger initial sidebar service values;
   */
  ngAfterContentInit() {
    this.onResize(null);
    this.sidebarService.setManual(2);
    this.findActiveRoute();
  }

  /**
   * When the window size changes, notify the sidebar service.
   *
   * @param event
     */
  onResize(event: UIEvent) {
    if (window.innerWidth > 768) {
      this.sidebarService.setWindow(2);
    } else {
      this.sidebarService.setWindow(1);
    }
  }

  /**
   * Removes references of sidebar-small and sidebar-large and injects the current class provided by className.
   *
   * @param className
     */
  setSidebarCss(className: string) {
    if(this.sidebarElement != null) {
      this.sidebarElement.className = this.sidebarElement.className.replace("sidebar-small", "").replace("sidebar-large", "");
      this.sidebarElement.className += " " + className;
    }
  }

  /**
   * Removes references of sidebar-small and sidebar-large and injects the current class provided by className.
   *
   * @param className
   */
  setMainCss(className: string) {
    if (this.mainElement != null) {
      this.mainElement.className = this.mainElement.className.replace("sidebar-small", "").replace("sidebar-large", "");
      this.mainElement.className += " " + className;
    }
  }

  /**
   * Doubles the size of the icons for the iconized state.
   *
   * @param className
     */
  setIconCss(className: string) {
    if (this.iconElements != null) {
      for (var i = 0; i < this.iconElements.length; i++) {
        this.iconElements[i].className = this.iconElements[i].className.replace(this.iconizedModifier, "");

        if (className != null) {
          this.iconElements[i].className += " " + className;
        }
      }
    }
  }

  /**
   * We provide this directive with its route segment (this.baseRouteSegment).  Based on that, find the next segment.
   * Assume that each ul > li in the sidebar routes to a next segment, use that segment name in the li.class to identify
   * which li should be switch to ".active".
   */
  findActiveRoute() {
    let i: number = this.router.url.indexOf(this.baseRouteSegment);
    if (i !== -1) {
      try {
        let activeRoute: string = this.router.url.substr(i + this.baseRouteSegment.length);

        if (activeRoute.indexOf("/") === activeRoute.lastIndexOf("/")) {
          activeRoute = activeRoute.replace("/", "");
        } else {
          activeRoute = activeRoute.substr(1, activeRoute.length);
          activeRoute = activeRoute.substr(0, activeRoute.indexOf("/"));
        }

        console.log("Active Sub Route Segment: " + activeRoute);
        this.updateActiveRoute(activeRoute);
      } catch (e) {
        console.log("SidebarResponsive.findActiveRoute: Exception: Failed to parse url tree.");
      }
    }
  }

  /**
   * Iterate over all .nav-sidebar > li and remove .active.  Set .active to the current active route segment.
   * If the full route is /a/b/sidebar/content1/x/y and this directive"s route segment is set to /sidebar, then
   * the active route segment will be "content1".
   *
   * @param activeRoute
     */
  updateActiveRoute(activeRoute: string) {
    this.activeRoute = activeRoute;

    let uls: HTMLElement[] = this.elementRef.nativeElement.getElementsByClassName("nav-sidebar");
    for (var i = 0; i < uls.length; i++) {
      let lis: NodeListOf<HTMLLIElement> = uls[i].getElementsByTagName("li");

      for (var j = 0; j < lis.length; j++) {
        lis[j].className = lis[j].className.replace("active", "");

        if (lis[j].className.indexOf(this.activeRoute) !== -1) {
          lis[j].className += " active";
        }
      }
    }
  }

  /**
   * Router observer is global, so we have to unsubscribe or else this directive keeps listening to the route events
   * even though this directive and its parent component might be gone.
   *
   * TODO: Need to unsubscribe for SidebarService?  Probably not because it should be destroyed when the component is destroyed.
   */
  ngOnDestroy() {
    this.routerSubscription.unsubscribe();
  }

}
