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
var jqxGridComponent = (function () {
    function jqxGridComponent(containerElement) {
        this.autoCreate = true;
        this.properties = ['altrows', 'altstart', 'altstep', 'autoshowloadelement', 'autoshowfiltericon', 'autoshowcolumnsmenubutton', 'clipboard', 'closeablegroups', 'columnsmenuwidth', 'columnmenuopening', 'columnmenuclosing', 'cellhover', 'enablekeyboarddelete', 'enableellipsis', 'enablemousewheel', 'enableanimations', 'enabletooltips', 'enablehover', 'enablebrowserselection', 'everpresentrowposition', 'everpresentrowheight', 'everpresentrowactions', 'everpresentrowactionsmode', 'filterrowheight', 'filtermode', 'groupsrenderer', 'groupcolumnrenderer', 'groupsexpandedbydefault', 'handlekeyboardnavigation', 'pagerrenderer', 'rtl', 'showdefaultloadelement', 'showfiltercolumnbackground', 'showfiltermenuitems', 'showpinnedcolumnbackground', 'showsortcolumnbackground', 'showsortmenuitems', 'showgroupmenuitems', 'showrowdetailscolumn', 'showheader', 'showgroupsheader', 'showaggregates', 'showgroupaggregates', 'showeverpresentrow', 'showfilterrow', 'showemptyrow', 'showstatusbar', 'statusbarheight', 'showtoolbar', 'selectionmode', 'theme', 'toolbarheight', 'autoheight', 'autorowheight', 'columnsheight', 'deferreddatafields', 'groupsheaderheight', 'groupindentwidth', 'height', 'pagerheight', 'rowsheight', 'scrollbarsize', 'scrollmode', 'scrollfeedback', 'width', 'autosavestate', 'autoloadstate', 'columns', 'columngroups', 'columnsmenu', 'columnsresize', 'columnsautoresize', 'columnsreorder', 'disabled', 'editable', 'editmode', 'filter', 'filterable', 'groupable', 'groups', 'horizontalscrollbarstep', 'horizontalscrollbarlargestep', 'initrowdetails', 'keyboardnavigation', 'localization', 'pagesize', 'pagesizeoptions', 'pagermode', 'pagerbuttonscount', 'pageable', 'rowdetails', 'rowdetailstemplate', 'ready', 'rendered', 'renderstatusbar', 'rendertoolbar', 'rendergridrows', 'sortable', 'selectedrowindex', 'selectedrowindexes', 'source', 'sorttogglestates', 'updatedelay', 'virtualmode', 'verticalscrollbarstep', 'verticalscrollbarlargestep'];
        // jqxGridComponent events
        this.onBindingcomplete = new EventEmitter();
        this.onColumnresized = new EventEmitter();
        this.onColumnreordered = new EventEmitter();
        this.onColumnclick = new EventEmitter();
        this.onCellclick = new EventEmitter();
        this.onCelldoubleclick = new EventEmitter();
        this.onCellselect = new EventEmitter();
        this.onCellunselect = new EventEmitter();
        this.onCellvaluechanged = new EventEmitter();
        this.onCellbeginedit = new EventEmitter();
        this.onCellendedit = new EventEmitter();
        this.onFilter = new EventEmitter();
        this.onGroupschanged = new EventEmitter();
        this.onGroupexpand = new EventEmitter();
        this.onGroupcollapse = new EventEmitter();
        this.onPagechanged = new EventEmitter();
        this.onPagesizechanged = new EventEmitter();
        this.onRowclick = new EventEmitter();
        this.onRowdoubleclick = new EventEmitter();
        this.onRowselect = new EventEmitter();
        this.onRowunselect = new EventEmitter();
        this.onRowexpand = new EventEmitter();
        this.onRowcollapse = new EventEmitter();
        this.onSort = new EventEmitter();
        this.elementRef = containerElement;
    }
    jqxGridComponent.prototype.ngOnInit = function () {
        if (this.autoCreate) {
            this.createComponent();
        }
    };
    ;
    jqxGridComponent.prototype.ngOnChanges = function (changes) {
        if (this.host) {
            for (var i = 0; i < this.properties.length; i++) {
                var attrName = 'attr' + this.properties[i].substring(0, 1).toUpperCase() + this.properties[i].substring(1);
                var areEqual = void 0;
                if (this[attrName] !== undefined) {
                    if (typeof this[attrName] === 'object') {
                        if (this[attrName] instanceof Array) {
                            areEqual = this.arraysEqual(this[attrName], this.host.jqxGrid(this.properties[i]));
                        }
                        if (areEqual) {
                            return false;
                        }
                        this.host.jqxGrid(this.properties[i], this[attrName]);
                        continue;
                    }
                    if (this[attrName] !== this.host.jqxGrid(this.properties[i])) {
                        this.host.jqxGrid(this.properties[i], this[attrName]);
                    }
                }
            }
        }
    };
    jqxGridComponent.prototype.arraysEqual = function (attrValue, hostValue) {
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
    jqxGridComponent.prototype.manageAttributes = function () {
        var options = {};
        for (var i = 0; i < this.properties.length; i++) {
            var attrName = 'attr' + this.properties[i].substring(0, 1).toUpperCase() + this.properties[i].substring(1);
            if (this[attrName] !== undefined) {
                options[this.properties[i]] = this[attrName];
            }
        }
        return options;
    };
    jqxGridComponent.prototype.createComponent = function (options) {
        if (options) {
            JQXLite.extend(options, this.manageAttributes());
        }
        else {
            options = this.manageAttributes();
        }
        this.host = JQXLite(this.elementRef.nativeElement.firstChild);
        this.__wireEvents__();
        this.widgetObject = jqwidgets.createInstance(this.host, 'jqxGrid', options);
        this.__updateRect__();
    };
    jqxGridComponent.prototype.createWidget = function (options) {
        this.createComponent(options);
    };
    jqxGridComponent.prototype.__updateRect__ = function () {
        this.host.css({ width: this.attrWidth, height: this.attrHeight });
    };
    jqxGridComponent.prototype.setOptions = function (options) {
        this.host.jqxGrid('setOptions', options);
    };
    // jqxGridComponent properties
    jqxGridComponent.prototype.altrows = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('altrows', arg);
        }
        else {
            return this.host.jqxGrid('altrows');
        }
    };
    jqxGridComponent.prototype.altstart = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('altstart', arg);
        }
        else {
            return this.host.jqxGrid('altstart');
        }
    };
    jqxGridComponent.prototype.altstep = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('altstep', arg);
        }
        else {
            return this.host.jqxGrid('altstep');
        }
    };
    jqxGridComponent.prototype.autoshowloadelement = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('autoshowloadelement', arg);
        }
        else {
            return this.host.jqxGrid('autoshowloadelement');
        }
    };
    jqxGridComponent.prototype.autoshowfiltericon = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('autoshowfiltericon', arg);
        }
        else {
            return this.host.jqxGrid('autoshowfiltericon');
        }
    };
    jqxGridComponent.prototype.autoshowcolumnsmenubutton = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('autoshowcolumnsmenubutton', arg);
        }
        else {
            return this.host.jqxGrid('autoshowcolumnsmenubutton');
        }
    };
    jqxGridComponent.prototype.clipboard = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('clipboard', arg);
        }
        else {
            return this.host.jqxGrid('clipboard');
        }
    };
    jqxGridComponent.prototype.closeablegroups = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('closeablegroups', arg);
        }
        else {
            return this.host.jqxGrid('closeablegroups');
        }
    };
    jqxGridComponent.prototype.columnsmenuwidth = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('columnsmenuwidth', arg);
        }
        else {
            return this.host.jqxGrid('columnsmenuwidth');
        }
    };
    jqxGridComponent.prototype.columnmenuopening = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('columnmenuopening', arg);
        }
        else {
            return this.host.jqxGrid('columnmenuopening');
        }
    };
    jqxGridComponent.prototype.columnmenuclosing = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('columnmenuclosing', arg);
        }
        else {
            return this.host.jqxGrid('columnmenuclosing');
        }
    };
    jqxGridComponent.prototype.cellhover = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('cellhover', arg);
        }
        else {
            return this.host.jqxGrid('cellhover');
        }
    };
    jqxGridComponent.prototype.enablekeyboarddelete = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('enablekeyboarddelete', arg);
        }
        else {
            return this.host.jqxGrid('enablekeyboarddelete');
        }
    };
    jqxGridComponent.prototype.enableellipsis = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('enableellipsis', arg);
        }
        else {
            return this.host.jqxGrid('enableellipsis');
        }
    };
    jqxGridComponent.prototype.enablemousewheel = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('enablemousewheel', arg);
        }
        else {
            return this.host.jqxGrid('enablemousewheel');
        }
    };
    jqxGridComponent.prototype.enableanimations = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('enableanimations', arg);
        }
        else {
            return this.host.jqxGrid('enableanimations');
        }
    };
    jqxGridComponent.prototype.enabletooltips = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('enabletooltips', arg);
        }
        else {
            return this.host.jqxGrid('enabletooltips');
        }
    };
    jqxGridComponent.prototype.enablehover = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('enablehover', arg);
        }
        else {
            return this.host.jqxGrid('enablehover');
        }
    };
    jqxGridComponent.prototype.enablebrowserselection = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('enablebrowserselection', arg);
        }
        else {
            return this.host.jqxGrid('enablebrowserselection');
        }
    };
    jqxGridComponent.prototype.everpresentrowposition = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('everpresentrowposition', arg);
        }
        else {
            return this.host.jqxGrid('everpresentrowposition');
        }
    };
    jqxGridComponent.prototype.everpresentrowheight = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('everpresentrowheight', arg);
        }
        else {
            return this.host.jqxGrid('everpresentrowheight');
        }
    };
    jqxGridComponent.prototype.everpresentrowactions = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('everpresentrowactions', arg);
        }
        else {
            return this.host.jqxGrid('everpresentrowactions');
        }
    };
    jqxGridComponent.prototype.everpresentrowactionsmode = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('everpresentrowactionsmode', arg);
        }
        else {
            return this.host.jqxGrid('everpresentrowactionsmode');
        }
    };
    jqxGridComponent.prototype.filterrowheight = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('filterrowheight', arg);
        }
        else {
            return this.host.jqxGrid('filterrowheight');
        }
    };
    jqxGridComponent.prototype.filtermode = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('filtermode', arg);
        }
        else {
            return this.host.jqxGrid('filtermode');
        }
    };
    jqxGridComponent.prototype.groupsrenderer = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('groupsrenderer', arg);
        }
        else {
            return this.host.jqxGrid('groupsrenderer');
        }
    };
    jqxGridComponent.prototype.groupcolumnrenderer = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('groupcolumnrenderer', arg);
        }
        else {
            return this.host.jqxGrid('groupcolumnrenderer');
        }
    };
    jqxGridComponent.prototype.groupsexpandedbydefault = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('groupsexpandedbydefault', arg);
        }
        else {
            return this.host.jqxGrid('groupsexpandedbydefault');
        }
    };
    jqxGridComponent.prototype.handlekeyboardnavigation = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('handlekeyboardnavigation', arg);
        }
        else {
            return this.host.jqxGrid('handlekeyboardnavigation');
        }
    };
    jqxGridComponent.prototype.pagerrenderer = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('pagerrenderer', arg);
        }
        else {
            return this.host.jqxGrid('pagerrenderer');
        }
    };
    jqxGridComponent.prototype.rtl = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('rtl', arg);
        }
        else {
            return this.host.jqxGrid('rtl');
        }
    };
    jqxGridComponent.prototype.showdefaultloadelement = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('showdefaultloadelement', arg);
        }
        else {
            return this.host.jqxGrid('showdefaultloadelement');
        }
    };
    jqxGridComponent.prototype.showfiltercolumnbackground = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('showfiltercolumnbackground', arg);
        }
        else {
            return this.host.jqxGrid('showfiltercolumnbackground');
        }
    };
    jqxGridComponent.prototype.showfiltermenuitems = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('showfiltermenuitems', arg);
        }
        else {
            return this.host.jqxGrid('showfiltermenuitems');
        }
    };
    jqxGridComponent.prototype.showpinnedcolumnbackground = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('showpinnedcolumnbackground', arg);
        }
        else {
            return this.host.jqxGrid('showpinnedcolumnbackground');
        }
    };
    jqxGridComponent.prototype.showsortcolumnbackground = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('showsortcolumnbackground', arg);
        }
        else {
            return this.host.jqxGrid('showsortcolumnbackground');
        }
    };
    jqxGridComponent.prototype.showsortmenuitems = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('showsortmenuitems', arg);
        }
        else {
            return this.host.jqxGrid('showsortmenuitems');
        }
    };
    jqxGridComponent.prototype.showgroupmenuitems = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('showgroupmenuitems', arg);
        }
        else {
            return this.host.jqxGrid('showgroupmenuitems');
        }
    };
    jqxGridComponent.prototype.showrowdetailscolumn = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('showrowdetailscolumn', arg);
        }
        else {
            return this.host.jqxGrid('showrowdetailscolumn');
        }
    };
    jqxGridComponent.prototype.showheader = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('showheader', arg);
        }
        else {
            return this.host.jqxGrid('showheader');
        }
    };
    jqxGridComponent.prototype.showgroupsheader = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('showgroupsheader', arg);
        }
        else {
            return this.host.jqxGrid('showgroupsheader');
        }
    };
    jqxGridComponent.prototype.showaggregates = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('showaggregates', arg);
        }
        else {
            return this.host.jqxGrid('showaggregates');
        }
    };
    jqxGridComponent.prototype.showgroupaggregates = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('showgroupaggregates', arg);
        }
        else {
            return this.host.jqxGrid('showgroupaggregates');
        }
    };
    jqxGridComponent.prototype.showeverpresentrow = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('showeverpresentrow', arg);
        }
        else {
            return this.host.jqxGrid('showeverpresentrow');
        }
    };
    jqxGridComponent.prototype.showfilterrow = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('showfilterrow', arg);
        }
        else {
            return this.host.jqxGrid('showfilterrow');
        }
    };
    jqxGridComponent.prototype.showemptyrow = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('showemptyrow', arg);
        }
        else {
            return this.host.jqxGrid('showemptyrow');
        }
    };
    jqxGridComponent.prototype.showstatusbar = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('showstatusbar', arg);
        }
        else {
            return this.host.jqxGrid('showstatusbar');
        }
    };
    jqxGridComponent.prototype.statusbarheight = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('statusbarheight', arg);
        }
        else {
            return this.host.jqxGrid('statusbarheight');
        }
    };
    jqxGridComponent.prototype.showtoolbar = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('showtoolbar', arg);
        }
        else {
            return this.host.jqxGrid('showtoolbar');
        }
    };
    jqxGridComponent.prototype.selectionmode = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('selectionmode', arg);
        }
        else {
            return this.host.jqxGrid('selectionmode');
        }
    };
    jqxGridComponent.prototype.theme = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('theme', arg);
        }
        else {
            return this.host.jqxGrid('theme');
        }
    };
    jqxGridComponent.prototype.toolbarheight = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('toolbarheight', arg);
        }
        else {
            return this.host.jqxGrid('toolbarheight');
        }
    };
    jqxGridComponent.prototype.autoheight = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('autoheight', arg);
        }
        else {
            return this.host.jqxGrid('autoheight');
        }
    };
    jqxGridComponent.prototype.autorowheight = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('autorowheight', arg);
        }
        else {
            return this.host.jqxGrid('autorowheight');
        }
    };
    jqxGridComponent.prototype.columnsheight = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('columnsheight', arg);
        }
        else {
            return this.host.jqxGrid('columnsheight');
        }
    };
    jqxGridComponent.prototype.deferreddatafields = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('deferreddatafields', arg);
        }
        else {
            return this.host.jqxGrid('deferreddatafields');
        }
    };
    jqxGridComponent.prototype.groupsheaderheight = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('groupsheaderheight', arg);
        }
        else {
            return this.host.jqxGrid('groupsheaderheight');
        }
    };
    jqxGridComponent.prototype.groupindentwidth = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('groupindentwidth', arg);
        }
        else {
            return this.host.jqxGrid('groupindentwidth');
        }
    };
    jqxGridComponent.prototype.height = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('height', arg);
        }
        else {
            return this.host.jqxGrid('height');
        }
    };
    jqxGridComponent.prototype.pagerheight = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('pagerheight', arg);
        }
        else {
            return this.host.jqxGrid('pagerheight');
        }
    };
    jqxGridComponent.prototype.rowsheight = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('rowsheight', arg);
        }
        else {
            return this.host.jqxGrid('rowsheight');
        }
    };
    jqxGridComponent.prototype.scrollbarsize = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('scrollbarsize', arg);
        }
        else {
            return this.host.jqxGrid('scrollbarsize');
        }
    };
    jqxGridComponent.prototype.scrollmode = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('scrollmode', arg);
        }
        else {
            return this.host.jqxGrid('scrollmode');
        }
    };
    jqxGridComponent.prototype.scrollfeedback = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('scrollfeedback', arg);
        }
        else {
            return this.host.jqxGrid('scrollfeedback');
        }
    };
    jqxGridComponent.prototype.width = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('width', arg);
        }
        else {
            return this.host.jqxGrid('width');
        }
    };
    jqxGridComponent.prototype.autosavestate = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('autosavestate', arg);
        }
        else {
            return this.host.jqxGrid('autosavestate');
        }
    };
    jqxGridComponent.prototype.autoloadstate = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('autoloadstate', arg);
        }
        else {
            return this.host.jqxGrid('autoloadstate');
        }
    };
    jqxGridComponent.prototype.columns = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('columns', arg);
        }
        else {
            return this.host.jqxGrid('columns');
        }
    };
    jqxGridComponent.prototype.columngroups = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('columngroups', arg);
        }
        else {
            return this.host.jqxGrid('columngroups');
        }
    };
    jqxGridComponent.prototype.columnsmenu = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('columnsmenu', arg);
        }
        else {
            return this.host.jqxGrid('columnsmenu');
        }
    };
    jqxGridComponent.prototype.columnsresize = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('columnsresize', arg);
        }
        else {
            return this.host.jqxGrid('columnsresize');
        }
    };
    jqxGridComponent.prototype.columnsautoresize = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('columnsautoresize', arg);
        }
        else {
            return this.host.jqxGrid('columnsautoresize');
        }
    };
    jqxGridComponent.prototype.columnsreorder = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('columnsreorder', arg);
        }
        else {
            return this.host.jqxGrid('columnsreorder');
        }
    };
    jqxGridComponent.prototype.disabled = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('disabled', arg);
        }
        else {
            return this.host.jqxGrid('disabled');
        }
    };
    jqxGridComponent.prototype.editable = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('editable', arg);
        }
        else {
            return this.host.jqxGrid('editable');
        }
    };
    jqxGridComponent.prototype.editmode = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('editmode', arg);
        }
        else {
            return this.host.jqxGrid('editmode');
        }
    };
    jqxGridComponent.prototype.filter = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('filter', arg);
        }
        else {
            return this.host.jqxGrid('filter');
        }
    };
    jqxGridComponent.prototype.filterable = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('filterable', arg);
        }
        else {
            return this.host.jqxGrid('filterable');
        }
    };
    jqxGridComponent.prototype.groupable = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('groupable', arg);
        }
        else {
            return this.host.jqxGrid('groupable');
        }
    };
    jqxGridComponent.prototype.groups = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('groups', arg);
        }
        else {
            return this.host.jqxGrid('groups');
        }
    };
    jqxGridComponent.prototype.horizontalscrollbarstep = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('horizontalscrollbarstep', arg);
        }
        else {
            return this.host.jqxGrid('horizontalscrollbarstep');
        }
    };
    jqxGridComponent.prototype.horizontalscrollbarlargestep = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('horizontalscrollbarlargestep', arg);
        }
        else {
            return this.host.jqxGrid('horizontalscrollbarlargestep');
        }
    };
    jqxGridComponent.prototype.initrowdetails = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('initrowdetails', arg);
        }
        else {
            return this.host.jqxGrid('initrowdetails');
        }
    };
    jqxGridComponent.prototype.keyboardnavigation = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('keyboardnavigation', arg);
        }
        else {
            return this.host.jqxGrid('keyboardnavigation');
        }
    };
    jqxGridComponent.prototype.localization = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('localization', arg);
        }
        else {
            return this.host.jqxGrid('localization');
        }
    };
    jqxGridComponent.prototype.pagesize = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('pagesize', arg);
        }
        else {
            return this.host.jqxGrid('pagesize');
        }
    };
    jqxGridComponent.prototype.pagesizeoptions = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('pagesizeoptions', arg);
        }
        else {
            return this.host.jqxGrid('pagesizeoptions');
        }
    };
    jqxGridComponent.prototype.pagermode = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('pagermode', arg);
        }
        else {
            return this.host.jqxGrid('pagermode');
        }
    };
    jqxGridComponent.prototype.pagerbuttonscount = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('pagerbuttonscount', arg);
        }
        else {
            return this.host.jqxGrid('pagerbuttonscount');
        }
    };
    jqxGridComponent.prototype.pageable = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('pageable', arg);
        }
        else {
            return this.host.jqxGrid('pageable');
        }
    };
    jqxGridComponent.prototype.rowdetails = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('rowdetails', arg);
        }
        else {
            return this.host.jqxGrid('rowdetails');
        }
    };
    jqxGridComponent.prototype.rowdetailstemplate = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('rowdetailstemplate', arg);
        }
        else {
            return this.host.jqxGrid('rowdetailstemplate');
        }
    };
    jqxGridComponent.prototype.ready = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('ready', arg);
        }
        else {
            return this.host.jqxGrid('ready');
        }
    };
    jqxGridComponent.prototype.rendered = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('rendered', arg);
        }
        else {
            return this.host.jqxGrid('rendered');
        }
    };
    jqxGridComponent.prototype.renderstatusbar = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('renderstatusbar', arg);
        }
        else {
            return this.host.jqxGrid('renderstatusbar');
        }
    };
    jqxGridComponent.prototype.rendertoolbar = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('rendertoolbar', arg);
        }
        else {
            return this.host.jqxGrid('rendertoolbar');
        }
    };
    jqxGridComponent.prototype.rendergridrows = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('rendergridrows', arg);
        }
        else {
            return this.host.jqxGrid('rendergridrows');
        }
    };
    jqxGridComponent.prototype.sortable = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('sortable', arg);
        }
        else {
            return this.host.jqxGrid('sortable');
        }
    };
    jqxGridComponent.prototype.selectedrowindex = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('selectedrowindex', arg);
        }
        else {
            return this.host.jqxGrid('selectedrowindex');
        }
    };
    jqxGridComponent.prototype.selectedrowindexes = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('selectedrowindexes', arg);
        }
        else {
            return this.host.jqxGrid('selectedrowindexes');
        }
    };
    jqxGridComponent.prototype.source = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('source', arg);
        }
        else {
            return this.host.jqxGrid('source');
        }
    };
    jqxGridComponent.prototype.sorttogglestates = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('sorttogglestates', arg);
        }
        else {
            return this.host.jqxGrid('sorttogglestates');
        }
    };
    jqxGridComponent.prototype.updatedelay = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('updatedelay', arg);
        }
        else {
            return this.host.jqxGrid('updatedelay');
        }
    };
    jqxGridComponent.prototype.virtualmode = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('virtualmode', arg);
        }
        else {
            return this.host.jqxGrid('virtualmode');
        }
    };
    jqxGridComponent.prototype.verticalscrollbarstep = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('verticalscrollbarstep', arg);
        }
        else {
            return this.host.jqxGrid('verticalscrollbarstep');
        }
    };
    jqxGridComponent.prototype.verticalscrollbarlargestep = function (arg) {
        if (arg !== undefined) {
            this.host.jqxGrid('verticalscrollbarlargestep', arg);
        }
        else {
            return this.host.jqxGrid('verticalscrollbarlargestep');
        }
    };
    // jqxGridComponent functions
    jqxGridComponent.prototype.autoresizecolumns = function (type) {
        this.host.jqxGrid('autoresizecolumns', type);
    };
    jqxGridComponent.prototype.autoresizecolumn = function (dataField, type) {
        this.host.jqxGrid('autoresizecolumn', dataField, type);
    };
    jqxGridComponent.prototype.beginupdate = function () {
        this.host.jqxGrid('beginupdate');
    };
    jqxGridComponent.prototype.clear = function () {
        this.host.jqxGrid('clear');
    };
    jqxGridComponent.prototype.destroy = function () {
        this.host.jqxGrid('destroy');
    };
    jqxGridComponent.prototype.endupdate = function () {
        this.host.jqxGrid('endupdate');
    };
    jqxGridComponent.prototype.ensurerowvisible = function (rowBoundIndex) {
        this.host.jqxGrid('ensurerowvisible', rowBoundIndex);
    };
    jqxGridComponent.prototype.focus = function () {
        this.host.jqxGrid('focus');
    };
    jqxGridComponent.prototype.getcolumnindex = function (dataField) {
        return this.host.jqxGrid('getcolumnindex', dataField);
    };
    jqxGridComponent.prototype.getcolumn = function (dataField) {
        return this.host.jqxGrid('getcolumn', dataField);
    };
    jqxGridComponent.prototype.getcolumnproperty = function (dataField, propertyName) {
        return this.host.jqxGrid('getcolumnproperty', dataField, propertyName);
    };
    jqxGridComponent.prototype.getrowid = function (rowBoundIndex) {
        return this.host.jqxGrid('getrowid', rowBoundIndex);
    };
    jqxGridComponent.prototype.getrowdata = function (rowBoundIndex) {
        return this.host.jqxGrid('getrowdata', rowBoundIndex);
    };
    jqxGridComponent.prototype.getrowdatabyid = function (rowID) {
        return this.host.jqxGrid('getrowdatabyid', rowID);
    };
    jqxGridComponent.prototype.getrowboundindexbyid = function (rowID) {
        return this.host.jqxGrid('getrowboundindexbyid', rowID);
    };
    jqxGridComponent.prototype.getrowboundindex = function (rowDisplayIndex) {
        return this.host.jqxGrid('getrowboundindex', rowDisplayIndex);
    };
    jqxGridComponent.prototype.getrows = function () {
        return this.host.jqxGrid('getrows');
    };
    jqxGridComponent.prototype.getboundrows = function () {
        return this.host.jqxGrid('getboundrows');
    };
    jqxGridComponent.prototype.getdisplayrows = function () {
        return this.host.jqxGrid('getdisplayrows');
    };
    jqxGridComponent.prototype.getdatainformation = function () {
        return this.host.jqxGrid('getdatainformation');
    };
    jqxGridComponent.prototype.getsortinformation = function () {
        return this.host.jqxGrid('getsortinformation');
    };
    jqxGridComponent.prototype.getpaginginformation = function () {
        return this.host.jqxGrid('getpaginginformation');
    };
    jqxGridComponent.prototype.hidecolumn = function (dataField) {
        this.host.jqxGrid('hidecolumn', dataField);
    };
    jqxGridComponent.prototype.hideloadelement = function () {
        this.host.jqxGrid('hideloadelement');
    };
    jqxGridComponent.prototype.hiderowdetails = function (rowBoundIndex) {
        this.host.jqxGrid('hiderowdetails', rowBoundIndex);
    };
    jqxGridComponent.prototype.iscolumnvisible = function (dataField) {
        return this.host.jqxGrid('iscolumnvisible', dataField);
    };
    jqxGridComponent.prototype.iscolumnpinned = function (dataField) {
        return this.host.jqxGrid('iscolumnpinned', dataField);
    };
    jqxGridComponent.prototype.localizestrings = function (localizationObject) {
        this.host.jqxGrid('localizestrings', localizationObject);
    };
    jqxGridComponent.prototype.pincolumn = function (dataField) {
        this.host.jqxGrid('pincolumn', dataField);
    };
    jqxGridComponent.prototype.refreshdata = function () {
        this.host.jqxGrid('refreshdata');
    };
    jqxGridComponent.prototype.refresh = function () {
        this.host.jqxGrid('refresh');
    };
    jqxGridComponent.prototype.render = function () {
        this.host.jqxGrid('render');
    };
    jqxGridComponent.prototype.scrolloffset = function (top, left) {
        this.host.jqxGrid('scrolloffset', top, left);
    };
    jqxGridComponent.prototype.scrollposition = function () {
        return this.host.jqxGrid('scrollposition');
    };
    jqxGridComponent.prototype.showloadelement = function () {
        this.host.jqxGrid('showloadelement');
    };
    jqxGridComponent.prototype.showrowdetails = function (rowBoundIndex) {
        this.host.jqxGrid('showrowdetails', rowBoundIndex);
    };
    jqxGridComponent.prototype.setcolumnindex = function (dataField, index) {
        this.host.jqxGrid('setcolumnindex', dataField, index);
    };
    jqxGridComponent.prototype.setcolumnproperty = function (dataField, propertyName, propertyValue) {
        this.host.jqxGrid('setcolumnproperty', dataField, propertyName, propertyValue);
    };
    jqxGridComponent.prototype.showcolumn = function (dataField) {
        this.host.jqxGrid('showcolumn', dataField);
    };
    jqxGridComponent.prototype.unpincolumn = function (dataField) {
        this.host.jqxGrid('unpincolumn', dataField);
    };
    jqxGridComponent.prototype.updatebounddata = function (type) {
        this.host.jqxGrid('updatebounddata', type);
    };
    jqxGridComponent.prototype.updating = function () {
        return this.host.jqxGrid('updating');
    };
    jqxGridComponent.prototype.getsortcolumn = function () {
        return this.host.jqxGrid('getsortcolumn');
    };
    jqxGridComponent.prototype.removesort = function () {
        this.host.jqxGrid('removesort');
    };
    jqxGridComponent.prototype.sortby = function (dataField, sortOrder) {
        this.host.jqxGrid('sortby', dataField, sortOrder);
    };
    jqxGridComponent.prototype.addgroup = function (dataField) {
        this.host.jqxGrid('addgroup', dataField);
    };
    jqxGridComponent.prototype.cleargroups = function () {
        this.host.jqxGrid('cleargroups');
    };
    jqxGridComponent.prototype.collapsegroup = function (group) {
        this.host.jqxGrid('collapsegroup', group);
    };
    jqxGridComponent.prototype.collapseallgroups = function () {
        this.host.jqxGrid('collapseallgroups');
    };
    jqxGridComponent.prototype.expandallgroups = function () {
        this.host.jqxGrid('expandallgroups');
    };
    jqxGridComponent.prototype.expandgroup = function (group) {
        this.host.jqxGrid('expandgroup', group);
    };
    jqxGridComponent.prototype.getrootgroupscount = function () {
        return this.host.jqxGrid('getrootgroupscount');
    };
    jqxGridComponent.prototype.getgroup = function (groupIndex) {
        return this.host.jqxGrid('getgroup', groupIndex);
    };
    jqxGridComponent.prototype.insertgroup = function (groupIndex, dataField) {
        this.host.jqxGrid('insertgroup', groupIndex, dataField);
    };
    jqxGridComponent.prototype.iscolumngroupable = function () {
        return this.host.jqxGrid('iscolumngroupable');
    };
    jqxGridComponent.prototype.removegroupat = function (groupIndex) {
        this.host.jqxGrid('removegroupat', groupIndex);
    };
    jqxGridComponent.prototype.removegroup = function (dataField) {
        this.host.jqxGrid('removegroup', dataField);
    };
    jqxGridComponent.prototype.addfilter = function (dataField, filterGroup, refreshGrid) {
        this.host.jqxGrid('addfilter', dataField, filterGroup, refreshGrid);
    };
    jqxGridComponent.prototype.applyfilters = function () {
        this.host.jqxGrid('applyfilters');
    };
    jqxGridComponent.prototype.clearfilters = function () {
        this.host.jqxGrid('clearfilters');
    };
    jqxGridComponent.prototype.getfilterinformation = function () {
        return this.host.jqxGrid('getfilterinformation');
    };
    jqxGridComponent.prototype.getcolumnat = function (index) {
        return this.host.jqxGrid('getcolumnat', index);
    };
    jqxGridComponent.prototype.removefilter = function (dataField, refreshGrid) {
        this.host.jqxGrid('removefilter', dataField, refreshGrid);
    };
    jqxGridComponent.prototype.refreshfilterrow = function () {
        this.host.jqxGrid('refreshfilterrow');
    };
    jqxGridComponent.prototype.gotopage = function (pageNumber) {
        this.host.jqxGrid('gotopage', pageNumber);
    };
    jqxGridComponent.prototype.gotoprevpage = function () {
        this.host.jqxGrid('gotoprevpage');
    };
    jqxGridComponent.prototype.gotonextpage = function () {
        this.host.jqxGrid('gotonextpage');
    };
    jqxGridComponent.prototype.addrow = function (rowIds, data, rowPosition) {
        this.host.jqxGrid('addrow', rowIds, data, rowPosition);
    };
    jqxGridComponent.prototype.begincelledit = function (rowBoundIndex, dataField) {
        this.host.jqxGrid('begincelledit', rowBoundIndex, dataField);
    };
    jqxGridComponent.prototype.beginrowedit = function (rowBoundIndex) {
        this.host.jqxGrid('beginrowedit', rowBoundIndex);
    };
    jqxGridComponent.prototype.closemenu = function () {
        this.host.jqxGrid('closemenu');
    };
    jqxGridComponent.prototype.deleterow = function (rowIds) {
        this.host.jqxGrid('deleterow', rowIds);
    };
    jqxGridComponent.prototype.endcelledit = function (rowBoundIndex, dataField, confirmChanges) {
        this.host.jqxGrid('endcelledit', rowBoundIndex, dataField, confirmChanges);
    };
    jqxGridComponent.prototype.endrowedit = function (rowBoundIndex, confirmChanges) {
        this.host.jqxGrid('endrowedit', rowBoundIndex, confirmChanges);
    };
    jqxGridComponent.prototype.getcell = function (rowBoundIndex, datafield) {
        return this.host.jqxGrid('getcell', rowBoundIndex, datafield);
    };
    jqxGridComponent.prototype.getcellatposition = function (left, top) {
        return this.host.jqxGrid('getcellatposition', left, top);
    };
    jqxGridComponent.prototype.getcelltext = function (rowBoundIndex, dataField) {
        return this.host.jqxGrid('getcelltext', rowBoundIndex, dataField);
    };
    jqxGridComponent.prototype.getcelltextbyid = function (rowID, dataField) {
        return this.host.jqxGrid('getcelltextbyid', rowID, dataField);
    };
    jqxGridComponent.prototype.getcellvaluebyid = function (rowID, dataField) {
        return this.host.jqxGrid('getcellvaluebyid', rowID, dataField);
    };
    jqxGridComponent.prototype.getcellvalue = function (rowBoundIndex, dataField) {
        return this.host.jqxGrid('getcellvalue', rowBoundIndex, dataField);
    };
    jqxGridComponent.prototype.isBindingCompleted = function () {
        return this.host.jqxGrid('isBindingCompleted');
    };
    jqxGridComponent.prototype.openmenu = function (dataField) {
        this.host.jqxGrid('openmenu', dataField);
    };
    jqxGridComponent.prototype.setcellvalue = function (rowBoundIndex, dataField, value) {
        this.host.jqxGrid('setcellvalue', rowBoundIndex, dataField, value);
    };
    jqxGridComponent.prototype.setcellvaluebyid = function (rowID, dataField, value) {
        this.host.jqxGrid('setcellvaluebyid', rowID, dataField, value);
    };
    jqxGridComponent.prototype.showvalidationpopup = function (rowBoundIndex, dataField, validationMessage) {
        this.host.jqxGrid('showvalidationpopup', rowBoundIndex, dataField, validationMessage);
    };
    jqxGridComponent.prototype.updaterow = function (rowIds, data) {
        this.host.jqxGrid('updaterow', rowIds, data);
    };
    jqxGridComponent.prototype.clearselection = function () {
        this.host.jqxGrid('clearselection');
    };
    jqxGridComponent.prototype.getselectedrowindex = function () {
        return this.host.jqxGrid('getselectedrowindex');
    };
    jqxGridComponent.prototype.getselectedrowindexes = function () {
        return this.host.jqxGrid('getselectedrowindexes');
    };
    jqxGridComponent.prototype.getselectedcell = function () {
        return this.host.jqxGrid('getselectedcell');
    };
    jqxGridComponent.prototype.getselectedcells = function () {
        return this.host.jqxGrid('getselectedcells');
    };
    jqxGridComponent.prototype.selectcell = function (rowBoundIndex, dataField) {
        this.host.jqxGrid('selectcell', rowBoundIndex, dataField);
    };
    jqxGridComponent.prototype.selectallrows = function () {
        this.host.jqxGrid('selectallrows');
    };
    jqxGridComponent.prototype.selectrow = function (rowBoundIndex) {
        this.host.jqxGrid('selectrow', rowBoundIndex);
    };
    jqxGridComponent.prototype.unselectrow = function (rowBoundIndex) {
        this.host.jqxGrid('unselectrow', rowBoundIndex);
    };
    jqxGridComponent.prototype.unselectcell = function (rowBoundIndex, dataField) {
        this.host.jqxGrid('unselectcell', rowBoundIndex, dataField);
    };
    jqxGridComponent.prototype.getcolumnaggregateddata = function (dataField, aggregates) {
        return this.host.jqxGrid('getcolumnaggregateddata', dataField, aggregates);
    };
    jqxGridComponent.prototype.refreshaggregates = function () {
        this.host.jqxGrid('refreshaggregates');
    };
    jqxGridComponent.prototype.renderaggregates = function () {
        this.host.jqxGrid('renderaggregates');
    };
    jqxGridComponent.prototype.exportdata = function (dataType, fileName, exportHeader, rows, exportHiddenColumns, serverURL, charSet) {
        return this.host.jqxGrid('exportdata', dataType, fileName, exportHeader, rows, exportHiddenColumns, serverURL, charSet);
    };
    jqxGridComponent.prototype.getstate = function () {
        return this.host.jqxGrid('getstate');
    };
    jqxGridComponent.prototype.loadstate = function (stateObject) {
        this.host.jqxGrid('loadstate', stateObject);
    };
    jqxGridComponent.prototype.savestate = function () {
        return this.host.jqxGrid('savestate');
    };
    jqxGridComponent.prototype.__wireEvents__ = function () {
        var _this = this;
        this.host.on('bindingcomplete', function (eventData) { _this.onBindingcomplete.emit(eventData); });
        this.host.on('columnresized', function (eventData) { _this.onColumnresized.emit(eventData); });
        this.host.on('columnreordered', function (eventData) { _this.onColumnreordered.emit(eventData); });
        this.host.on('columnclick', function (eventData) { _this.onColumnclick.emit(eventData); });
        this.host.on('cellclick', function (eventData) { _this.onCellclick.emit(eventData); });
        this.host.on('celldoubleclick', function (eventData) { _this.onCelldoubleclick.emit(eventData); });
        this.host.on('cellselect', function (eventData) { _this.onCellselect.emit(eventData); });
        this.host.on('cellunselect', function (eventData) { _this.onCellunselect.emit(eventData); });
        this.host.on('cellvaluechanged', function (eventData) { _this.onCellvaluechanged.emit(eventData); });
        this.host.on('cellbeginedit', function (eventData) { _this.onCellbeginedit.emit(eventData); });
        this.host.on('cellendedit', function (eventData) { _this.onCellendedit.emit(eventData); });
        this.host.on('filter', function (eventData) { _this.onFilter.emit(eventData); });
        this.host.on('groupschanged', function (eventData) { _this.onGroupschanged.emit(eventData); });
        this.host.on('groupexpand', function (eventData) { _this.onGroupexpand.emit(eventData); });
        this.host.on('groupcollapse', function (eventData) { _this.onGroupcollapse.emit(eventData); });
        this.host.on('pagechanged', function (eventData) { _this.onPagechanged.emit(eventData); });
        this.host.on('pagesizechanged', function (eventData) { _this.onPagesizechanged.emit(eventData); });
        this.host.on('rowclick', function (eventData) { _this.onRowclick.emit(eventData); });
        this.host.on('rowdoubleclick', function (eventData) { _this.onRowdoubleclick.emit(eventData); });
        this.host.on('rowselect', function (eventData) { _this.onRowselect.emit(eventData); });
        this.host.on('rowunselect', function (eventData) { _this.onRowunselect.emit(eventData); });
        this.host.on('rowexpand', function (eventData) { _this.onRowexpand.emit(eventData); });
        this.host.on('rowcollapse', function (eventData) { _this.onRowcollapse.emit(eventData); });
        this.host.on('sort', function (eventData) { _this.onSort.emit(eventData); });
    };
    return jqxGridComponent;
}()); //jqxGridComponent
__decorate([
    Input('altrows'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrAltrows", void 0);
__decorate([
    Input('altstart'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrAltstart", void 0);
__decorate([
    Input('altstep'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrAltstep", void 0);
__decorate([
    Input('autoshowloadelement'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrAutoshowloadelement", void 0);
__decorate([
    Input('autoshowfiltericon'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrAutoshowfiltericon", void 0);
__decorate([
    Input('autoshowcolumnsmenubutton'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrAutoshowcolumnsmenubutton", void 0);
__decorate([
    Input('clipboard'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrClipboard", void 0);
__decorate([
    Input('closeablegroups'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrCloseablegroups", void 0);
__decorate([
    Input('columnsmenuwidth'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrColumnsmenuwidth", void 0);
__decorate([
    Input('columnmenuopening'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrColumnmenuopening", void 0);
__decorate([
    Input('columnmenuclosing'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrColumnmenuclosing", void 0);
__decorate([
    Input('cellhover'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrCellhover", void 0);
__decorate([
    Input('enablekeyboarddelete'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrEnablekeyboarddelete", void 0);
__decorate([
    Input('enableellipsis'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrEnableellipsis", void 0);
__decorate([
    Input('enablemousewheel'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrEnablemousewheel", void 0);
__decorate([
    Input('enableanimations'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrEnableanimations", void 0);
__decorate([
    Input('enabletooltips'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrEnabletooltips", void 0);
__decorate([
    Input('enablehover'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrEnablehover", void 0);
__decorate([
    Input('enablebrowserselection'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrEnablebrowserselection", void 0);
__decorate([
    Input('everpresentrowposition'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrEverpresentrowposition", void 0);
__decorate([
    Input('everpresentrowheight'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrEverpresentrowheight", void 0);
__decorate([
    Input('everpresentrowactions'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrEverpresentrowactions", void 0);
__decorate([
    Input('everpresentrowactionsmode'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrEverpresentrowactionsmode", void 0);
__decorate([
    Input('filterrowheight'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrFilterrowheight", void 0);
__decorate([
    Input('filtermode'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrFiltermode", void 0);
__decorate([
    Input('groupsrenderer'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrGroupsrenderer", void 0);
__decorate([
    Input('groupcolumnrenderer'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrGroupcolumnrenderer", void 0);
__decorate([
    Input('groupsexpandedbydefault'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrGroupsexpandedbydefault", void 0);
__decorate([
    Input('handlekeyboardnavigation'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrHandlekeyboardnavigation", void 0);
__decorate([
    Input('pagerrenderer'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrPagerrenderer", void 0);
__decorate([
    Input('rtl'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrRtl", void 0);
__decorate([
    Input('showdefaultloadelement'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrShowdefaultloadelement", void 0);
__decorate([
    Input('showfiltercolumnbackground'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrShowfiltercolumnbackground", void 0);
__decorate([
    Input('showfiltermenuitems'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrShowfiltermenuitems", void 0);
__decorate([
    Input('showpinnedcolumnbackground'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrShowpinnedcolumnbackground", void 0);
__decorate([
    Input('showsortcolumnbackground'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrShowsortcolumnbackground", void 0);
__decorate([
    Input('showsortmenuitems'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrShowsortmenuitems", void 0);
__decorate([
    Input('showgroupmenuitems'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrShowgroupmenuitems", void 0);
__decorate([
    Input('showrowdetailscolumn'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrShowrowdetailscolumn", void 0);
__decorate([
    Input('showheader'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrShowheader", void 0);
__decorate([
    Input('showgroupsheader'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrShowgroupsheader", void 0);
__decorate([
    Input('showaggregates'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrShowaggregates", void 0);
__decorate([
    Input('showgroupaggregates'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrShowgroupaggregates", void 0);
__decorate([
    Input('showeverpresentrow'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrShoweverpresentrow", void 0);
__decorate([
    Input('showfilterrow'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrShowfilterrow", void 0);
__decorate([
    Input('showemptyrow'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrShowemptyrow", void 0);
__decorate([
    Input('showstatusbar'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrShowstatusbar", void 0);
__decorate([
    Input('statusbarheight'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrStatusbarheight", void 0);
__decorate([
    Input('showtoolbar'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrShowtoolbar", void 0);
__decorate([
    Input('selectionmode'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrSelectionmode", void 0);
__decorate([
    Input('theme'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrTheme", void 0);
__decorate([
    Input('toolbarheight'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrToolbarheight", void 0);
__decorate([
    Input('autoheight'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrAutoheight", void 0);
__decorate([
    Input('autorowheight'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrAutorowheight", void 0);
__decorate([
    Input('columnsheight'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrColumnsheight", void 0);
__decorate([
    Input('deferreddatafields'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrDeferreddatafields", void 0);
__decorate([
    Input('groupsheaderheight'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrGroupsheaderheight", void 0);
__decorate([
    Input('groupindentwidth'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrGroupindentwidth", void 0);
__decorate([
    Input('pagerheight'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrPagerheight", void 0);
__decorate([
    Input('rowsheight'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrRowsheight", void 0);
__decorate([
    Input('scrollbarsize'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrScrollbarsize", void 0);
__decorate([
    Input('scrollmode'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrScrollmode", void 0);
__decorate([
    Input('scrollfeedback'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrScrollfeedback", void 0);
__decorate([
    Input('autosavestate'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrAutosavestate", void 0);
__decorate([
    Input('autoloadstate'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrAutoloadstate", void 0);
__decorate([
    Input('columns'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrColumns", void 0);
__decorate([
    Input('columngroups'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrColumngroups", void 0);
__decorate([
    Input('columnsmenu'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrColumnsmenu", void 0);
__decorate([
    Input('columnsresize'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrColumnsresize", void 0);
__decorate([
    Input('columnsautoresize'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrColumnsautoresize", void 0);
__decorate([
    Input('columnsreorder'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrColumnsreorder", void 0);
__decorate([
    Input('disabled'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrDisabled", void 0);
__decorate([
    Input('editable'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrEditable", void 0);
__decorate([
    Input('editmode'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrEditmode", void 0);
__decorate([
    Input('filter'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrFilter", void 0);
__decorate([
    Input('filterable'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrFilterable", void 0);
__decorate([
    Input('groupable'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrGroupable", void 0);
__decorate([
    Input('groups'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrGroups", void 0);
__decorate([
    Input('horizontalscrollbarstep'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrHorizontalscrollbarstep", void 0);
__decorate([
    Input('horizontalscrollbarlargestep'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrHorizontalscrollbarlargestep", void 0);
__decorate([
    Input('initrowdetails'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrInitrowdetails", void 0);
__decorate([
    Input('keyboardnavigation'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrKeyboardnavigation", void 0);
__decorate([
    Input('localization'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrLocalization", void 0);
__decorate([
    Input('pagesize'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrPagesize", void 0);
__decorate([
    Input('pagesizeoptions'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrPagesizeoptions", void 0);
__decorate([
    Input('pagermode'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrPagermode", void 0);
__decorate([
    Input('pagerbuttonscount'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrPagerbuttonscount", void 0);
__decorate([
    Input('pageable'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrPageable", void 0);
__decorate([
    Input('rowdetails'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrRowdetails", void 0);
__decorate([
    Input('rowdetailstemplate'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrRowdetailstemplate", void 0);
__decorate([
    Input('ready'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrReady", void 0);
__decorate([
    Input('rendered'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrRendered", void 0);
__decorate([
    Input('renderstatusbar'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrRenderstatusbar", void 0);
__decorate([
    Input('rendertoolbar'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrRendertoolbar", void 0);
__decorate([
    Input('rendergridrows'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrRendergridrows", void 0);
__decorate([
    Input('sortable'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrSortable", void 0);
__decorate([
    Input('selectedrowindex'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrSelectedrowindex", void 0);
__decorate([
    Input('selectedrowindexes'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrSelectedrowindexes", void 0);
__decorate([
    Input('source'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrSource", void 0);
__decorate([
    Input('sorttogglestates'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrSorttogglestates", void 0);
__decorate([
    Input('updatedelay'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrUpdatedelay", void 0);
__decorate([
    Input('virtualmode'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrVirtualmode", void 0);
__decorate([
    Input('verticalscrollbarstep'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrVerticalscrollbarstep", void 0);
__decorate([
    Input('verticalscrollbarlargestep'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrVerticalscrollbarlargestep", void 0);
__decorate([
    Input('width'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrWidth", void 0);
__decorate([
    Input('height'),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "attrHeight", void 0);
__decorate([
    Input('auto-create'),
    __metadata("design:type", Boolean)
], jqxGridComponent.prototype, "autoCreate", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "onBindingcomplete", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "onColumnresized", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "onColumnreordered", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "onColumnclick", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "onCellclick", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "onCelldoubleclick", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "onCellselect", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "onCellunselect", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "onCellvaluechanged", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "onCellbeginedit", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "onCellendedit", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "onFilter", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "onGroupschanged", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "onGroupexpand", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "onGroupcollapse", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "onPagechanged", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "onPagesizechanged", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "onRowclick", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "onRowdoubleclick", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "onRowselect", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "onRowunselect", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "onRowexpand", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "onRowcollapse", void 0);
__decorate([
    Output(),
    __metadata("design:type", Object)
], jqxGridComponent.prototype, "onSort", void 0);
jqxGridComponent = __decorate([
    Component({
        selector: 'jqxGrid',
        template: '<div><ng-content></ng-content></div>'
    }),
    __metadata("design:paramtypes", [ElementRef])
], jqxGridComponent);
export { jqxGridComponent };
//# sourceMappingURL=angular_jqxgrid.js.map