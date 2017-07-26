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
var jqxLinkButtonComponent = (function () {
    function jqxLinkButtonComponent(containerElement) {
        this.autoCreate = true;
        this.properties = ['delay', 'disabled', 'height', 'imgSrc', 'imgWidth', 'imgHeight', 'imgPosition', 'roundedCorners', 'rtl', 'textPosition', 'textImageRelation', 'theme', 'template', 'toggled', 'width', 'value'];
        // jqxLinkButtonComponent events
        this.onClick = new EventEmitter();
        this.elementRef = containerElement;
    }
    jqxLinkButtonComponent.prototype.ngOnInit = function () {
        if (this.autoCreate) {
            this.createComponent();
        }
    };
    ;
    jqxLinkButtonComponent.prototype.ngOnChanges = function (changes) {
        if (this.host) {
            for (var i = 0; i < this.properties.length; i++) {
                var attrName = 'attr' + this.properties[i].substring(0, 1).toUpperCase() + this.properties[i].substring(1);
                var areEqual = void 0;
                if (this[attrName] !== undefined) {
                    if (typeof this[attrName] === 'object') {
                        if (this[attrName] instanceof Array) {
                            areEqual = this.arraysEqual(this[attrName], this.host.jqxLinkButton(this.properties[i]));
                        }
                        if (areEqual) {
                            return false;
                        }
                        this.host.jqxLinkButton(this.properties[i], this[attrName]);
                        continue;
                    }
                    if (this[attrName] !== this.host.jqxLinkButton(this.properties[i])) {
                        this.host.jqxLinkButton(this.properties[i], this[attrName]);
                    }
                }
            }
        }
    };
    jqxLinkButtonComponent.prototype.arraysEqual = function (attrValue, hostValue) {
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
    jqxLinkButtonComponent.prototype.manageAttributes = function () {
        var options = {};
        for (var i = 0; i < this.properties.length; i++) {
            var attrName = 'attr' + this.properties[i].substring(0, 1).toUpperCase() + this.properties[i].substring(1);
            if (this[attrName] !== undefined) {
                options[this.properties[i]] = this[attrName];
            }
        }
        return options;
    };
    jqxLinkButtonComponent.prototype.createComponent = function (options) {
        if (options) {
            JQXLite.extend(options, this.manageAttributes());
        }
        else {
            options = this.manageAttributes();
        }
        this.host = JQXLite(this.elementRef.nativeElement.firstChild);
        this.__wireEvents__();
        this.widgetObject = jqwidgets.createInstance(this.host, 'jqxLinkButton', options);
        this.__updateRect__();
    };
    jqxLinkButtonComponent.prototype.createWidget = function (options) {
        this.createComponent(options);
    };
    jqxLinkButtonComponent.prototype.__updateRect__ = function () {
        this.host.css({ width: this.attrWidth, height: this.attrHeight });
    };
    jqxLinkButtonComponent.prototype.setOptions = function (options) {
        this.host.jqxLinkButton('setOptions', options);
    };
    // jqxLinkButtonComponent properties
    jqxLinkButtonComponent.prototype.delay = function (arg) {
        if (arg !== undefined) {
            this.host.jqxLinkButton('delay', arg);
        }
        else {
            return this.host.jqxLinkButton('delay');
        }
    };
    jqxLinkButtonComponent.prototype.disabled = function (arg) {
        if (arg !== undefined) {
            this.host.jqxLinkButton('disabled', arg);
        }
        else {
            return this.host.jqxLinkButton('disabled');
        }
    };
    jqxLinkButtonComponent.prototype.height = function (arg) {
        if (arg !== undefined) {
            this.host.jqxLinkButton('height', arg);
        }
        else {
            return this.host.jqxLinkButton('height');
        }
    };
    jqxLinkButtonComponent.prototype.imgSrc = function (arg) {
        if (arg !== undefined) {
            this.host.jqxLinkButton('imgSrc', arg);
        }
        else {
            return this.host.jqxLinkButton('imgSrc');
        }
    };
    jqxLinkButtonComponent.prototype.imgWidth = function (arg) {
        if (arg !== undefined) {
            this.host.jqxLinkButton('imgWidth', arg);
        }
        else {
            return this.host.jqxLinkButton('imgWidth');
        }
    };
    jqxLinkButtonComponent.prototype.imgHeight = function (arg) {
        if (arg !== undefined) {
            this.host.jqxLinkButton('imgHeight', arg);
        }
        else {
            return this.host.jqxLinkButton('imgHeight');
        }
    };
    jqxLinkButtonComponent.prototype.imgPosition = function (arg) {
        if (arg !== undefined) {
            this.host.jqxLinkButton('imgPosition', arg);
        }
        else {
            return this.host.jqxLinkButton('imgPosition');
        }
    };
    jqxLinkButtonComponent.prototype.roundedCorners = function (arg) {
        if (arg !== undefined) {
            this.host.jqxLinkButton('roundedCorners', arg);
        }
        else {
            return this.host.jqxLinkButton('roundedCorners');
        }
    };
    jqxLinkButtonComponent.prototype.rtl = function (arg) {
        if (arg !== undefined) {
            this.host.jqxLinkButton('rtl', arg);
        }
        else {
            return this.host.jqxLinkButton('rtl');
        }
    };
    jqxLinkButtonComponent.prototype.textPosition = function (arg) {
        if (arg !== undefined) {
            this.host.jqxLinkButton('textPosition', arg);
        }
        else {
            return this.host.jqxLinkButton('textPosition');
        }
    };
    jqxLinkButtonComponent.prototype.textImageRelation = function (arg) {
        if (arg !== undefined) {
            this.host.jqxLinkButton('textImageRelation', arg);
        }
        else {
            return this.host.jqxLinkButton('textImageRelation');
        }
    };
    jqxLinkButtonComponent.prototype.theme = function (arg) {
        if (arg !== undefined) {
            this.host.jqxLinkButton('theme', arg);
        }
        else {
            return this.host.jqxLinkButton('theme');
        }
    };
    jqxLinkButtonComponent.prototype.template = function (arg) {
        if (arg !== undefined) {
            this.host.jqxLinkButton('template', arg);
        }
        else {
            return this.host.jqxLinkButton('template');
        }
    };
    jqxLinkButtonComponent.prototype.toggled = function (arg) {
        if (arg !== undefined) {
            this.host.jqxLinkButton('toggled', arg);
        }
        else {
            return this.host.jqxLinkButton('toggled');
        }
    };
    jqxLinkButtonComponent.prototype.width = function (arg) {
        if (arg !== undefined) {
            this.host.jqxLinkButton('width', arg);
        }
        else {
            return this.host.jqxLinkButton('width');
        }
    };
    jqxLinkButtonComponent.prototype.value = function (arg) {
        if (arg !== undefined) {
            this.host.jqxLinkButton('value', arg);
        }
        else {
            return this.host.jqxLinkButton('value');
        }
    };
    // jqxLinkButtonComponent functions
    jqxLinkButtonComponent.prototype.check = function () {
        this.host.jqxLinkButton('check');
    };
    jqxLinkButtonComponent.prototype.destroy = function () {
        this.host.jqxLinkButton('destroy');
    };
    jqxLinkButtonComponent.prototype.focus = function () {
        this.host.jqxLinkButton('focus');
    };
    jqxLinkButtonComponent.prototype.render = function () {
        this.host.jqxLinkButton('render');
    };
    jqxLinkButtonComponent.prototype.toggle = function () {
        this.host.jqxLinkButton('toggle');
    };
    jqxLinkButtonComponent.prototype.unCheck = function () {
        this.host.jqxLinkButton('unCheck');
    };
    jqxLinkButtonComponent.prototype.val = function (value) {
        if (value !== undefined) {
            this.host.jqxLinkButton("val", value);
        }
        else {
            return this.host.jqxLinkButton("val");
        }
    };
    ;
    jqxLinkButtonComponent.prototype.__wireEvents__ = function () {
        var _this = this;
        this.host.on('click', function (eventData) { _this.onClick.emit(eventData); });
    };
    return jqxLinkButtonComponent;
}()); //jqxLinkButtonComponent
__decorate([
    Input('delay'),
    __metadata("design:type", Object)
], jqxLinkButtonComponent.prototype, "attrDelay", void 0);
__decorate([
    Input('disabled'),
    __metadata("design:type", Object)
], jqxLinkButtonComponent.prototype, "attrDisabled", void 0);
__decorate([
    Input('imgSrc'),
    __metadata("design:type", Object)
], jqxLinkButtonComponent.prototype, "attrImgSrc", void 0);
__decorate([
    Input('imgWidth'),
    __metadata("design:type", Object)
], jqxLinkButtonComponent.prototype, "attrImgWidth", void 0);
__decorate([
    Input('imgHeight'),
    __metadata("design:type", Object)
], jqxLinkButtonComponent.prototype, "attrImgHeight", void 0);
__decorate([
    Input('imgPosition'),
    __metadata("design:type", Object)
], jqxLinkButtonComponent.prototype, "attrImgPosition", void 0);
__decorate([
    Input('roundedCorners'),
    __metadata("design:type", Object)
], jqxLinkButtonComponent.prototype, "attrRoundedCorners", void 0);
__decorate([
    Input('rtl'),
    __metadata("design:type", Object)
], jqxLinkButtonComponent.prototype, "attrRtl", void 0);
__decorate([
    Input('textPosition'),
    __metadata("design:type", Object)
], jqxLinkButtonComponent.prototype, "attrTextPosition", void 0);
__decorate([
    Input('textImageRelation'),
    __metadata("design:type", Object)
], jqxLinkButtonComponent.prototype, "attrTextImageRelation", void 0);
__decorate([
    Input('theme'),
    __metadata("design:type", Object)
], jqxLinkButtonComponent.prototype, "attrTheme", void 0);
__decorate([
    Input('template'),
    __metadata("design:type", Object)
], jqxLinkButtonComponent.prototype, "attrTemplate", void 0);
__decorate([
    Input('toggled'),
    __metadata("design:type", Object)
], jqxLinkButtonComponent.prototype, "attrToggled", void 0);
__decorate([
    Input('value'),
    __metadata("design:type", Object)
], jqxLinkButtonComponent.prototype, "attrValue", void 0);
__decorate([
    Input('width'),
    __metadata("design:type", Object)
], jqxLinkButtonComponent.prototype, "attrWidth", void 0);
__decorate([
    Input('height'),
    __metadata("design:type", Object)
], jqxLinkButtonComponent.prototype, "attrHeight", void 0);
__decorate([
    Input('auto-create'),
    __metadata("design:type", Boolean)
], jqxLinkButtonComponent.prototype, "autoCreate", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxLinkButtonComponent.prototype, "onClick", void 0);
jqxLinkButtonComponent = __decorate([
    Component({
        selector: 'jqxLinkButton',
        template: '<div><ng-content></ng-content></div>'
    }),
    __metadata("design:paramtypes", [ElementRef])
], jqxLinkButtonComponent);
export { jqxLinkButtonComponent };
//# sourceMappingURL=angular_jqxlinkbutton.js.map