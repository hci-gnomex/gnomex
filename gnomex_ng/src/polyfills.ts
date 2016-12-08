import "core-js/es6";
import "reflect-metadata";
require("zone.js/dist/zone");

// TODO: JEH (10/14/2016) - Figure out how to handle this.  process.env.ENV does not exits
/*
if (process.env.ENV === "production") {
  // Production
} else {
  // Development
  Error["stackTraceLimit"] = Infinity;
  require("zone.js/dist/long-stack-trace-zone");
}
*/
