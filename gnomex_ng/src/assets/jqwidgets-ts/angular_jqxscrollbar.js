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
var jqxScrollBarComponent = (function () {
    function jqxScrollBarComponent(containerElement) {
        this.autoCreate = true;
        this.properties = ['disabled', 'height', 'largestep', 'min', 'max', 'rtl', 'step', 'showButtons', 'thumbMinSize', 'theme', 'vertical', 'value', 'width'];
        // jqxScrollBarComponent events
        this.onValueChanged = new EventEmitter();
        this.elementRef = containerElement;
    }
    jqxScrollBarComponent.prototype.ngOnInit = function () {
        if (this.autoCreate) {
            this.createComponent();
        }
    };
    ;
    jqxScrollBarComponent.prototype.ngOnChanges = function (changes) {
        if (this.host) {
            for (var i = 0; i < this.properties.length; i++) {
                var attrName = 'attr' + this.properties[i].substring(0, 1).toUpperCase() + this.properties[i].substring(1);
                var areEqual = void 0;
                if (this[attrName] !== undefined) {
                    if (typeof this[attrName] === 'object') {
                        if (this[attrName] instanceof Array) {
                            areEqual = this.arraysEqual(this[attrName], this.host.jqxScrollBar(this.properties[i]));
                        }
                        if (areEqual) {
                            return false;
                        }
                        this.host.jqxScrollBar(this.properties[i], this[attrName]);
                        continue;
                    }
                    if (this[attrName] !== this.host.jqxScrollBar(this.properties[i])) {
                        this.host.jqxScrollBar(this.properties[i], this[attrName]);
                    }
                }
            }
        }
    };
    jqxScrollBarComponent.prototype.arraysEqual = function (attrValue, hostValue) {
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
    jqxScrollBarComponent.prototype.manageAttributes = function () {
        var options = {};
        for (var i = 0; i < this.properties.length; i++) {
            var attrName = 'attr' + this.properties[i].substring(0, 1).toUpperCase() + this.properties[i].substring(1);
            if (this[attrName] !== undefined) {
                options[this.properties[i]] = this[attrName];
            }
        }
        return options;
    };
    jqxScrollBarComponent.prototype.createComponent = function (options) {
        if (options) {
            JQXLite.extend(options, this.manageAttributes());
        }
        else {
            options = this.manageAttributes();
        }
        this.host = JQXLite(this.elementRef.nativeElement.firstChild);
        this.__wireEvents__();
        this.widgetObject = jqwidgets.createInstance(this.host, 'jqxScrollBar', options);
        this.__updateRect__();
    };
    jqxScrollBarComponent.prototype.createWidget = function (options) {
        this.createComponent(options);
    };
    jqxScrollBarComponent.prototype.__updateRect__ = function () {
        this.host.css({ width: this.attrWidth, height: this.attrHeight });
    };
    jqxScrollBarComponent.prototype.setOptions = function (options) {
        this.host.jqxScrollBar('setOptions', options);
    };
    // jqxScrollBarComponent properties
    jqxScrollBarComponent.prototype.disabled = function (arg) {
        if (arg !== undefined) {
            this.host.jqxScrollBar('disabled', arg);
        }
        else {
            return this.host.jqxScrollBar('disabled');
        }
    };
    jqxScrollBarComponent.prototype.height = function (arg) {
        if (arg !== undefined) {
            this.host.jqxScrollBar('height', arg);
        }
        else {
            return this.host.jqxScrollBar('height');
        }
    };
    jqxScrollBarComponent.prototype.largestep = function (arg) {
        if (arg !== undefined) {
            this.host.jqxScrollBar('largestep', arg);
        }
        else {
            return this.host.jqxScrollBar('largestep');
        }
    };
    jqxScrollBarComponent.prototype.min = function (arg) {
        if (arg !== undefined) {
            this.host.jqxScrollBar('min', arg);
        }
        else {
            return this.host.jqxScrollBar('min');
        }
    };
    jqxScrollBarComponent.prototype.max = function (arg) {
        if (arg !== undefined) {
            this.host.jqxScrollBar('max', arg);
        }
        else {
            return this.host.jqxScrollBar('max');
        }
    };
    jqxScrollBarComponent.prototype.rtl = function (arg) {
        if (arg !== undefined) {
            this.host.jqxScrollBar('rtl', arg);
        }
        else {
            return this.host.jqxScrollBar('rtl');
        }
    };
    jqxScrollBarComponent.prototype.step = function (arg) {
        if (arg !== undefined) {
            this.host.jqxScrollBar('step', arg);
        }
        else {
            return this.host.jqxScrollBar('step');
        }
    };
    jqxScrollBarComponent.prototype.showButtons = function (arg) {
        if (arg !== undefined) {
            this.host.jqxScrollBar('showButtons', arg);
        }
        else {
            return this.host.jqxScrollBar('showButtons');
        }
    };
    jqxScrollBarComponent.prototype.thumbMinSize = function (arg) {
        if (arg !== undefined) {
            this.host.jqxScrollBar('thumbMinSize', arg);
        }
        else {
            return this.host.jqxScrollBar('thumbMinSize');
        }
    };
    jqxScrollBarComponent.prototype.theme = function (arg) {
        if (arg !== undefined) {
            this.host.jqxScrollBar('theme', arg);
        }
        else {
            return this.host.jqxScrollBar('theme');
        }
    };
    jqxScrollBarComponent.prototype.vertical = function (arg) {
        if (arg !== undefined) {
            this.host.jqxScrollBar('vertical', arg);
        }
        else {
            return this.host.jqxScrollBar('vertical');
        }
    };
    jqxScrollBarComponent.prototype.value = function (arg) {
        if (arg !== undefined) {
            this.host.jqxScrollBar('value', arg);
        }
        else {
            return this.host.jqxScrollBar('value');
        }
    };
    jqxScrollBarComponent.prototype.width = function (arg) {
        if (arg !== undefined) {
            this.host.jqxScrollBar('width', arg);
        }
        else {
            return this.host.jqxScrollBar('width');
        }
    };
    // jqxScrollBarComponent functions
    jqxScrollBarComponent.prototype.destroy = function () {
        this.host.jqxScrollBar('destroy');
    };
    jqxScrollBarComponent.prototype.isScrolling = function () {
        return this.host.jqxScrollBar('isScrolling');
    };
    jqxScrollBarComponent.prototype.setPosition = function (index) {
        this.host.jqxScrollBar('setPosition', index);
    };
    jqxScrollBarComponent.prototype.__wireEvents__ = function () {
        var _this = this;
        this.host.on('valueChanged', function (eventData) { _this.onValueChanged.emit(eventData); });
    };
    return jqxScrollBarComponent;
}()); //jqxScrollBarComponent
__decorate([
    Input('disabled'),
    __metadata("design:type", Object)
], jqxScrollBarComponent.prototype, "attrDisabled", void 0);
__decorate([
    Input('largestep'),
    __metadata("design:type", Object)
], jqxScrollBarComponent.prototype, "attrLargestep", void 0);
__decorate([
    Input('min'),
    __metadata("design:type", Object)
], jqxScrollBarComponent.prototype, "attrMin", void 0);
__decorate([
    Input('max'),
    __metadata("design:type", Object)
], jqxScrollBarComponent.prototype, "attrMax", void 0);
__decorate([
    Input('rtl'),
    __metadata("design:type", Object)
], jqxScrollBarComponent.prototype, "attrRtl", void 0);
__decorate([
    Input('step'),
    __metadata("design:type", Object)
], jqxScrollBarComponent.prototype, "attrStep", void 0);
__decorate([
    Input('showButtons'),
    __metadata("design:type", Object)
], jqxScrollBarComponent.prototype, "attrShowButtons", void 0);
__decorate([
    Input('thumbMinSize'),
    __metadata("design:type", Object)
], jqxScrollBarComponent.prototype, "attrThumbMinSize", void 0);
__decorate([
    Input('theme'),
    __metadata("design:type", Object)
], jqxScrollBarComponent.prototype, "attrTheme", void 0);
__decorate([
    Input('vertical'),
    __metadata("design:type", Object)
], jqxScrollBarComponent.prototype, "attrVertical", void 0);
__decorate([
    Input('value'),
    __metadata("design:type", Object)
], jqxScrollBarComponent.prototype, "attrValue", void 0);
__decorate([
    Input('width'),
    __metadata("design:type", Object)
], jqxScrollBarComponent.prototype, "attrWidth", void 0);
__decorate([
    Input('height'),
    __metadata("design:type", Object)
], jqxScrollBarComponent.prototype, "attrHeight", void 0);
__decorate([
    Input('auto-create'),
    __metadata("design:type", Boolean)
], jqxScrollBarComponent.prototype, "autoCreate", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxScrollBarComponent.prototype, "onValueChanged", void 0);
jqxScrollBarComponent = __decorate([
    Component({
        selector: 'jqxScrollBar',
        template: '<div><ng-content></ng-content></div>'
    }),
    __metadata("design:paramtypes", [ElementRef])
], jqxScrollBarComponent);
export { jqxScrollBarComponent };
//# sourceMappingURL=angular_jqxscrollbar.js.map