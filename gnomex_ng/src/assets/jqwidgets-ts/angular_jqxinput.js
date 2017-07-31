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
    useExisting: forwardRef(function () { return jqxInputComponent; }),
    multi: true
};
var jqxInputComponent = (function () {
    function jqxInputComponent(containerElement) {
        this.autoCreate = true;
        this.properties = ['disabled', 'dropDownWidth', 'displayMember', 'height', 'items', 'minLength', 'maxLength', 'opened', 'placeHolder', 'popupZIndex', 'query', 'renderer', 'rtl', 'searchMode', 'source', 'theme', 'valueMember', 'width', 'value'];
        this.onTouchedCallback = noop;
        this.onChangeCallback = noop;
        // jqxInputComponent events
        this.onChange = new EventEmitter();
        this.onClose = new EventEmitter();
        this.onOpen = new EventEmitter();
        this.onSelect = new EventEmitter();
        this.elementRef = containerElement;
    }
    jqxInputComponent.prototype.ngOnInit = function () {
        if (this.autoCreate) {
            this.createComponent();
        }
    };
    ;
    jqxInputComponent.prototype.ngOnChanges = function (changes) {
        if (this.host) {
            for (var i = 0; i < this.properties.length; i++) {
                var attrName = 'attr' + this.properties[i].substring(0, 1).toUpperCase() + this.properties[i].substring(1);
                var areEqual = void 0;
                if (this[attrName] !== undefined) {
                    if (typeof this[attrName] === 'object') {
                        if (this[attrName] instanceof Array) {
                            areEqual = this.arraysEqual(this[attrName], this.host.jqxInput(this.properties[i]));
                        }
                        if (areEqual) {
                            return false;
                        }
                        this.host.jqxInput(this.properties[i], this[attrName]);
                        continue;
                    }
                    if (this[attrName] !== this.host.jqxInput(this.properties[i])) {
                        this.host.jqxInput(this.properties[i], this[attrName]);
                    }
                }
            }
        }
    };
    jqxInputComponent.prototype.arraysEqual = function (attrValue, hostValue) {
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
    jqxInputComponent.prototype.manageAttributes = function () {
        var options = {};
        for (var i = 0; i < this.properties.length; i++) {
            var attrName = 'attr' + this.properties[i].substring(0, 1).toUpperCase() + this.properties[i].substring(1);
            if (this[attrName] !== undefined) {
                options[this.properties[i]] = this[attrName];
            }
        }
        return options;
    };
    jqxInputComponent.prototype.createComponent = function (options) {
        if (options) {
            JQXLite.extend(options, this.manageAttributes());
        }
        else {
            options = this.manageAttributes();
        }
        this.host = JQXLite(this.elementRef.nativeElement.firstChild);
        this.__wireEvents__();
        this.widgetObject = jqwidgets.createInstance(this.host, 'jqxInput', options);
        this.__updateRect__();
    };
    jqxInputComponent.prototype.createWidget = function (options) {
        this.createComponent(options);
    };
    jqxInputComponent.prototype.__updateRect__ = function () {
        this.host.css({ width: this.attrWidth, height: this.attrHeight });
    };
    Object.defineProperty(jqxInputComponent.prototype, "ngValue", {
        get: function () {
            if (this.widgetObject)
                return this.host.val();
            return '';
        },
        set: function (value) {
            if (this.widgetObject) {
                this.onChangeCallback(value);
            }
        },
        enumerable: true,
        configurable: true
    });
    jqxInputComponent.prototype.writeValue = function (value) {
        if (this.widgetObject) {
            this.host.jqxInput('val', value);
        }
    };
    jqxInputComponent.prototype.registerOnChange = function (fn) {
        this.onChangeCallback = fn;
    };
    jqxInputComponent.prototype.registerOnTouched = function (fn) {
        this.onTouchedCallback = fn;
    };
    jqxInputComponent.prototype.setOptions = function (options) {
        this.host.jqxInput('setOptions', options);
    };
    // jqxInputComponent properties
    jqxInputComponent.prototype.disabled = function (arg) {
        if (arg !== undefined) {
            this.host.jqxInput('disabled', arg);
        }
        else {
            return this.host.jqxInput('disabled');
        }
    };
    jqxInputComponent.prototype.dropDownWidth = function (arg) {
        if (arg !== undefined) {
            this.host.jqxInput('dropDownWidth', arg);
        }
        else {
            return this.host.jqxInput('dropDownWidth');
        }
    };
    jqxInputComponent.prototype.displayMember = function (arg) {
        if (arg !== undefined) {
            this.host.jqxInput('displayMember', arg);
        }
        else {
            return this.host.jqxInput('displayMember');
        }
    };
    jqxInputComponent.prototype.height = function (arg) {
        if (arg !== undefined) {
            this.host.jqxInput('height', arg);
        }
        else {
            return this.host.jqxInput('height');
        }
    };
    jqxInputComponent.prototype.items = function (arg) {
        if (arg !== undefined) {
            this.host.jqxInput('items', arg);
        }
        else {
            return this.host.jqxInput('items');
        }
    };
    jqxInputComponent.prototype.minLength = function (arg) {
        if (arg !== undefined) {
            this.host.jqxInput('minLength', arg);
        }
        else {
            return this.host.jqxInput('minLength');
        }
    };
    jqxInputComponent.prototype.maxLength = function (arg) {
        if (arg !== undefined) {
            this.host.jqxInput('maxLength', arg);
        }
        else {
            return this.host.jqxInput('maxLength');
        }
    };
    jqxInputComponent.prototype.opened = function (arg) {
        if (arg !== undefined) {
            this.host.jqxInput('opened', arg);
        }
        else {
            return this.host.jqxInput('opened');
        }
    };
    jqxInputComponent.prototype.placeHolder = function (arg) {
        if (arg !== undefined) {
            this.host.jqxInput('placeHolder', arg);
        }
        else {
            return this.host.jqxInput('placeHolder');
        }
    };
    jqxInputComponent.prototype.popupZIndex = function (arg) {
        if (arg !== undefined) {
            this.host.jqxInput('popupZIndex', arg);
        }
        else {
            return this.host.jqxInput('popupZIndex');
        }
    };
    jqxInputComponent.prototype.query = function (arg) {
        if (arg !== undefined) {
            this.host.jqxInput('query', arg);
        }
        else {
            return this.host.jqxInput('query');
        }
    };
    jqxInputComponent.prototype.renderer = function (arg) {
        if (arg !== undefined) {
            this.host.jqxInput('renderer', arg);
        }
        else {
            return this.host.jqxInput('renderer');
        }
    };
    jqxInputComponent.prototype.rtl = function (arg) {
        if (arg !== undefined) {
            this.host.jqxInput('rtl', arg);
        }
        else {
            return this.host.jqxInput('rtl');
        }
    };
    jqxInputComponent.prototype.searchMode = function (arg) {
        if (arg !== undefined) {
            this.host.jqxInput('searchMode', arg);
        }
        else {
            return this.host.jqxInput('searchMode');
        }
    };
    jqxInputComponent.prototype.source = function (arg) {
        if (arg !== undefined) {
            this.host.jqxInput('source', arg);
        }
        else {
            return this.host.jqxInput('source');
        }
    };
    jqxInputComponent.prototype.theme = function (arg) {
        if (arg !== undefined) {
            this.host.jqxInput('theme', arg);
        }
        else {
            return this.host.jqxInput('theme');
        }
    };
    jqxInputComponent.prototype.valueMember = function (arg) {
        if (arg !== undefined) {
            this.host.jqxInput('valueMember', arg);
        }
        else {
            return this.host.jqxInput('valueMember');
        }
    };
    jqxInputComponent.prototype.width = function (arg) {
        if (arg !== undefined) {
            this.host.jqxInput('width', arg);
        }
        else {
            return this.host.jqxInput('width');
        }
    };
    jqxInputComponent.prototype.value = function (arg) {
        if (arg !== undefined) {
            this.host.jqxInput('value', arg);
        }
        else {
            return this.host.jqxInput('value');
        }
    };
    // jqxInputComponent functions
    jqxInputComponent.prototype.destroy = function () {
        this.host.jqxInput('destroy');
    };
    jqxInputComponent.prototype.focus = function () {
        this.host.jqxInput('focus');
    };
    jqxInputComponent.prototype.selectAll = function () {
        this.host.jqxInput('selectAll');
    };
    jqxInputComponent.prototype.val = function (value) {
        if (value !== undefined) {
            this.host.jqxInput("val", value);
        }
        else {
            return this.host.jqxInput("val");
        }
    };
    ;
    jqxInputComponent.prototype.__wireEvents__ = function () {
        var _this = this;
        this.host.on('change', function (eventData) { _this.onChange.emit(eventData); });
        this.host.on('close', function (eventData) { _this.onClose.emit(eventData); });
        this.host.on('open', function (eventData) { _this.onOpen.emit(eventData); });
        this.host.on('select', function (eventData) { _this.onSelect.emit(eventData); if (eventData.args)
            _this.onChangeCallback(eventData.args.value); });
    };
    return jqxInputComponent;
}()); //jqxInputComponent
__decorate([
    Input('disabled'),
    __metadata("design:type", Object)
], jqxInputComponent.prototype, "attrDisabled", void 0);
__decorate([
    Input('dropDownWidth'),
    __metadata("design:type", Object)
], jqxInputComponent.prototype, "attrDropDownWidth", void 0);
__decorate([
    Input('displayMember'),
    __metadata("design:type", Object)
], jqxInputComponent.prototype, "attrDisplayMember", void 0);
__decorate([
    Input('items'),
    __metadata("design:type", Object)
], jqxInputComponent.prototype, "attrItems", void 0);
__decorate([
    Input('minLength'),
    __metadata("design:type", Object)
], jqxInputComponent.prototype, "attrMinLength", void 0);
__decorate([
    Input('maxLength'),
    __metadata("design:type", Object)
], jqxInputComponent.prototype, "attrMaxLength", void 0);
__decorate([
    Input('opened'),
    __metadata("design:type", Object)
], jqxInputComponent.prototype, "attrOpened", void 0);
__decorate([
    Input('placeHolder'),
    __metadata("design:type", Object)
], jqxInputComponent.prototype, "attrPlaceHolder", void 0);
__decorate([
    Input('popupZIndex'),
    __metadata("design:type", Object)
], jqxInputComponent.prototype, "attrPopupZIndex", void 0);
__decorate([
    Input('query'),
    __metadata("design:type", Object)
], jqxInputComponent.prototype, "attrQuery", void 0);
__decorate([
    Input('renderer'),
    __metadata("design:type", Object)
], jqxInputComponent.prototype, "attrRenderer", void 0);
__decorate([
    Input('rtl'),
    __metadata("design:type", Object)
], jqxInputComponent.prototype, "attrRtl", void 0);
__decorate([
    Input('searchMode'),
    __metadata("design:type", Object)
], jqxInputComponent.prototype, "attrSearchMode", void 0);
__decorate([
    Input('source'),
    __metadata("design:type", Object)
], jqxInputComponent.prototype, "attrSource", void 0);
__decorate([
    Input('theme'),
    __metadata("design:type", Object)
], jqxInputComponent.prototype, "attrTheme", void 0);
__decorate([
    Input('valueMember'),
    __metadata("design:type", Object)
], jqxInputComponent.prototype, "attrValueMember", void 0);
__decorate([
    Input('value'),
    __metadata("design:type", Object)
], jqxInputComponent.prototype, "attrValue", void 0);
__decorate([
    Input('width'),
    __metadata("design:type", Object)
], jqxInputComponent.prototype, "attrWidth", void 0);
__decorate([
    Input('height'),
    __metadata("design:type", Object)
], jqxInputComponent.prototype, "attrHeight", void 0);
__decorate([
    Input('auto-create'),
    __metadata("design:type", Boolean)
], jqxInputComponent.prototype, "autoCreate", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxInputComponent.prototype, "onChange", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxInputComponent.prototype, "onClose", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxInputComponent.prototype, "onOpen", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxInputComponent.prototype, "onSelect", void 0);
jqxInputComponent = __decorate([
    Component({
        selector: 'jqxInput',
        template: '<input type="text" [(ngModel)]="ngValue">',
        providers: [CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR],
        changeDetection: ChangeDetectionStrategy.OnPush
    }),
    __metadata("design:paramtypes", [ElementRef])
], jqxInputComponent);
export { jqxInputComponent };
//# sourceMappingURL=angular_jqxinput.js.map