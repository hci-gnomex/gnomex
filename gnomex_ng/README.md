
## Getting Started

At this time not all of the @hci npm modules have been published to a public repository, so if you are trying to build 
this project and you are outside of the HCI organization, then you may experience some problems. We are working to 
correct this situation.

### Prerequisites
You will, of course need [Git](http://git-scm.com) to clone the project.  You will also need NodeJS and 
[npm](https://www.npmjs.com/) (node package manager).  You can get them from here http://nodejs.org

### Install your dependencies
Your project has two kinds of dependencies, both install and managed through and with [npm](https://www.npmjs.com/):
* Runtime dependencies - found in the `dependencies` array in [package.json](package.json). All of these are the dependencies your application needs at runtime.
* Dev Dependencies - found in the `devDependencies` array in [package.json](package.json). All of these are the dependencies use in development for compiling, running and testing your application.

To install all of the dependencies, simply type the following command into you terminal
```
npm install
```
It may take a minute or two. Everything that is installed in put into the `node_modules` directory in your project.  This 
directory is NOT source controlled and should not be.

### Run the applications
This is pre-configured to compile and run you applications in a simple development web server. Start this with:
```
npm start
```
When started, this will server the app on http://localhost:3000/ This port is pre-configured, but you can change it if you 
like. It is specified in two spots: 
 * [package.json](package.json) - line 9
 * [webpack/webpack.dev.js](config/webpack.dev.js) - line 11

As you make changes this web server will actively watch all files in the project, recompile them and reload your browser. 
It's pretty handy. 

## Directory Layout and Code Style Guide
The project structure attempts to adhere to the [Angular Style Guide](https://angular.io/docs/ts/latest/guide/style-guide.html). Please take some time to review its conventions. Following the suggestions in teh style guide will help us to keep our code organized in a common manner and help all of us more easily and quickly adapt to new, unfamiliar projects.

## Testing
More to come as I learn more about testing. If you know about this, please fill in the blanks.

## Tooling
This project, as mentioned above, relies on NodeJS and npm to manage dependencies and run project related scripts for 
building, etc.

This project uses [Webpack](https://webpack.github.io/) for building, bundling and serving for development. This is a 
helpful Webpack tutorial/introduction: [WEBPACK: AN INTRODUCTION](https://angular.io/docs/ts/latest/guide/webpack.html). 

For testing, this project uses Jasmine and Karma, more information can be found here: [Techniques and practices for testing an Angular app](https://angular.io/docs/ts/latest/guide/testing.html)

We will be using [less](http://lesscss.org/) as a CSS pre-processor.