/*
 * Copyright (c) 2016 Huntsman Cancer Institute at the University of Utah, Confidential and Proprietary
 */
import {Component, ViewChild, OnInit} from "@angular/core";
import {Router} from "@angular/router";
import {UserService} from "@hci/user";
import {AppHeaderComponent} from "@hci/app-header";
import {NavigationAction, NavigationItem, PrimaryNavigationItem, PrimaryNavigationItemGroup} from "@hci/navigation";
import {AppFooterComponent} from "@hci/app-footer";

/**
 * The core application component.
 *
 * @author brandony <brandon.youkstetter@hci.utah.edu>
 */
@Component({
  selector: "gnomex-app",
  providers: [],
  /*styles: [require("./gnomex-app.component.less")],*/
  template: require("./gnomex-app.component.html")
})
export class GnomexAppComponent implements OnInit {
  public isCollapsed: boolean = true;
  public status: {isopen: boolean} = {isopen: false};

  private appNameTitle: string = "Gnomex";

  @ViewChild(AppHeaderComponent)
  private _appHdrCmpt: AppHeaderComponent;

  @ViewChild(AppFooterComponent)
  private _appFooterCmpt: AppFooterComponent;

  constructor(private userService: UserService,
              private router: Router) {
  }

  ngOnInit() {
    this.setupHeaderComponent();
    this.setupFooterComponent();
  }

  private setupFooterComponent() {
    this._appFooterCmpt.appName = this.appNameTitle;
    this._appFooterCmpt.copyright = "Huntsman Cancer Institute";
  }

  private setupHeaderComponent() {
    this._appHdrCmpt.iconPath = "./assets/gnomex_logo.png";
    //this._appHdrCmpt.title = this.appNameTitle;
    this._appHdrCmpt.homeRoute = "/";
    this._appHdrCmpt.navbarClasses = "bg-faded";

    // Currently no roles guard these menus, but they can be configure
    this._appHdrCmpt.primaryMenuGroups = [
      new PrimaryNavigationItemGroup(
        {
          name: "main",
          items: [
            new PrimaryNavigationItem({name: "Experiments", route: "/experiments", iconClass: "fa fa-flask"}),
            new PrimaryNavigationItem({name: "Analysis", route: "/analysis", iconClass: "fa fa-line-chart"}),
            new PrimaryNavigationItem({name: "Data Tracks", route: "/datatracks", iconClass: "fa fa-area-chart"}),
            new PrimaryNavigationItem({name: "Topics", route: "/topics", iconClass: "fa fa-tag"}),
            new PrimaryNavigationItem({name: "Workflow", route: "/workflow", iconClass: "fa fa-random", visibility: "LIMS"}),
            new PrimaryNavigationItem({name: "Products", route: "/products", iconClass: "fa fa-shopping-basket"}),
            new PrimaryNavigationItem({name: "Billing", route: "/billing", iconClass: "fa fa-money"}),
            new PrimaryNavigationItem({name: "Reports", route: "/reports", iconClass: "fa fa-file"})
          ]
        }
      )
    ];


    /*
     * TODO: BHY (08/12/16) - This changes based on the users current state (authenticated or not) as informed by the user
     * service.
     */
    this._appHdrCmpt.userMenuDropdownClass = "fa fa-user gx-user-menu";
    this._appHdrCmpt.userMenuItems = [
      new NavigationItem({name: "Account", route: "foo"}),
      new NavigationItem({name: "Preferences", route: "goo"}),
    ];
    this._appHdrCmpt.userMenuActions = [new NavigationAction(
      {
        name: "Logout",
        action: () => {
          if (this.userService.logout()) {
            this.router.navigate(["/login"]);
          }
        }
      })];

    /*
     * TODO: JEH (10/28/16) - Update this ADMIN role when we know what it is. May also add roles to individual nav items
     * The role specified below is require to expose the admin menu
     */
    this._appHdrCmpt.adminMenuDropdownClass = "fa fa-gear gx-admin-menu"
    this._appHdrCmpt.adminRole = "ADMIN";
    // Each item/action in the admin menu may be guarded by a different role or none at all.
    this._appHdrCmpt.adminMenuItems = [
      new NavigationItem({name: "Users & Groups", route: "/admin/usersgroups", iconClass: "fa fa-users"}),
      new NavigationItem({name: "Configure", route: "/config", iconClass: "fa fa-gear"}),
    ];
    /* add back when/if necessary
    this._appHdrCmpt.adminActionItems = [
      new NavigationAction(
        {
          name: "Admin Action",
          action: () => {
            alert("Admin action menu item executed!");
          }
        })
    ];*/
    // TODO: BHY (08/12/16) - Enable this when we identify the use case.
    // this._appHdrCmpt.headerAction = new HciHeaderAction("Create Foo", this._demoAppSvc.buttonAction());
    // TODO: BHY (08/12/16) - Enable this when we identify the use case.
    // this._appHdrCmpt.searchFn = this._demoAppSvc.searchFn();
  }
}
