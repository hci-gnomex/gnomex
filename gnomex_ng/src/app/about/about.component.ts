/*
 * Copyright (c) 2016 Huntsman Cancer Institute at the University of Utah, Confidential and Proprietary
 */
import {Component, OnInit} from "@angular/core";
import {Http} from "@angular/http";


@Component({
  selector: "about",
  template: require("./about.component.html"),
  styles: [require("./about.component.less")],
  providers: []
})
export class AboutComponent implements OnInit {
  bacon = "assets/bacon.png";
  constructor(http: Http) {
    // Do instance construction here.
  }

  ngOnInit() {
    console.log("in on init");
  }
}
