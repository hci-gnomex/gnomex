/*
 * Copyright (c) 2016 Huntsman Cancer Institute at the University of Utah, Confidential and Proprietary
 */
import {GnomexAppComponent} from "./gnomex-app.component";
import {BrowserModule} from "@angular/platform-browser";
import {NgModule} from "@angular/core";

import {APP_ROUTING} from "./gnomex-app.routes";
import {HttpModule} from "@angular/http";
import {HomeModule} from "./home/home.module";
import {BROWSE_EXPERIMENTS_ENDPOINT, VIEW_EXPERIMENT_ENDPOINT} from "./experiments/experiments.service";
import {ExperimentsService} from "./experiments/experiments.service";
import {ExperimentsModule} from "./experiments/experiments.module";
import {RouterModule} from "@angular/router";
import {FormsModule} from "@angular/forms";
import {DropdownModule, CollapseModule} from "ng2-bootstrap";
import {
  AUTHENTICATED_USER_ENDPOINT, UserModule, UserService
} from "@hci/user";
import {
  AuthenticationModule, AuthenticationService,
  AUTHENTICATION_LOGOUT_PATH, AUTHENTICATION_ROUTE,
  AUTHENTICATION_TOKEN_KEY, AUTHENTICATION_TOKEN_ENDPOINT, AUTHENTICATION_DIRECT_ENDPOINT
} from "@hci/authentication";
import {AppHeaderModule} from "@hci/app-header";
import {NavigationModule} from "@hci/navigation";
import {LocalStorageModule, LocalStorageService, ILocalStorageServiceConfig} from "angular-2-local-storage";

import "./gnomex-app.css";
import {AppFooterModule, APP_INFO_SOURCE} from "@hci/app-footer";

let localStorageServiceConfig: ILocalStorageServiceConfig = {
  prefix: "hci-ri-core",
  storageType: "localStorage"
};

/**
 * @since 1.0.0
 */
@NgModule({
  imports: [
    BrowserModule,
    APP_ROUTING,
    HttpModule,
    RouterModule,
    FormsModule,
    HomeModule,
    DropdownModule.forRoot(),
    CollapseModule.forRoot(),
    AppHeaderModule,
    UserModule,
    AuthenticationModule,
    NavigationModule,
    AppFooterModule,
    ExperimentsModule,
    LocalStorageModule.withConfig(localStorageServiceConfig)
  ],
  declarations: [GnomexAppComponent],
  bootstrap: [GnomexAppComponent],
  providers: [
    {provide: BROWSE_EXPERIMENTS_ENDPOINT, useValue: "/gnomex/GetExperimentOverviewList.gx"},
    {provide: VIEW_EXPERIMENT_ENDPOINT, useValue: "/gnomex/GetRequest.gx"},
    {provide: AUTHENTICATED_USER_ENDPOINT, useValue: "/gnomex/api/user/authenticated"},
    {provide: AUTHENTICATION_DIRECT_ENDPOINT, useValue: "/gnomex/api/user-session"},
    {provide: AUTHENTICATION_TOKEN_ENDPOINT, useValue: "/gnomex/api/token"},
    {provide: AUTHENTICATION_LOGOUT_PATH, useValue: "/gnomex/logout"},
    {provide: AUTHENTICATION_ROUTE, useValue: "/authenticate"},
    {provide: AUTHENTICATION_TOKEN_KEY, useValue: "gnomex-jwt"},
    UserService,
    AuthenticationService,
    ExperimentsService,
    LocalStorageService,
    {provide: APP_INFO_SOURCE, useValue: "data/appInfo.json"}
  ]
})
export class GnomexAppModule {
}
