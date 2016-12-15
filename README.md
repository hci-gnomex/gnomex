# GNomEx

Project website:  <http://hci-bio-demo.hci.utah.edu/gnomexDocumentation/wordpress/> 
TODO (JEH 12/15/2016) - UPDATE THIS - This document is out of date for Gnomex 6


## Description

GNomEx is genomic LIMS and data repository. It holds annotated experiments and downstream analysis and serves data tracks to popular genome browsers such as IGB, IGV, and UCSC genome browser. The LIMS handles all aspects of the experiment from order through results delivery including billing for services/consumables. Experiment platforms supported include Illumina HiSeq, MiSeq, iScan, ABI Sanger sequencing, Affy and Agilent Microarrays, Sequenom MassArray and Bioanalyzer.

GNomEx is a web application with a Flash client user interface and a Java backend that runs on Apache Tomcat. MySQL is used for the database, although any modern RDBMS can be used.

## Features
- Configurable for many different sequencing and experiment platforms
- Support for multiple core facilities
- Automated billing/customer notifications
- Configurable annotations for experiments, samples, or analyses
- Built in ABI file viewer

### SourceForge Link 
<https://sourceforge.net/projects/gnomex/>
TODO (JEH 12/15/2016) - UPDATE THIS - This document is out of date for Gnomex 6


## Gnomex 6 - a work in progress
Gnomex 6 is a Gradle multi-project comprised of 4 distinct project

**NOTE** - Some libraries and NPM pacakages use in this project are not yet available in a public space.  We will be making 
these available as soon as possible. So, it is unlikely that this project could be successfully built by those working outside 
of Huntsman Cancer Institute Research Informatics.

### Overview

1. **gnomex** - the primary or parent project.  This is the root directory of the cloned repo
1. **[gnomex_ng]()** - the Angular 2 project
1. **[gnomex_crypt]()** - a library project that gets built separately and on a server like Tomcat, is deployed to `<tomcat>/lib`
1. **[gnomex_tomcat_support]()** - a library project that gets built separately and on a server like Tomcat, is deployed to `<tomcat>/lib`

### Building

#### Prerequisites:

These utilities are required:
1. Node
1. Npm
1. Java 8
1. Gradle (optional, we use the Gradle wrapper)
1. All `hci-conventions` related properties in `<user_home>/.gradle/gradle.properties`
1. Add `gnomexServerHome` and `gnomexDeployDir` to `<user_home>/.gradle/gradle.properties`

> The properties `gnomexServerHome` and `gnomexDeployDir` may be overridden or included via the command-line
> but is is easier to include them in your `<user_home>/.gradle/gradle.properties`. They are defined as: 
>
> The root directory of Tomcat, like:
>
> ```
> gnomexServerHome=/some/path/to/apache-tomcat
> ```
>
> The deploy directory in Tomat, normall `webapps`
> 
> ```
> gnomexDeployDir=webapps
> ```

With the prerequisites in place...

#### Building the application:
1. Change to the [gnomex_ng]() directory and run `npm install` or `npm update`
1. Change back to the root project directory and run: `./gradlew gnomex_all` or on Windows: `gradlew gnomex_all`
    + This will build the war and the other projects (gnomex_ng, gnomex_crypt, gnomex_tomcat_support) assemble the war 
    and copy it all out to the targeted Tomcat server. You can inspect the resulting war in `./build/libs`
    + This build current is not building out anything database related, to the deployed app assumes you have the Gnomex database in place.
1. You should be able to start up Tomcat and login.

