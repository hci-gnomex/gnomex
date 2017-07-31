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
var jqxRepeatButtonComponent = (function () {
    function jqxRepeatButtonComponent(containerElement) {
        this.autoCreate = true;
        this.properties = ['delay', 'disabled', 'height', 'imgSrc', 'imgWidth', 'imgHeight', 'imgPosition', 'roundedCorners', 'rtl', 'textPosition', 'textImageRelation', 'theme', 'template', 'toggled', 'width', 'value'];
        // jqxRepeatButtonComponent events
        this.onClick = new EventEmitter();
        this.elementRef = containerElement;
    }
    jqxRepeatButtonComponent.prototype.ngOnInit = function () {
        if (this.autoCreate) {
            this.createComponent();
        }
    };
    ;
    jqxRepeatButtonComponent.prototype.ngOnChanges = function (changes) {
        if (this.host) {
            for (var i = 0; i < this.properties.length; i++) {
                var attrName = 'attr' + this.properties[i].substring(0, 1).toUpperCase() + this.properties[i].substring(1);
                var areEqual = void 0;
                if (this[attrName] !== undefined) {
                    if (typeof this[attrName] === 'object') {
                        if (this[attrName] instanceof Array) {
                            areEqual = this.arraysEqual(this[attrName], this.host.jqxRepeatButton(this.properties[i]));
                        }
                        if (areEqual) {
                            return false;
                        }
                        this.host.jqxRepeatButton(this.properties[i], this[attrName]);
                        continue;
                    }
                    if (this[attrName] !== this.host.jqxRepeatButton(this.properties[i])) {
                        this.host.jqxRepeatButton(this.properties[i], this[attrName]);
                    }
                }
            }
        }
    };
    jqxRepeatButtonComponent.prototype.arraysEqual = function (attrValue, hostValue) {
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
    jqxRepeatButtonComponent.prototype.manageAttributes = function () {
        var options = {};
        for (var i = 0; i < this.properties.length; i++) {
            var attrName = 'attr' + this.properties[i].substring(0, 1).toUpperCase() + this.properties[i].substring(1);
            if (this[attrName] !== undefined) {
                options[this.properties[i]] = this[attrName];
            }
        }
        return options;
    };
    jqxRepeatButtonComponent.prototype.createComponent = function (options) {
        if (options) {
            JQXLite.extend(options, this.manageAttributes());
        }
        else {
            options = this.manageAttributes();
        }
        this.host = JQXLite(this.elementRef.nativeElement.firstChild);
        this.__wireEvents__();
        this.widgetObject = jqwidgets.createInstance(this.host, 'jqxRepeatButton', options);
        this.__updateRect__();
    };
    jqxRepeatButtonComponent.prototype.createWidget = function (options) {
        this.createComponent(options);
    };
    jqxRepeatButtonComponent.prototype.__updateRect__ = function () {
        this.host.css({ width: this.attrWidth, height: this.attrHeight });
    };
    jqxRepeatButtonComponent.prototype.setOptions = function (options) {
        this.host.jqxRepeatButton('setOptions', options);
    };
    // jqxRepeatButtonComponent properties
    jqxRepeatButtonComponent.prototype.delay = function (arg) {
        if (arg !== undefined) {
            this.host.jqxRepeatButton('delay', arg);
        }
        else {
            return this.host.jqxRepeatButton('delay');
        }
    };
    jqxRepeatButtonComponent.prototype.disabled = function (arg) {
        if (arg !== undefined) {
            this.host.jqxRepeatButton('disabled', arg);
        }
        else {
            return this.host.jqxRepeatButton('disabled');
        }
    };
    jqxRepeatButtonComponent.prototype.height = function (arg) {
        if (arg !== undefined) {
            this.host.jqxRepeatButton('height', arg);
        }
        else {
            return this.host.jqxRepeatButton('height');
        }
    };
    jqxRepeatButtonComponent.prototype.imgSrc = function (arg) {
        if (arg !== undefined) {
            this.host.jqxRepeatButton('imgSrc', arg);
        }
        else {
            return this.host.jqxRepeatButton('imgSrc');
        }
    };
    jqxRepeatButtonComponent.prototype.imgWidth = function (arg) {
        if (arg !== undefined) {
            this.host.jqxRepeatButton('imgWidth', arg);
        }
        else {
            return this.host.jqxRepeatButton('imgWidth');
        }
    };
    jqxRepeatButtonComponent.prototype.imgHeight = function (arg) {
        if (arg !== undefined) {
            this.host.jqxRepeatButton('imgHeight', arg);
        }
        else {
            return this.host.jqxRepeatButton('imgHeight');
        }
    };
    jqxRepeatButtonComponent.prototype.imgPosition = function (arg) {
        if (arg !== undefined) {
            this.host.jqxRepeatButton('imgPosition', arg);
        }
        else {
            return this.host.jqxRepeatButton('imgPosition');
        }
    };
    jqxRepeatButtonComponent.prototype.roundedCorners = function (arg) {
        if (arg !== undefined) {
            this.host.jqxRepeatButton('roundedCorners', arg);
        }
        else {
            return this.host.jqxRepeatButton('roundedCorners');
        }
    };
    jqxRepeatButtonComponent.prototype.rtl = function (arg) {
        if (arg !== undefined) {
            this.host.jqxRepeatButton('rtl', arg);
        }
        else {
            return this.host.jqxRepeatButton('rtl');
        }
    };
    jqxRepeatButtonComponent.prototype.textPosition = function (arg) {
        if (arg !== undefined) {
            this.host.jqxRepeatButton('textPosition', arg);
        }
        else {
            return this.host.jqxRepeatButton('textPosition');
        }
    };
    jqxRepeatButtonComponent.prototype.textImageRelation = function (arg) {
        if (arg !== undefined) {
            this.host.jqxRepeatButton('textImageRelation', arg);
        }
        else {
            return this.host.jqxRepeatButton('textImageRelation');
        }
    };
    jqxRepeatButtonComponent.prototype.theme = function (arg) {
        if (arg !== undefined) {
            this.host.jqxRepeatButton('theme', arg);
        }
        else {
            return this.host.jqxRepeatButton('theme');
        }
    };
    jqxRepeatButtonComponent.prototype.template = function (arg) {
        if (arg !== undefined) {
            this.host.jqxRepeatButton('template', arg);
        }
        else {
            return this.host.jqxRepeatButton('template');
        }
    };
    jqxRepeatButtonComponent.prototype.toggled = function (arg) {
        if (arg !== undefined) {
            this.host.jqxRepeatButton('toggled', arg);
        }
        else {
            return this.host.jqxRepeatButton('toggled');
        }
    };
    jqxRepeatButtonComponent.prototype.width = function (arg) {
        if (arg !== undefined) {
            this.host.jqxRepeatButton('width', arg);
        }
        else {
            return this.host.jqxRepeatButton('width');
        }
    };
    jqxRepeatButtonComponent.prototype.value = function (arg) {
        if (arg !== undefined) {
            this.host.jqxRepeatButton('value', arg);
        }
        else {
            return this.host.jqxRepeatButton('value');
        }
    };
    // jqxRepeatButtonComponent functions
    jqxRepeatButtonComponent.prototype.check = function () {
        this.host.jqxRepeatButton('check');
    };
    jqxRepeatButtonComponent.prototype.destroy = function () {
        this.host.jqxRepeatButton('destroy');
    };
    jqxRepeatButtonComponent.prototype.focus = function () {
        this.host.jqxRepeatButton('focus');
    };
    jqxRepeatButtonComponent.prototype.render = function () {
        this.host.jqxRepeatButton('render');
    };
    jqxRepeatButtonComponent.prototype.toggle = function () {
        this.host.jqxRepeatButton('toggle');
    };
    jqxRepeatButtonComponent.prototype.unCheck = function () {
        this.host.jqxRepeatButton('unCheck');
    };
    jqxRepeatButtonComponent.prototype.val = function (value) {
        if (value !== undefined) {
            this.host.jqxRepeatButton("val", value);
        }
        else {
            return this.host.jqxRepeatButton("val");
        }
    };
    ;
    jqxRepeatButtonComponent.prototype.__wireEvents__ = function () {
        var _this = this;
        this.host.on('click', function (eventData) { _this.onClick.emit(eventData); });
    };
    return jqxRepeatButtonComponent;
}()); //jqxRepeatButtonComponent
__decorate([
    Input('delay'),
    __metadata("design:type", Object)
], jqxRepeatButtonComponent.prototype, "attrDelay", void 0);
__decorate([
    Input('disabled'),
    __metadata("design:type", Object)
], jqxRepeatButtonComponent.prototype, "attrDisabled", void 0);
__decorate([
    Input('imgSrc'),
    __metadata("design:type", Object)
], jqxRepeatButtonComponent.prototype, "attrImgSrc", void 0);
__decorate([
    Input('imgWidth'),
    __metadata("design:type", Object)
], jqxRepeatButtonComponent.prototype, "attrImgWidth", void 0);
__decorate([
    Input('imgHeight'),
    __metadata("design:type", Object)
], jqxRepeatButtonComponent.prototype, "attrImgHeight", void 0);
__decorate([
    Input('imgPosition'),
    __metadata("design:type", Object)
], jqxRepeatButtonComponent.prototype, "attrImgPosition", void 0);
__decorate([
    Input('roundedCorners'),
    __metadata("design:type", Object)
], jqxRepeatButtonComponent.prototype, "attrRoundedCorners", void 0);
__decorate([
    Input('rtl'),
    __metadata("design:type", Object)
], jqxRepeatButtonComponent.prototype, "attrRtl", void 0);
__decorate([
    Input('textPosition'),
    __metadata("design:type", Object)
], jqxRepeatButtonComponent.prototype, "attrTextPosition", void 0);
__decorate([
    Input('textImageRelation'),
    __metadata("design:type", Object)
], jqxRepeatButtonComponent.prototype, "attrTextImageRelation", void 0);
__decorate([
    Input('theme'),
    __metadata("design:type", Object)
], jqxRepeatButtonComponent.prototype, "attrTheme", void 0);
__decorate([
    Input('template'),
    __metadata("design:type", Object)
], jqxRepeatButtonComponent.prototype, "attrTemplate", void 0);
__decorate([
    Input('toggled'),
    __metadata("design:type", Object)
], jqxRepeatButtonComponent.prototype, "attrToggled", void 0);
__decorate([
    Input('value'),
    __metadata("design:type", Object)
], jqxRepeatButtonComponent.prototype, "attrValue", void 0);
__decorate([
    Input('width'),
    __metadata("design:type", Object)
], jqxRepeatButtonComponent.prototype, "attrWidth", void 0);
__decorate([
    Input('height'),
    __metadata("design:type", Object)
], jqxRepeatButtonComponent.prototype, "attrHeight", void 0);
__decorate([
    Input('auto-create'),
    __metadata("design:type", Boolean)
], jqxRepeatButtonComponent.prototype, "autoCreate", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxRepeatButtonComponent.prototype, "onClick", void 0);
jqxRepeatButtonComponent = __decorate([
    Component({
        selector: 'jqxRepeatButton',
        template: '<div><ng-content></ng-content></div>'
    }),
    __metadata("design:paramtypes", [ElementRef])
], jqxRepeatButtonComponent);
export { jqxRepeatButtonComponent };
//# sourceMappingURL=angular_jqxrepeatbutton.js.map