var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
/*
jQWidgets v4.5.4 (2017-June)
Copyright (c) 2011-2017 jQWidgets.
License: http://jqwidgets.com/license/
*/
/// <reference path="jqwidgets.d.ts" />
import { Component, Input, Output, EventEmitter, ElementRef } from '@angular/core';
var jqxSplitterComponent = (function () {
    function jqxSplitterComponent(containerElement) {
        this.autoCreate = true;
        this.properties = ['disabled', 'height', 'orientation', 'panels', 'resizable', 'splitBarSize', 'showSplitBar', 'theme', 'width'];
        // jqxSplitterComponent events
        this.onCollapsed = new EventEmitter();
        this.onExpanded = new EventEmitter();
        this.onResize = new EventEmitter();
        this.onResizeStart = new EventEmitter();
        this.elementRef = containerElement;
    }
    jqxSplitterComponent.prototype.ngOnInit = function () {
        if (this.autoCreate) {
            this.createComponent();
        }
    };
    ;
    jqxSplitterComponent.prototype.ngOnChanges = function (changes) {
        if (this.host) {
            for (var i = 0; i < this.properties.length; i++) {
                var attrName = 'attr' + this.properties[i].substring(0, 1).toUpperCase() + this.properties[i].substring(1);
                var areEqual = void 0;
                if (this[attrName] !== undefined) {
                    if (typeof this[attrName] === 'object') {
                        if (this[attrName] instanceof Array) {
                            areEqual = this.arraysEqual(this[attrName], this.host.jqxSplitter(this.properties[i]));
                        }
                        if (areEqual) {
                            return false;
                        }
                        this.host.jqxSplitter(this.properties[i], this[attrName]);
                        continue;
                    }
                    if (this[attrName] !== this.host.jqxSplitter(this.properties[i])) {
                        this.host.jqxSplitter(this.properties[i], this[attrName]);
                    }
                }
            }
        }
    };
    jqxSplitterComponent.prototype.arraysEqual = function (attrValue, hostValue) {
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
    jqxSplitterComponent.prototype.manageAttributes = function () {
        var options = {};
        for (var i = 0; i < this.properties.length; i++) {
            var attrName = 'attr' + this.properties[i].substring(0, 1).toUpperCase() + this.properties[i].substring(1);
            if (this[attrName] !== undefined) {
                options[this.properties[i]] = this[attrName];
            }
        }
        return options;
    };
    jqxSplitterComponent.prototype.createComponent = function (options) {
        if (options) {
            JQXLite.extend(options, this.manageAttributes());
        }
        else {
            options = this.manageAttributes();
        }
        this.host = JQXLite(this.elementRef.nativeElement.firstChild);
        this.__wireEvents__();
        this.widgetObject = jqwidgets.createInstance(this.host, 'jqxSplitter', options);
        this.__updateRect__();
    };
    jqxSplitterComponent.prototype.createWidget = function (options) {
        this.createComponent(options);
    };
    jqxSplitterComponent.prototype.__updateRect__ = function () {
        this.host.css({ width: this.attrWidth, height: this.attrHeight });
    };
    jqxSplitterComponent.prototype.setOptions = function (options) {
        this.host.jqxSplitter('setOptions', options);
    };
    // jqxSplitterComponent properties
    jqxSplitterComponent.prototype.disabled = function (arg) {
        if (arg !== undefined) {
            this.host.jqxSplitter('disabled', arg);
        }
        else {
            return this.host.jqxSplitter('disabled');
        }
    };
    jqxSplitterComponent.prototype.height = function (arg) {
        if (arg !== undefined) {
            this.host.jqxSplitter('height', arg);
        }
        else {
            return this.host.jqxSplitter('height');
        }
    };
    jqxSplitterComponent.prototype.orientation = function (arg) {
        if (arg !== undefined) {
            this.host.jqxSplitter('orientation', arg);
        }
        else {
            return this.host.jqxSplitter('orientation');
        }
    };
    jqxSplitterComponent.prototype.panels = function (arg) {
        if (arg !== undefined) {
            this.host.jqxSplitter('panels', arg);
        }
        else {
            return this.host.jqxSplitter('panels');
        }
    };
    jqxSplitterComponent.prototype.resizable = function (arg) {
        if (arg !== undefined) {
            this.host.jqxSplitter('resizable', arg);
        }
        else {
            return this.host.jqxSplitter('resizable');
        }
    };
    jqxSplitterComponent.prototype.splitBarSize = function (arg) {
        if (arg !== undefined) {
            this.host.jqxSplitter('splitBarSize', arg);
        }
        else {
            return this.host.jqxSplitter('splitBarSize');
        }
    };
    jqxSplitterComponent.prototype.showSplitBar = function (arg) {
        if (arg !== undefined) {
            this.host.jqxSplitter('showSplitBar', arg);
        }
        else {
            return this.host.jqxSplitter('showSplitBar');
        }
    };
    jqxSplitterComponent.prototype.theme = function (arg) {
        if (arg !== undefined) {
            this.host.jqxSplitter('theme', arg);
        }
        else {
            return this.host.jqxSplitter('theme');
        }
    };
    jqxSplitterComponent.prototype.width = function (arg) {
        if (arg !== undefined) {
            this.host.jqxSplitter('width', arg);
        }
        else {
            return this.host.jqxSplitter('width');
        }
    };
    // jqxSplitterComponent functions
    jqxSplitterComponent.prototype.collapse = function () {
        this.host.jqxSplitter('collapse');
    };
    jqxSplitterComponent.prototype.destroy = function () {
        this.host.jqxSplitter('destroy');
    };
    jqxSplitterComponent.prototype.disable = function () {
        this.host.jqxSplitter('disable');
    };
    jqxSplitterComponent.prototype.enable = function () {
        this.host.jqxSplitter('enable');
    };
    jqxSplitterComponent.prototype.expand = function () {
        this.host.jqxSplitter('expand');
    };
    jqxSplitterComponent.prototype.render = function () {
        this.host.jqxSplitter('render');
    };
    jqxSplitterComponent.prototype.refresh = function () {
        this.host.jqxSplitter('refresh');
    };
    jqxSplitterComponent.prototype.__wireEvents__ = function () {
        var _this = this;
        this.host.on('collapsed', function (eventData) { _this.onCollapsed.emit(eventData); });
        this.host.on('expanded', function (eventData) { _this.onExpanded.emit(eventData); });
        this.host.on('resize', function (eventData) { _this.onResize.emit(eventData); });
        this.host.on('resizeStart', function (eventData) { _this.onResizeStart.emit(eventData); });
    };
    return jqxSplitterComponent;
}()); //jqxSplitterComponent
__decorate([
    Input('disabled'),
    __metadata("design:type", Object)
], jqxSplitterComponent.prototype, "attrDisabled", void 0);
__decorate([
    Input('orientation'),
    __metadata("design:type", Object)
], jqxSplitterComponent.prototype, "attrOrientation", void 0);
__decorate([
    Input('panels'),
    __metadata("design:type", Object)
], jqxSplitterComponent.prototype, "attrPanels", void 0);
__decorate([
    Input('resizable'),
    __metadata("design:type", Object)
], jqxSplitterComponent.prototype, "attrResizable", void 0);
__decorate([
    Input('splitBarSize'),
    __metadata("design:type", Object)
], jqxSplitterComponent.prototype, "attrSplitBarSize", void 0);
__decorate([
    Input('showSplitBar'),
    __metadata("design:type", Object)
], jqxSplitterComponent.prototype, "attrShowSplitBar", void 0);
__decorate([
    Input('theme'),
    __metadata("design:type", Object)
], jqxSplitterComponent.prototype, "attrTheme", void 0);
__decorate([
    Input('width'),
    __metadata("design:type", Object)
], jqxSplitterComponent.prototype, "attrWidth", void 0);
__decorate([
    Input('height'),
    __metadata("design:type", Object)
], jqxSplitterComponent.prototype, "attrHeight", void 0);
__decorate([
    Input('auto-create'),
    __metadata("design:type", Boolean)
], jqxSplitterComponent.prototype, "autoCreate", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxSplitterComponent.prototype, "onCollapsed", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxSplitterComponent.prototype, "onExpanded", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxSplitterComponent.prototype, "onResize", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxSplitterComponent.prototype, "onResizeStart", void 0);
jqxSplitterComponent = __decorate([
    Component({
        selector: 'jqxSplitter',
        template: '<div><ng-content></ng-content></div>'
    }),
    __metadata("design:paramtypes", [ElementRef])
], jqxSplitterComponent);
export { jqxSplitterComponent };
//# sourceMappingURL=angular_jqxsplitter.js.map