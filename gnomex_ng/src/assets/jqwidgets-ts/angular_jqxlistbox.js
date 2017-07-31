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
    useExisting: forwardRef(function () { return jqxListBoxComponent; }),
    multi: true
};
var jqxListBoxComponent = (function () {
    function jqxListBoxComponent(containerElement) {
        this.autoCreate = true;
        this.properties = ['autoHeight', 'allowDrag', 'allowDrop', 'checkboxes', 'disabled', 'displayMember', 'dropAction', 'dragStart', 'dragEnd', 'enableHover', 'enableSelection', 'equalItemsWidth', 'filterable', 'filterHeight', 'filterDelay', 'filterPlaceHolder', 'height', 'hasThreeStates', 'itemHeight', 'incrementalSearch', 'incrementalSearchDelay', 'multiple', 'multipleextended', 'renderer', 'rtl', 'selectedIndex', 'selectedIndexes', 'source', 'scrollBarSize', 'searchMode', 'theme', 'valueMember', 'width'];
        this.onTouchedCallback = noop;
        this.onChangeCallback = noop;
        // jqxListBoxComponent events
        this.onBindingComplete = new EventEmitter();
        this.onChange = new EventEmitter();
        this.onCheckChange = new EventEmitter();
        this.onDragStart = new EventEmitter();
        this.onDragEnd = new EventEmitter();
        this.onSelect = new EventEmitter();
        this.onUnselect = new EventEmitter();
        this.elementRef = containerElement;
    }
    jqxListBoxComponent.prototype.ngOnInit = function () {
        if (this.autoCreate) {
            this.createComponent();
        }
    };
    ;
    jqxListBoxComponent.prototype.ngOnChanges = function (changes) {
        if (this.host) {
            for (var i = 0; i < this.properties.length; i++) {
                var attrName = 'attr' + this.properties[i].substring(0, 1).toUpperCase() + this.properties[i].substring(1);
                var areEqual = void 0;
                if (this[attrName] !== undefined) {
                    if (typeof this[attrName] === 'object') {
                        if (this[attrName] instanceof Array) {
                            areEqual = this.arraysEqual(this[attrName], this.host.jqxListBox(this.properties[i]));
                        }
                        if (areEqual) {
                            return false;
                        }
                        this.host.jqxListBox(this.properties[i], this[attrName]);
                        continue;
                    }
                    if (this[attrName] !== this.host.jqxListBox(this.properties[i])) {
                        this.host.jqxListBox(this.properties[i], this[attrName]);
                    }
                }
            }
        }
    };
    jqxListBoxComponent.prototype.arraysEqual = function (attrValue, hostValue) {
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
    jqxListBoxComponent.prototype.manageAttributes = function () {
        var options = {};
        for (var i = 0; i < this.properties.length; i++) {
            var attrName = 'attr' + this.properties[i].substring(0, 1).toUpperCase() + this.properties[i].substring(1);
            if (this[attrName] !== undefined) {
                options[this.properties[i]] = this[attrName];
            }
        }
        return options;
    };
    jqxListBoxComponent.prototype.createComponent = function (options) {
        if (options) {
            JQXLite.extend(options, this.manageAttributes());
        }
        else {
            options = this.manageAttributes();
        }
        this.host = JQXLite(this.elementRef.nativeElement.firstChild);
        this.__wireEvents__();
        this.widgetObject = jqwidgets.createInstance(this.host, 'jqxListBox', options);
        this.__updateRect__();
    };
    jqxListBoxComponent.prototype.createWidget = function (options) {
        this.createComponent(options);
    };
    jqxListBoxComponent.prototype.__updateRect__ = function () {
        this.host.css({ width: this.attrWidth, height: this.attrHeight });
    };
    jqxListBoxComponent.prototype.writeValue = function (value) {
        if (this.widgetObject) {
            this.onChangeCallback(this.host.val());
        }
    };
    jqxListBoxComponent.prototype.registerOnChange = function (fn) {
        this.onChangeCallback = fn;
    };
    jqxListBoxComponent.prototype.registerOnTouched = function (fn) {
        this.onTouchedCallback = fn;
    };
    jqxListBoxComponent.prototype.setOptions = function (options) {
        this.host.jqxListBox('setOptions', options);
    };
    // jqxListBoxComponent properties
    jqxListBoxComponent.prototype.autoHeight = function (arg) {
        if (arg !== undefined) {
            this.host.jqxListBox('autoHeight', arg);
        }
        else {
            return this.host.jqxListBox('autoHeight');
        }
    };
    jqxListBoxComponent.prototype.allowDrag = function (arg) {
        if (arg !== undefined) {
            this.host.jqxListBox('allowDrag', arg);
        }
        else {
            return this.host.jqxListBox('allowDrag');
        }
    };
    jqxListBoxComponent.prototype.allowDrop = function (arg) {
        if (arg !== undefined) {
            this.host.jqxListBox('allowDrop', arg);
        }
        else {
            return this.host.jqxListBox('allowDrop');
        }
    };
    jqxListBoxComponent.prototype.checkboxes = function (arg) {
        if (arg !== undefined) {
            this.host.jqxListBox('checkboxes', arg);
        }
        else {
            return this.host.jqxListBox('checkboxes');
        }
    };
    jqxListBoxComponent.prototype.disabled = function (arg) {
        if (arg !== undefined) {
            this.host.jqxListBox('disabled', arg);
        }
        else {
            return this.host.jqxListBox('disabled');
        }
    };
    jqxListBoxComponent.prototype.displayMember = function (arg) {
        if (arg !== undefined) {
            this.host.jqxListBox('displayMember', arg);
        }
        else {
            return this.host.jqxListBox('displayMember');
        }
    };
    jqxListBoxComponent.prototype.dropAction = function (arg) {
        if (arg !== undefined) {
            this.host.jqxListBox('dropAction', arg);
        }
        else {
            return this.host.jqxListBox('dropAction');
        }
    };
    jqxListBoxComponent.prototype.dragStart = function (arg) {
        if (arg !== undefined) {
            this.host.jqxListBox('dragStart', arg);
        }
        else {
            return this.host.jqxListBox('dragStart');
        }
    };
    jqxListBoxComponent.prototype.dragEnd = function (arg) {
        if (arg !== undefined) {
            this.host.jqxListBox('dragEnd', arg);
        }
        else {
            return this.host.jqxListBox('dragEnd');
        }
    };
    jqxListBoxComponent.prototype.enableHover = function (arg) {
        if (arg !== undefined) {
            this.host.jqxListBox('enableHover', arg);
        }
        else {
            return this.host.jqxListBox('enableHover');
        }
    };
    jqxListBoxComponent.prototype.enableSelection = function (arg) {
        if (arg !== undefined) {
            this.host.jqxListBox('enableSelection', arg);
        }
        else {
            return this.host.jqxListBox('enableSelection');
        }
    };
    jqxListBoxComponent.prototype.equalItemsWidth = function (arg) {
        if (arg !== undefined) {
            this.host.jqxListBox('equalItemsWidth', arg);
        }
        else {
            return this.host.jqxListBox('equalItemsWidth');
        }
    };
    jqxListBoxComponent.prototype.filterable = function (arg) {
        if (arg !== undefined) {
            this.host.jqxListBox('filterable', arg);
        }
        else {
            return this.host.jqxListBox('filterable');
        }
    };
    jqxListBoxComponent.prototype.filterHeight = function (arg) {
        if (arg !== undefined) {
            this.host.jqxListBox('filterHeight', arg);
        }
        else {
            return this.host.jqxListBox('filterHeight');
        }
    };
    jqxListBoxComponent.prototype.filterDelay = function (arg) {
        if (arg !== undefined) {
            this.host.jqxListBox('filterDelay', arg);
        }
        else {
            return this.host.jqxListBox('filterDelay');
        }
    };
    jqxListBoxComponent.prototype.filterPlaceHolder = function (arg) {
        if (arg !== undefined) {
            this.host.jqxListBox('filterPlaceHolder', arg);
        }
        else {
            return this.host.jqxListBox('filterPlaceHolder');
        }
    };
    jqxListBoxComponent.prototype.height = function (arg) {
        if (arg !== undefined) {
            this.host.jqxListBox('height', arg);
        }
        else {
            return this.host.jqxListBox('height');
        }
    };
    jqxListBoxComponent.prototype.hasThreeStates = function (arg) {
        if (arg !== undefined) {
            this.host.jqxListBox('hasThreeStates', arg);
        }
        else {
            return this.host.jqxListBox('hasThreeStates');
        }
    };
    jqxListBoxComponent.prototype.itemHeight = function (arg) {
        if (arg !== undefined) {
            this.host.jqxListBox('itemHeight', arg);
        }
        else {
            return this.host.jqxListBox('itemHeight');
        }
    };
    jqxListBoxComponent.prototype.incrementalSearch = function (arg) {
        if (arg !== undefined) {
            this.host.jqxListBox('incrementalSearch', arg);
        }
        else {
            return this.host.jqxListBox('incrementalSearch');
        }
    };
    jqxListBoxComponent.prototype.incrementalSearchDelay = function (arg) {
        if (arg !== undefined) {
            this.host.jqxListBox('incrementalSearchDelay', arg);
        }
        else {
            return this.host.jqxListBox('incrementalSearchDelay');
        }
    };
    jqxListBoxComponent.prototype.multiple = function (arg) {
        if (arg !== undefined) {
            this.host.jqxListBox('multiple', arg);
        }
        else {
            return this.host.jqxListBox('multiple');
        }
    };
    jqxListBoxComponent.prototype.multipleextended = function (arg) {
        if (arg !== undefined) {
            this.host.jqxListBox('multipleextended', arg);
        }
        else {
            return this.host.jqxListBox('multipleextended');
        }
    };
    jqxListBoxComponent.prototype.renderer = function (arg) {
        if (arg !== undefined) {
            this.host.jqxListBox('renderer', arg);
        }
        else {
            return this.host.jqxListBox('renderer');
        }
    };
    jqxListBoxComponent.prototype.rtl = function (arg) {
        if (arg !== undefined) {
            this.host.jqxListBox('rtl', arg);
        }
        else {
            return this.host.jqxListBox('rtl');
        }
    };
    jqxListBoxComponent.prototype.selectedIndex = function (arg) {
        if (arg !== undefined) {
            this.host.jqxListBox('selectedIndex', arg);
        }
        else {
            return this.host.jqxListBox('selectedIndex');
        }
    };
    jqxListBoxComponent.prototype.selectedIndexes = function (arg) {
        if (arg !== undefined) {
            this.host.jqxListBox('selectedIndexes', arg);
        }
        else {
            return this.host.jqxListBox('selectedIndexes');
        }
    };
    jqxListBoxComponent.prototype.source = function (arg) {
        if (arg !== undefined) {
            this.host.jqxListBox('source', arg);
        }
        else {
            return this.host.jqxListBox('source');
        }
    };
    jqxListBoxComponent.prototype.scrollBarSize = function (arg) {
        if (arg !== undefined) {
            this.host.jqxListBox('scrollBarSize', arg);
        }
        else {
            return this.host.jqxListBox('scrollBarSize');
        }
    };
    jqxListBoxComponent.prototype.searchMode = function (arg) {
        if (arg !== undefined) {
            this.host.jqxListBox('searchMode', arg);
        }
        else {
            return this.host.jqxListBox('searchMode');
        }
    };
    jqxListBoxComponent.prototype.theme = function (arg) {
        if (arg !== undefined) {
            this.host.jqxListBox('theme', arg);
        }
        else {
            return this.host.jqxListBox('theme');
        }
    };
    jqxListBoxComponent.prototype.valueMember = function (arg) {
        if (arg !== undefined) {
            this.host.jqxListBox('valueMember', arg);
        }
        else {
            return this.host.jqxListBox('valueMember');
        }
    };
    jqxListBoxComponent.prototype.width = function (arg) {
        if (arg !== undefined) {
            this.host.jqxListBox('width', arg);
        }
        else {
            return this.host.jqxListBox('width');
        }
    };
    // jqxListBoxComponent functions
    jqxListBoxComponent.prototype.addItem = function (Item) {
        return this.host.jqxListBox('addItem', Item);
    };
    jqxListBoxComponent.prototype.beginUpdate = function () {
        this.host.jqxListBox('beginUpdate');
    };
    jqxListBoxComponent.prototype.clear = function () {
        this.host.jqxListBox('clear');
    };
    jqxListBoxComponent.prototype.clearSelection = function () {
        this.host.jqxListBox('clearSelection');
    };
    jqxListBoxComponent.prototype.checkIndex = function (Index) {
        this.host.jqxListBox('checkIndex', Index);
    };
    jqxListBoxComponent.prototype.checkItem = function (Item) {
        this.host.jqxListBox('checkItem', Item);
    };
    jqxListBoxComponent.prototype.checkAll = function () {
        this.host.jqxListBox('checkAll');
    };
    jqxListBoxComponent.prototype.clearFilter = function () {
        this.host.jqxListBox('clearFilter');
    };
    jqxListBoxComponent.prototype.destroy = function () {
        this.host.jqxListBox('destroy');
    };
    jqxListBoxComponent.prototype.disableItem = function (Item) {
        this.host.jqxListBox('disableItem', Item);
    };
    jqxListBoxComponent.prototype.disableAt = function (Index) {
        this.host.jqxListBox('disableAt', Index);
    };
    jqxListBoxComponent.prototype.enableItem = function (Item) {
        this.host.jqxListBox('enableItem', Item);
    };
    jqxListBoxComponent.prototype.enableAt = function (Index) {
        this.host.jqxListBox('enableAt', Index);
    };
    jqxListBoxComponent.prototype.ensureVisible = function (item) {
        this.host.jqxListBox('ensureVisible', item);
    };
    jqxListBoxComponent.prototype.endUpdate = function () {
        this.host.jqxListBox('endUpdate');
    };
    jqxListBoxComponent.prototype.focus = function () {
        this.host.jqxListBox('focus');
    };
    jqxListBoxComponent.prototype.getItems = function () {
        return this.host.jqxListBox('getItems');
    };
    jqxListBoxComponent.prototype.getSelectedItems = function () {
        return this.host.jqxListBox('getSelectedItems');
    };
    jqxListBoxComponent.prototype.getCheckedItems = function () {
        return this.host.jqxListBox('getCheckedItems');
    };
    jqxListBoxComponent.prototype.getItem = function (Index) {
        return this.host.jqxListBox('getItem', Index);
    };
    jqxListBoxComponent.prototype.getItemByValue = function (Item) {
        return this.host.jqxListBox('getItemByValue', Item);
    };
    jqxListBoxComponent.prototype.getSelectedItem = function () {
        return this.host.jqxListBox('getSelectedItem');
    };
    jqxListBoxComponent.prototype.getSelectedIndex = function () {
        return this.host.jqxListBox('getSelectedIndex');
    };
    jqxListBoxComponent.prototype.insertAt = function (Item, Index) {
        this.host.jqxListBox('insertAt', Item, Index);
    };
    jqxListBoxComponent.prototype.invalidate = function () {
        this.host.jqxListBox('invalidate');
    };
    jqxListBoxComponent.prototype.indeterminateItem = function (Item) {
        this.host.jqxListBox('indeterminateItem', Item);
    };
    jqxListBoxComponent.prototype.indeterminateIndex = function (Index) {
        this.host.jqxListBox('indeterminateIndex', Index);
    };
    jqxListBoxComponent.prototype.loadFromSelect = function (selector) {
        this.host.jqxListBox('loadFromSelect', selector);
    };
    jqxListBoxComponent.prototype.removeItem = function (Item) {
        this.host.jqxListBox('removeItem', Item);
    };
    jqxListBoxComponent.prototype.removeAt = function (Index) {
        this.host.jqxListBox('removeAt', Index);
    };
    jqxListBoxComponent.prototype.render = function () {
        this.host.jqxListBox('render');
    };
    jqxListBoxComponent.prototype.refresh = function () {
        this.host.jqxListBox('refresh');
    };
    jqxListBoxComponent.prototype.selectItem = function (Item) {
        this.host.jqxListBox('selectItem', Item);
    };
    jqxListBoxComponent.prototype.selectIndex = function (Index) {
        this.host.jqxListBox('selectIndex', Index);
    };
    jqxListBoxComponent.prototype.updateItem = function (Item, Value) {
        this.host.jqxListBox('updateItem', Item, Value);
    };
    jqxListBoxComponent.prototype.updateAt = function (item, index) {
        this.host.jqxListBox('updateAt', item, index);
    };
    jqxListBoxComponent.prototype.unselectIndex = function (index) {
        this.host.jqxListBox('unselectIndex', index);
    };
    jqxListBoxComponent.prototype.unselectItem = function (item) {
        this.host.jqxListBox('unselectItem', item);
    };
    jqxListBoxComponent.prototype.uncheckIndex = function (index) {
        this.host.jqxListBox('uncheckIndex', index);
    };
    jqxListBoxComponent.prototype.uncheckItem = function (item) {
        this.host.jqxListBox('uncheckItem', item);
    };
    jqxListBoxComponent.prototype.uncheckAll = function () {
        this.host.jqxListBox('uncheckAll');
    };
    jqxListBoxComponent.prototype.val = function (value) {
        if (value !== undefined) {
            this.host.jqxListBox("val", value);
        }
        else {
            return this.host.jqxListBox("val");
        }
    };
    ;
    jqxListBoxComponent.prototype.__wireEvents__ = function () {
        var _this = this;
        this.host.on('bindingComplete', function (eventData) { _this.onBindingComplete.emit(eventData); });
        this.host.on('change', function (eventData) { _this.onChange.emit(eventData); if (eventData.args)
            _this.onChangeCallback(eventData.args.item.label); });
        this.host.on('checkChange', function (eventData) { _this.onCheckChange.emit(eventData); });
        this.host.on('dragStart', function (eventData) { _this.onDragStart.emit(eventData); });
        this.host.on('dragEnd', function (eventData) { _this.onDragEnd.emit(eventData); });
        this.host.on('select', function (eventData) { _this.onSelect.emit(eventData); });
        this.host.on('unselect', function (eventData) { _this.onUnselect.emit(eventData); });
    };
    return jqxListBoxComponent;
}()); //jqxListBoxComponent
__decorate([
    Input('autoHeight'),
    __metadata("design:type", Object)
], jqxListBoxComponent.prototype, "attrAutoHeight", void 0);
__decorate([
    Input('allowDrag'),
    __metadata("design:type", Object)
], jqxListBoxComponent.prototype, "attrAllowDrag", void 0);
__decorate([
    Input('allowDrop'),
    __metadata("design:type", Object)
], jqxListBoxComponent.prototype, "attrAllowDrop", void 0);
__decorate([
    Input('checkboxes'),
    __metadata("design:type", Object)
], jqxListBoxComponent.prototype, "attrCheckboxes", void 0);
__decorate([
    Input('disabled'),
    __metadata("design:type", Object)
], jqxListBoxComponent.prototype, "attrDisabled", void 0);
__decorate([
    Input('displayMember'),
    __metadata("design:type", Object)
], jqxListBoxComponent.prototype, "attrDisplayMember", void 0);
__decorate([
    Input('dropAction'),
    __metadata("design:type", Object)
], jqxListBoxComponent.prototype, "attrDropAction", void 0);
__decorate([
    Input('dragStart'),
    __metadata("design:type", Object)
], jqxListBoxComponent.prototype, "attrDragStart", void 0);
__decorate([
    Input('dragEnd'),
    __metadata("design:type", Object)
], jqxListBoxComponent.prototype, "attrDragEnd", void 0);
__decorate([
    Input('enableHover'),
    __metadata("design:type", Object)
], jqxListBoxComponent.prototype, "attrEnableHover", void 0);
__decorate([
    Input('enableSelection'),
    __metadata("design:type", Object)
], jqxListBoxComponent.prototype, "attrEnableSelection", void 0);
__decorate([
    Input('equalItemsWidth'),
    __metadata("design:type", Object)
], jqxListBoxComponent.prototype, "attrEqualItemsWidth", void 0);
__decorate([
    Input('filterable'),
    __metadata("design:type", Object)
], jqxListBoxComponent.prototype, "attrFilterable", void 0);
__decorate([
    Input('filterHeight'),
    __metadata("design:type", Object)
], jqxListBoxComponent.prototype, "attrFilterHeight", void 0);
__decorate([
    Input('filterDelay'),
    __metadata("design:type", Object)
], jqxListBoxComponent.prototype, "attrFilterDelay", void 0);
__decorate([
    Input('filterPlaceHolder'),
    __metadata("design:type", Object)
], jqxListBoxComponent.prototype, "attrFilterPlaceHolder", void 0);
__decorate([
    Input('hasThreeStates'),
    __metadata("design:type", Object)
], jqxListBoxComponent.prototype, "attrHasThreeStates", void 0);
__decorate([
    Input('itemHeight'),
    __metadata("design:type", Object)
], jqxListBoxComponent.prototype, "attrItemHeight", void 0);
__decorate([
    Input('incrementalSearch'),
    __metadata("design:type", Object)
], jqxListBoxComponent.prototype, "attrIncrementalSearch", void 0);
__decorate([
    Input('incrementalSearchDelay'),
    __metadata("design:type", Object)
], jqxListBoxComponent.prototype, "attrIncrementalSearchDelay", void 0);
__decorate([
    Input('multiple'),
    __metadata("design:type", Object)
], jqxListBoxComponent.prototype, "attrMultiple", void 0);
__decorate([
    Input('multipleextended'),
    __metadata("design:type", Object)
], jqxListBoxComponent.prototype, "attrMultipleextended", void 0);
__decorate([
    Input('renderer'),
    __metadata("design:type", Object)
], jqxListBoxComponent.prototype, "attrRenderer", void 0);
__decorate([
    Input('rtl'),
    __metadata("design:type", Object)
], jqxListBoxComponent.prototype, "attrRtl", void 0);
__decorate([
    Input('selectedIndex'),
    __metadata("design:type", Object)
], jqxListBoxComponent.prototype, "attrSelectedIndex", void 0);
__decorate([
    Input('selectedIndexes'),
    __metadata("design:type", Object)
], jqxListBoxComponent.prototype, "attrSelectedIndexes", void 0);
__decorate([
    Input('source'),
    __metadata("design:type", Object)
], jqxListBoxComponent.prototype, "attrSource", void 0);
__decorate([
    Input('scrollBarSize'),
    __metadata("design:type", Object)
], jqxListBoxComponent.prototype, "attrScrollBarSize", void 0);
__decorate([
    Input('searchMode'),
    __metadata("design:type", Object)
], jqxListBoxComponent.prototype, "attrSearchMode", void 0);
__decorate([
    Input('theme'),
    __metadata("design:type", Object)
], jqxListBoxComponent.prototype, "attrTheme", void 0);
__decorate([
    Input('valueMember'),
    __metadata("design:type", Object)
], jqxListBoxComponent.prototype, "attrValueMember", void 0);
__decorate([
    Input('width'),
    __metadata("design:type", Object)
], jqxListBoxComponent.prototype, "attrWidth", void 0);
__decorate([
    Input('height'),
    __metadata("design:type", Object)
], jqxListBoxComponent.prototype, "attrHeight", void 0);
__decorate([
    Input('auto-create'),
    __metadata("design:type", Boolean)
], jqxListBoxComponent.prototype, "autoCreate", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxListBoxComponent.prototype, "onBindingComplete", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxListBoxComponent.prototype, "onChange", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxListBoxComponent.prototype, "onCheckChange", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxListBoxComponent.prototype, "onDragStart", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxListBoxComponent.prototype, "onDragEnd", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxListBoxComponent.prototype, "onSelect", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxListBoxComponent.prototype, "onUnselect", void 0);
jqxListBoxComponent = __decorate([
    Component({
        selector: 'jqxListBox',
        template: '<div><ng-content></ng-content></div>',
        providers: [CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR],
        changeDetection: ChangeDetectionStrategy.OnPush
    }),
    __metadata("design:paramtypes", [ElementRef])
], jqxListBoxComponent);
export { jqxListBoxComponent };
//# sourceMappingURL=angular_jqxlistbox.js.map