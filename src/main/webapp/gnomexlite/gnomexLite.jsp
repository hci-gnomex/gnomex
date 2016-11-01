<!DOCTYPE HTML>
<!--
/*
 * GNomExLite - This is the main html.  Note that this is an AngularJS application
 *            which resides in app directory. The app directory is organized by
 *            modules.  Each subdirectory under app represents a module.  Within each
 *            module subdirectory you will find the javascript files for Controllers,
 *            directives, filters, services, etc. along with the html files.
 *
 * Copyright 2015
 *
 */
-->
<html lang="en">

    <head>
        <!-- Don't force latest IE rendering engine or ChromeFrame if installed -->
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">

        <!-- <meta charset="utf-8"> -->
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <title>GNomExLite</title>

        <style type="text/css">
            [ng-cloak].splash {
                display: block !important;
            }
            [ng-cloak] {display: none;}

            /* some naive styles for a splash page */
            .splash {
                display: none;
            }

        </style>

    </head>

    <body data-ng-app="app">
        <!-- The splash screen must be first 
        <div id="splash" ng-cloak="">
          <img src="resources/images/gnomex_splash_credits.png"/>
        </div>
        -->   
        <link href='https://fonts.googleapis.com/css?family=Raleway:300,400' rel='stylesheet' type='text/css'>
        <link href='https://fonts.googleapis.com/css?family=Lato:300,400' rel='stylesheet' type='text/css'>
        <link href='https://fonts.googleapis.com/css?family=Muli:400,300' rel='stylesheet' type='text/css'>

        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>

        <link rel="stylesheet" href="resources/css/jquery.fileupload.css">
        <link rel="stylesheet" href="resources/css/jquery.fileupload-ui.css">
        <link rel="stylesheet" href="resources/css/angucomplete-alt.css">
        <link rel="stylesheet" href="resources/css/dialogs.min.css">
        <link rel="stylesheet" href="resources/css/ngProgress.css">
        <link rel="stylesheet" href="resources/css/nv.d3.min.css">
        <link rel="stylesheet" href="resources/css/chosen.css">
        <link rel="stylesheet" href="resources/css/chosen-bootstrap.css">
        <link rel="stylesheet" href="resources/css/biominer.css">
        <link rel="stylesheet" href="resources/css/angular-busy.min.css">
        <link rel="stylesheet" href="resources/css/gnomexLite.css">


        <nav  class="navbar navbar-default navbar-fixed-top gnomex-top-nav" role="navigation" >
            <div class="container-fluid gnomex-container-fluid" ng-controller="NavbarController">
                <!-- Brand and toggle get grouped for better mobile display -->
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <a class="navbar-brand" href="index.html"><img src="resources/images/gnomex_logo.png"/> Lite <sup style="color: red;">beta</sup></a>
                </div>

                <!-- Collect the nav links, forms, and other content for toggling -->
                <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                    <ul class="nav navbar-nav gnomex-top-search">

                        <p class="navbar-text navbar-left" style="margin:-1px">                    </p>

                        <form class="navbar-form navbar-left" role="lookup">
                            <div class="form-group">
                                <input type="text" class="form-control" placeholder="Lookup by #" ng-model="whatToLookup">
                            </div>
                            <button type="lookup" class="btn btn-default" aria-label="Lookup By Number" ng-click="lookup(whatToLookup)">
                                <img src="resources/images/arrow_right.png"/>
                            </button>        

                        </form>

                        <form class="navbar-form navbar-left" role="search">
                            <div class="form-group">
                                <input type="text" class="form-control" placeholder="Search">
                            </div>
                            <button type="search" class="btn btn-default" aria-label="Search">
                                <img src="resources/images/magnifier.png"/>	
                            </button>        

                        </form>

                    </ul>
                    <div class="nav navbar-nav navbar-right">
                        <ul class="nav navbar-nav">
                            <li><span class="glyphicon glyphicon-thumbs-down" style="padding-top: 19px"></span><button class="btn btn-link" ng-click="showReportModal()"> Report Issue</button></li>
                            <li class="dropdown">
                                <a class="dropdown-toggle" data-toggle="dropdown" href="#"> Help <span class="caret"></span></a>
                                <ul class="dropdown-menu">
                                    <li><a href="#">User Guide</a></li>
                                    <li><a href="#">About</a></li>
                                    <li><a href="#">Contact us</a></li>            
                                </ul>
                            </li>

                            <li class="dropdown">
                                <a class="dropdown-toggle" data-toggle="dropdown" href="#"> Account <span class="caret"></span></a>
                                <ul class="dropdown-menu">
                                    <li><a href="#">My Account</a></li>
                                    <li><a href="#">Sign out</a></li>
                                </ul>
                            </li>

                        </ul>
                    </div>
                </div> <!-- /.navbar-collapse -->

                <div class="row">
                    <ul class="nav nav-pills">
                        <li class="col-md-2 col-md-offset-1"><a href="#/experiment"><img src="resources/images/flask.png"/> Experiments</a></li>
                        <li class="col-md-2"><a href="#/analysis"><img src="resources/images/map.png"/> Analysis</a></li>
                        <li class="col-md-2"><a href="#/datatrack"><img src="resources/images/datatrack.png"/> Data Tracks</a></li>
                        <li class="col-md-2"><a href="#/topic"><img src="resources/images/topic_tag.png"/> Topics</a></li>
                        <li class="dropdown col-md-2">
                            <a class="dropdown-toggle" data-toggle="dropdown" href="#"><img src="resources/images/data.png"/> Reports <span class="caret"></span></a>
                            <ul class="dropdown-menu">
                                <li><a href="#"><img src="resources/images/chart_bar.png"/>Track Usage</a></li>
                                <li><a href="#"><img src="resources/images/data.png"/>Annotation Report</a></li>
                                <li><a href="#/report"><img src="resources/images/data.png"/>Annotation Progress Report</a></li>
                                <li><a href="#"><img src="resources/images/flask.png"/>Project/Experiment Report</a></li>
                            </ul>
                        </li>
                        <!-- <li><a ng-click="refresh()"><span class="glyphicon glyphicon-refresh"></span> Refresh</a></li> -->
                    </ul>
                </div>
            </div> <!-- /.container-fluid -->

        </nav>

        <!-- ***************************************************************************************** -->
        <!-- This is filled in with the appropriate html view based on the AngularJS routing in app.js -->
        <div class="container-fluid" data-ng-view></div>



        <!--  Angular upload (two files, this one is loaded before angular.js -->
        <script src="resources/js/angular-file-upload-shim.min.js"></script>

        <!--  Angular libraries -->
        <script src="//ajax.googleapis.com/ajax/libs/angularjs/1.2.16/angular.js"></script>
        <script src="//ajax.googleapis.com/ajax/libs/angularjs/1.2.16/angular-route.js"></script>
        <script src="//ajax.googleapis.com/ajax/libs/angularjs/1.2.16/angular-resource.js"></script>
        <script src="//ajax.googleapis.com/ajax/libs/angularjs/1.2.16/angular-sanitize.js"></script>

        <script type="text/javascript" src="resources/js/angular-tree-control.js"></script>
        <link rel="stylesheet" type="text/css" href="resources/css/tree-control.css">
        <link rel="stylesheet" type="text/css" href="resources/css/tree-control-attribute.css">

        <!--  Angular charts -->
        <script src='resources/js/d3.min.js'></script>
        <script src='resources/js/nv.d3.js'></script>
        <script src='resources/js/angular-nvd3.js'></script>

        <script src="https://cdnjs.cloudflare.com/ajax/libs/flot/0.8.3/jquery.flot.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/flot/0.8.3/jquery.flot.pie.min.js"></script>

        <!-- Bootstrap JS is not required, but included for the responsive demo navigation -->
        <!-- <script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script> -->
        <script src="resources/js/ui-bootstrap-tpls-0.12.0.min.js"></script>

        <script src="resources/js/dialogs.min.js"></script>
        <script src="resources/js/ngProgress.min.js"></script>

        <!-- The File Upload Angular JS module -->
        <script src="resources/js/jquery.fileupload-angular.js"></script>
        <script src="resources/js/angular-busy.min.js"></script>
        <!-- Angular JS UI.Utils.  Does cool things like masking -->
        <script src="resources/js/ui-utils.js"></script>
        <!-- Chosen JQuery stuff -->
        <script src="resources/js/chosen.jquery.js"></script>
        <script src="resources/js/chosen.js"></script>
        <!--  autocomplete  -->
        <script src="resources/js/angucomplete-alt.js"></script>
        <!-- treeGrid -->
        <script src="resources/js/tree-grid-directive.js"></script>
        <link rel="stylesheet" type="text/css" href="resources/css/treeGrid.css">

        <!-- saveAs -->
        <script src="resources/js/FileSaver.js"></script>

        <!-- Angular upload (two files, this one is loaded after angular.js -->
        <script src="resources/js/angular-file-upload.min.js"></script>


        <script src="app/app.js"></script>
        <script src="app/common/filters.js"></script>
        <script src="app/common/directives.js"></script>
        <script src="app/common/services.js"></script>
        <script src="app/common/ConfirmationController.js"></script>
        <script src="app/common/ErrorController.js"></script>
        <script src="app/common/ReportIssueController.js"></script>
        <script src="app/common/NavbarController.js"></script>
        <script src="app/experiment/ExperimentController.js"></script>
        <script src="app/analysis/AnalysisController.js"></script>
        <script src="app/datatrack/DataTrackController.js"></script>
        <script src="app/topic/TopicController.js"></script>
        <script src="app/dashboard/DashboardController.js"></script>
        <script src="app/report/ReportController.js" ></script>

    </body>
</html>
