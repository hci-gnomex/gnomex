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
var jqxProgressBarComponent = (function () {
    function jqxProgressBarComponent(containerElement) {
        this.autoCreate = true;
        this.properties = ['animationDuration', 'colorRanges', 'disabled', 'height', 'layout', 'max', 'min', 'orientation', 'rtl', 'renderText', 'showText', 'template', 'theme', 'value', 'width'];
        // jqxProgressBarComponent events
        this.onComplete = new EventEmitter();
        this.onInvalidvalue = new EventEmitter();
        this.onValueChanged = new EventEmitter();
        this.elementRef = containerElement;
    }
    jqxProgressBarComponent.prototype.ngOnInit = function () {
        if (this.autoCreate) {
            this.createComponent();
        }
    };
    ;
    jqxProgressBarComponent.prototype.ngOnChanges = function (changes) {
        if (this.host) {
            for (var i = 0; i < this.properties.length; i++) {
                var attrName = 'attr' + this.properties[i].substring(0, 1).toUpperCase() + this.properties[i].substring(1);
                var areEqual = void 0;
                if (this[attrName] !== undefined) {
                    if (typeof this[attrName] === 'object') {
                        if (this[attrName] instanceof Array) {
                            areEqual = this.arraysEqual(this[attrName], this.host.jqxProgressBar(this.properties[i]));
                        }
                        if (areEqual) {
                            return false;
                        }
                        this.host.jqxProgressBar(this.properties[i], this[attrName]);
                        continue;
                    }
                    if (this[attrName] !== this.host.jqxProgressBar(this.properties[i])) {
                        this.host.jqxProgressBar(this.properties[i], this[attrName]);
                    }
                }
            }
        }
    };
    jqxProgressBarComponent.prototype.arraysEqual = function (attrValue, hostValue) {
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
    jqxProgressBarComponent.prototype.manageAttributes = function () {
        var options = {};
        for (var i = 0; i < this.properties.length; i++) {
            var attrName = 'attr' + this.properties[i].substring(0, 1).toUpperCase() + this.properties[i].substring(1);
            if (this[attrName] !== undefined) {
                options[this.properties[i]] = this[attrName];
            }
        }
        return options;
    };
    jqxProgressBarComponent.prototype.createComponent = function (options) {
        if (options) {
            JQXLite.extend(options, this.manageAttributes());
        }
        else {
            options = this.manageAttributes();
        }
        this.host = JQXLite(this.elementRef.nativeElement.firstChild);
        this.__wireEvents__();
        this.widgetObject = jqwidgets.createInstance(this.host, 'jqxProgressBar', options);
        this.__updateRect__();
    };
    jqxProgressBarComponent.prototype.createWidget = function (options) {
        this.createComponent(options);
    };
    jqxProgressBarComponent.prototype.__updateRect__ = function () {
        this.host.css({ width: this.attrWidth, height: this.attrHeight });
    };
    jqxProgressBarComponent.prototype.setOptions = function (options) {
        this.host.jqxProgressBar('setOptions', options);
    };
    // jqxProgressBarComponent properties
    jqxProgressBarComponent.prototype.animationDuration = function (arg) {
        if (arg !== undefined) {
            this.host.jqxProgressBar('animationDuration', arg);
        }
        else {
            return this.host.jqxProgressBar('animationDuration');
        }
    };
    jqxProgressBarComponent.prototype.colorRanges = function (arg) {
        if (arg !== undefined) {
            this.host.jqxProgressBar('colorRanges', arg);
        }
        else {
            return this.host.jqxProgressBar('colorRanges');
        }
    };
    jqxProgressBarComponent.prototype.disabled = function (arg) {
        if (arg !== undefined) {
            this.host.jqxProgressBar('disabled', arg);
        }
        else {
            return this.host.jqxProgressBar('disabled');
        }
    };
    jqxProgressBarComponent.prototype.height = function (arg) {
        if (arg !== undefined) {
            this.host.jqxProgressBar('height', arg);
        }
        else {
            return this.host.jqxProgressBar('height');
        }
    };
    jqxProgressBarComponent.prototype.layout = function (arg) {
        if (arg !== undefined) {
            this.host.jqxProgressBar('layout', arg);
        }
        else {
            return this.host.jqxProgressBar('layout');
        }
    };
    jqxProgressBarComponent.prototype.max = function (arg) {
        if (arg !== undefined) {
            this.host.jqxProgressBar('max', arg);
        }
        else {
            return this.host.jqxProgressBar('max');
        }
    };
    jqxProgressBarComponent.prototype.min = function (arg) {
        if (arg !== undefined) {
            this.host.jqxProgressBar('min', arg);
        }
        else {
            return this.host.jqxProgressBar('min');
        }
    };
    jqxProgressBarComponent.prototype.orientation = function (arg) {
        if (arg !== undefined) {
            this.host.jqxProgressBar('orientation', arg);
        }
        else {
            return this.host.jqxProgressBar('orientation');
        }
    };
    jqxProgressBarComponent.prototype.rtl = function (arg) {
        if (arg !== undefined) {
            this.host.jqxProgressBar('rtl', arg);
        }
        else {
            return this.host.jqxProgressBar('rtl');
        }
    };
    jqxProgressBarComponent.prototype.renderText = function (arg) {
        if (arg !== undefined) {
            this.host.jqxProgressBar('renderText', arg);
        }
        else {
            return this.host.jqxProgressBar('renderText');
        }
    };
    jqxProgressBarComponent.prototype.showText = function (arg) {
        if (arg !== undefined) {
            this.host.jqxProgressBar('showText', arg);
        }
        else {
            return this.host.jqxProgressBar('showText');
        }
    };
    jqxProgressBarComponent.prototype.template = function (arg) {
        if (arg !== undefined) {
            this.host.jqxProgressBar('template', arg);
        }
        else {
            return this.host.jqxProgressBar('template');
        }
    };
    jqxProgressBarComponent.prototype.theme = function (arg) {
        if (arg !== undefined) {
            this.host.jqxProgressBar('theme', arg);
        }
        else {
            return this.host.jqxProgressBar('theme');
        }
    };
    jqxProgressBarComponent.prototype.value = function (arg) {
        if (arg !== undefined) {
            this.host.jqxProgressBar('value', arg);
        }
        else {
            return this.host.jqxProgressBar('value');
        }
    };
    jqxProgressBarComponent.prototype.width = function (arg) {
        if (arg !== undefined) {
            this.host.jqxProgressBar('width', arg);
        }
        else {
            return this.host.jqxProgressBar('width');
        }
    };
    // jqxProgressBarComponent functions
    jqxProgressBarComponent.prototype.actualValue = function (value) {
        this.host.jqxProgressBar('actualValue', value);
    };
    jqxProgressBarComponent.prototype.destroy = function () {
        this.host.jqxProgressBar('destroy');
    };
    jqxProgressBarComponent.prototype.val = function (value) {
        if (value !== undefined) {
            this.host.jqxProgressBar("val", value);
        }
        else {
            return this.host.jqxProgressBar("val");
        }
    };
    ;
    jqxProgressBarComponent.prototype.__wireEvents__ = function () {
        var _this = this;
        this.host.on('complete', function (eventData) { _this.onComplete.emit(eventData); });
        this.host.on('invalidvalue', function (eventData) { _this.onInvalidvalue.emit(eventData); });
        this.host.on('valueChanged', function (eventData) { _this.onValueChanged.emit(eventData); });
    };
    return jqxProgressBarComponent;
}()); //jqxProgressBarComponent
__decorate([
    Input('animationDuration'),
    __metadata("design:type", Object)
], jqxProgressBarComponent.prototype, "attrAnimationDuration", void 0);
__decorate([
    Input('colorRanges'),
    __metadata("design:type", Object)
], jqxProgressBarComponent.prototype, "attrColorRanges", void 0);
__decorate([
    Input('disabled'),
    __metadata("design:type", Object)
], jqxProgressBarComponent.prototype, "attrDisabled", void 0);
__decorate([
    Input('layout'),
    __metadata("design:type", Object)
], jqxProgressBarComponent.prototype, "attrLayout", void 0);
__decorate([
    Input('max'),
    __metadata("design:type", Object)
], jqxProgressBarComponent.prototype, "attrMax", void 0);
__decorate([
    Input('min'),
    __metadata("design:type", Object)
], jqxProgressBarComponent.prototype, "attrMin", void 0);
__decorate([
    Input('orientation'),
    __metadata("design:type", Object)
], jqxProgressBarComponent.prototype, "attrOrientation", void 0);
__decorate([
    Input('rtl'),
    __metadata("design:type", Object)
], jqxProgressBarComponent.prototype, "attrRtl", void 0);
__decorate([
    Input('renderText'),
    __metadata("design:type", Object)
], jqxProgressBarComponent.prototype, "attrRenderText", void 0);
__decorate([
    Input('showText'),
    __metadata("design:type", Object)
], jqxProgressBarComponent.prototype, "attrShowText", void 0);
__decorate([
    Input('template'),
    __metadata("design:type", Object)
], jqxProgressBarComponent.prototype, "attrTemplate", void 0);
__decorate([
    Input('theme'),
    __metadata("design:type", Object)
], jqxProgressBarComponent.prototype, "attrTheme", void 0);
__decorate([
    Input('value'),
    __metadata("design:type", Object)
], jqxProgressBarComponent.prototype, "attrValue", void 0);
__decorate([
    Input('width'),
    __metadata("design:type", Object)
], jqxProgressBarComponent.prototype, "attrWidth", void 0);
__decorate([
    Input('height'),
    __metadata("design:type", Object)
], jqxProgressBarComponent.prototype, "attrHeight", void 0);
__decorate([
    Input('auto-create'),
    __metadata("design:type", Boolean)
], jqxProgressBarComponent.prototype, "autoCreate", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxProgressBarComponent.prototype, "onComplete", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxProgressBarComponent.prototype, "onInvalidvalue", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxProgressBarComponent.prototype, "onValueChanged", void 0);
jqxProgressBarComponent = __decorate([
    Component({
        selector: 'jqxProgressBar',
        template: '<div><ng-content></ng-content></div>'
    }),
    __metadata("design:paramtypes", [ElementRef])
], jqxProgressBarComponent);
export { jqxProgressBarComponent };
//# sourceMappingURL=angular_jqxprogressbar.js.map