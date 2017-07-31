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
    useExisting: forwardRef(function () { return jqxCheckBoxComponent; }),
    multi: true
};
var jqxCheckBoxComponent = (function () {
    function jqxCheckBoxComponent(containerElement) {
        this.autoCreate = true;
        this.properties = ['animationShowDelay', 'animationHideDelay', 'boxSize', 'checked', 'disabled', 'enableContainerClick', 'groupName', 'height', 'hasThreeStates', 'locked', 'rtl', 'theme', 'width'];
        this.onTouchedCallback = noop;
        this.onChangeCallback = noop;
        // jqxCheckBoxComponent events
        this.onChecked = new EventEmitter();
        this.onChange = new EventEmitter();
        this.onIndeterminate = new EventEmitter();
        this.onUnchecked = new EventEmitter();
        this.elementRef = containerElement;
    }
    jqxCheckBoxComponent.prototype.ngOnInit = function () {
        if (this.autoCreate) {
            this.createComponent();
        }
    };
    ;
    jqxCheckBoxComponent.prototype.ngOnChanges = function (changes) {
        if (this.host) {
            for (var i = 0; i < this.properties.length; i++) {
                var attrName = 'attr' + this.properties[i].substring(0, 1).toUpperCase() + this.properties[i].substring(1);
                var areEqual = void 0;
                if (this[attrName] !== undefined) {
                    if (typeof this[attrName] === 'object') {
                        if (this[attrName] instanceof Array) {
                            areEqual = this.arraysEqual(this[attrName], this.host.jqxCheckBox(this.properties[i]));
                        }
                        if (areEqual) {
                            return false;
                        }
                        this.host.jqxCheckBox(this.properties[i], this[attrName]);
                        continue;
                    }
                    if (this[attrName] !== this.host.jqxCheckBox(this.properties[i])) {
                        this.host.jqxCheckBox(this.properties[i], this[attrName]);
                    }
                }
            }
        }
    };
    jqxCheckBoxComponent.prototype.arraysEqual = function (attrValue, hostValue) {
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
    jqxCheckBoxComponent.prototype.manageAttributes = function () {
        var options = {};
        for (var i = 0; i < this.properties.length; i++) {
            var attrName = 'attr' + this.properties[i].substring(0, 1).toUpperCase() + this.properties[i].substring(1);
            if (this[attrName] !== undefined) {
                options[this.properties[i]] = this[attrName];
            }
        }
        return options;
    };
    jqxCheckBoxComponent.prototype.createComponent = function (options) {
        if (options) {
            JQXLite.extend(options, this.manageAttributes());
        }
        else {
            options = this.manageAttributes();
        }
        this.host = JQXLite(this.elementRef.nativeElement.firstChild);
        this.__wireEvents__();
        this.widgetObject = jqwidgets.createInstance(this.host, 'jqxCheckBox', options);
        this.__updateRect__();
        options.checked !== undefined ? this.onChangeCallback(options.checked) : this.onChangeCallback(false);
    };
    jqxCheckBoxComponent.prototype.createWidget = function (options) {
        this.createComponent(options);
    };
    jqxCheckBoxComponent.prototype.__updateRect__ = function () {
        this.host.css({ width: this.attrWidth, height: this.attrHeight });
    };
    jqxCheckBoxComponent.prototype.writeValue = function (value) {
        if (this.widgetObject) {
        }
    };
    jqxCheckBoxComponent.prototype.registerOnChange = function (fn) {
        this.onChangeCallback = fn;
    };
    jqxCheckBoxComponent.prototype.registerOnTouched = function (fn) {
        this.onTouchedCallback = fn;
    };
    jqxCheckBoxComponent.prototype.setOptions = function (options) {
        this.host.jqxCheckBox('setOptions', options);
    };
    // jqxCheckBoxComponent properties
    jqxCheckBoxComponent.prototype.animationShowDelay = function (arg) {
        if (arg !== undefined) {
            this.host.jqxCheckBox('animationShowDelay', arg);
        }
        else {
            return this.host.jqxCheckBox('animationShowDelay');
        }
    };
    jqxCheckBoxComponent.prototype.animationHideDelay = function (arg) {
        if (arg !== undefined) {
            this.host.jqxCheckBox('animationHideDelay', arg);
        }
        else {
            return this.host.jqxCheckBox('animationHideDelay');
        }
    };
    jqxCheckBoxComponent.prototype.boxSize = function (arg) {
        if (arg !== undefined) {
            this.host.jqxCheckBox('boxSize', arg);
        }
        else {
            return this.host.jqxCheckBox('boxSize');
        }
    };
    jqxCheckBoxComponent.prototype.checked = function (arg) {
        if (arg !== undefined) {
            this.host.jqxCheckBox('checked', arg);
        }
        else {
            return this.host.jqxCheckBox('checked');
        }
    };
    jqxCheckBoxComponent.prototype.disabled = function (arg) {
        if (arg !== undefined) {
            this.host.jqxCheckBox('disabled', arg);
        }
        else {
            return this.host.jqxCheckBox('disabled');
        }
    };
    jqxCheckBoxComponent.prototype.enableContainerClick = function (arg) {
        if (arg !== undefined) {
            this.host.jqxCheckBox('enableContainerClick', arg);
        }
        else {
            return this.host.jqxCheckBox('enableContainerClick');
        }
    };
    jqxCheckBoxComponent.prototype.groupName = function (arg) {
        if (arg !== undefined) {
            this.host.jqxCheckBox('groupName', arg);
        }
        else {
            return this.host.jqxCheckBox('groupName');
        }
    };
    jqxCheckBoxComponent.prototype.height = function (arg) {
        if (arg !== undefined) {
            this.host.jqxCheckBox('height', arg);
        }
        else {
            return this.host.jqxCheckBox('height');
        }
    };
    jqxCheckBoxComponent.prototype.hasThreeStates = function (arg) {
        if (arg !== undefined) {
            this.host.jqxCheckBox('hasThreeStates', arg);
        }
        else {
            return this.host.jqxCheckBox('hasThreeStates');
        }
    };
    jqxCheckBoxComponent.prototype.locked = function (arg) {
        if (arg !== undefined) {
            this.host.jqxCheckBox('locked', arg);
        }
        else {
            return this.host.jqxCheckBox('locked');
        }
    };
    jqxCheckBoxComponent.prototype.rtl = function (arg) {
        if (arg !== undefined) {
            this.host.jqxCheckBox('rtl', arg);
        }
        else {
            return this.host.jqxCheckBox('rtl');
        }
    };
    jqxCheckBoxComponent.prototype.theme = function (arg) {
        if (arg !== undefined) {
            this.host.jqxCheckBox('theme', arg);
        }
        else {
            return this.host.jqxCheckBox('theme');
        }
    };
    jqxCheckBoxComponent.prototype.width = function (arg) {
        if (arg !== undefined) {
            this.host.jqxCheckBox('width', arg);
        }
        else {
            return this.host.jqxCheckBox('width');
        }
    };
    // jqxCheckBoxComponent functions
    jqxCheckBoxComponent.prototype.check = function () {
        this.host.jqxCheckBox('check');
    };
    jqxCheckBoxComponent.prototype.disable = function () {
        this.host.jqxCheckBox('disable');
    };
    jqxCheckBoxComponent.prototype.destroy = function () {
        this.host.jqxCheckBox('destroy');
    };
    jqxCheckBoxComponent.prototype.enable = function () {
        this.host.jqxCheckBox('enable');
    };
    jqxCheckBoxComponent.prototype.focus = function () {
        this.host.jqxCheckBox('focus');
    };
    jqxCheckBoxComponent.prototype.indeterminate = function () {
        this.host.jqxCheckBox('indeterminate');
    };
    jqxCheckBoxComponent.prototype.render = function () {
        this.host.jqxCheckBox('render');
    };
    jqxCheckBoxComponent.prototype.toggle = function () {
        this.host.jqxCheckBox('toggle');
    };
    jqxCheckBoxComponent.prototype.uncheck = function () {
        this.host.jqxCheckBox('uncheck');
    };
    jqxCheckBoxComponent.prototype.val = function (value) {
        if (value !== undefined) {
            this.host.jqxCheckBox("val", value);
        }
        else {
            return this.host.jqxCheckBox("val");
        }
    };
    ;
    jqxCheckBoxComponent.prototype.__wireEvents__ = function () {
        var _this = this;
        this.host.on('checked', function (eventData) { _this.onChecked.emit(eventData); });
        this.host.on('change', function (eventData) { _this.onChange.emit(eventData); if (eventData.args)
            _this.onChangeCallback(eventData.args.checked); });
        this.host.on('indeterminate', function (eventData) { _this.onIndeterminate.emit(eventData); });
        this.host.on('unchecked', function (eventData) { _this.onUnchecked.emit(eventData); });
    };
    return jqxCheckBoxComponent;
}()); //jqxCheckBoxComponent
__decorate([
    Input('animationShowDelay'),
    __metadata("design:type", Object)
], jqxCheckBoxComponent.prototype, "attrAnimationShowDelay", void 0);
__decorate([
    Input('animationHideDelay'),
    __metadata("design:type", Object)
], jqxCheckBoxComponent.prototype, "attrAnimationHideDelay", void 0);
__decorate([
    Input('boxSize'),
    __metadata("design:type", Object)
], jqxCheckBoxComponent.prototype, "attrBoxSize", void 0);
__decorate([
    Input('checked'),
    __metadata("design:type", Object)
], jqxCheckBoxComponent.prototype, "attrChecked", void 0);
__decorate([
    Input('disabled'),
    __metadata("design:type", Object)
], jqxCheckBoxComponent.prototype, "attrDisabled", void 0);
__decorate([
    Input('enableContainerClick'),
    __metadata("design:type", Object)
], jqxCheckBoxComponent.prototype, "attrEnableContainerClick", void 0);
__decorate([
    Input('groupName'),
    __metadata("design:type", Object)
], jqxCheckBoxComponent.prototype, "attrGroupName", void 0);
__decorate([
    Input('hasThreeStates'),
    __metadata("design:type", Object)
], jqxCheckBoxComponent.prototype, "attrHasThreeStates", void 0);
__decorate([
    Input('locked'),
    __metadata("design:type", Object)
], jqxCheckBoxComponent.prototype, "attrLocked", void 0);
__decorate([
    Input('rtl'),
    __metadata("design:type", Object)
], jqxCheckBoxComponent.prototype, "attrRtl", void 0);
__decorate([
    Input('theme'),
    __metadata("design:type", Object)
], jqxCheckBoxComponent.prototype, "attrTheme", void 0);
__decorate([
    Input('width'),
    __metadata("design:type", Object)
], jqxCheckBoxComponent.prototype, "attrWidth", void 0);
__decorate([
    Input('height'),
    __metadata("design:type", Object)
], jqxCheckBoxComponent.prototype, "attrHeight", void 0);
__decorate([
    Input('auto-create'),
    __metadata("design:type", Boolean)
], jqxCheckBoxComponent.prototype, "autoCreate", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxCheckBoxComponent.prototype, "onChecked", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxCheckBoxComponent.prototype, "onChange", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxCheckBoxComponent.prototype, "onIndeterminate", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxCheckBoxComponent.prototype, "onUnchecked", void 0);
jqxCheckBoxComponent = __decorate([
    Component({
        selector: 'jqxCheckBox',
        template: '<div><ng-content></ng-content></div>',
        providers: [CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR],
        changeDetection: ChangeDetectionStrategy.OnPush
    }),
    __metadata("design:paramtypes", [ElementRef])
], jqxCheckBoxComponent);
export { jqxCheckBoxComponent };
//# sourceMappingURL=angular_jqxcheckbox.js.map