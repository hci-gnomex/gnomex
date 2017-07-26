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
import { Component, Input, Output, EventEmitter, ElementRef, forwardRef, ChangeDetectionStrategy } from '@angular/core';
import { NG_VALUE_ACCESSOR } from '@angular/forms';
var noop = function () { };
export var CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR = {
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(function () { return jqxRadioButtonComponent; }),
    multi: true
};
var jqxRadioButtonComponent = (function () {
    function jqxRadioButtonComponent(containerElement) {
        this.autoCreate = true;
        this.properties = ['animationShowDelay', 'animationHideDelay', 'boxSize', 'checked', 'disabled', 'enableContainerClick', 'groupName', 'hasThreeStates', 'height', 'rtl', 'theme', 'width'];
        this.onTouchedCallback = noop;
        this.onChangeCallback = noop;
        // jqxRadioButtonComponent events
        this.onChecked = new EventEmitter();
        this.onChange = new EventEmitter();
        this.onUnchecked = new EventEmitter();
        this.elementRef = containerElement;
    }
    jqxRadioButtonComponent.prototype.ngOnInit = function () {
        if (this.autoCreate) {
            this.createComponent();
        }
    };
    ;
    jqxRadioButtonComponent.prototype.ngOnChanges = function (changes) {
        if (this.host) {
            for (var i = 0; i < this.properties.length; i++) {
                var attrName = 'attr' + this.properties[i].substring(0, 1).toUpperCase() + this.properties[i].substring(1);
                var areEqual = void 0;
                if (this[attrName] !== undefined) {
                    if (typeof this[attrName] === 'object') {
                        if (this[attrName] instanceof Array) {
                            areEqual = this.arraysEqual(this[attrName], this.host.jqxRadioButton(this.properties[i]));
                        }
                        if (areEqual) {
                            return false;
                        }
                        this.host.jqxRadioButton(this.properties[i], this[attrName]);
                        continue;
                    }
                    if (this[attrName] !== this.host.jqxRadioButton(this.properties[i])) {
                        this.host.jqxRadioButton(this.properties[i], this[attrName]);
                    }
                }
            }
        }
    };
    jqxRadioButtonComponent.prototype.arraysEqual = function (attrValue, hostValue) {
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
    jqxRadioButtonComponent.prototype.manageAttributes = function () {
        var options = {};
        for (var i = 0; i < this.properties.length; i++) {
            var attrName = 'attr' + this.properties[i].substring(0, 1).toUpperCase() + this.properties[i].substring(1);
            if (this[attrName] !== undefined) {
                options[this.properties[i]] = this[attrName];
            }
        }
        return options;
    };
    jqxRadioButtonComponent.prototype.createComponent = function (options) {
        if (options) {
            JQXLite.extend(options, this.manageAttributes());
        }
        else {
            options = this.manageAttributes();
        }
        this.host = JQXLite(this.elementRef.nativeElement.firstChild);
        this.__wireEvents__();
        this.widgetObject = jqwidgets.createInstance(this.host, 'jqxRadioButton', options);
        this.__updateRect__();
        this.valueAttr = this.host[0].parentElement.getAttribute('value');
        if (options.checked === true)
            this.onChangeCallback(this.valueAttr);
    };
    jqxRadioButtonComponent.prototype.createWidget = function (options) {
        this.createComponent(options);
    };
    jqxRadioButtonComponent.prototype.__updateRect__ = function () {
        this.host.css({ width: this.attrWidth, height: this.attrHeight });
    };
    jqxRadioButtonComponent.prototype.writeValue = function (value) {
        if (this.widgetObject) {
        }
    };
    jqxRadioButtonComponent.prototype.registerOnChange = function (fn) {
        this.onChangeCallback = fn;
    };
    jqxRadioButtonComponent.prototype.registerOnTouched = function (fn) {
        this.onTouchedCallback = fn;
    };
    jqxRadioButtonComponent.prototype.setOptions = function (options) {
        this.host.jqxRadioButton('setOptions', options);
    };
    // jqxRadioButtonComponent properties
    jqxRadioButtonComponent.prototype.animationShowDelay = function (arg) {
        if (arg !== undefined) {
            this.host.jqxRadioButton('animationShowDelay', arg);
        }
        else {
            return this.host.jqxRadioButton('animationShowDelay');
        }
    };
    jqxRadioButtonComponent.prototype.animationHideDelay = function (arg) {
        if (arg !== undefined) {
            this.host.jqxRadioButton('animationHideDelay', arg);
        }
        else {
            return this.host.jqxRadioButton('animationHideDelay');
        }
    };
    jqxRadioButtonComponent.prototype.boxSize = function (arg) {
        if (arg !== undefined) {
            this.host.jqxRadioButton('boxSize', arg);
        }
        else {
            return this.host.jqxRadioButton('boxSize');
        }
    };
    jqxRadioButtonComponent.prototype.checked = function (arg) {
        if (arg !== undefined) {
            this.host.jqxRadioButton('checked', arg);
        }
        else {
            return this.host.jqxRadioButton('checked');
        }
    };
    jqxRadioButtonComponent.prototype.disabled = function (arg) {
        if (arg !== undefined) {
            this.host.jqxRadioButton('disabled', arg);
        }
        else {
            return this.host.jqxRadioButton('disabled');
        }
    };
    jqxRadioButtonComponent.prototype.enableContainerClick = function (arg) {
        if (arg !== undefined) {
            this.host.jqxRadioButton('enableContainerClick', arg);
        }
        else {
            return this.host.jqxRadioButton('enableContainerClick');
        }
    };
    jqxRadioButtonComponent.prototype.groupName = function (arg) {
        if (arg !== undefined) {
            this.host.jqxRadioButton('groupName', arg);
        }
        else {
            return this.host.jqxRadioButton('groupName');
        }
    };
    jqxRadioButtonComponent.prototype.hasThreeStates = function (arg) {
        if (arg !== undefined) {
            this.host.jqxRadioButton('hasThreeStates', arg);
        }
        else {
            return this.host.jqxRadioButton('hasThreeStates');
        }
    };
    jqxRadioButtonComponent.prototype.height = function (arg) {
        if (arg !== undefined) {
            this.host.jqxRadioButton('height', arg);
        }
        else {
            return this.host.jqxRadioButton('height');
        }
    };
    jqxRadioButtonComponent.prototype.rtl = function (arg) {
        if (arg !== undefined) {
            this.host.jqxRadioButton('rtl', arg);
        }
        else {
            return this.host.jqxRadioButton('rtl');
        }
    };
    jqxRadioButtonComponent.prototype.theme = function (arg) {
        if (arg !== undefined) {
            this.host.jqxRadioButton('theme', arg);
        }
        else {
            return this.host.jqxRadioButton('theme');
        }
    };
    jqxRadioButtonComponent.prototype.width = function (arg) {
        if (arg !== undefined) {
            this.host.jqxRadioButton('width', arg);
        }
        else {
            return this.host.jqxRadioButton('width');
        }
    };
    // jqxRadioButtonComponent functions
    jqxRadioButtonComponent.prototype.check = function () {
        this.host.jqxRadioButton('check');
    };
    jqxRadioButtonComponent.prototype.disable = function () {
        this.host.jqxRadioButton('disable');
    };
    jqxRadioButtonComponent.prototype.destroy = function () {
        this.host.jqxRadioButton('destroy');
    };
    jqxRadioButtonComponent.prototype.enable = function () {
        this.host.jqxRadioButton('enable');
    };
    jqxRadioButtonComponent.prototype.focus = function () {
        this.host.jqxRadioButton('focus');
    };
    jqxRadioButtonComponent.prototype.render = function () {
        this.host.jqxRadioButton('render');
    };
    jqxRadioButtonComponent.prototype.uncheck = function () {
        this.host.jqxRadioButton('uncheck');
    };
    jqxRadioButtonComponent.prototype.val = function (value) {
        if (value !== undefined) {
            this.host.jqxRadioButton("val", value);
        }
        else {
            return this.host.jqxRadioButton("val");
        }
    };
    ;
    jqxRadioButtonComponent.prototype.__wireEvents__ = function () {
        var _this = this;
        this.host.on('checked', function (eventData) { _this.onChecked.emit(eventData); _this.onChangeCallback(_this.valueAttr); });
        this.host.on('change', function (eventData) { _this.onChange.emit(eventData); });
        this.host.on('unchecked', function (eventData) { _this.onUnchecked.emit(eventData); });
    };
    return jqxRadioButtonComponent;
}()); //jqxRadioButtonComponent
__decorate([
    Input('animationShowDelay'),
    __metadata("design:type", Object)
], jqxRadioButtonComponent.prototype, "attrAnimationShowDelay", void 0);
__decorate([
    Input('animationHideDelay'),
    __metadata("design:type", Object)
], jqxRadioButtonComponent.prototype, "attrAnimationHideDelay", void 0);
__decorate([
    Input('boxSize'),
    __metadata("design:type", Object)
], jqxRadioButtonComponent.prototype, "attrBoxSize", void 0);
__decorate([
    Input('checked'),
    __metadata("design:type", Object)
], jqxRadioButtonComponent.prototype, "attrChecked", void 0);
__decorate([
    Input('disabled'),
    __metadata("design:type", Object)
], jqxRadioButtonComponent.prototype, "attrDisabled", void 0);
__decorate([
    Input('enableContainerClick'),
    __metadata("design:type", Object)
], jqxRadioButtonComponent.prototype, "attrEnableContainerClick", void 0);
__decorate([
    Input('groupName'),
    __metadata("design:type", Object)
], jqxRadioButtonComponent.prototype, "attrGroupName", void 0);
__decorate([
    Input('hasThreeStates'),
    __metadata("design:type", Object)
], jqxRadioButtonComponent.prototype, "attrHasThreeStates", void 0);
__decorate([
    Input('rtl'),
    __metadata("design:type", Object)
], jqxRadioButtonComponent.prototype, "attrRtl", void 0);
__decorate([
    Input('theme'),
    __metadata("design:type", Object)
], jqxRadioButtonComponent.prototype, "attrTheme", void 0);
__decorate([
    Input('width'),
    __metadata("design:type", Object)
], jqxRadioButtonComponent.prototype, "attrWidth", void 0);
__decorate([
    Input('height'),
    __metadata("design:type", Object)
], jqxRadioButtonComponent.prototype, "attrHeight", void 0);
__decorate([
    Input('auto-create'),
    __metadata("design:type", Boolean)
], jqxRadioButtonComponent.prototype, "autoCreate", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxRadioButtonComponent.prototype, "onChecked", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxRadioButtonComponent.prototype, "onChange", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxRadioButtonComponent.prototype, "onUnchecked", void 0);
jqxRadioButtonComponent = __decorate([
    Component({
        selector: 'jqxRadioButton',
        template: '<div><ng-content></ng-content></div>',
        providers: [CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR],
        changeDetection: ChangeDetectionStrategy.OnPush
    }),
    __metadata("design:paramtypes", [ElementRef])
], jqxRadioButtonComponent);
export { jqxRadioButtonComponent };
//# sourceMappingURL=angular_jqxradiobutton.js.map