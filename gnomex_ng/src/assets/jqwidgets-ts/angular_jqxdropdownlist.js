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
    useExisting: forwardRef(function () { return jqxDropDownListComponent; }),
    multi: true
};
var jqxDropDownListComponent = (function () {
    function jqxDropDownListComponent(containerElement) {
        this.autoCreate = true;
        this.properties = ['autoOpen', 'autoDropDownHeight', 'animationType', 'checkboxes', 'closeDelay', 'disabled', 'displayMember', 'dropDownHorizontalAlignment', 'dropDownVerticalAlignment', 'dropDownHeight', 'dropDownWidth', 'enableSelection', 'enableBrowserBoundsDetection', 'enableHover', 'filterable', 'filterHeight', 'filterDelay', 'filterPlaceHolder', 'height', 'incrementalSearch', 'incrementalSearchDelay', 'itemHeight', 'openDelay', 'placeHolder', 'popupZIndex', 'rtl', 'renderer', 'selectionRenderer', 'searchMode', 'scrollBarSize', 'source', 'selectedIndex', 'theme', 'template', 'valueMember', 'width'];
        this.onTouchedCallback = noop;
        this.onChangeCallback = noop;
        // jqxDropDownListComponent events
        this.onBindingComplete = new EventEmitter();
        this.onClose = new EventEmitter();
        this.onCheckChange = new EventEmitter();
        this.onChange = new EventEmitter();
        this.onOpen = new EventEmitter();
        this.onSelect = new EventEmitter();
        this.onUnselect = new EventEmitter();
        this.elementRef = containerElement;
    }
    jqxDropDownListComponent.prototype.ngOnInit = function () {
        if (this.autoCreate) {
            this.createComponent();
        }
    };
    ;
    jqxDropDownListComponent.prototype.ngOnChanges = function (changes) {
        if (this.host) {
            for (var i = 0; i < this.properties.length; i++) {
                var attrName = 'attr' + this.properties[i].substring(0, 1).toUpperCase() + this.properties[i].substring(1);
                var areEqual = void 0;
                if (this[attrName] !== undefined) {
                    if (typeof this[attrName] === 'object') {
                        if (this[attrName] instanceof Array) {
                            areEqual = this.arraysEqual(this[attrName], this.host.jqxDropDownList(this.properties[i]));
                        }
                        if (areEqual) {
                            return false;
                        }
                        this.host.jqxDropDownList(this.properties[i], this[attrName]);
                        continue;
                    }
                    if (this[attrName] !== this.host.jqxDropDownList(this.properties[i])) {
                        this.host.jqxDropDownList(this.properties[i], this[attrName]);
                    }
                }
            }
        }
    };
    jqxDropDownListComponent.prototype.arraysEqual = function (attrValue, hostValue) {
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
    jqxDropDownListComponent.prototype.manageAttributes = function () {
        var options = {};
        for (var i = 0; i < this.properties.length; i++) {
            var attrName = 'attr' + this.properties[i].substring(0, 1).toUpperCase() + this.properties[i].substring(1);
            if (this[attrName] !== undefined) {
                options[this.properties[i]] = this[attrName];
            }
        }
        return options;
    };
    jqxDropDownListComponent.prototype.createComponent = function (options) {
        if (options) {
            JQXLite.extend(options, this.manageAttributes());
        }
        else {
            options = this.manageAttributes();
        }
        this.host = JQXLite(this.elementRef.nativeElement.firstChild);
        this.__wireEvents__();
        this.widgetObject = jqwidgets.createInstance(this.host, 'jqxDropDownList', options);
        this.__updateRect__();
    };
    jqxDropDownListComponent.prototype.createWidget = function (options) {
        this.createComponent(options);
    };
    jqxDropDownListComponent.prototype.__updateRect__ = function () {
        this.host.css({ width: this.attrWidth, height: this.attrHeight });
    };
    jqxDropDownListComponent.prototype.writeValue = function (value) {
        if (this.widgetObject) {
            this.onChangeCallback(this.host.val());
        }
    };
    jqxDropDownListComponent.prototype.registerOnChange = function (fn) {
        this.onChangeCallback = fn;
    };
    jqxDropDownListComponent.prototype.registerOnTouched = function (fn) {
        this.onTouchedCallback = fn;
    };
    jqxDropDownListComponent.prototype.setOptions = function (options) {
        this.host.jqxDropDownList('setOptions', options);
    };
    // jqxDropDownListComponent properties
    jqxDropDownListComponent.prototype.autoOpen = function (arg) {
        if (arg !== undefined) {
            this.host.jqxDropDownList('autoOpen', arg);
        }
        else {
            return this.host.jqxDropDownList('autoOpen');
        }
    };
    jqxDropDownListComponent.prototype.autoDropDownHeight = function (arg) {
        if (arg !== undefined) {
            this.host.jqxDropDownList('autoDropDownHeight', arg);
        }
        else {
            return this.host.jqxDropDownList('autoDropDownHeight');
        }
    };
    jqxDropDownListComponent.prototype.animationType = function (arg) {
        if (arg !== undefined) {
            this.host.jqxDropDownList('animationType', arg);
        }
        else {
            return this.host.jqxDropDownList('animationType');
        }
    };
    jqxDropDownListComponent.prototype.checkboxes = function (arg) {
        if (arg !== undefined) {
            this.host.jqxDropDownList('checkboxes', arg);
        }
        else {
            return this.host.jqxDropDownList('checkboxes');
        }
    };
    jqxDropDownListComponent.prototype.closeDelay = function (arg) {
        if (arg !== undefined) {
            this.host.jqxDropDownList('closeDelay', arg);
        }
        else {
            return this.host.jqxDropDownList('closeDelay');
        }
    };
    jqxDropDownListComponent.prototype.disabled = function (arg) {
        if (arg !== undefined) {
            this.host.jqxDropDownList('disabled', arg);
        }
        else {
            return this.host.jqxDropDownList('disabled');
        }
    };
    jqxDropDownListComponent.prototype.displayMember = function (arg) {
        if (arg !== undefined) {
            this.host.jqxDropDownList('displayMember', arg);
        }
        else {
            return this.host.jqxDropDownList('displayMember');
        }
    };
    jqxDropDownListComponent.prototype.dropDownHorizontalAlignment = function (arg) {
        if (arg !== undefined) {
            this.host.jqxDropDownList('dropDownHorizontalAlignment', arg);
        }
        else {
            return this.host.jqxDropDownList('dropDownHorizontalAlignment');
        }
    };
    jqxDropDownListComponent.prototype.dropDownVerticalAlignment = function (arg) {
        if (arg !== undefined) {
            this.host.jqxDropDownList('dropDownVerticalAlignment', arg);
        }
        else {
            return this.host.jqxDropDownList('dropDownVerticalAlignment');
        }
    };
    jqxDropDownListComponent.prototype.dropDownHeight = function (arg) {
        if (arg !== undefined) {
            this.host.jqxDropDownList('dropDownHeight', arg);
        }
        else {
            return this.host.jqxDropDownList('dropDownHeight');
        }
    };
    jqxDropDownListComponent.prototype.dropDownWidth = function (arg) {
        if (arg !== undefined) {
            this.host.jqxDropDownList('dropDownWidth', arg);
        }
        else {
            return this.host.jqxDropDownList('dropDownWidth');
        }
    };
    jqxDropDownListComponent.prototype.enableSelection = function (arg) {
        if (arg !== undefined) {
            this.host.jqxDropDownList('enableSelection', arg);
        }
        else {
            return this.host.jqxDropDownList('enableSelection');
        }
    };
    jqxDropDownListComponent.prototype.enableBrowserBoundsDetection = function (arg) {
        if (arg !== undefined) {
            this.host.jqxDropDownList('enableBrowserBoundsDetection', arg);
        }
        else {
            return this.host.jqxDropDownList('enableBrowserBoundsDetection');
        }
    };
    jqxDropDownListComponent.prototype.enableHover = function (arg) {
        if (arg !== undefined) {
            this.host.jqxDropDownList('enableHover', arg);
        }
        else {
            return this.host.jqxDropDownList('enableHover');
        }
    };
    jqxDropDownListComponent.prototype.filterable = function (arg) {
        if (arg !== undefined) {
            this.host.jqxDropDownList('filterable', arg);
        }
        else {
            return this.host.jqxDropDownList('filterable');
        }
    };
    jqxDropDownListComponent.prototype.filterHeight = function (arg) {
        if (arg !== undefined) {
            this.host.jqxDropDownList('filterHeight', arg);
        }
        else {
            return this.host.jqxDropDownList('filterHeight');
        }
    };
    jqxDropDownListComponent.prototype.filterDelay = function (arg) {
        if (arg !== undefined) {
            this.host.jqxDropDownList('filterDelay', arg);
        }
        else {
            return this.host.jqxDropDownList('filterDelay');
        }
    };
    jqxDropDownListComponent.prototype.filterPlaceHolder = function (arg) {
        if (arg !== undefined) {
            this.host.jqxDropDownList('filterPlaceHolder', arg);
        }
        else {
            return this.host.jqxDropDownList('filterPlaceHolder');
        }
    };
    jqxDropDownListComponent.prototype.height = function (arg) {
        if (arg !== undefined) {
            this.host.jqxDropDownList('height', arg);
        }
        else {
            return this.host.jqxDropDownList('height');
        }
    };
    jqxDropDownListComponent.prototype.incrementalSearch = function (arg) {
        if (arg !== undefined) {
            this.host.jqxDropDownList('incrementalSearch', arg);
        }
        else {
            return this.host.jqxDropDownList('incrementalSearch');
        }
    };
    jqxDropDownListComponent.prototype.incrementalSearchDelay = function (arg) {
        if (arg !== undefined) {
            this.host.jqxDropDownList('incrementalSearchDelay', arg);
        }
        else {
            return this.host.jqxDropDownList('incrementalSearchDelay');
        }
    };
    jqxDropDownListComponent.prototype.itemHeight = function (arg) {
        if (arg !== undefined) {
            this.host.jqxDropDownList('itemHeight', arg);
        }
        else {
            return this.host.jqxDropDownList('itemHeight');
        }
    };
    jqxDropDownListComponent.prototype.openDelay = function (arg) {
        if (arg !== undefined) {
            this.host.jqxDropDownList('openDelay', arg);
        }
        else {
            return this.host.jqxDropDownList('openDelay');
        }
    };
    jqxDropDownListComponent.prototype.placeHolder = function (arg) {
        if (arg !== undefined) {
            this.host.jqxDropDownList('placeHolder', arg);
        }
        else {
            return this.host.jqxDropDownList('placeHolder');
        }
    };
    jqxDropDownListComponent.prototype.popupZIndex = function (arg) {
        if (arg !== undefined) {
            this.host.jqxDropDownList('popupZIndex', arg);
        }
        else {
            return this.host.jqxDropDownList('popupZIndex');
        }
    };
    jqxDropDownListComponent.prototype.rtl = function (arg) {
        if (arg !== undefined) {
            this.host.jqxDropDownList('rtl', arg);
        }
        else {
            return this.host.jqxDropDownList('rtl');
        }
    };
    jqxDropDownListComponent.prototype.renderer = function (arg) {
        if (arg !== undefined) {
            this.host.jqxDropDownList('renderer', arg);
        }
        else {
            return this.host.jqxDropDownList('renderer');
        }
    };
    jqxDropDownListComponent.prototype.selectionRenderer = function (arg) {
        if (arg !== undefined) {
            this.host.jqxDropDownList('selectionRenderer', arg);
        }
        else {
            return this.host.jqxDropDownList('selectionRenderer');
        }
    };
    jqxDropDownListComponent.prototype.searchMode = function (arg) {
        if (arg !== undefined) {
            this.host.jqxDropDownList('searchMode', arg);
        }
        else {
            return this.host.jqxDropDownList('searchMode');
        }
    };
    jqxDropDownListComponent.prototype.scrollBarSize = function (arg) {
        if (arg !== undefined) {
            this.host.jqxDropDownList('scrollBarSize', arg);
        }
        else {
            return this.host.jqxDropDownList('scrollBarSize');
        }
    };
    jqxDropDownListComponent.prototype.source = function (arg) {
        if (arg !== undefined) {
            this.host.jqxDropDownList('source', arg);
        }
        else {
            return this.host.jqxDropDownList('source');
        }
    };
    jqxDropDownListComponent.prototype.selectedIndex = function (arg) {
        if (arg !== undefined) {
            this.host.jqxDropDownList('selectedIndex', arg);
        }
        else {
            return this.host.jqxDropDownList('selectedIndex');
        }
    };
    jqxDropDownListComponent.prototype.theme = function (arg) {
        if (arg !== undefined) {
            this.host.jqxDropDownList('theme', arg);
        }
        else {
            return this.host.jqxDropDownList('theme');
        }
    };
    jqxDropDownListComponent.prototype.template = function (arg) {
        if (arg !== undefined) {
            this.host.jqxDropDownList('template', arg);
        }
        else {
            return this.host.jqxDropDownList('template');
        }
    };
    jqxDropDownListComponent.prototype.valueMember = function (arg) {
        if (arg !== undefined) {
            this.host.jqxDropDownList('valueMember', arg);
        }
        else {
            return this.host.jqxDropDownList('valueMember');
        }
    };
    jqxDropDownListComponent.prototype.width = function (arg) {
        if (arg !== undefined) {
            this.host.jqxDropDownList('width', arg);
        }
        else {
            return this.host.jqxDropDownList('width');
        }
    };
    // jqxDropDownListComponent functions
    jqxDropDownListComponent.prototype.addItem = function (item) {
        return this.host.jqxDropDownList('addItem', item);
    };
    jqxDropDownListComponent.prototype.clearSelection = function () {
        this.host.jqxDropDownList('clearSelection');
    };
    jqxDropDownListComponent.prototype.clear = function () {
        this.host.jqxDropDownList('clear');
    };
    jqxDropDownListComponent.prototype.close = function () {
        this.host.jqxDropDownList('close');
    };
    jqxDropDownListComponent.prototype.checkIndex = function (index) {
        this.host.jqxDropDownList('checkIndex', index);
    };
    jqxDropDownListComponent.prototype.checkItem = function (item) {
        this.host.jqxDropDownList('checkItem', item);
    };
    jqxDropDownListComponent.prototype.checkAll = function () {
        this.host.jqxDropDownList('checkAll');
    };
    jqxDropDownListComponent.prototype.clearFilter = function () {
        this.host.jqxDropDownList('clearFilter');
    };
    jqxDropDownListComponent.prototype.destroy = function () {
        this.host.jqxDropDownList('destroy');
    };
    jqxDropDownListComponent.prototype.disableItem = function (item) {
        this.host.jqxDropDownList('disableItem', item);
    };
    jqxDropDownListComponent.prototype.disableAt = function (index) {
        this.host.jqxDropDownList('disableAt', index);
    };
    jqxDropDownListComponent.prototype.enableItem = function (item) {
        this.host.jqxDropDownList('enableItem', item);
    };
    jqxDropDownListComponent.prototype.enableAt = function (index) {
        this.host.jqxDropDownList('enableAt', index);
    };
    jqxDropDownListComponent.prototype.ensureVisible = function (index) {
        this.host.jqxDropDownList('ensureVisible', index);
    };
    jqxDropDownListComponent.prototype.focus = function () {
        this.host.jqxDropDownList('focus');
    };
    jqxDropDownListComponent.prototype.getItem = function (index) {
        return this.host.jqxDropDownList('getItem', index);
    };
    jqxDropDownListComponent.prototype.getItemByValue = function (itemValue) {
        return this.host.jqxDropDownList('getItemByValue', itemValue);
    };
    jqxDropDownListComponent.prototype.getItems = function () {
        return this.host.jqxDropDownList('getItems');
    };
    jqxDropDownListComponent.prototype.getCheckedItems = function () {
        return this.host.jqxDropDownList('getCheckedItems');
    };
    jqxDropDownListComponent.prototype.getSelectedItem = function () {
        return this.host.jqxDropDownList('getSelectedItem');
    };
    jqxDropDownListComponent.prototype.getSelectedIndex = function () {
        return this.host.jqxDropDownList('getSelectedIndex');
    };
    jqxDropDownListComponent.prototype.insertAt = function (item, index) {
        this.host.jqxDropDownList('insertAt', item, index);
    };
    jqxDropDownListComponent.prototype.isOpened = function () {
        return this.host.jqxDropDownList('isOpened');
    };
    jqxDropDownListComponent.prototype.indeterminateIndex = function (index) {
        this.host.jqxDropDownList('indeterminateIndex', index);
    };
    jqxDropDownListComponent.prototype.indeterminateItem = function (item) {
        this.host.jqxDropDownList('indeterminateItem', item);
    };
    jqxDropDownListComponent.prototype.loadFromSelect = function (arg) {
        this.host.jqxDropDownList('loadFromSelect', arg);
    };
    jqxDropDownListComponent.prototype.open = function () {
        this.host.jqxDropDownList('open');
    };
    jqxDropDownListComponent.prototype.removeItem = function (item) {
        this.host.jqxDropDownList('removeItem', item);
    };
    jqxDropDownListComponent.prototype.removeAt = function (index) {
        this.host.jqxDropDownList('removeAt', index);
    };
    jqxDropDownListComponent.prototype.selectIndex = function (index) {
        this.host.jqxDropDownList('selectIndex', index);
    };
    jqxDropDownListComponent.prototype.selectItem = function (item) {
        this.host.jqxDropDownList('selectItem', item);
    };
    jqxDropDownListComponent.prototype.setContent = function (content) {
        this.host.jqxDropDownList('setContent', content);
    };
    jqxDropDownListComponent.prototype.updateItem = function (newItem, item) {
        this.host.jqxDropDownList('updateItem', newItem, item);
    };
    jqxDropDownListComponent.prototype.updateAt = function (item, index) {
        this.host.jqxDropDownList('updateAt', item, index);
    };
    jqxDropDownListComponent.prototype.unselectIndex = function (index) {
        this.host.jqxDropDownList('unselectIndex', index);
    };
    jqxDropDownListComponent.prototype.unselectItem = function (item) {
        this.host.jqxDropDownList('unselectItem', item);
    };
    jqxDropDownListComponent.prototype.uncheckIndex = function (index) {
        this.host.jqxDropDownList('uncheckIndex', index);
    };
    jqxDropDownListComponent.prototype.uncheckItem = function (item) {
        this.host.jqxDropDownList('uncheckItem', item);
    };
    jqxDropDownListComponent.prototype.uncheckAll = function () {
        this.host.jqxDropDownList('uncheckAll');
    };
    jqxDropDownListComponent.prototype.val = function (value) {
        if (value !== undefined) {
            this.host.jqxDropDownList("val", value);
        }
        else {
            return this.host.jqxDropDownList("val");
        }
    };
    ;
    jqxDropDownListComponent.prototype.__wireEvents__ = function () {
        var _this = this;
        this.host.on('bindingComplete', function (eventData) { _this.onBindingComplete.emit(eventData); });
        this.host.on('close', function (eventData) { _this.onClose.emit(eventData); });
        this.host.on('checkChange', function (eventData) { _this.onCheckChange.emit(eventData); });
        this.host.on('change', function (eventData) { _this.onChange.emit(eventData); if (eventData.args)
            _this.onChangeCallback(eventData.args.item.label); });
        this.host.on('open', function (eventData) { _this.onOpen.emit(eventData); });
        this.host.on('select', function (eventData) { _this.onSelect.emit(eventData); });
        this.host.on('unselect', function (eventData) { _this.onUnselect.emit(eventData); });
    };
    return jqxDropDownListComponent;
}()); //jqxDropDownListComponent
__decorate([
    Input('autoOpen'),
    __metadata("design:type", Object)
], jqxDropDownListComponent.prototype, "attrAutoOpen", void 0);
__decorate([
    Input('autoDropDownHeight'),
    __metadata("design:type", Object)
], jqxDropDownListComponent.prototype, "attrAutoDropDownHeight", void 0);
__decorate([
    Input('animationType'),
    __metadata("design:type", Object)
], jqxDropDownListComponent.prototype, "attrAnimationType", void 0);
__decorate([
    Input('checkboxes'),
    __metadata("design:type", Object)
], jqxDropDownListComponent.prototype, "attrCheckboxes", void 0);
__decorate([
    Input('closeDelay'),
    __metadata("design:type", Object)
], jqxDropDownListComponent.prototype, "attrCloseDelay", void 0);
__decorate([
    Input('disabled'),
    __metadata("design:type", Object)
], jqxDropDownListComponent.prototype, "attrDisabled", void 0);
__decorate([
    Input('displayMember'),
    __metadata("design:type", Object)
], jqxDropDownListComponent.prototype, "attrDisplayMember", void 0);
__decorate([
    Input('dropDownHorizontalAlignment'),
    __metadata("design:type", Object)
], jqxDropDownListComponent.prototype, "attrDropDownHorizontalAlignment", void 0);
__decorate([
    Input('dropDownVerticalAlignment'),
    __metadata("design:type", Object)
], jqxDropDownListComponent.prototype, "attrDropDownVerticalAlignment", void 0);
__decorate([
    Input('dropDownHeight'),
    __metadata("design:type", Object)
], jqxDropDownListComponent.prototype, "attrDropDownHeight", void 0);
__decorate([
    Input('dropDownWidth'),
    __metadata("design:type", Object)
], jqxDropDownListComponent.prototype, "attrDropDownWidth", void 0);
__decorate([
    Input('enableSelection'),
    __metadata("design:type", Object)
], jqxDropDownListComponent.prototype, "attrEnableSelection", void 0);
__decorate([
    Input('enableBrowserBoundsDetection'),
    __metadata("design:type", Object)
], jqxDropDownListComponent.prototype, "attrEnableBrowserBoundsDetection", void 0);
__decorate([
    Input('enableHover'),
    __metadata("design:type", Object)
], jqxDropDownListComponent.prototype, "attrEnableHover", void 0);
__decorate([
    Input('filterable'),
    __metadata("design:type", Object)
], jqxDropDownListComponent.prototype, "attrFilterable", void 0);
__decorate([
    Input('filterHeight'),
    __metadata("design:type", Object)
], jqxDropDownListComponent.prototype, "attrFilterHeight", void 0);
__decorate([
    Input('filterDelay'),
    __metadata("design:type", Object)
], jqxDropDownListComponent.prototype, "attrFilterDelay", void 0);
__decorate([
    Input('filterPlaceHolder'),
    __metadata("design:type", Object)
], jqxDropDownListComponent.prototype, "attrFilterPlaceHolder", void 0);
__decorate([
    Input('incrementalSearch'),
    __metadata("design:type", Object)
], jqxDropDownListComponent.prototype, "attrIncrementalSearch", void 0);
__decorate([
    Input('incrementalSearchDelay'),
    __metadata("design:type", Object)
], jqxDropDownListComponent.prototype, "attrIncrementalSearchDelay", void 0);
__decorate([
    Input('itemHeight'),
    __metadata("design:type", Object)
], jqxDropDownListComponent.prototype, "attrItemHeight", void 0);
__decorate([
    Input('openDelay'),
    __metadata("design:type", Object)
], jqxDropDownListComponent.prototype, "attrOpenDelay", void 0);
__decorate([
    Input('placeHolder'),
    __metadata("design:type", Object)
], jqxDropDownListComponent.prototype, "attrPlaceHolder", void 0);
__decorate([
    Input('popupZIndex'),
    __metadata("design:type", Object)
], jqxDropDownListComponent.prototype, "attrPopupZIndex", void 0);
__decorate([
    Input('rtl'),
    __metadata("design:type", Object)
], jqxDropDownListComponent.prototype, "attrRtl", void 0);
__decorate([
    Input('renderer'),
    __metadata("design:type", Object)
], jqxDropDownListComponent.prototype, "attrRenderer", void 0);
__decorate([
    Input('selectionRenderer'),
    __metadata("design:type", Object)
], jqxDropDownListComponent.prototype, "attrSelectionRenderer", void 0);
__decorate([
    Input('searchMode'),
    __metadata("design:type", Object)
], jqxDropDownListComponent.prototype, "attrSearchMode", void 0);
__decorate([
    Input('scrollBarSize'),
    __metadata("design:type", Object)
], jqxDropDownListComponent.prototype, "attrScrollBarSize", void 0);
__decorate([
    Input('source'),
    __metadata("design:type", Object)
], jqxDropDownListComponent.prototype, "attrSource", void 0);
__decorate([
    Input('selectedIndex'),
    __metadata("design:type", Object)
], jqxDropDownListComponent.prototype, "attrSelectedIndex", void 0);
__decorate([
    Input('theme'),
    __metadata("design:type", Object)
], jqxDropDownListComponent.prototype, "attrTheme", void 0);
__decorate([
    Input('template'),
    __metadata("design:type", Object)
], jqxDropDownListComponent.prototype, "attrTemplate", void 0);
__decorate([
    Input('valueMember'),
    __metadata("design:type", Object)
], jqxDropDownListComponent.prototype, "attrValueMember", void 0);
__decorate([
    Input('width'),
    __metadata("design:type", Object)
], jqxDropDownListComponent.prototype, "attrWidth", void 0);
__decorate([
    Input('height'),
    __metadata("design:type", Object)
], jqxDropDownListComponent.prototype, "attrHeight", void 0);
__decorate([
    Input('auto-create'),
    __metadata("design:type", Boolean)
], jqxDropDownListComponent.prototype, "autoCreate", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxDropDownListComponent.prototype, "onBindingComplete", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxDropDownListComponent.prototype, "onClose", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxDropDownListComponent.prototype, "onCheckChange", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxDropDownListComponent.prototype, "onChange", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxDropDownListComponent.prototype, "onOpen", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxDropDownListComponent.prototype, "onSelect", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxDropDownListComponent.prototype, "onUnselect", void 0);
jqxDropDownListComponent = __decorate([
    Component({
        selector: 'jqxDropDownList',
        template: '<div><ng-content></ng-content></div>',
        providers: [CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR],
        changeDetection: ChangeDetectionStrategy.OnPush
    }),
    __metadata("design:paramtypes", [ElementRef])
], jqxDropDownListComponent);
export { jqxDropDownListComponent };
//# sourceMappingURL=angular_jqxdropdownlist.js.map