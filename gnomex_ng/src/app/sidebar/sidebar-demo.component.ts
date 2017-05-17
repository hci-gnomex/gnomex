/*
 * Copyright (c) 2016 Huntsman Cancer Institute at the University of Utah, Confidential and Proprietary
 */
import {Component} from "@angular/core";

import {SidebarService} from "./sidebar.service";

/**
 * This sidebar component isn"t just a sidebar but a demo component that features a sidebar.  This is designed to fit
 * inside a router-outlet designated as the main content area in which this component offers a sidebar as well as its
 * own internal router-outlet.  That internal router content is dictated by naviation options chosen by the sidebar.
 *
 * The sidebar is designed to be responsive.  There are two states, a large version with menu items with icons and full
 * text, and a small sidebar with icons only.  If the screen is less than 768px, the icon based sidebar will appear as
 * the only option.  If the screen is larger, then by default the sidebar will be full size, but the user has the option
 * of changing to the icon version to increase the size of the content area.
 *
 * The user toggling the sidebar size or the window being resized each update a value that can be small (1) or large (2).
 * When the listener updates these values, a method is then called to determine what the actual sidebar size should be.
 * Basically it always sets a small sidebar unless both the user indicates a larger sidebar and the window allows a
 * large sidebar.
 */
@Component({
  selector: "sidebar-demo",
  providers: [SidebarService, Window],
  styles: [require("./sidebar-demo.component.less").toString()],
  template: require("./sidebar-demo.component.html")
})
export class SidebarDemoComponent {

}
