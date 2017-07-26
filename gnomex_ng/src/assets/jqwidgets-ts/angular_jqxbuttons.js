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
var jqxButtonComponent = (function () {
    function jqxButtonComponent(containerElement) {
        this.autoCreate = true;
        this.properties = ['delay', 'disabled', 'height', 'imgSrc', 'imgWidth', 'imgHeight', 'imgPosition', 'roundedCorners', 'rtl', 'textPosition', 'textImageRelation', 'theme', 'template', 'toggled', 'width', 'value'];
        // jqxButtonComponent events
        this.onClick = new EventEmitter();
        this.elementRef = containerElement;
    }
    jqxButtonComponent.prototype.ngOnInit = function () {
        if (this.autoCreate) {
            this.createComponent();
        }
    };
    ;
    jqxButtonComponent.prototype.ngOnChanges = function (changes) {
        if (this.host) {
            for (var i = 0; i < this.properties.length; i++) {
                var attrName = 'attr' + this.properties[i].substring(0, 1).toUpperCase() + this.properties[i].substring(1);
                var areEqual = void 0;
                if (this[attrName] !== undefined) {
                    if (typeof this[attrName] === 'object') {
                        if (this[attrName] instanceof Array) {
                            areEqual = this.arraysEqual(this[attrName], this.host.jqxButton(this.properties[i]));
                        }
                        if (areEqual) {
                            return false;
                        }
                        this.host.jqxButton(this.properties[i], this[attrName]);
                        continue;
                    }
                    if (this[attrName] !== this.host.jqxButton(this.properties[i])) {
                        this.host.jqxButton(this.properties[i], this[attrName]);
                    }
                }
            }
        }
    };
    jqxButtonComponent.prototype.arraysEqual = function (attrValue, hostValue) {
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
    jqxButtonComponent.prototype.manageAttributes = function () {
        var options = {};
        for (var i = 0; i < this.properties.length; i++) {
            var attrName = 'attr' + this.properties[i].substring(0, 1).toUpperCase() + this.properties[i].substring(1);
            if (this[attrName] !== undefined) {
                options[this.properties[i]] = this[attrName];
            }
        }
        return options;
    };
    jqxButtonComponent.prototype.createComponent = function (options) {
        if (options) {
            JQXLite.extend(options, this.manageAttributes());
        }
        else {
            options = this.manageAttributes();
        }
        this.host = JQXLite(this.elementRef.nativeElement.firstChild);
        this.widgetObject = jqwidgets.createInstance(this.host, 'jqxButton', options);
        this.host = this.widgetObject['host'];
        this.__wireEvents__();
        this.__updateRect__();
    };
    jqxButtonComponent.prototype.createWidget = function (options) {
        this.createComponent(options);
    };
    jqxButtonComponent.prototype.__updateRect__ = function () {
        this.host.css({ width: this.attrWidth, height: this.attrHeight });
    };
    jqxButtonComponent.prototype.setOptions = function (options) {
        this.host.jqxButton('setOptions', options);
    };
    // jqxButtonComponent properties
    jqxButtonComponent.prototype.delay = function (arg) {
        if (arg !== undefined) {
            this.host.jqxButton('delay', arg);
        }
        else {
            return this.host.jqxButton('delay');
        }
    };
    jqxButtonComponent.prototype.disabled = function (arg) {
        if (arg !== undefined) {
            this.host.jqxButton('disabled', arg);
        }
        else {
            return this.host.jqxButton('disabled');
        }
    };
    jqxButtonComponent.prototype.height = function (arg) {
        if (arg !== undefined) {
            this.host.jqxButton('height', arg);
        }
        else {
            return this.host.jqxButton('height');
        }
    };
    jqxButtonComponent.prototype.imgSrc = function (arg) {
        if (arg !== undefined) {
            this.host.jqxButton('imgSrc', arg);
        }
        else {
            return this.host.jqxButton('imgSrc');
        }
    };
    jqxButtonComponent.prototype.imgWidth = function (arg) {
        if (arg !== undefined) {
            this.host.jqxButton('imgWidth', arg);
        }
        else {
            return this.host.jqxButton('imgWidth');
        }
    };
    jqxButtonComponent.prototype.imgHeight = function (arg) {
        if (arg !== undefined) {
            this.host.jqxButton('imgHeight', arg);
        }
        else {
            return this.host.jqxButton('imgHeight');
        }
    };
    jqxButtonComponent.prototype.imgPosition = function (arg) {
        if (arg !== undefined) {
            this.host.jqxButton('imgPosition', arg);
        }
        else {
            return this.host.jqxButton('imgPosition');
        }
    };
    jqxButtonComponent.prototype.roundedCorners = function (arg) {
        if (arg !== undefined) {
            this.host.jqxButton('roundedCorners', arg);
        }
        else {
            return this.host.jqxButton('roundedCorners');
        }
    };
    jqxButtonComponent.prototype.rtl = function (arg) {
        if (arg !== undefined) {
            this.host.jqxButton('rtl', arg);
        }
        else {
            return this.host.jqxButton('rtl');
        }
    };
    jqxButtonComponent.prototype.textPosition = function (arg) {
        if (arg !== undefined) {
            this.host.jqxButton('textPosition', arg);
        }
        else {
            return this.host.jqxButton('textPosition');
        }
    };
    jqxButtonComponent.prototype.textImageRelation = function (arg) {
        if (arg !== undefined) {
            this.host.jqxButton('textImageRelation', arg);
        }
        else {
            return this.host.jqxButton('textImageRelation');
        }
    };
    jqxButtonComponent.prototype.theme = function (arg) {
        if (arg !== undefined) {
            this.host.jqxButton('theme', arg);
        }
        else {
            return this.host.jqxButton('theme');
        }
    };
    jqxButtonComponent.prototype.template = function (arg) {
        if (arg !== undefined) {
            this.host.jqxButton('template', arg);
        }
        else {
            return this.host.jqxButton('template');
        }
    };
    jqxButtonComponent.prototype.toggled = function (arg) {
        if (arg !== undefined) {
            this.host.jqxButton('toggled', arg);
        }
        else {
            return this.host.jqxButton('toggled');
        }
    };
    jqxButtonComponent.prototype.width = function (arg) {
        if (arg !== undefined) {
            this.host.jqxButton('width', arg);
        }
        else {
            return this.host.jqxButton('width');
        }
    };
    jqxButtonComponent.prototype.value = function (arg) {
        if (arg !== undefined) {
            this.host.jqxButton('value', arg);
        }
        else {
            return this.host.jqxButton('value');
        }
    };
    // jqxButtonComponent functions
    jqxButtonComponent.prototype.check = function () {
        this.host.jqxButton('check');
    };
    jqxButtonComponent.prototype.destroy = function () {
        this.host.jqxButton('destroy');
    };
    jqxButtonComponent.prototype.focus = function () {
        this.host.jqxButton('focus');
    };
    jqxButtonComponent.prototype.render = function () {
        this.host.jqxButton('render');
    };
    jqxButtonComponent.prototype.toggle = function () {
        this.host.jqxButton('toggle');
    };
    jqxButtonComponent.prototype.unCheck = function () {
        this.host.jqxButton('unCheck');
    };
    jqxButtonComponent.prototype.val = function (value) {
        if (value !== undefined) {
            this.host.jqxButton("val", value);
        }
        else {
            return this.host.jqxButton("val");
        }
    };
    ;
    jqxButtonComponent.prototype.__wireEvents__ = function () {
        var _this = this;
        this.host.on('click', function (eventData) { _this.onClick.emit(eventData); });
    };
    return jqxButtonComponent;
}()); //jqxButtonComponent
__decorate([
    Input('delay'),
    __metadata("design:type", Object)
], jqxButtonComponent.prototype, "attrDelay", void 0);
__decorate([
    Input('disabled'),
    __metadata("design:type", Object)
], jqxButtonComponent.prototype, "attrDisabled", void 0);
__decorate([
    Input('imgSrc'),
    __metadata("design:type", Object)
], jqxButtonComponent.prototype, "attrImgSrc", void 0);
__decorate([
    Input('imgWidth'),
    __metadata("design:type", Object)
], jqxButtonComponent.prototype, "attrImgWidth", void 0);
__decorate([
    Input('imgHeight'),
    __metadata("design:type", Object)
], jqxButtonComponent.prototype, "attrImgHeight", void 0);
__decorate([
    Input('imgPosition'),
    __metadata("design:type", Object)
], jqxButtonComponent.prototype, "attrImgPosition", void 0);
__decorate([
    Input('roundedCorners'),
    __metadata("design:type", Object)
], jqxButtonComponent.prototype, "attrRoundedCorners", void 0);
__decorate([
    Input('rtl'),
    __metadata("design:type", Object)
], jqxButtonComponent.prototype, "attrRtl", void 0);
__decorate([
    Input('textPosition'),
    __metadata("design:type", Object)
], jqxButtonComponent.prototype, "attrTextPosition", void 0);
__decorate([
    Input('textImageRelation'),
    __metadata("design:type", Object)
], jqxButtonComponent.prototype, "attrTextImageRelation", void 0);
__decorate([
    Input('theme'),
    __metadata("design:type", Object)
], jqxButtonComponent.prototype, "attrTheme", void 0);
__decorate([
    Input('template'),
    __metadata("design:type", Object)
], jqxButtonComponent.prototype, "attrTemplate", void 0);
__decorate([
    Input('toggled'),
    __metadata("design:type", Object)
], jqxButtonComponent.prototype, "attrToggled", void 0);
__decorate([
    Input('value'),
    __metadata("design:type", Object)
], jqxButtonComponent.prototype, "attrValue", void 0);
__decorate([
    Input('width'),
    __metadata("design:type", Object)
], jqxButtonComponent.prototype, "attrWidth", void 0);
__decorate([
    Input('height'),
    __metadata("design:type", Object)
], jqxButtonComponent.prototype, "attrHeight", void 0);
__decorate([
    Input('auto-create'),
    __metadata("design:type", Boolean)
], jqxButtonComponent.prototype, "autoCreate", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxButtonComponent.prototype, "onClick", void 0);
jqxButtonComponent = __decorate([
    Component({
        selector: 'jqxButton',
        template: '<button><ng-content></ng-content></button>'
    }),
    __metadata("design:paramtypes", [ElementRef])
], jqxButtonComponent);
export { jqxButtonComponent };
//# sourceMappingURL=angular_jqxbuttons.js.map