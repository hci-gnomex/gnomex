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
var jqxTreeMapComponent = (function () {
    function jqxTreeMapComponent(containerElement) {
        this.autoCreate = true;
        this.properties = ['baseColor', 'colorRanges', 'colorRange', 'colorMode', 'displayMember', 'height', 'hoverEnabled', 'headerHeight', 'legendLabel', 'legendPosition', 'legendScaleCallback', 'renderCallbacks', 'selectionEnabled', 'showLegend', 'source', 'theme', 'valueMember', 'width'];
        // jqxTreeMapComponent events
        this.onBindingComplete = new EventEmitter();
        this.elementRef = containerElement;
    }
    jqxTreeMapComponent.prototype.ngOnInit = function () {
        if (this.autoCreate) {
            this.createComponent();
        }
    };
    ;
    jqxTreeMapComponent.prototype.ngOnChanges = function (changes) {
        if (this.host) {
            for (var i = 0; i < this.properties.length; i++) {
                var attrName = 'attr' + this.properties[i].substring(0, 1).toUpperCase() + this.properties[i].substring(1);
                var areEqual = void 0;
                if (this[attrName] !== undefined) {
                    if (typeof this[attrName] === 'object') {
                        if (this[attrName] instanceof Array) {
                            areEqual = this.arraysEqual(this[attrName], this.host.jqxTreeMap(this.properties[i]));
                        }
                        if (areEqual) {
                            return false;
                        }
                        this.host.jqxTreeMap(this.properties[i], this[attrName]);
                        continue;
                    }
                    if (this[attrName] !== this.host.jqxTreeMap(this.properties[i])) {
                        this.host.jqxTreeMap(this.properties[i], this[attrName]);
                    }
                }
            }
        }
    };
    jqxTreeMapComponent.prototype.arraysEqual = function (attrValue, hostValue) {
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
    jqxTreeMapComponent.prototype.manageAttributes = function () {
        var options = {};
        for (var i = 0; i < this.properties.length; i++) {
            var attrName = 'attr' + this.properties[i].substring(0, 1).toUpperCase() + this.properties[i].substring(1);
            if (this[attrName] !== undefined) {
                options[this.properties[i]] = this[attrName];
            }
        }
        return options;
    };
    jqxTreeMapComponent.prototype.createComponent = function (options) {
        if (options) {
            JQXLite.extend(options, this.manageAttributes());
        }
        else {
            options = this.manageAttributes();
        }
        this.host = JQXLite(this.elementRef.nativeElement.firstChild);
        this.host[0].style.marginLeft = '1px';
        this.__wireEvents__();
        this.widgetObject = jqwidgets.createInstance(this.host, 'jqxTreeMap', options);
        this.__updateRect__();
    };
    jqxTreeMapComponent.prototype.createWidget = function (options) {
        this.createComponent(options);
    };
    jqxTreeMapComponent.prototype.__updateRect__ = function () {
        this.host.css({ width: this.attrWidth, height: this.attrHeight });
    };
    jqxTreeMapComponent.prototype.setOptions = function (options) {
        this.host.jqxTreeMap('setOptions', options);
    };
    // jqxTreeMapComponent properties
    jqxTreeMapComponent.prototype.baseColor = function (arg) {
        if (arg !== undefined) {
            this.host.jqxTreeMap('baseColor', arg);
        }
        else {
            return this.host.jqxTreeMap('baseColor');
        }
    };
    jqxTreeMapComponent.prototype.colorRanges = function (arg) {
        if (arg !== undefined) {
            this.host.jqxTreeMap('colorRanges', arg);
        }
        else {
            return this.host.jqxTreeMap('colorRanges');
        }
    };
    jqxTreeMapComponent.prototype.colorRange = function (arg) {
        if (arg !== undefined) {
            this.host.jqxTreeMap('colorRange', arg);
        }
        else {
            return this.host.jqxTreeMap('colorRange');
        }
    };
    jqxTreeMapComponent.prototype.colorMode = function (arg) {
        if (arg !== undefined) {
            this.host.jqxTreeMap('colorMode', arg);
        }
        else {
            return this.host.jqxTreeMap('colorMode');
        }
    };
    jqxTreeMapComponent.prototype.displayMember = function (arg) {
        if (arg !== undefined) {
            this.host.jqxTreeMap('displayMember', arg);
        }
        else {
            return this.host.jqxTreeMap('displayMember');
        }
    };
    jqxTreeMapComponent.prototype.height = function (arg) {
        if (arg !== undefined) {
            this.host.jqxTreeMap('height', arg);
        }
        else {
            return this.host.jqxTreeMap('height');
        }
    };
    jqxTreeMapComponent.prototype.hoverEnabled = function (arg) {
        if (arg !== undefined) {
            this.host.jqxTreeMap('hoverEnabled', arg);
        }
        else {
            return this.host.jqxTreeMap('hoverEnabled');
        }
    };
    jqxTreeMapComponent.prototype.headerHeight = function (arg) {
        if (arg !== undefined) {
            this.host.jqxTreeMap('headerHeight', arg);
        }
        else {
            return this.host.jqxTreeMap('headerHeight');
        }
    };
    jqxTreeMapComponent.prototype.legendLabel = function (arg) {
        if (arg !== undefined) {
            this.host.jqxTreeMap('legendLabel', arg);
        }
        else {
            return this.host.jqxTreeMap('legendLabel');
        }
    };
    jqxTreeMapComponent.prototype.legendPosition = function (arg) {
        if (arg !== undefined) {
            this.host.jqxTreeMap('legendPosition', arg);
        }
        else {
            return this.host.jqxTreeMap('legendPosition');
        }
    };
    jqxTreeMapComponent.prototype.legendScaleCallback = function (arg) {
        if (arg !== undefined) {
            this.host.jqxTreeMap('legendScaleCallback', arg);
        }
        else {
            return this.host.jqxTreeMap('legendScaleCallback');
        }
    };
    jqxTreeMapComponent.prototype.renderCallbacks = function (arg) {
        if (arg !== undefined) {
            this.host.jqxTreeMap('renderCallbacks', arg);
        }
        else {
            return this.host.jqxTreeMap('renderCallbacks');
        }
    };
    jqxTreeMapComponent.prototype.selectionEnabled = function (arg) {
        if (arg !== undefined) {
            this.host.jqxTreeMap('selectionEnabled', arg);
        }
        else {
            return this.host.jqxTreeMap('selectionEnabled');
        }
    };
    jqxTreeMapComponent.prototype.showLegend = function (arg) {
        if (arg !== undefined) {
            this.host.jqxTreeMap('showLegend', arg);
        }
        else {
            return this.host.jqxTreeMap('showLegend');
        }
    };
    jqxTreeMapComponent.prototype.source = function (arg) {
        if (arg !== undefined) {
            this.host.jqxTreeMap('source', arg);
        }
        else {
            return this.host.jqxTreeMap('source');
        }
    };
    jqxTreeMapComponent.prototype.theme = function (arg) {
        if (arg !== undefined) {
            this.host.jqxTreeMap('theme', arg);
        }
        else {
            return this.host.jqxTreeMap('theme');
        }
    };
    jqxTreeMapComponent.prototype.valueMember = function (arg) {
        if (arg !== undefined) {
            this.host.jqxTreeMap('valueMember', arg);
        }
        else {
            return this.host.jqxTreeMap('valueMember');
        }
    };
    jqxTreeMapComponent.prototype.width = function (arg) {
        if (arg !== undefined) {
            this.host.jqxTreeMap('width', arg);
        }
        else {
            return this.host.jqxTreeMap('width');
        }
    };
    // jqxTreeMapComponent functions
    jqxTreeMapComponent.prototype.destroy = function () {
        this.host.jqxTreeMap('destroy');
    };
    jqxTreeMapComponent.prototype.render = function () {
        this.host.jqxTreeMap('render');
    };
    jqxTreeMapComponent.prototype.__wireEvents__ = function () {
        var _this = this;
        this.host.on('bindingComplete', function (eventData) { _this.onBindingComplete.emit(eventData); });
    };
    return jqxTreeMapComponent;
}()); //jqxTreeMapComponent
__decorate([
    Input('baseColor'),
    __metadata("design:type", Object)
], jqxTreeMapComponent.prototype, "attrBaseColor", void 0);
__decorate([
    Input('colorRanges'),
    __metadata("design:type", Object)
], jqxTreeMapComponent.prototype, "attrColorRanges", void 0);
__decorate([
    Input('colorRange'),
    __metadata("design:type", Object)
], jqxTreeMapComponent.prototype, "attrColorRange", void 0);
__decorate([
    Input('colorMode'),
    __metadata("design:type", Object)
], jqxTreeMapComponent.prototype, "attrColorMode", void 0);
__decorate([
    Input('displayMember'),
    __metadata("design:type", Object)
], jqxTreeMapComponent.prototype, "attrDisplayMember", void 0);
__decorate([
    Input('hoverEnabled'),
    __metadata("design:type", Object)
], jqxTreeMapComponent.prototype, "attrHoverEnabled", void 0);
__decorate([
    Input('headerHeight'),
    __metadata("design:type", Object)
], jqxTreeMapComponent.prototype, "attrHeaderHeight", void 0);
__decorate([
    Input('legendLabel'),
    __metadata("design:type", Object)
], jqxTreeMapComponent.prototype, "attrLegendLabel", void 0);
__decorate([
    Input('legendPosition'),
    __metadata("design:type", Object)
], jqxTreeMapComponent.prototype, "attrLegendPosition", void 0);
__decorate([
    Input('legendScaleCallback'),
    __metadata("design:type", Object)
], jqxTreeMapComponent.prototype, "attrLegendScaleCallback", void 0);
__decorate([
    Input('renderCallbacks'),
    __metadata("design:type", Object)
], jqxTreeMapComponent.prototype, "attrRenderCallbacks", void 0);
__decorate([
    Input('selectionEnabled'),
    __metadata("design:type", Object)
], jqxTreeMapComponent.prototype, "attrSelectionEnabled", void 0);
__decorate([
    Input('showLegend'),
    __metadata("design:type", Object)
], jqxTreeMapComponent.prototype, "attrShowLegend", void 0);
__decorate([
    Input('source'),
    __metadata("design:type", Object)
], jqxTreeMapComponent.prototype, "attrSource", void 0);
__decorate([
    Input('theme'),
    __metadata("design:type", Object)
], jqxTreeMapComponent.prototype, "attrTheme", void 0);
__decorate([
    Input('valueMember'),
    __metadata("design:type", Object)
], jqxTreeMapComponent.prototype, "attrValueMember", void 0);
__decorate([
    Input('width'),
    __metadata("design:type", Object)
], jqxTreeMapComponent.prototype, "attrWidth", void 0);
__decorate([
    Input('height'),
    __metadata("design:type", Object)
], jqxTreeMapComponent.prototype, "attrHeight", void 0);
__decorate([
    Input('auto-create'),
    __metadata("design:type", Boolean)
], jqxTreeMapComponent.prototype, "autoCreate", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxTreeMapComponent.prototype, "onBindingComplete", void 0);
jqxTreeMapComponent = __decorate([
    Component({
        selector: 'jqxTreeMap',
        template: '<div><ng-content></ng-content></div>'
    }),
    __metadata("design:paramtypes", [ElementRef])
], jqxTreeMapComponent);
export { jqxTreeMapComponent };
//# sourceMappingURL=angular_jqxtreemap.js.map