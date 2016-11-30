/*
 * Copyright (c) 2016 Huntsman Cancer Institute at the University of Utah, Confidential and Proprietary
 */
import {Ng2BootstrapConfig, Ng2BootstrapTheme} from "ng2-bootstrap";
import {platformBrowserDynamic} from "@angular/platform-browser-dynamic";

import {GnomexAppModule} from "./app/gnomex-app.module";

/**
 * The entry point for the CORE client application.
 *
 * @author brandony <brandon.youkstetter@hci.utah.edu>
 * @author jasonholmberg <jason.holmberg@hci.utah.edu>
 * @since 8/24/16
 */

Ng2BootstrapConfig.theme = Ng2BootstrapTheme.BS4;

// TODO: JEH (10/14/2016) - Figure out how to handle this.  process.env.ENV does not exits
/*if (process.env.ENV === "production") {
  enableProdMode();
}*/

platformBrowserDynamic().bootstrapModule(GnomexAppModule);
