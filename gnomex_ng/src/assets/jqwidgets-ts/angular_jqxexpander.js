"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
/*
jQWidgets v4.5.4 (2017-June)
Copyright (c) 2011-2017 jQWidgets.
License: http://jqwidgets.com/license/
*/
/// <reference path="jqwidgets.d.ts" />
var core_1 = require("@angular/core");
var jqxExpanderComponent = (function () {
    function jqxExpanderComponent(containerElement) {
        this.autoCreate = true;
        this.properties = ['animationType', 'arrowPosition', 'collapseAnimationDuration', 'disabled', 'expanded', 'expandAnimationDuration', 'height', 'headerPosition', 'initContent', 'rtl', 'showArrow', 'theme', 'toggleMode', 'width'];
        // jqxExpanderComponent events
        this.onCollapsing = new core_1.EventEmitter();
        this.onCollapsed = new core_1.EventEmitter();
        this.onExpanding = new core_1.EventEmitter();
        this.onExpanded = new core_1.EventEmitter();
        this.elementRef = containerElement;
    }
    jqxExpanderComponent.prototype.ngOnInit = function () {
        if (this.autoCreate) {
            this.createComponent();
        }
    };
    ;
    jqxExpanderComponent.prototype.ngOnChanges = function (changes) {
        if (this.host) {
            for (var i = 0; i < this.properties.length; i++) {
                var attrName = 'attr' + this.properties[i].substring(0, 1).toUpperCase() + this.properties[i].substring(1);
                var areEqual = void 0;
                if (this[attrName] !== undefined) {
                    if (typeof this[attrName] === 'object') {
                        if (this[attrName] instanceof Array) {
                            areEqual = this.arraysEqual(this[attrName], this.host.jqxExpander(this.properties[i]));
                        }
                        if (areEqual) {
                            return false;
                        }
                        this.host.jqxExpander(this.properties[i], this[attrName]);
                        continue;
                    }
                    if (this[attrName] !== this.host.jqxExpander(this.properties[i])) {
                        this.host.jqxExpander(this.properties[i], this[attrName]);
                    }
                }
            }
        }
    };
    jqxExpanderComponent.prototype.arraysEqual = function (attrValue, hostValue) {
        if (attrValue.length != hostValue.length) {
            return false;
        }
        for (var i = 0; i < attrValue.length; i++) {
            if (attrValue[i] !== hostValue[i]) {
                return false;
            }
        }
        return true;
    };
    jqxExpanderComponent.prototype.manageAttributes = function () {
        var options = {};
        for (var i = 0; i < this.properties.length; i++) {
            var attrName = 'attr' + this.properties[i].substring(0, 1).toUpperCase() + this.properties[i].substring(1);
            if (this[attrName] !== undefined) {
                options[this.properties[i]] = this[attrName];
            }
        }
        return options;
    };
    jqxExpanderComponent.prototype.createComponent = function (options) {
        if (options) {
            JQXLite.extend(options, this.manageAttributes());
        }
        else {
            options = this.manageAttributes();
        }
        this.host = JQXLite(this.elementRef.nativeElement.firstChild);
        this.__wireEvents__();
        this.widgetObject = jqwidgets.createInstance(this.host, 'jqxExpander', options);
        this.__updateRect__();
    };
    jqxExpanderComponent.prototype.createWidget = function (options) {
        this.createComponent(options);
    };
    jqxExpanderComponent.prototype.__updateRect__ = function () {
        this.host.css({ width: this.attrWidth, height: this.attrHeight });
    };
    jqxExpanderComponent.prototype.setOptions = function (options) {
        this.host.jqxExpander('setOptions', options);
    };
    // jqxExpanderComponent properties
    jqxExpanderComponent.prototype.animationType = function (arg) {
        if (arg !== undefined) {
            this.host.jqxExpander('animationType', arg);
        }
        else {
            return this.host.jqxExpander('animationType');
        }
    };
    jqxExpanderComponent.prototype.arrowPosition = function (arg) {
        if (arg !== undefined) {
            this.host.jqxExpander('arrowPosition', arg);
        }
        else {
            return this.host.jqxExpander('arrowPosition');
        }
    };
    jqxExpanderComponent.prototype.collapseAnimationDuration = function (arg) {
        if (arg !== undefined) {
            this.host.jqxExpander('collapseAnimationDuration', arg);
        }
        else {
            return this.host.jqxExpander('collapseAnimationDuration');
        }
    };
    jqxExpanderComponent.prototype.disabled = function (arg) {
        if (arg !== undefined) {
            this.host.jqxExpander('disabled', arg);
        }
        else {
            return this.host.jqxExpander('disabled');
        }
    };
    jqxExpanderComponent.prototype.expanded = function (arg) {
        if (arg !== undefined) {
            this.host.jqxExpander('expanded', arg);
        }
        else {
            return this.host.jqxExpander('expanded');
        }
    };
    jqxExpanderComponent.prototype.expandAnimationDuration = function (arg) {
        if (arg !== undefined) {
            this.host.jqxExpander('expandAnimationDuration', arg);
        }
        else {
            return this.host.jqxExpander('expandAnimationDuration');
        }
    };
    jqxExpanderComponent.prototype.height = function (arg) {
        if (arg !== undefined) {
            this.host.jqxExpander('height', arg);
        }
        else {
            return this.host.jqxExpander('height');
        }
    };
    jqxExpanderComponent.prototype.headerPosition = function (arg) {
        if (arg !== undefined) {
            this.host.jqxExpander('headerPosition', arg);
        }
        else {
            return this.host.jqxExpander('headerPosition');
        }
    };
    jqxExpanderComponent.prototype.initContent = function (arg) {
        if (arg !== undefined) {
            this.host.jqxExpander('initContent', arg);
        }
        else {
            return this.host.jqxExpander('initContent');
        }
    };
    jqxExpanderComponent.prototype.rtl = function (arg) {
        if (arg !== undefined) {
            this.host.jqxExpander('rtl', arg);
        }
        else {
            return this.host.jqxExpander('rtl');
        }
    };
    jqxExpanderComponent.prototype.showArrow = function (arg) {
        if (arg !== undefined) {
            this.host.jqxExpander('showArrow', arg);
        }
        else {
            return this.host.jqxExpander('showArrow');
        }
    };
    jqxExpanderComponent.prototype.theme = function (arg) {
        if (arg !== undefined) {
            this.host.jqxExpander('theme', arg);
        }
        else {
            return this.host.jqxExpander('theme');
        }
    };
    jqxExpanderComponent.prototype.toggleMode = function (arg) {
        if (arg !== undefined) {
            this.host.jqxExpander('toggleMode', arg);
        }
        else {
            return this.host.jqxExpander('toggleMode');
        }
    };
    jqxExpanderComponent.prototype.width = function (arg) {
        if (arg !== undefined) {
            this.host.jqxExpander('width', arg);
        }
        else {
            return this.host.jqxExpander('width');
        }
    };
    // jqxExpanderComponent functions
    jqxExpanderComponent.prototype.collapse = function () {
        this.host.jqxExpander('collapse');
    };
    jqxExpanderComponent.prototype.disable = function () {
        this.host.jqxExpander('disable');
    };
    jqxExpanderComponent.prototype.destroy = function () {
        this.host.jqxExpander('destroy');
    };
    jqxExpanderComponent.prototype.enable = function () {
        this.host.jqxExpander('enable');
    };
    jqxExpanderComponent.prototype.expand = function () {
        this.host.jqxExpander('expand');
    };
    jqxExpanderComponent.prototype.focus = function () {
        this.host.jqxExpander('focus');
    };
    jqxExpanderComponent.prototype.getContent = function () {
        return this.host.jqxExpander('getContent');
    };
    jqxExpanderComponent.prototype.getHeaderContent = function () {
        return this.host.jqxExpander('getHeaderContent');
    };
    jqxExpanderComponent.prototype.invalidate = function () {
        this.host.jqxExpander('invalidate');
    };
    jqxExpanderComponent.prototype.refresh = function () {
        this.host.jqxExpander('refresh');
    };
    jqxExpanderComponent.prototype.render = function () {
        this.host.jqxExpander('render');
    };
    jqxExpanderComponent.prototype.setHeaderContent = function (headerContent) {
        this.host.jqxExpander('setHeaderContent', headerContent);
    };
    jqxExpanderComponent.prototype.setContent = function (content) {
        this.host.jqxExpander('setContent', content);
    };
    jqxExpanderComponent.prototype.__wireEvents__ = function () {
        var _this = this;
        this.host.on('collapsing', function (eventData) { _this.onCollapsing.emit(eventData); });
        this.host.on('collapsed', function (eventData) { _this.onCollapsed.emit(eventData); });
        this.host.on('expanding', function (eventData) { _this.onExpanding.emit(eventData); });
        this.host.on('expanded', function (eventData) { _this.onExpanded.emit(eventData); });
    };
    return jqxExpanderComponent;
}()); //jqxExpanderComponent
__decorate([
    core_1.Input('animationType')
], jqxExpanderComponent.prototype, "attrAnimationType", void 0);
__decorate([
    core_1.Input('arrowPosition')
], jqxExpanderComponent.prototype, "attrArrowPosition", void 0);
__decorate([
    core_1.Input('collapseAnimationDuration')
], jqxExpanderComponent.prototype, "attrCollapseAnimationDuration", void 0);
__decorate([
    core_1.Input('disabled')
], jqxExpanderComponent.prototype, "attrDisabled", void 0);
__decorate([
    core_1.Input('expanded')
], jqxExpanderComponent.prototype, "attrExpanded", void 0);
__decorate([
    core_1.Input('expandAnimationDuration')
], jqxExpanderComponent.prototype, "attrExpandAnimationDuration", void 0);
__decorate([
    core_1.Input('headerPosition')
], jqxExpanderComponent.prototype, "attrHeaderPosition", void 0);
__decorate([
    core_1.Input('initContent')
], jqxExpanderComponent.prototype, "attrInitContent", void 0);
__decorate([
    core_1.Input('rtl')
], jqxExpanderComponent.prototype, "attrRtl", void 0);
__decorate([
    core_1.Input('showArrow')
], jqxExpanderComponent.prototype, "attrShowArrow", void 0);
__decorate([
    core_1.Input('theme')
], jqxExpanderComponent.prototype, "attrTheme", void 0);
__decorate([
    core_1.Input('toggleMode')
], jqxExpanderComponent.prototype, "attrToggleMode", void 0);
__decorate([
    core_1.Input('width')
], jqxExpanderComponent.prototype, "attrWidth", void 0);
__decorate([
    core_1.Input('height')
], jqxExpanderComponent.prototype, "attrHeight", void 0);
__decorate([
    core_1.Input('auto-create')
], jqxExpanderComponent.prototype, "autoCreate", void 0);
__decorate([
    core_1.Output()
], jqxExpanderComponent.prototype, "onCollapsing", void 0);
__decorate([
    core_1.Output()
], jqxExpanderComponent.prototype, "onCollapsed", void 0);
__decorate([
    core_1.Output()
], jqxExpanderComponent.prototype, "onExpanding", void 0);
__decorate([
    core_1.Output()
], jqxExpanderComponent.prototype, "onExpanded", void 0);
jqxExpanderComponent = __decorate([
    core_1.Component({
        selector: 'jqxExpander',
        template: '<div><ng-content></ng-content></div>'
    })
], jqxExpanderComponent);
exports.jqxExpanderComponent = jqxExpanderComponent;
