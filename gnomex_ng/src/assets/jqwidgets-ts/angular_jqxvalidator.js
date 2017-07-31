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
var jqxValidatorComponent = (function () {
    function jqxValidatorComponent(containerElement) {
        this.autoCreate = true;
        this.properties = ['arrow', 'animation', 'animationDuration', 'closeOnClick', 'focus', 'hintType', 'onError', 'onSuccess', 'position', 'rules', 'rtl'];
        // jqxValidatorComponent events
        this.onValidationError = new EventEmitter();
        this.onValidationSuccess = new EventEmitter();
        this.elementRef = containerElement;
    }
    jqxValidatorComponent.prototype.ngOnInit = function () {
        if (this.autoCreate) {
            this.createComponent();
        }
    };
    ;
    jqxValidatorComponent.prototype.ngOnChanges = function (changes) {
        if (this.host) {
            for (var i = 0; i < this.properties.length; i++) {
                var attrName = 'attr' + this.properties[i].substring(0, 1).toUpperCase() + this.properties[i].substring(1);
                var areEqual = void 0;
                if (this[attrName] !== undefined) {
                    if (typeof this[attrName] === 'object') {
                        if (this[attrName] instanceof Array) {
                            areEqual = this.arraysEqual(this[attrName], this.host.jqxValidator(this.properties[i]));
                        }
                        if (areEqual) {
                            return false;
                        }
                        this.host.jqxValidator(this.properties[i], this[attrName]);
                        continue;
                    }
                    if (this[attrName] !== this.host.jqxValidator(this.properties[i])) {
                        this.host.jqxValidator(this.properties[i], this[attrName]);
                    }
                }
            }
        }
    };
    jqxValidatorComponent.prototype.arraysEqual = function (attrValue, hostValue) {
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
    jqxValidatorComponent.prototype.manageAttributes = function () {
        var options = {};
        for (var i = 0; i < this.properties.length; i++) {
            var attrName = 'attr' + this.properties[i].substring(0, 1).toUpperCase() + this.properties[i].substring(1);
            if (this[attrName] !== undefined) {
                options[this.properties[i]] = this[attrName];
            }
        }
        return options;
    };
    jqxValidatorComponent.prototype.createComponent = function (options) {
        if (options) {
            JQXLite.extend(options, this.manageAttributes());
        }
        else {
            options = this.manageAttributes();
        }
        this.host = JQXLite(this.elementRef.nativeElement.firstChild);
        this.__wireEvents__();
        this.widgetObject = jqwidgets.createInstance(this.host, 'jqxValidator', options);
        this.__updateRect__();
    };
    jqxValidatorComponent.prototype.createWidget = function (options) {
        this.createComponent(options);
    };
    jqxValidatorComponent.prototype.__updateRect__ = function () {
        this.host.css({ width: this.attrWidth, height: this.attrHeight });
    };
    jqxValidatorComponent.prototype.setOptions = function (options) {
        this.host.jqxValidator('setOptions', options);
    };
    // jqxValidatorComponent properties
    jqxValidatorComponent.prototype.arrow = function (arg) {
        if (arg !== undefined) {
            this.host.jqxValidator('arrow', arg);
        }
        else {
            return this.host.jqxValidator('arrow');
        }
    };
    jqxValidatorComponent.prototype.animation = function (arg) {
        if (arg !== undefined) {
            this.host.jqxValidator('animation', arg);
        }
        else {
            return this.host.jqxValidator('animation');
        }
    };
    jqxValidatorComponent.prototype.animationDuration = function (arg) {
        if (arg !== undefined) {
            this.host.jqxValidator('animationDuration', arg);
        }
        else {
            return this.host.jqxValidator('animationDuration');
        }
    };
    jqxValidatorComponent.prototype.closeOnClick = function (arg) {
        if (arg !== undefined) {
            this.host.jqxValidator('closeOnClick', arg);
        }
        else {
            return this.host.jqxValidator('closeOnClick');
        }
    };
    jqxValidatorComponent.prototype.focus = function (arg) {
        if (arg !== undefined) {
            this.host.jqxValidator('focus', arg);
        }
        else {
            return this.host.jqxValidator('focus');
        }
    };
    jqxValidatorComponent.prototype.hintType = function (arg) {
        if (arg !== undefined) {
            this.host.jqxValidator('hintType', arg);
        }
        else {
            return this.host.jqxValidator('hintType');
        }
    };
    jqxValidatorComponent.prototype.onError = function (arg) {
        if (arg !== undefined) {
            this.host.jqxValidator('onError', arg);
        }
        else {
            return this.host.jqxValidator('onError');
        }
    };
    jqxValidatorComponent.prototype.onSuccess = function (arg) {
        if (arg !== undefined) {
            this.host.jqxValidator('onSuccess', arg);
        }
        else {
            return this.host.jqxValidator('onSuccess');
        }
    };
    jqxValidatorComponent.prototype.position = function (arg) {
        if (arg !== undefined) {
            this.host.jqxValidator('position', arg);
        }
        else {
            return this.host.jqxValidator('position');
        }
    };
    jqxValidatorComponent.prototype.rules = function (arg) {
        if (arg !== undefined) {
            this.host.jqxValidator('rules', arg);
        }
        else {
            return this.host.jqxValidator('rules');
        }
    };
    jqxValidatorComponent.prototype.rtl = function (arg) {
        if (arg !== undefined) {
            this.host.jqxValidator('rtl', arg);
        }
        else {
            return this.host.jqxValidator('rtl');
        }
    };
    // jqxValidatorComponent functions
    jqxValidatorComponent.prototype.hideHint = function (id) {
        this.host.jqxValidator('hideHint', id);
    };
    jqxValidatorComponent.prototype.hide = function () {
        this.host.jqxValidator('hide');
    };
    jqxValidatorComponent.prototype.updatePosition = function () {
        this.host.jqxValidator('updatePosition');
    };
    jqxValidatorComponent.prototype.validate = function (htmlElement) {
        this.host.jqxValidator('validate', htmlElement);
    };
    jqxValidatorComponent.prototype.validateInput = function (id) {
        this.host.jqxValidator('validateInput', id);
    };
    jqxValidatorComponent.prototype.__wireEvents__ = function () {
        var _this = this;
        this.host.on('validationError', function (eventData) { _this.onValidationError.emit(eventData); });
        this.host.on('validationSuccess', function (eventData) { _this.onValidationSuccess.emit(eventData); });
    };
    return jqxValidatorComponent;
}()); //jqxValidatorComponent
__decorate([
    Input('arrow'),
    __metadata("design:type", Object)
], jqxValidatorComponent.prototype, "attrArrow", void 0);
__decorate([
    Input('animation'),
    __metadata("design:type", Object)
], jqxValidatorComponent.prototype, "attrAnimation", void 0);
__decorate([
    Input('animationDuration'),
    __metadata("design:type", Object)
], jqxValidatorComponent.prototype, "attrAnimationDuration", void 0);
__decorate([
    Input('closeOnClick'),
    __metadata("design:type", Object)
], jqxValidatorComponent.prototype, "attrCloseOnClick", void 0);
__decorate([
    Input('focus'),
    __metadata("design:type", Object)
], jqxValidatorComponent.prototype, "attrFocus", void 0);
__decorate([
    Input('hintType'),
    __metadata("design:type", Object)
], jqxValidatorComponent.prototype, "attrHintType", void 0);
__decorate([
    Input('onError'),
    __metadata("design:type", Object)
], jqxValidatorComponent.prototype, "attrOnError", void 0);
__decorate([
    Input('onSuccess'),
    __metadata("design:type", Object)
], jqxValidatorComponent.prototype, "attrOnSuccess", void 0);
__decorate([
    Input('position'),
    __metadata("design:type", Object)
], jqxValidatorComponent.prototype, "attrPosition", void 0);
__decorate([
    Input('rules'),
    __metadata("design:type", Object)
], jqxValidatorComponent.prototype, "attrRules", void 0);
__decorate([
    Input('rtl'),
    __metadata("design:type", Object)
], jqxValidatorComponent.prototype, "attrRtl", void 0);
__decorate([
    Input('width'),
    __metadata("design:type", Object)
], jqxValidatorComponent.prototype, "attrWidth", void 0);
__decorate([
    Input('height'),
    __metadata("design:type", Object)
], jqxValidatorComponent.prototype, "attrHeight", void 0);
__decorate([
    Input('auto-create'),
    __metadata("design:type", Boolean)
], jqxValidatorComponent.prototype, "autoCreate", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxValidatorComponent.prototype, "onValidationError", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxValidatorComponent.prototype, "onValidationSuccess", void 0);
jqxValidatorComponent = __decorate([
    Component({
        selector: 'jqxValidator',
        template: '<div><ng-content></ng-content></div>'
    }),
    __metadata("design:paramtypes", [ElementRef])
], jqxValidatorComponent);
export { jqxValidatorComponent };
//# sourceMappingURL=angular_jqxvalidator.js.map