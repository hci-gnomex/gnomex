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
var jqxLinearGaugeComponent = (function () {
    function jqxLinearGaugeComponent(containerElement) {
        this.autoCreate = true;
        this.properties = ['animationDuration', 'background', 'colorScheme', 'disabled', 'easing', 'height', 'int64', 'labels', 'min', 'max', 'orientation', 'pointer', 'rangesOffset', 'rangeSize', 'ranges', 'showRanges', 'scaleStyle', 'scaleLength', 'ticksOffset', 'ticksPosition', 'ticksMinor', 'ticksMajor', 'value', 'width', 'disable', 'enable'];
        // jqxLinearGaugeComponent events
        this.onValueChanging = new EventEmitter();
        this.onValueChanged = new EventEmitter();
        this.elementRef = containerElement;
    }
    jqxLinearGaugeComponent.prototype.ngOnInit = function () {
        if (this.autoCreate) {
            this.createComponent();
        }
    };
    ;
    jqxLinearGaugeComponent.prototype.ngOnChanges = function (changes) {
        if (this.host) {
            for (var i = 0; i < this.properties.length; i++) {
                var attrName = 'attr' + this.properties[i].substring(0, 1).toUpperCase() + this.properties[i].substring(1);
                var areEqual = void 0;
                if (this[attrName] !== undefined) {
                    if (typeof this[attrName] === 'object') {
                        if (this[attrName] instanceof Array) {
                            areEqual = this.arraysEqual(this[attrName], this.host.jqxLinearGauge(this.properties[i]));
                        }
                        if (areEqual) {
                            return false;
                        }
                        this.host.jqxLinearGauge(this.properties[i], this[attrName]);
                        continue;
                    }
                    if (this[attrName] !== this.host.jqxLinearGauge(this.properties[i])) {
                        this.host.jqxLinearGauge(this.properties[i], this[attrName]);
                    }
                }
            }
        }
    };
    jqxLinearGaugeComponent.prototype.arraysEqual = function (attrValue, hostValue) {
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
    jqxLinearGaugeComponent.prototype.manageAttributes = function () {
        var options = {};
        for (var i = 0; i < this.properties.length; i++) {
            var attrName = 'attr' + this.properties[i].substring(0, 1).toUpperCase() + this.properties[i].substring(1);
            if (this[attrName] !== undefined) {
                options[this.properties[i]] = this[attrName];
            }
        }
        return options;
    };
    jqxLinearGaugeComponent.prototype.createComponent = function (options) {
        if (options) {
            JQXLite.extend(options, this.manageAttributes());
        }
        else {
            options = this.manageAttributes();
        }
        this.host = JQXLite(this.elementRef.nativeElement.firstChild);
        this.__wireEvents__();
        this.widgetObject = jqwidgets.createInstance(this.host, 'jqxLinearGauge', options);
        this.__updateRect__();
    };
    jqxLinearGaugeComponent.prototype.createWidget = function (options) {
        this.createComponent(options);
    };
    jqxLinearGaugeComponent.prototype.__updateRect__ = function () {
        this.host.css({ width: this.attrWidth, height: this.attrHeight });
    };
    jqxLinearGaugeComponent.prototype.setOptions = function (options) {
        this.host.jqxLinearGauge('setOptions', options);
    };
    // jqxLinearGaugeComponent properties
    jqxLinearGaugeComponent.prototype.animationDuration = function (arg) {
        if (arg !== undefined) {
            this.host.jqxLinearGauge('animationDuration', arg);
        }
        else {
            return this.host.jqxLinearGauge('animationDuration');
        }
    };
    jqxLinearGaugeComponent.prototype.background = function (arg) {
        if (arg !== undefined) {
            this.host.jqxLinearGauge('background', arg);
        }
        else {
            return this.host.jqxLinearGauge('background');
        }
    };
    jqxLinearGaugeComponent.prototype.colorScheme = function (arg) {
        if (arg !== undefined) {
            this.host.jqxLinearGauge('colorScheme', arg);
        }
        else {
            return this.host.jqxLinearGauge('colorScheme');
        }
    };
    jqxLinearGaugeComponent.prototype.disabled = function (arg) {
        if (arg !== undefined) {
            this.host.jqxLinearGauge('disabled', arg);
        }
        else {
            return this.host.jqxLinearGauge('disabled');
        }
    };
    jqxLinearGaugeComponent.prototype.easing = function (arg) {
        if (arg !== undefined) {
            this.host.jqxLinearGauge('easing', arg);
        }
        else {
            return this.host.jqxLinearGauge('easing');
        }
    };
    jqxLinearGaugeComponent.prototype.height = function (arg) {
        if (arg !== undefined) {
            this.host.jqxLinearGauge('height', arg);
        }
        else {
            return this.host.jqxLinearGauge('height');
        }
    };
    jqxLinearGaugeComponent.prototype.int64 = function (arg) {
        if (arg !== undefined) {
            this.host.jqxLinearGauge('int64', arg);
        }
        else {
            return this.host.jqxLinearGauge('int64');
        }
    };
    jqxLinearGaugeComponent.prototype.labels = function (arg) {
        if (arg !== undefined) {
            this.host.jqxLinearGauge('labels', arg);
        }
        else {
            return this.host.jqxLinearGauge('labels');
        }
    };
    jqxLinearGaugeComponent.prototype.min = function (arg) {
        if (arg !== undefined) {
            this.host.jqxLinearGauge('min', arg);
        }
        else {
            return this.host.jqxLinearGauge('min');
        }
    };
    jqxLinearGaugeComponent.prototype.max = function (arg) {
        if (arg !== undefined) {
            this.host.jqxLinearGauge('max', arg);
        }
        else {
            return this.host.jqxLinearGauge('max');
        }
    };
    jqxLinearGaugeComponent.prototype.orientation = function (arg) {
        if (arg !== undefined) {
            this.host.jqxLinearGauge('orientation', arg);
        }
        else {
            return this.host.jqxLinearGauge('orientation');
        }
    };
    jqxLinearGaugeComponent.prototype.pointer = function (arg) {
        if (arg !== undefined) {
            this.host.jqxLinearGauge('pointer', arg);
        }
        else {
            return this.host.jqxLinearGauge('pointer');
        }
    };
    jqxLinearGaugeComponent.prototype.rangesOffset = function (arg) {
        if (arg !== undefined) {
            this.host.jqxLinearGauge('rangesOffset', arg);
        }
        else {
            return this.host.jqxLinearGauge('rangesOffset');
        }
    };
    jqxLinearGaugeComponent.prototype.rangeSize = function (arg) {
        if (arg !== undefined) {
            this.host.jqxLinearGauge('rangeSize', arg);
        }
        else {
            return this.host.jqxLinearGauge('rangeSize');
        }
    };
    jqxLinearGaugeComponent.prototype.ranges = function (arg) {
        if (arg !== undefined) {
            this.host.jqxLinearGauge('ranges', arg);
        }
        else {
            return this.host.jqxLinearGauge('ranges');
        }
    };
    jqxLinearGaugeComponent.prototype.showRanges = function (arg) {
        if (arg !== undefined) {
            this.host.jqxLinearGauge('showRanges', arg);
        }
        else {
            return this.host.jqxLinearGauge('showRanges');
        }
    };
    jqxLinearGaugeComponent.prototype.scaleStyle = function (arg) {
        if (arg !== undefined) {
            this.host.jqxLinearGauge('scaleStyle', arg);
        }
        else {
            return this.host.jqxLinearGauge('scaleStyle');
        }
    };
    jqxLinearGaugeComponent.prototype.scaleLength = function (arg) {
        if (arg !== undefined) {
            this.host.jqxLinearGauge('scaleLength', arg);
        }
        else {
            return this.host.jqxLinearGauge('scaleLength');
        }
    };
    jqxLinearGaugeComponent.prototype.ticksOffset = function (arg) {
        if (arg !== undefined) {
            this.host.jqxLinearGauge('ticksOffset', arg);
        }
        else {
            return this.host.jqxLinearGauge('ticksOffset');
        }
    };
    jqxLinearGaugeComponent.prototype.ticksPosition = function (arg) {
        if (arg !== undefined) {
            this.host.jqxLinearGauge('ticksPosition', arg);
        }
        else {
            return this.host.jqxLinearGauge('ticksPosition');
        }
    };
    jqxLinearGaugeComponent.prototype.ticksMinor = function (arg) {
        if (arg !== undefined) {
            this.host.jqxLinearGauge('ticksMinor', arg);
        }
        else {
            return this.host.jqxLinearGauge('ticksMinor');
        }
    };
    jqxLinearGaugeComponent.prototype.ticksMajor = function (arg) {
        if (arg !== undefined) {
            this.host.jqxLinearGauge('ticksMajor', arg);
        }
        else {
            return this.host.jqxLinearGauge('ticksMajor');
        }
    };
    jqxLinearGaugeComponent.prototype.value = function (arg) {
        if (arg !== undefined) {
            this.host.jqxLinearGauge('value', arg);
        }
        else {
            return this.host.jqxLinearGauge('value');
        }
    };
    jqxLinearGaugeComponent.prototype.width = function (arg) {
        if (arg !== undefined) {
            this.host.jqxLinearGauge('width', arg);
        }
        else {
            return this.host.jqxLinearGauge('width');
        }
    };
    jqxLinearGaugeComponent.prototype.disable = function (arg) {
        if (arg !== undefined) {
            this.host.jqxLinearGauge('disable', arg);
        }
        else {
            return this.host.jqxLinearGauge('disable');
        }
    };
    jqxLinearGaugeComponent.prototype.enable = function (arg) {
        if (arg !== undefined) {
            this.host.jqxLinearGauge('enable', arg);
        }
        else {
            return this.host.jqxLinearGauge('enable');
        }
    };
    // jqxLinearGaugeComponent functions
    jqxLinearGaugeComponent.prototype.val = function (value) {
        if (value !== undefined) {
            this.host.jqxLinearGauge("val", value);
        }
        else {
            return this.host.jqxLinearGauge("val");
        }
    };
    ;
    jqxLinearGaugeComponent.prototype.__wireEvents__ = function () {
        var _this = this;
        this.host.on('valueChanging', function (eventData) { _this.onValueChanging.emit(eventData); });
        this.host.on('valueChanged', function (eventData) { _this.onValueChanged.emit(eventData); });
    };
    return jqxLinearGaugeComponent;
}()); //jqxLinearGaugeComponent
__decorate([
    Input('animationDuration'),
    __metadata("design:type", Object)
], jqxLinearGaugeComponent.prototype, "attrAnimationDuration", void 0);
__decorate([
    Input('background'),
    __metadata("design:type", Object)
], jqxLinearGaugeComponent.prototype, "attrBackground", void 0);
__decorate([
    Input('colorScheme'),
    __metadata("design:type", Object)
], jqxLinearGaugeComponent.prototype, "attrColorScheme", void 0);
__decorate([
    Input('disabled'),
    __metadata("design:type", Object)
], jqxLinearGaugeComponent.prototype, "attrDisabled", void 0);
__decorate([
    Input('easing'),
    __metadata("design:type", Object)
], jqxLinearGaugeComponent.prototype, "attrEasing", void 0);
__decorate([
    Input('int64'),
    __metadata("design:type", Object)
], jqxLinearGaugeComponent.prototype, "attrInt64", void 0);
__decorate([
    Input('labels'),
    __metadata("design:type", Object)
], jqxLinearGaugeComponent.prototype, "attrLabels", void 0);
__decorate([
    Input('min'),
    __metadata("design:type", Object)
], jqxLinearGaugeComponent.prototype, "attrMin", void 0);
__decorate([
    Input('max'),
    __metadata("design:type", Object)
], jqxLinearGaugeComponent.prototype, "attrMax", void 0);
__decorate([
    Input('orientation'),
    __metadata("design:type", Object)
], jqxLinearGaugeComponent.prototype, "attrOrientation", void 0);
__decorate([
    Input('pointer'),
    __metadata("design:type", Object)
], jqxLinearGaugeComponent.prototype, "attrPointer", void 0);
__decorate([
    Input('rangesOffset'),
    __metadata("design:type", Object)
], jqxLinearGaugeComponent.prototype, "attrRangesOffset", void 0);
__decorate([
    Input('rangeSize'),
    __metadata("design:type", Object)
], jqxLinearGaugeComponent.prototype, "attrRangeSize", void 0);
__decorate([
    Input('ranges'),
    __metadata("design:type", Object)
], jqxLinearGaugeComponent.prototype, "attrRanges", void 0);
__decorate([
    Input('showRanges'),
    __metadata("design:type", Object)
], jqxLinearGaugeComponent.prototype, "attrShowRanges", void 0);
__decorate([
    Input('scaleStyle'),
    __metadata("design:type", Object)
], jqxLinearGaugeComponent.prototype, "attrScaleStyle", void 0);
__decorate([
    Input('scaleLength'),
    __metadata("design:type", Object)
], jqxLinearGaugeComponent.prototype, "attrScaleLength", void 0);
__decorate([
    Input('ticksOffset'),
    __metadata("design:type", Object)
], jqxLinearGaugeComponent.prototype, "attrTicksOffset", void 0);
__decorate([
    Input('ticksPosition'),
    __metadata("design:type", Object)
], jqxLinearGaugeComponent.prototype, "attrTicksPosition", void 0);
__decorate([
    Input('ticksMinor'),
    __metadata("design:type", Object)
], jqxLinearGaugeComponent.prototype, "attrTicksMinor", void 0);
__decorate([
    Input('ticksMajor'),
    __metadata("design:type", Object)
], jqxLinearGaugeComponent.prototype, "attrTicksMajor", void 0);
__decorate([
    Input('value'),
    __metadata("design:type", Object)
], jqxLinearGaugeComponent.prototype, "attrValue", void 0);
__decorate([
    Input('disable'),
    __metadata("design:type", Object)
], jqxLinearGaugeComponent.prototype, "attrDisable", void 0);
__decorate([
    Input('enable'),
    __metadata("design:type", Object)
], jqxLinearGaugeComponent.prototype, "attrEnable", void 0);
__decorate([
    Input('width'),
    __metadata("design:type", Object)
], jqxLinearGaugeComponent.prototype, "attrWidth", void 0);
__decorate([
    Input('height'),
    __metadata("design:type", Object)
], jqxLinearGaugeComponent.prototype, "attrHeight", void 0);
__decorate([
    Input('auto-create'),
    __metadata("design:type", Boolean)
], jqxLinearGaugeComponent.prototype, "autoCreate", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxLinearGaugeComponent.prototype, "onValueChanging", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxLinearGaugeComponent.prototype, "onValueChanged", void 0);
jqxLinearGaugeComponent = __decorate([
    Component({
        selector: 'jqxLinearGauge',
        template: '<div><ng-content></ng-content></div>'
    }),
    __metadata("design:paramtypes", [ElementRef])
], jqxLinearGaugeComponent);
export { jqxLinearGaugeComponent };
//# sourceMappingURL=angular_jqxlineargauge.js.map