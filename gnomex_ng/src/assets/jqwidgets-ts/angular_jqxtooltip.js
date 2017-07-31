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
var jqxTooltipComponent = (function () {
    function jqxTooltipComponent(containerElement) {
        this.autoCreate = true;
        this.properties = ['absolutePositionX', 'absolutePositionY', 'autoHide', 'autoHideDelay', 'animationShowDelay', 'animationHideDelay', 'content', 'closeOnClick', 'disabled', 'enableBrowserBoundsDetection', 'height', 'left', 'name', 'opacity', 'position', 'rtl', 'showDelay', 'showArrow', 'top', 'trigger', 'theme', 'width'];
        // jqxTooltipComponent events
        this.onClose = new EventEmitter();
        this.onClosing = new EventEmitter();
        this.onOpen = new EventEmitter();
        this.onOpening = new EventEmitter();
        this.elementRef = containerElement;
    }
    jqxTooltipComponent.prototype.ngOnInit = function () {
        if (this.autoCreate) {
            this.createComponent();
        }
    };
    ;
    jqxTooltipComponent.prototype.ngOnChanges = function (changes) {
        if (this.host) {
            for (var i = 0; i < this.properties.length; i++) {
                var attrName = 'attr' + this.properties[i].substring(0, 1).toUpperCase() + this.properties[i].substring(1);
                var areEqual = void 0;
                if (this[attrName] !== undefined) {
                    if (typeof this[attrName] === 'object') {
                        if (this[attrName] instanceof Array) {
                            areEqual = this.arraysEqual(this[attrName], this.host.jqxTooltip(this.properties[i]));
                        }
                        if (areEqual) {
                            return false;
                        }
                        this.host.jqxTooltip(this.properties[i], this[attrName]);
                        continue;
                    }
                    if (this[attrName] !== this.host.jqxTooltip(this.properties[i])) {
                        this.host.jqxTooltip(this.properties[i], this[attrName]);
                    }
                }
            }
        }
    };
    jqxTooltipComponent.prototype.arraysEqual = function (attrValue, hostValue) {
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
    jqxTooltipComponent.prototype.manageAttributes = function () {
        var options = {};
        for (var i = 0; i < this.properties.length; i++) {
            var attrName = 'attr' + this.properties[i].substring(0, 1).toUpperCase() + this.properties[i].substring(1);
            if (this[attrName] !== undefined) {
                options[this.properties[i]] = this[attrName];
            }
        }
        return options;
    };
    jqxTooltipComponent.prototype.createComponent = function (options) {
        if (options) {
            JQXLite.extend(options, this.manageAttributes());
        }
        else {
            options = this.manageAttributes();
        }
        this.host = JQXLite(this.elementRef.nativeElement.firstChild);
        this.__wireEvents__();
        this.widgetObject = jqwidgets.createInstance(this.host, 'jqxTooltip', options);
        this.__updateRect__();
    };
    jqxTooltipComponent.prototype.createWidget = function (options) {
        this.createComponent(options);
    };
    jqxTooltipComponent.prototype.__updateRect__ = function () {
        this.host.css({ width: this.attrWidth, height: this.attrHeight });
    };
    jqxTooltipComponent.prototype.setOptions = function (options) {
        this.host.jqxTooltip('setOptions', options);
    };
    // jqxTooltipComponent properties
    jqxTooltipComponent.prototype.absolutePositionX = function (arg) {
        if (arg !== undefined) {
            this.host.jqxTooltip('absolutePositionX', arg);
        }
        else {
            return this.host.jqxTooltip('absolutePositionX');
        }
    };
    jqxTooltipComponent.prototype.absolutePositionY = function (arg) {
        if (arg !== undefined) {
            this.host.jqxTooltip('absolutePositionY', arg);
        }
        else {
            return this.host.jqxTooltip('absolutePositionY');
        }
    };
    jqxTooltipComponent.prototype.autoHide = function (arg) {
        if (arg !== undefined) {
            this.host.jqxTooltip('autoHide', arg);
        }
        else {
            return this.host.jqxTooltip('autoHide');
        }
    };
    jqxTooltipComponent.prototype.autoHideDelay = function (arg) {
        if (arg !== undefined) {
            this.host.jqxTooltip('autoHideDelay', arg);
        }
        else {
            return this.host.jqxTooltip('autoHideDelay');
        }
    };
    jqxTooltipComponent.prototype.animationShowDelay = function (arg) {
        if (arg !== undefined) {
            this.host.jqxTooltip('animationShowDelay', arg);
        }
        else {
            return this.host.jqxTooltip('animationShowDelay');
        }
    };
    jqxTooltipComponent.prototype.animationHideDelay = function (arg) {
        if (arg !== undefined) {
            this.host.jqxTooltip('animationHideDelay', arg);
        }
        else {
            return this.host.jqxTooltip('animationHideDelay');
        }
    };
    jqxTooltipComponent.prototype.content = function (arg) {
        if (arg !== undefined) {
            this.host.jqxTooltip('content', arg);
        }
        else {
            return this.host.jqxTooltip('content');
        }
    };
    jqxTooltipComponent.prototype.closeOnClick = function (arg) {
        if (arg !== undefined) {
            this.host.jqxTooltip('closeOnClick', arg);
        }
        else {
            return this.host.jqxTooltip('closeOnClick');
        }
    };
    jqxTooltipComponent.prototype.disabled = function (arg) {
        if (arg !== undefined) {
            this.host.jqxTooltip('disabled', arg);
        }
        else {
            return this.host.jqxTooltip('disabled');
        }
    };
    jqxTooltipComponent.prototype.enableBrowserBoundsDetection = function (arg) {
        if (arg !== undefined) {
            this.host.jqxTooltip('enableBrowserBoundsDetection', arg);
        }
        else {
            return this.host.jqxTooltip('enableBrowserBoundsDetection');
        }
    };
    jqxTooltipComponent.prototype.height = function (arg) {
        if (arg !== undefined) {
            this.host.jqxTooltip('height', arg);
        }
        else {
            return this.host.jqxTooltip('height');
        }
    };
    jqxTooltipComponent.prototype.left = function (arg) {
        if (arg !== undefined) {
            this.host.jqxTooltip('left', arg);
        }
        else {
            return this.host.jqxTooltip('left');
        }
    };
    jqxTooltipComponent.prototype.name = function (arg) {
        if (arg !== undefined) {
            this.host.jqxTooltip('name', arg);
        }
        else {
            return this.host.jqxTooltip('name');
        }
    };
    jqxTooltipComponent.prototype.opacity = function (arg) {
        if (arg !== undefined) {
            this.host.jqxTooltip('opacity', arg);
        }
        else {
            return this.host.jqxTooltip('opacity');
        }
    };
    jqxTooltipComponent.prototype.position = function (arg) {
        if (arg !== undefined) {
            this.host.jqxTooltip('position', arg);
        }
        else {
            return this.host.jqxTooltip('position');
        }
    };
    jqxTooltipComponent.prototype.rtl = function (arg) {
        if (arg !== undefined) {
            this.host.jqxTooltip('rtl', arg);
        }
        else {
            return this.host.jqxTooltip('rtl');
        }
    };
    jqxTooltipComponent.prototype.showDelay = function (arg) {
        if (arg !== undefined) {
            this.host.jqxTooltip('showDelay', arg);
        }
        else {
            return this.host.jqxTooltip('showDelay');
        }
    };
    jqxTooltipComponent.prototype.showArrow = function (arg) {
        if (arg !== undefined) {
            this.host.jqxTooltip('showArrow', arg);
        }
        else {
            return this.host.jqxTooltip('showArrow');
        }
    };
    jqxTooltipComponent.prototype.top = function (arg) {
        if (arg !== undefined) {
            this.host.jqxTooltip('top', arg);
        }
        else {
            return this.host.jqxTooltip('top');
        }
    };
    jqxTooltipComponent.prototype.trigger = function (arg) {
        if (arg !== undefined) {
            this.host.jqxTooltip('trigger', arg);
        }
        else {
            return this.host.jqxTooltip('trigger');
        }
    };
    jqxTooltipComponent.prototype.theme = function (arg) {
        if (arg !== undefined) {
            this.host.jqxTooltip('theme', arg);
        }
        else {
            return this.host.jqxTooltip('theme');
        }
    };
    jqxTooltipComponent.prototype.width = function (arg) {
        if (arg !== undefined) {
            this.host.jqxTooltip('width', arg);
        }
        else {
            return this.host.jqxTooltip('width');
        }
    };
    // jqxTooltipComponent functions
    jqxTooltipComponent.prototype.close = function (index) {
        this.host.jqxTooltip('close', index);
    };
    jqxTooltipComponent.prototype.destroy = function () {
        this.host.jqxTooltip('destroy');
    };
    jqxTooltipComponent.prototype.open = function () {
        this.host.jqxTooltip('open');
    };
    jqxTooltipComponent.prototype.refresh = function () {
        this.host.jqxTooltip('refresh');
    };
    jqxTooltipComponent.prototype.__wireEvents__ = function () {
        var _this = this;
        this.host.on('close', function (eventData) { _this.onClose.emit(eventData); });
        this.host.on('closing', function (eventData) { _this.onClosing.emit(eventData); });
        this.host.on('open', function (eventData) { _this.onOpen.emit(eventData); });
        this.host.on('opening', function (eventData) { _this.onOpening.emit(eventData); });
    };
    return jqxTooltipComponent;
}()); //jqxTooltipComponent
__decorate([
    Input('absolutePositionX'),
    __metadata("design:type", Object)
], jqxTooltipComponent.prototype, "attrAbsolutePositionX", void 0);
__decorate([
    Input('absolutePositionY'),
    __metadata("design:type", Object)
], jqxTooltipComponent.prototype, "attrAbsolutePositionY", void 0);
__decorate([
    Input('autoHide'),
    __metadata("design:type", Object)
], jqxTooltipComponent.prototype, "attrAutoHide", void 0);
__decorate([
    Input('autoHideDelay'),
    __metadata("design:type", Object)
], jqxTooltipComponent.prototype, "attrAutoHideDelay", void 0);
__decorate([
    Input('animationShowDelay'),
    __metadata("design:type", Object)
], jqxTooltipComponent.prototype, "attrAnimationShowDelay", void 0);
__decorate([
    Input('animationHideDelay'),
    __metadata("design:type", Object)
], jqxTooltipComponent.prototype, "attrAnimationHideDelay", void 0);
__decorate([
    Input('content'),
    __metadata("design:type", Object)
], jqxTooltipComponent.prototype, "attrContent", void 0);
__decorate([
    Input('closeOnClick'),
    __metadata("design:type", Object)
], jqxTooltipComponent.prototype, "attrCloseOnClick", void 0);
__decorate([
    Input('disabled'),
    __metadata("design:type", Object)
], jqxTooltipComponent.prototype, "attrDisabled", void 0);
__decorate([
    Input('enableBrowserBoundsDetection'),
    __metadata("design:type", Object)
], jqxTooltipComponent.prototype, "attrEnableBrowserBoundsDetection", void 0);
__decorate([
    Input('left'),
    __metadata("design:type", Object)
], jqxTooltipComponent.prototype, "attrLeft", void 0);
__decorate([
    Input('name'),
    __metadata("design:type", Object)
], jqxTooltipComponent.prototype, "attrName", void 0);
__decorate([
    Input('opacity'),
    __metadata("design:type", Object)
], jqxTooltipComponent.prototype, "attrOpacity", void 0);
__decorate([
    Input('position'),
    __metadata("design:type", Object)
], jqxTooltipComponent.prototype, "attrPosition", void 0);
__decorate([
    Input('rtl'),
    __metadata("design:type", Object)
], jqxTooltipComponent.prototype, "attrRtl", void 0);
__decorate([
    Input('showDelay'),
    __metadata("design:type", Object)
], jqxTooltipComponent.prototype, "attrShowDelay", void 0);
__decorate([
    Input('showArrow'),
    __metadata("design:type", Object)
], jqxTooltipComponent.prototype, "attrShowArrow", void 0);
__decorate([
    Input('top'),
    __metadata("design:type", Object)
], jqxTooltipComponent.prototype, "attrTop", void 0);
__decorate([
    Input('trigger'),
    __metadata("design:type", Object)
], jqxTooltipComponent.prototype, "attrTrigger", void 0);
__decorate([
    Input('theme'),
    __metadata("design:type", Object)
], jqxTooltipComponent.prototype, "attrTheme", void 0);
__decorate([
    Input('width'),
    __metadata("design:type", Object)
], jqxTooltipComponent.prototype, "attrWidth", void 0);
__decorate([
    Input('height'),
    __metadata("design:type", Object)
], jqxTooltipComponent.prototype, "attrHeight", void 0);
__decorate([
    Input('auto-create'),
    __metadata("design:type", Boolean)
], jqxTooltipComponent.prototype, "autoCreate", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxTooltipComponent.prototype, "onClose", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxTooltipComponent.prototype, "onClosing", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxTooltipComponent.prototype, "onOpen", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxTooltipComponent.prototype, "onOpening", void 0);
jqxTooltipComponent = __decorate([
    Component({
        selector: 'jqxTooltip',
        template: '<div><ng-content></ng-content></div>'
    }),
    __metadata("design:paramtypes", [ElementRef])
], jqxTooltipComponent);
export { jqxTooltipComponent };
//# sourceMappingURL=angular_jqxtooltip.js.map