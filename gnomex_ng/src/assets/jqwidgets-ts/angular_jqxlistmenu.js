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
import { Component, Input, ElementRef } from '@angular/core';
var jqxListMenuComponent = (function () {
    function jqxListMenuComponent(containerElement) {
        this.autoCreate = true;
        this.properties = ['alwaysShowNavigationArrows', 'animationType', 'animationDuration', 'autoSeparators', 'backLabel', 'disabled', 'enableScrolling', 'filterCallback', 'height', 'headerAnimationDuration', 'placeHolder', 'readOnly', 'rtl', 'roundedCorners', 'showNavigationArrows', 'showFilter', 'showHeader', 'showBackButton', 'theme', 'width'];
        this.elementRef = containerElement;
    }
    jqxListMenuComponent.prototype.ngOnInit = function () {
        if (this.autoCreate) {
            this.createComponent();
        }
    };
    ;
    jqxListMenuComponent.prototype.ngOnChanges = function (changes) {
        if (this.host) {
            for (var i = 0; i < this.properties.length; i++) {
                var attrName = 'attr' + this.properties[i].substring(0, 1).toUpperCase() + this.properties[i].substring(1);
                var areEqual = void 0;
                if (this[attrName] !== undefined) {
                    if (typeof this[attrName] === 'object') {
                        if (this[attrName] instanceof Array) {
                            areEqual = this.arraysEqual(this[attrName], this.host.jqxListMenu(this.properties[i]));
                        }
                        if (areEqual) {
                            return false;
                        }
                        this.host.jqxListMenu(this.properties[i], this[attrName]);
                        continue;
                    }
                    if (this[attrName] !== this.host.jqxListMenu(this.properties[i])) {
                        this.host.jqxListMenu(this.properties[i], this[attrName]);
                    }
                }
            }
        }
    };
    jqxListMenuComponent.prototype.arraysEqual = function (attrValue, hostValue) {
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
    jqxListMenuComponent.prototype.manageAttributes = function () {
        var options = {};
        for (var i = 0; i < this.properties.length; i++) {
            var attrName = 'attr' + this.properties[i].substring(0, 1).toUpperCase() + this.properties[i].substring(1);
            if (this[attrName] !== undefined) {
                options[this.properties[i]] = this[attrName];
            }
        }
        return options;
    };
    jqxListMenuComponent.prototype.createComponent = function (options) {
        if (options) {
            JQXLite.extend(options, this.manageAttributes());
        }
        else {
            options = this.manageAttributes();
        }
        this.host = JQXLite(this.elementRef.nativeElement.firstChild);
        this.__wireEvents__();
        this.widgetObject = jqwidgets.createInstance(this.host, 'jqxListMenu', options);
        this.__updateRect__();
    };
    jqxListMenuComponent.prototype.createWidget = function (options) {
        this.createComponent(options);
    };
    jqxListMenuComponent.prototype.__updateRect__ = function () {
        this.host.css({ width: this.attrWidth, height: this.attrHeight });
    };
    jqxListMenuComponent.prototype.setOptions = function (options) {
        this.host.jqxListMenu('setOptions', options);
    };
    // jqxListMenuComponent properties
    jqxListMenuComponent.prototype.alwaysShowNavigationArrows = function (arg) {
        if (arg !== undefined) {
            this.host.jqxListMenu('alwaysShowNavigationArrows', arg);
        }
        else {
            return this.host.jqxListMenu('alwaysShowNavigationArrows');
        }
    };
    jqxListMenuComponent.prototype.animationType = function (arg) {
        if (arg !== undefined) {
            this.host.jqxListMenu('animationType', arg);
        }
        else {
            return this.host.jqxListMenu('animationType');
        }
    };
    jqxListMenuComponent.prototype.animationDuration = function (arg) {
        if (arg !== undefined) {
            this.host.jqxListMenu('animationDuration', arg);
        }
        else {
            return this.host.jqxListMenu('animationDuration');
        }
    };
    jqxListMenuComponent.prototype.autoSeparators = function (arg) {
        if (arg !== undefined) {
            this.host.jqxListMenu('autoSeparators', arg);
        }
        else {
            return this.host.jqxListMenu('autoSeparators');
        }
    };
    jqxListMenuComponent.prototype.backLabel = function (arg) {
        if (arg !== undefined) {
            this.host.jqxListMenu('backLabel', arg);
        }
        else {
            return this.host.jqxListMenu('backLabel');
        }
    };
    jqxListMenuComponent.prototype.disabled = function (arg) {
        if (arg !== undefined) {
            this.host.jqxListMenu('disabled', arg);
        }
        else {
            return this.host.jqxListMenu('disabled');
        }
    };
    jqxListMenuComponent.prototype.enableScrolling = function (arg) {
        if (arg !== undefined) {
            this.host.jqxListMenu('enableScrolling', arg);
        }
        else {
            return this.host.jqxListMenu('enableScrolling');
        }
    };
    jqxListMenuComponent.prototype.filterCallback = function (arg) {
        if (arg !== undefined) {
            this.host.jqxListMenu('filterCallback', arg);
        }
        else {
            return this.host.jqxListMenu('filterCallback');
        }
    };
    jqxListMenuComponent.prototype.height = function (arg) {
        if (arg !== undefined) {
            this.host.jqxListMenu('height', arg);
        }
        else {
            return this.host.jqxListMenu('height');
        }
    };
    jqxListMenuComponent.prototype.headerAnimationDuration = function (arg) {
        if (arg !== undefined) {
            this.host.jqxListMenu('headerAnimationDuration', arg);
        }
        else {
            return this.host.jqxListMenu('headerAnimationDuration');
        }
    };
    jqxListMenuComponent.prototype.placeHolder = function (arg) {
        if (arg !== undefined) {
            this.host.jqxListMenu('placeHolder', arg);
        }
        else {
            return this.host.jqxListMenu('placeHolder');
        }
    };
    jqxListMenuComponent.prototype.readOnly = function (arg) {
        if (arg !== undefined) {
            this.host.jqxListMenu('readOnly', arg);
        }
        else {
            return this.host.jqxListMenu('readOnly');
        }
    };
    jqxListMenuComponent.prototype.rtl = function (arg) {
        if (arg !== undefined) {
            this.host.jqxListMenu('rtl', arg);
        }
        else {
            return this.host.jqxListMenu('rtl');
        }
    };
    jqxListMenuComponent.prototype.roundedCorners = function (arg) {
        if (arg !== undefined) {
            this.host.jqxListMenu('roundedCorners', arg);
        }
        else {
            return this.host.jqxListMenu('roundedCorners');
        }
    };
    jqxListMenuComponent.prototype.showNavigationArrows = function (arg) {
        if (arg !== undefined) {
            this.host.jqxListMenu('showNavigationArrows', arg);
        }
        else {
            return this.host.jqxListMenu('showNavigationArrows');
        }
    };
    jqxListMenuComponent.prototype.showFilter = function (arg) {
        if (arg !== undefined) {
            this.host.jqxListMenu('showFilter', arg);
        }
        else {
            return this.host.jqxListMenu('showFilter');
        }
    };
    jqxListMenuComponent.prototype.showHeader = function (arg) {
        if (arg !== undefined) {
            this.host.jqxListMenu('showHeader', arg);
        }
        else {
            return this.host.jqxListMenu('showHeader');
        }
    };
    jqxListMenuComponent.prototype.showBackButton = function (arg) {
        if (arg !== undefined) {
            this.host.jqxListMenu('showBackButton', arg);
        }
        else {
            return this.host.jqxListMenu('showBackButton');
        }
    };
    jqxListMenuComponent.prototype.theme = function (arg) {
        if (arg !== undefined) {
            this.host.jqxListMenu('theme', arg);
        }
        else {
            return this.host.jqxListMenu('theme');
        }
    };
    jqxListMenuComponent.prototype.width = function (arg) {
        if (arg !== undefined) {
            this.host.jqxListMenu('width', arg);
        }
        else {
            return this.host.jqxListMenu('width');
        }
    };
    // jqxListMenuComponent functions
    jqxListMenuComponent.prototype.back = function () {
        this.host.jqxListMenu('back');
    };
    jqxListMenuComponent.prototype.changePage = function (Item) {
        this.host.jqxListMenu('changePage', Item);
    };
    jqxListMenuComponent.prototype.destroy = function () {
        this.host.jqxListMenu('destroy');
    };
    // jqxListMenuComponent events
    jqxListMenuComponent.prototype.__wireEvents__ = function () {
    };
    return jqxListMenuComponent;
}()); //jqxListMenuComponent
__decorate([
    Input('alwaysShowNavigationArrows'),
    __metadata("design:type", Object)
], jqxListMenuComponent.prototype, "attrAlwaysShowNavigationArrows", void 0);
__decorate([
    Input('animationType'),
    __metadata("design:type", Object)
], jqxListMenuComponent.prototype, "attrAnimationType", void 0);
__decorate([
    Input('animationDuration'),
    __metadata("design:type", Object)
], jqxListMenuComponent.prototype, "attrAnimationDuration", void 0);
__decorate([
    Input('autoSeparators'),
    __metadata("design:type", Object)
], jqxListMenuComponent.prototype, "attrAutoSeparators", void 0);
__decorate([
    Input('backLabel'),
    __metadata("design:type", Object)
], jqxListMenuComponent.prototype, "attrBackLabel", void 0);
__decorate([
    Input('disabled'),
    __metadata("design:type", Object)
], jqxListMenuComponent.prototype, "attrDisabled", void 0);
__decorate([
    Input('enableScrolling'),
    __metadata("design:type", Object)
], jqxListMenuComponent.prototype, "attrEnableScrolling", void 0);
__decorate([
    Input('filterCallback'),
    __metadata("design:type", Object)
], jqxListMenuComponent.prototype, "attrFilterCallback", void 0);
__decorate([
    Input('headerAnimationDuration'),
    __metadata("design:type", Object)
], jqxListMenuComponent.prototype, "attrHeaderAnimationDuration", void 0);
__decorate([
    Input('placeHolder'),
    __metadata("design:type", Object)
], jqxListMenuComponent.prototype, "attrPlaceHolder", void 0);
__decorate([
    Input('readOnly'),
    __metadata("design:type", Object)
], jqxListMenuComponent.prototype, "attrReadOnly", void 0);
__decorate([
    Input('rtl'),
    __metadata("design:type", Object)
], jqxListMenuComponent.prototype, "attrRtl", void 0);
__decorate([
    Input('roundedCorners'),
    __metadata("design:type", Object)
], jqxListMenuComponent.prototype, "attrRoundedCorners", void 0);
__decorate([
    Input('showNavigationArrows'),
    __metadata("design:type", Object)
], jqxListMenuComponent.prototype, "attrShowNavigationArrows", void 0);
__decorate([
    Input('showFilter'),
    __metadata("design:type", Object)
], jqxListMenuComponent.prototype, "attrShowFilter", void 0);
__decorate([
    Input('showHeader'),
    __metadata("design:type", Object)
], jqxListMenuComponent.prototype, "attrShowHeader", void 0);
__decorate([
    Input('showBackButton'),
    __metadata("design:type", Object)
], jqxListMenuComponent.prototype, "attrShowBackButton", void 0);
__decorate([
    Input('theme'),
    __metadata("design:type", Object)
], jqxListMenuComponent.prototype, "attrTheme", void 0);
__decorate([
    Input('width'),
    __metadata("design:type", Object)
], jqxListMenuComponent.prototype, "attrWidth", void 0);
__decorate([
    Input('height'),
    __metadata("design:type", Object)
], jqxListMenuComponent.prototype, "attrHeight", void 0);
__decorate([
    Input('auto-create'),
    __metadata("design:type", Boolean)
], jqxListMenuComponent.prototype, "autoCreate", void 0);
jqxListMenuComponent = __decorate([
    Component({
        selector: 'jqxListMenu',
        template: '<div><ng-content></ng-content></div>'
    }),
    __metadata("design:paramtypes", [ElementRef])
], jqxListMenuComponent);
export { jqxListMenuComponent };
//# sourceMappingURL=angular_jqxlistmenu.js.map