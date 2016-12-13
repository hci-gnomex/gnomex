import { Directive, ElementRef, Input } from "@angular/core";

import { SidebarService } from "./sidebar.service";

/**
 * The toggle directive can be tagged onto any element within a component that provides the SidebarService.
 * It adds a click event onto the attached element and notifies the SidebarService of a negation of the
 * user"s sidebar toggle choice.  It assumes that there is a <i> element which has a class that can be
 * modified with a "small-icon" or "large-icon" class.
 *
 * Example:
 * <li>
 *   <a sidebar-toggle smallIcon="fa-angle-right" largeIcon="fa-angle-left"><i class="fa"></i></a>
 * </li>
 */
@Directive({
  selector: "[sidebar-toggle]"
})
export class SidebarToggle {

  @Input() smallIcon: string;
  @Input() largeIcon: string;

  constructor(private el: ElementRef, private sidebarService: SidebarService) {

  }

  ngOnInit() {
    this.el.nativeElement.onclick = (e: UIEvent) => {
      this.sidebarToggle();
    };

    this.sidebarService.addSidebarStateObserver((value) => {
      console.log("SidebarToggle.sidebarStateObserver changed");

      try {
        let iel: HTMLElement = this.el.nativeElement.getElementsByTagName("i")[0];
        iel.className = iel.className.replace(this.smallIcon, "").replace(this.largeIcon, "");

        if (value === 2) {
          iel.className += " " + this.largeIcon;
        } else {
          iel.className += " " + this.smallIcon;
        }
      } catch (e) {
        console.log("SidebarToggle.sidebarStateObserver: Exception: Failed to find an <i> child.");
      }
    });
  }

  sidebarToggle() {
    this.sidebarService.negateManual();
  }
}
