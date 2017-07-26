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
    useExisting: forwardRef(function () { return jqxComboBoxComponent; }),
    multi: true
};
var jqxComboBoxComponent = (function () {
    function jqxComboBoxComponent(containerElement) {
        this.autoCreate = true;
        this.properties = ['animationType', 'autoComplete', 'autoOpen', 'autoItemsHeight', 'autoDropDownHeight', 'closeDelay', 'checkboxes', 'disabled', 'displayMember', 'dropDownHorizontalAlignment', 'dropDownVerticalAlignment', 'dropDownHeight', 'dropDownWidth', 'enableHover', 'enableSelection', 'enableBrowserBoundsDetection', 'height', 'itemHeight', 'multiSelect', 'minLength', 'openDelay', 'popupZIndex', 'placeHolder', 'remoteAutoComplete', 'remoteAutoCompleteDelay', 'renderer', 'renderSelectedItem', 'rtl', 'selectedIndex', 'showArrow', 'showCloseButtons', 'searchMode', 'search', 'source', 'scrollBarSize', 'template', 'theme', 'validateSelection', 'valueMember', 'width'];
        this.onTouchedCallback = noop;
        this.onChangeCallback = noop;
        // jqxComboBoxComponent events
        this.onBindingComplete = new EventEmitter();
        this.onCheckChange = new EventEmitter();
        this.onClose = new EventEmitter();
        this.onChange = new EventEmitter();
        this.onOpen = new EventEmitter();
        this.onSelect = new EventEmitter();
        this.onUnselect = new EventEmitter();
        this.elementRef = containerElement;
    }
    jqxComboBoxComponent.prototype.ngOnInit = function () {
        if (this.autoCreate) {
            this.createComponent();
        }
    };
    ;
    jqxComboBoxComponent.prototype.ngOnChanges = function (changes) {
        if (this.host) {
            for (var i = 0; i < this.properties.length; i++) {
                var attrName = 'attr' + this.properties[i].substring(0, 1).toUpperCase() + this.properties[i].substring(1);
                var areEqual = void 0;
                if (this[attrName] !== undefined) {
                    if (typeof this[attrName] === 'object') {
                        if (this[attrName] instanceof Array) {
                            areEqual = this.arraysEqual(this[attrName], this.host.jqxComboBox(this.properties[i]));
                        }
                        if (areEqual) {
                            return false;
                        }
                        this.host.jqxComboBox(this.properties[i], this[attrName]);
                        continue;
                    }
                    if (this[attrName] !== this.host.jqxComboBox(this.properties[i])) {
                        this.host.jqxComboBox(this.properties[i], this[attrName]);
                    }
                }
            }
        }
    };
    jqxComboBoxComponent.prototype.arraysEqual = function (attrValue, hostValue) {
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
    jqxComboBoxComponent.prototype.manageAttributes = function () {
        var options = {};
        for (var i = 0; i < this.properties.length; i++) {
            var attrName = 'attr' + this.properties[i].substring(0, 1).toUpperCase() + this.properties[i].substring(1);
            if (this[attrName] !== undefined) {
                options[this.properties[i]] = this[attrName];
            }
        }
        return options;
    };
    jqxComboBoxComponent.prototype.createComponent = function (options) {
        if (options) {
            JQXLite.extend(options, this.manageAttributes());
        }
        else {
            options = this.manageAttributes();
        }
        this.host = JQXLite(this.elementRef.nativeElement.firstChild);
        this.__wireEvents__();
        this.widgetObject = jqwidgets.createInstance(this.host, 'jqxComboBox', options);
        this.__updateRect__();
    };
    jqxComboBoxComponent.prototype.createWidget = function (options) {
        this.createComponent(options);
    };
    jqxComboBoxComponent.prototype.__updateRect__ = function () {
        this.host.css({ width: this.attrWidth, height: this.attrHeight });
    };
    jqxComboBoxComponent.prototype.writeValue = function (value) {
        if (this.widgetObject) {
            this.onChangeCallback(this.host.val());
        }
    };
    jqxComboBoxComponent.prototype.registerOnChange = function (fn) {
        this.onChangeCallback = fn;
    };
    jqxComboBoxComponent.prototype.registerOnTouched = function (fn) {
        this.onTouchedCallback = fn;
    };
    jqxComboBoxComponent.prototype.setOptions = function (options) {
        this.host.jqxComboBox('setOptions', options);
    };
    // jqxComboBoxComponent properties
    jqxComboBoxComponent.prototype.animationType = function (arg) {
        if (arg !== undefined) {
            this.host.jqxComboBox('animationType', arg);
        }
        else {
            return this.host.jqxComboBox('animationType');
        }
    };
    jqxComboBoxComponent.prototype.autoComplete = function (arg) {
        if (arg !== undefined) {
            this.host.jqxComboBox('autoComplete', arg);
        }
        else {
            return this.host.jqxComboBox('autoComplete');
        }
    };
    jqxComboBoxComponent.prototype.autoOpen = function (arg) {
        if (arg !== undefined) {
            this.host.jqxComboBox('autoOpen', arg);
        }
        else {
            return this.host.jqxComboBox('autoOpen');
        }
    };
    jqxComboBoxComponent.prototype.autoItemsHeight = function (arg) {
        if (arg !== undefined) {
            this.host.jqxComboBox('autoItemsHeight', arg);
        }
        else {
            return this.host.jqxComboBox('autoItemsHeight');
        }
    };
    jqxComboBoxComponent.prototype.autoDropDownHeight = function (arg) {
        if (arg !== undefined) {
            this.host.jqxComboBox('autoDropDownHeight', arg);
        }
        else {
            return this.host.jqxComboBox('autoDropDownHeight');
        }
    };
    jqxComboBoxComponent.prototype.closeDelay = function (arg) {
        if (arg !== undefined) {
            this.host.jqxComboBox('closeDelay', arg);
        }
        else {
            return this.host.jqxComboBox('closeDelay');
        }
    };
    jqxComboBoxComponent.prototype.checkboxes = function (arg) {
        if (arg !== undefined) {
            this.host.jqxComboBox('checkboxes', arg);
        }
        else {
            return this.host.jqxComboBox('checkboxes');
        }
    };
    jqxComboBoxComponent.prototype.disabled = function (arg) {
        if (arg !== undefined) {
            this.host.jqxComboBox('disabled', arg);
        }
        else {
            return this.host.jqxComboBox('disabled');
        }
    };
    jqxComboBoxComponent.prototype.displayMember = function (arg) {
        if (arg !== undefined) {
            this.host.jqxComboBox('displayMember', arg);
        }
        else {
            return this.host.jqxComboBox('displayMember');
        }
    };
    jqxComboBoxComponent.prototype.dropDownHorizontalAlignment = function (arg) {
        if (arg !== undefined) {
            this.host.jqxComboBox('dropDownHorizontalAlignment', arg);
        }
        else {
            return this.host.jqxComboBox('dropDownHorizontalAlignment');
        }
    };
    jqxComboBoxComponent.prototype.dropDownVerticalAlignment = function (arg) {
        if (arg !== undefined) {
            this.host.jqxComboBox('dropDownVerticalAlignment', arg);
        }
        else {
            return this.host.jqxComboBox('dropDownVerticalAlignment');
        }
    };
    jqxComboBoxComponent.prototype.dropDownHeight = function (arg) {
        if (arg !== undefined) {
            this.host.jqxComboBox('dropDownHeight', arg);
        }
        else {
            return this.host.jqxComboBox('dropDownHeight');
        }
    };
    jqxComboBoxComponent.prototype.dropDownWidth = function (arg) {
        if (arg !== undefined) {
            this.host.jqxComboBox('dropDownWidth', arg);
        }
        else {
            return this.host.jqxComboBox('dropDownWidth');
        }
    };
    jqxComboBoxComponent.prototype.enableHover = function (arg) {
        if (arg !== undefined) {
            this.host.jqxComboBox('enableHover', arg);
        }
        else {
            return this.host.jqxComboBox('enableHover');
        }
    };
    jqxComboBoxComponent.prototype.enableSelection = function (arg) {
        if (arg !== undefined) {
            this.host.jqxComboBox('enableSelection', arg);
        }
        else {
            return this.host.jqxComboBox('enableSelection');
        }
    };
    jqxComboBoxComponent.prototype.enableBrowserBoundsDetection = function (arg) {
        if (arg !== undefined) {
            this.host.jqxComboBox('enableBrowserBoundsDetection', arg);
        }
        else {
            return this.host.jqxComboBox('enableBrowserBoundsDetection');
        }
    };
    jqxComboBoxComponent.prototype.height = function (arg) {
        if (arg !== undefined) {
            this.host.jqxComboBox('height', arg);
        }
        else {
            return this.host.jqxComboBox('height');
        }
    };
    jqxComboBoxComponent.prototype.itemHeight = function (arg) {
        if (arg !== undefined) {
            this.host.jqxComboBox('itemHeight', arg);
        }
        else {
            return this.host.jqxComboBox('itemHeight');
        }
    };
    jqxComboBoxComponent.prototype.multiSelect = function (arg) {
        if (arg !== undefined) {
            this.host.jqxComboBox('multiSelect', arg);
        }
        else {
            return this.host.jqxComboBox('multiSelect');
        }
    };
    jqxComboBoxComponent.prototype.minLength = function (arg) {
        if (arg !== undefined) {
            this.host.jqxComboBox('minLength', arg);
        }
        else {
            return this.host.jqxComboBox('minLength');
        }
    };
    jqxComboBoxComponent.prototype.openDelay = function (arg) {
        if (arg !== undefined) {
            this.host.jqxComboBox('openDelay', arg);
        }
        else {
            return this.host.jqxComboBox('openDelay');
        }
    };
    jqxComboBoxComponent.prototype.popupZIndex = function (arg) {
        if (arg !== undefined) {
            this.host.jqxComboBox('popupZIndex', arg);
        }
        else {
            return this.host.jqxComboBox('popupZIndex');
        }
    };
    jqxComboBoxComponent.prototype.placeHolder = function (arg) {
        if (arg !== undefined) {
            this.host.jqxComboBox('placeHolder', arg);
        }
        else {
            return this.host.jqxComboBox('placeHolder');
        }
    };
    jqxComboBoxComponent.prototype.remoteAutoComplete = function (arg) {
        if (arg !== undefined) {
            this.host.jqxComboBox('remoteAutoComplete', arg);
        }
        else {
            return this.host.jqxComboBox('remoteAutoComplete');
        }
    };
    jqxComboBoxComponent.prototype.remoteAutoCompleteDelay = function (arg) {
        if (arg !== undefined) {
            this.host.jqxComboBox('remoteAutoCompleteDelay', arg);
        }
        else {
            return this.host.jqxComboBox('remoteAutoCompleteDelay');
        }
    };
    jqxComboBoxComponent.prototype.renderer = function (arg) {
        if (arg !== undefined) {
            this.host.jqxComboBox('renderer', arg);
        }
        else {
            return this.host.jqxComboBox('renderer');
        }
    };
    jqxComboBoxComponent.prototype.renderSelectedItem = function (arg) {
        if (arg !== undefined) {
            this.host.jqxComboBox('renderSelectedItem', arg);
        }
        else {
            return this.host.jqxComboBox('renderSelectedItem');
        }
    };
    jqxComboBoxComponent.prototype.rtl = function (arg) {
        if (arg !== undefined) {
            this.host.jqxComboBox('rtl', arg);
        }
        else {
            return this.host.jqxComboBox('rtl');
        }
    };
    jqxComboBoxComponent.prototype.selectedIndex = function (arg) {
        if (arg !== undefined) {
            this.host.jqxComboBox('selectedIndex', arg);
        }
        else {
            return this.host.jqxComboBox('selectedIndex');
        }
    };
    jqxComboBoxComponent.prototype.showArrow = function (arg) {
        if (arg !== undefined) {
            this.host.jqxComboBox('showArrow', arg);
        }
        else {
            return this.host.jqxComboBox('showArrow');
        }
    };
    jqxComboBoxComponent.prototype.showCloseButtons = function (arg) {
        if (arg !== undefined) {
            this.host.jqxComboBox('showCloseButtons', arg);
        }
        else {
            return this.host.jqxComboBox('showCloseButtons');
        }
    };
    jqxComboBoxComponent.prototype.searchMode = function (arg) {
        if (arg !== undefined) {
            this.host.jqxComboBox('searchMode', arg);
        }
        else {
            return this.host.jqxComboBox('searchMode');
        }
    };
    jqxComboBoxComponent.prototype.search = function (arg) {
        if (arg !== undefined) {
            this.host.jqxComboBox('search', arg);
        }
        else {
            return this.host.jqxComboBox('search');
        }
    };
    jqxComboBoxComponent.prototype.source = function (arg) {
        if (arg !== undefined) {
            this.host.jqxComboBox('source', arg);
        }
        else {
            return this.host.jqxComboBox('source');
        }
    };
    jqxComboBoxComponent.prototype.scrollBarSize = function (arg) {
        if (arg !== undefined) {
            this.host.jqxComboBox('scrollBarSize', arg);
        }
        else {
            return this.host.jqxComboBox('scrollBarSize');
        }
    };
    jqxComboBoxComponent.prototype.template = function (arg) {
        if (arg !== undefined) {
            this.host.jqxComboBox('template', arg);
        }
        else {
            return this.host.jqxComboBox('template');
        }
    };
    jqxComboBoxComponent.prototype.theme = function (arg) {
        if (arg !== undefined) {
            this.host.jqxComboBox('theme', arg);
        }
        else {
            return this.host.jqxComboBox('theme');
        }
    };
    jqxComboBoxComponent.prototype.validateSelection = function (arg) {
        if (arg !== undefined) {
            this.host.jqxComboBox('validateSelection', arg);
        }
        else {
            return this.host.jqxComboBox('validateSelection');
        }
    };
    jqxComboBoxComponent.prototype.valueMember = function (arg) {
        if (arg !== undefined) {
            this.host.jqxComboBox('valueMember', arg);
        }
        else {
            return this.host.jqxComboBox('valueMember');
        }
    };
    jqxComboBoxComponent.prototype.width = function (arg) {
        if (arg !== undefined) {
            this.host.jqxComboBox('width', arg);
        }
        else {
            return this.host.jqxComboBox('width');
        }
    };
    // jqxComboBoxComponent functions
    jqxComboBoxComponent.prototype.addItem = function (item) {
        return this.host.jqxComboBox('addItem', item);
    };
    jqxComboBoxComponent.prototype.clearSelection = function () {
        this.host.jqxComboBox('clearSelection');
    };
    jqxComboBoxComponent.prototype.clear = function () {
        this.host.jqxComboBox('clear');
    };
    jqxComboBoxComponent.prototype.close = function () {
        this.host.jqxComboBox('close');
    };
    jqxComboBoxComponent.prototype.checkIndex = function (index) {
        this.host.jqxComboBox('checkIndex', index);
    };
    jqxComboBoxComponent.prototype.checkItem = function (item) {
        this.host.jqxComboBox('checkItem', item);
    };
    jqxComboBoxComponent.prototype.checkAll = function () {
        this.host.jqxComboBox('checkAll');
    };
    jqxComboBoxComponent.prototype.destroy = function () {
        this.host.jqxComboBox('destroy');
    };
    jqxComboBoxComponent.prototype.disableItem = function (item) {
        this.host.jqxComboBox('disableItem', item);
    };
    jqxComboBoxComponent.prototype.disableAt = function (index) {
        this.host.jqxComboBox('disableAt', index);
    };
    jqxComboBoxComponent.prototype.enableItem = function (item) {
        this.host.jqxComboBox('enableItem', item);
    };
    jqxComboBoxComponent.prototype.enableAt = function (index) {
        this.host.jqxComboBox('enableAt', index);
    };
    jqxComboBoxComponent.prototype.ensureVisible = function (index) {
        this.host.jqxComboBox('ensureVisible', index);
    };
    jqxComboBoxComponent.prototype.focus = function () {
        this.host.jqxComboBox('focus');
    };
    jqxComboBoxComponent.prototype.getItem = function (index) {
        return this.host.jqxComboBox('getItem', index);
    };
    jqxComboBoxComponent.prototype.getItemByValue = function (value) {
        return this.host.jqxComboBox('getItemByValue', value);
    };
    jqxComboBoxComponent.prototype.getVisibleItems = function () {
        return this.host.jqxComboBox('getVisibleItems');
    };
    jqxComboBoxComponent.prototype.getItems = function () {
        return this.host.jqxComboBox('getItems');
    };
    jqxComboBoxComponent.prototype.getCheckedItems = function () {
        return this.host.jqxComboBox('getCheckedItems');
    };
    jqxComboBoxComponent.prototype.getSelectedItem = function () {
        return this.host.jqxComboBox('getSelectedItem');
    };
    jqxComboBoxComponent.prototype.getSelectedItems = function () {
        return this.host.jqxComboBox('getSelectedItems');
    };
    jqxComboBoxComponent.prototype.getSelectedIndex = function () {
        return this.host.jqxComboBox('getSelectedIndex');
    };
    jqxComboBoxComponent.prototype.insertAt = function (item, index) {
        return this.host.jqxComboBox('insertAt', item, index);
    };
    jqxComboBoxComponent.prototype.isOpened = function () {
        return this.host.jqxComboBox('isOpened');
    };
    jqxComboBoxComponent.prototype.indeterminateIndex = function (index) {
        this.host.jqxComboBox('indeterminateIndex', index);
    };
    jqxComboBoxComponent.prototype.indeterminateItem = function (item) {
        this.host.jqxComboBox('indeterminateItem', item);
    };
    jqxComboBoxComponent.prototype.loadFromSelect = function (selectTagId) {
        this.host.jqxComboBox('loadFromSelect', selectTagId);
    };
    jqxComboBoxComponent.prototype.open = function () {
        this.host.jqxComboBox('open');
    };
    jqxComboBoxComponent.prototype.removeItem = function (item) {
        return this.host.jqxComboBox('removeItem', item);
    };
    jqxComboBoxComponent.prototype.removeAt = function (index) {
        return this.host.jqxComboBox('removeAt', index);
    };
    jqxComboBoxComponent.prototype.selectIndex = function (index) {
        this.host.jqxComboBox('selectIndex', index);
    };
    jqxComboBoxComponent.prototype.selectItem = function (item) {
        this.host.jqxComboBox('selectItem', item);
    };
    jqxComboBoxComponent.prototype.searchString = function () {
        return this.host.jqxComboBox('searchString');
    };
    jqxComboBoxComponent.prototype.updateItem = function (item, itemValue) {
        this.host.jqxComboBox('updateItem', item, itemValue);
    };
    jqxComboBoxComponent.prototype.updateAt = function (item, index) {
        this.host.jqxComboBox('updateAt', item, index);
    };
    jqxComboBoxComponent.prototype.unselectIndex = function (index) {
        this.host.jqxComboBox('unselectIndex', index);
    };
    jqxComboBoxComponent.prototype.unselectItem = function (item) {
        this.host.jqxComboBox('unselectItem', item);
    };
    jqxComboBoxComponent.prototype.uncheckIndex = function (index) {
        this.host.jqxComboBox('uncheckIndex', index);
    };
    jqxComboBoxComponent.prototype.uncheckItem = function (item) {
        this.host.jqxComboBox('uncheckItem', item);
    };
    jqxComboBoxComponent.prototype.uncheckAll = function () {
        this.host.jqxComboBox('uncheckAll');
    };
    jqxComboBoxComponent.prototype.val = function (value) {
        if (value !== undefined) {
            this.host.jqxComboBox("val", value);
        }
        else {
            return this.host.jqxComboBox("val");
        }
    };
    ;
    jqxComboBoxComponent.prototype.__wireEvents__ = function () {
        var _this = this;
        this.host.on('bindingComplete', function (eventData) { _this.onBindingComplete.emit(eventData); });
        this.host.on('checkChange', function (eventData) { _this.onCheckChange.emit(eventData); });
        this.host.on('close', function (eventData) { _this.onClose.emit(eventData); });
        this.host.on('change', function (eventData) { _this.onChange.emit(eventData); if (eventData.args)
            if (eventData.args.item !== null)
                _this.onChangeCallback(eventData.args.item.label); });
        this.host.on('open', function (eventData) { _this.onOpen.emit(eventData); });
        this.host.on('select', function (eventData) { _this.onSelect.emit(eventData); });
        this.host.on('unselect', function (eventData) { _this.onUnselect.emit(eventData); });
    };
    return jqxComboBoxComponent;
}()); //jqxComboBoxComponent
__decorate([
    Input('animationType'),
    __metadata("design:type", Object)
], jqxComboBoxComponent.prototype, "attrAnimationType", void 0);
__decorate([
    Input('autoComplete'),
    __metadata("design:type", Object)
], jqxComboBoxComponent.prototype, "attrAutoComplete", void 0);
__decorate([
    Input('autoOpen'),
    __metadata("design:type", Object)
], jqxComboBoxComponent.prototype, "attrAutoOpen", void 0);
__decorate([
    Input('autoItemsHeight'),
    __metadata("design:type", Object)
], jqxComboBoxComponent.prototype, "attrAutoItemsHeight", void 0);
__decorate([
    Input('autoDropDownHeight'),
    __metadata("design:type", Object)
], jqxComboBoxComponent.prototype, "attrAutoDropDownHeight", void 0);
__decorate([
    Input('closeDelay'),
    __metadata("design:type", Object)
], jqxComboBoxComponent.prototype, "attrCloseDelay", void 0);
__decorate([
    Input('checkboxes'),
    __metadata("design:type", Object)
], jqxComboBoxComponent.prototype, "attrCheckboxes", void 0);
__decorate([
    Input('disabled'),
    __metadata("design:type", Object)
], jqxComboBoxComponent.prototype, "attrDisabled", void 0);
__decorate([
    Input('displayMember'),
    __metadata("design:type", Object)
], jqxComboBoxComponent.prototype, "attrDisplayMember", void 0);
__decorate([
    Input('dropDownHorizontalAlignment'),
    __metadata("design:type", Object)
], jqxComboBoxComponent.prototype, "attrDropDownHorizontalAlignment", void 0);
__decorate([
    Input('dropDownVerticalAlignment'),
    __metadata("design:type", Object)
], jqxComboBoxComponent.prototype, "attrDropDownVerticalAlignment", void 0);
__decorate([
    Input('dropDownHeight'),
    __metadata("design:type", Object)
], jqxComboBoxComponent.prototype, "attrDropDownHeight", void 0);
__decorate([
    Input('dropDownWidth'),
    __metadata("design:type", Object)
], jqxComboBoxComponent.prototype, "attrDropDownWidth", void 0);
__decorate([
    Input('enableHover'),
    __metadata("design:type", Object)
], jqxComboBoxComponent.prototype, "attrEnableHover", void 0);
__decorate([
    Input('enableSelection'),
    __metadata("design:type", Object)
], jqxComboBoxComponent.prototype, "attrEnableSelection", void 0);
__decorate([
    Input('enableBrowserBoundsDetection'),
    __metadata("design:type", Object)
], jqxComboBoxComponent.prototype, "attrEnableBrowserBoundsDetection", void 0);
__decorate([
    Input('itemHeight'),
    __metadata("design:type", Object)
], jqxComboBoxComponent.prototype, "attrItemHeight", void 0);
__decorate([
    Input('multiSelect'),
    __metadata("design:type", Object)
], jqxComboBoxComponent.prototype, "attrMultiSelect", void 0);
__decorate([
    Input('minLength'),
    __metadata("design:type", Object)
], jqxComboBoxComponent.prototype, "attrMinLength", void 0);
__decorate([
    Input('openDelay'),
    __metadata("design:type", Object)
], jqxComboBoxComponent.prototype, "attrOpenDelay", void 0);
__decorate([
    Input('popupZIndex'),
    __metadata("design:type", Object)
], jqxComboBoxComponent.prototype, "attrPopupZIndex", void 0);
__decorate([
    Input('placeHolder'),
    __metadata("design:type", Object)
], jqxComboBoxComponent.prototype, "attrPlaceHolder", void 0);
__decorate([
    Input('remoteAutoComplete'),
    __metadata("design:type", Object)
], jqxComboBoxComponent.prototype, "attrRemoteAutoComplete", void 0);
__decorate([
    Input('remoteAutoCompleteDelay'),
    __metadata("design:type", Object)
], jqxComboBoxComponent.prototype, "attrRemoteAutoCompleteDelay", void 0);
__decorate([
    Input('renderer'),
    __metadata("design:type", Object)
], jqxComboBoxComponent.prototype, "attrRenderer", void 0);
__decorate([
    Input('renderSelectedItem'),
    __metadata("design:type", Object)
], jqxComboBoxComponent.prototype, "attrRenderSelectedItem", void 0);
__decorate([
    Input('rtl'),
    __metadata("design:type", Object)
], jqxComboBoxComponent.prototype, "attrRtl", void 0);
__decorate([
    Input('selectedIndex'),
    __metadata("design:type", Object)
], jqxComboBoxComponent.prototype, "attrSelectedIndex", void 0);
__decorate([
    Input('showArrow'),
    __metadata("design:type", Object)
], jqxComboBoxComponent.prototype, "attrShowArrow", void 0);
__decorate([
    Input('showCloseButtons'),
    __metadata("design:type", Object)
], jqxComboBoxComponent.prototype, "attrShowCloseButtons", void 0);
__decorate([
    Input('searchMode'),
    __metadata("design:type", Object)
], jqxComboBoxComponent.prototype, "attrSearchMode", void 0);
__decorate([
    Input('search'),
    __metadata("design:type", Object)
], jqxComboBoxComponent.prototype, "attrSearch", void 0);
__decorate([
    Input('source'),
    __metadata("design:type", Object)
], jqxComboBoxComponent.prototype, "attrSource", void 0);
__decorate([
    Input('scrollBarSize'),
    __metadata("design:type", Object)
], jqxComboBoxComponent.prototype, "attrScrollBarSize", void 0);
__decorate([
    Input('template'),
    __metadata("design:type", Object)
], jqxComboBoxComponent.prototype, "attrTemplate", void 0);
__decorate([
    Input('theme'),
    __metadata("design:type", Object)
], jqxComboBoxComponent.prototype, "attrTheme", void 0);
__decorate([
    Input('validateSelection'),
    __metadata("design:type", Object)
], jqxComboBoxComponent.prototype, "attrValidateSelection", void 0);
__decorate([
    Input('valueMember'),
    __metadata("design:type", Object)
], jqxComboBoxComponent.prototype, "attrValueMember", void 0);
__decorate([
    Input('width'),
    __metadata("design:type", Object)
], jqxComboBoxComponent.prototype, "attrWidth", void 0);
__decorate([
    Input('height'),
    __metadata("design:type", Object)
], jqxComboBoxComponent.prototype, "attrHeight", void 0);
__decorate([
    Input('auto-create'),
    __metadata("design:type", Boolean)
], jqxComboBoxComponent.prototype, "autoCreate", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxComboBoxComponent.prototype, "onBindingComplete", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxComboBoxComponent.prototype, "onCheckChange", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxComboBoxComponent.prototype, "onClose", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxComboBoxComponent.prototype, "onChange", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxComboBoxComponent.prototype, "onOpen", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxComboBoxComponent.prototype, "onSelect", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxComboBoxComponent.prototype, "onUnselect", void 0);
jqxComboBoxComponent = __decorate([
    Component({
        selector: 'jqxComboBox',
        template: '<div><ng-content></ng-content></div>',
        providers: [CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR],
        changeDetection: ChangeDetectionStrategy.OnPush
    }),
    __metadata("design:paramtypes", [ElementRef])
], jqxComboBoxComponent);
export { jqxComboBoxComponent };
//# sourceMappingURL=angular_jqxcombobox.js.map