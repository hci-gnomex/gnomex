package views.util.grid
{
	import flash.display.Sprite;
	import flash.events.ContextMenuEvent;
	import flash.events.KeyboardEvent;
	import flash.events.MouseEvent;
	import flash.events.TextEvent;
	import flash.system.System;
	import flash.ui.ContextMenu;
	import flash.ui.ContextMenuItem;
	import flash.utils.getQualifiedClassName;
	
	import mx.collections.ArrayCollection;
	import mx.collections.GroupingCollection;
	import mx.collections.HierarchicalCollectionView;
	import mx.collections.XMLListCollection;
	import mx.controls.AdvancedDataGrid;
	import mx.controls.TextArea;
	import mx.controls.advancedDataGridClasses.AdvancedDataGridColumn;
	import mx.controls.dataGridClasses.DataGridColumn;
	import mx.core.UIComponent;
	import mx.managers.FocusManager;
	import mx.utils.ObjectUtil;
	import mx.utils.StringUtil;
	
	import views.util.grid.DataGridUtil;
	
	public class CopyPasteDataGrid extends AdvancedDataGrid
	{
		
		protected var _lastDataProvider:Object;
		protected var _textArea:TextArea;
		
		// Context menu items
		protected var _contextMenu:ContextMenu;
		protected var _copyMenuItem:ContextMenuItem;
		protected var _dupMenuItem:ContextMenuItem;
		protected var _expTSVMenuItem:ContextMenuItem;
		protected var _insertLineMenuItem:ContextMenuItem;
		protected var _addLineMenuItem:ContextMenuItem;
		protected var _delMenuItem:ContextMenuItem;
		protected var _clearAllMenuItem:ContextMenuItem;
		protected var _undoMenuItem:ContextMenuItem;
		
		// Customizable functions (Paste, add row, delete selected rows, and clear all)
		protected var _pasteFunction:Function;
		protected var _insertRowFunction:Function;
		protected var _addRowFunction:Function;
		protected var _deleteRowFunction:Function;
		protected var _clearAllFunction:Function;
		
		// Fields
		protected var _pasteEnabled:Boolean;
		protected var _addRowEnabled:Boolean = true;
		protected var _rowOperationsAllowed:Boolean = true;
		protected var _insertRowEnabled:Boolean = true;
		protected var _dataType:String;
		protected var _ignoredColumns:Array;
		protected var _importantFields:Array;
		protected var _colorRowsByField:String;
		
		
		public function CopyPasteDataGrid()
		{
			super();
		}
		
		
		protected function createContextMenu():void 
		{
			// Custom context (right-click) menu
			_copyMenuItem = new ContextMenuItem( "Copy row to clipboard" );
			_copyMenuItem.addEventListener( ContextMenuEvent.MENU_ITEM_SELECT, handleCopySelect );
			_expTSVMenuItem = new ContextMenuItem( "Copy grid to clipboard" );
			_expTSVMenuItem.addEventListener( ContextMenuEvent.MENU_ITEM_SELECT, handleExpGridSelect );
			
			_insertLineMenuItem = new ContextMenuItem( "Insert row (shift rows down)", true, _insertRowEnabled, _insertRowEnabled );
			_insertLineMenuItem.addEventListener( ContextMenuEvent.MENU_ITEM_SELECT, handleInsertRowSelect );
			_addLineMenuItem = new ContextMenuItem( "Add row", !_insertRowEnabled, _addRowEnabled, _addRowEnabled );
			_addLineMenuItem.addEventListener( ContextMenuEvent.MENU_ITEM_SELECT, handleAddRowSelect );
			_dupMenuItem = new ContextMenuItem( "Duplicate row" );
			_dupMenuItem.addEventListener( ContextMenuEvent.MENU_ITEM_SELECT, handleDupSelect );
			
			_delMenuItem = new ContextMenuItem( "Delete row", true );
			_delMenuItem.addEventListener( ContextMenuEvent.MENU_ITEM_SELECT, handleDelSelect );
			_clearAllMenuItem = new ContextMenuItem( "Clear all" );
			_clearAllMenuItem.addEventListener( ContextMenuEvent.MENU_ITEM_SELECT, handleClearSelect );
			
			_undoMenuItem = new ContextMenuItem( "Undo\u00A0", true );
			_undoMenuItem.addEventListener( ContextMenuEvent.MENU_ITEM_SELECT, handleUndoSelect );
			
			_contextMenu = new ContextMenu();
			_contextMenu.hideBuiltInItems();
			
			if ( !this.editable ) {
				_contextMenu.customItems = [ _expTSVMenuItem ];
				_pasteEnabled = false;
				_ignoredColumns = [];
			} else if (!_rowOperationsAllowed) {
				_contextMenu.customItems = [ _copyMenuItem, _expTSVMenuItem ];
				_pasteEnabled = false;
				_ignoredColumns = [];
			} else {
				_contextMenu.customItems = [ _copyMenuItem, _expTSVMenuItem, _insertLineMenuItem, _addLineMenuItem, _dupMenuItem, _delMenuItem, _clearAllMenuItem, _undoMenuItem ];
				if (dataProvider is HierarchicalCollectionView ) {
					this._undoMenuItem.visible =  this._undoMenuItem.enabled = false;
				} else {
					this._undoMenuItem.visible =  this._undoMenuItem.enabled = true;
				}
			}
			
			this.contextMenu = _contextMenu;
		}
		
		
		// Adjust text of context menus depending on if 1 or more items are selected in grid
		protected function handleChange( event:Event ):void
		{
			_insertLineMenuItem.enabled = _insertRowEnabled && selectedIndex >= 0;
			
			_copyMenuItem.caption = selectedItems.length > 1 ? "Copy rows to clipboard" : "Copy row to clipboard";
			_dupMenuItem.caption = selectedItems.length > 1 ? "Duplicate rows" : "Duplicate row";
			_delMenuItem.caption = selectedItems.length > 1 ? "Delete rows" : "Delete row";
			
		}
		
		protected function handleCopySelect( event:Event ):void
		{
			System.setClipboard( getTextFromSelectedItems() );
		}
		
		protected function handleDupSelect( event:Event ):void
		{
			saveDataProvider();
			duplicateSelectedRows();
		}
		
		protected function handleExpGridSelect( event:Event ):void
		{
			System.setClipboard( getTextFromGrid() );
		}
		
		protected function handleInsertRowSelect( event:Event ):void
		{
			saveDataProvider();
			this.insertRow();
		}
		
		protected function handleAddRowSelect( event:Event ):void
		{
			saveDataProvider();
			this.addRow();
		}
		
		protected function handleDelSelect( event:Event ):void
		{
			saveDataProvider();
			this.deleteRows();
		}
		
		protected function handleClearSelect( event:Event ):void
		{
			saveDataProvider();
			this.clearAll();
		}
		
		protected function handleUndoSelect( event:Event ):void
		{
			undoChange();
		}
		
		protected function handleClick( event:Event ):void
		{
			// Select no rows if user clicks on empty row
			if ( !event.target.hasOwnProperty('listData') && !event.target.hasOwnProperty('text') ){
				this.selectedIndex = -1;
				setFocus();
				drawFocus( true );
			} 
		}
		
		protected function handleKeyPressed( event:KeyboardEvent ):void
		{
			if (event.target != this)
			{
				return;
			}
			
			// if we're currently editing the grid then we'll disable the copy/paste feature
			// so the profile is able to paste into the edited cell
			if (itemEditorInstance != null)
			{
				return;
			}
			
			// there's a strange bug that sometimes the ctrlkey appears as the alt
			// key, there shouldn't be a problem just watching for both
			if ((event.ctrlKey || event.altKey) && event.charCode == 0 && !_textArea)
			{
				saveDataProvider();
				
				// Add an invisible TextField object to the DataGrid
				_textArea = new TextArea();
				_textArea.visible = false;
				_textArea.height = this.height;
				_textArea.width = this.width;
				
				// Populate the TextField with selected data in TSV format
				_textArea.text = getTextFromItems();
				_textArea.setSelection( 0, _textArea.text.length - 1 );
				
				// Listen for textInput event
				_textArea.addEventListener( TextEvent.TEXT_INPUT, handleTextPasted, false, 0, true );
				
				// Set player-level focus to the TextField
				addChild( _textArea );
				
				_textArea.setFocus();
			}				
		}
		
		protected function handleKeyReleased( event:KeyboardEvent ):void
		{
			if (!event.ctrlKey && _textArea)
			{
				var keepFocus:Boolean;
				
				if (focusManager && focusManager.getFocus() == _textArea)
				{
					keepFocus = true;
				}
				
				removeChild( _textArea );
				_textArea = null;
				
				if (keepFocus)
				{
					setFocus();
				}
				
				event.stopImmediatePropagation();	
			}							
		}
		
		protected function handleTextPasted( event:TextEvent ):void
		{
			if (!_textArea)
			{
				return;
			}
			
			if (!_pasteEnabled && _pasteFunction == null)
			{
				return;
			}
			
			if (_pasteFunction != null)
			{
				_pasteFunction( event );
			}
			else
			{
				// Extract values from TSV format and populate the DataGrid
				var items:XMLListCollection = DataGridUtil.getItemsFromText( event.text, this, this.parentApplication, _dataType );
				
				for each (var item:XML in items)
				{
					addItemToDataProvider(item);
				}
			}
		}
		
		// Get the selected rows as a tab-delimited string
		public function getTextFromItems():String
		{
			return DataGridUtil.copyData( this, DataGridUtil.TYPE_TSV, DataGridUtil.EXPORT_SELECTED, this.parentApplication, false );
		}
		
		// Get the entire grid as a tab-delimited string with headers
		public function getTextFromGrid():String
		{
			return DataGridUtil.copyData( this, DataGridUtil.TYPE_TSV, DataGridUtil.EXPORT_ALL, this.parentApplication, true );
		}
		
		// Returns a tab-delimited string corresponding to the selected rows of the grid
		public function getTextFromSelectedItems():String
		{
			var selectedItems:XMLListCollection = DataGridUtil.getSelectedRows( this,  _dataType, null );
			return DataGridUtil.getTextFromXMLList( this, selectedItems, DataGridUtil.TYPE_TSV, this.parentApplication );
		}
		
		// Duplicates selected rows and adds them to the dataprovider
		public function duplicateSelectedRows():void
		{
			var selectedItems:XMLListCollection = DataGridUtil.getSelectedRows( this,  _dataType, _ignoredColumns );
			for each ( var item:XML in selectedItems ) {
				addItemToDataProvider(item);
			}
		}
		
		// Adds an empty row to the dataprovider
		public function insertRow():void {
			if (_insertRowFunction != null)
			{
				_insertRowFunction();
			}
			else
			{
				var item:XML = DataGridUtil.getEmptyRow(this, _dataType);
				this.getUnderlyingDataProvider().addItemAt( item, selectedIndex );
			}
		}
		
		// Adds an empty row to the dataprovider
		public function addRow():void {
			if (_addRowFunction != null)
			{
				_addRowFunction();
			}
			else
			{
				var item:XML = DataGridUtil.getEmptyRow(this, _dataType);
				this.getUnderlyingDataProvider().addItem( item );
			}
		}
		
		// Deletes the selected rows
		public function deleteRows():void {
			saveDataProvider();
			if (_deleteRowFunction != null)
			{
				_deleteRowFunction();
			}
			else
			{
				var newDataProvider:XMLListCollection = new XMLListCollection();
				var index:int = 0;
				for each ( var listItem:Object in this.getUnderlyingDataProvider() ) {
					if ( this.selectedIndices.indexOf( index ) < 0 ) {
						newDataProvider.addItem(listItem);
					}
					index++;
				}					
				this.getUnderlyingDataProvider().removeAll();
				this.getUnderlyingDataProvider().addAll(newDataProvider);
			}
		}
		
		// Clears the grid
		public function clearAll():void {
			saveDataProvider();
			if (_clearAllFunction != null)
			{
				_clearAllFunction();
			}
			else
			{
				var item:XML = DataGridUtil.getEmptyRow(this, _dataType);
				this.getUnderlyingDataProvider().removeAll();
				addItemToDataProvider( item );	
			}
		}	
		
		// Makes a copy of the dataprovider
		protected function saveDataProvider():void {
			_undoMenuItem.caption = "Undo\u00A0";
			if ( dataProvider != null ) {
				this._lastDataProvider = new XMLListCollection( this.getUnderlyingDataProvider().copy() );
			}
		}
		
		// Replaces dataprovider with latest copy
		public function undoChange():void {
			if ( _lastDataProvider == null ) {
				return;
			}
			var temp:Object;
			temp = new XMLListCollection( this.getUnderlyingDataProvider().copy() );
			this.getUnderlyingDataProvider().removeAll();
			this.getUnderlyingDataProvider().addAll(this._lastDataProvider);	
			_lastDataProvider = temp;
			if ( _undoMenuItem.caption == "Redo\u00A0" ) {
				_undoMenuItem.caption = "Undo\u00A0";
			} else {
				_undoMenuItem.caption = "Redo\u00A0";
			}
		}
		
		protected function getUnderlyingDataProvider():Object {
			if ( dataProvider != null && (dataProvider is HierarchicalCollectionView ) ) {
				// If we have Hierarchical data, we need to get the underlying XMLListCollection
				return dataProvider.source.source;
			} 
			return dataProvider;
		}
		
		protected function addItemToDataProvider(newItem:XML):void {
			var ind:int = getFirstEmptyRowIndex();
			var emptyNode:Object;
			
			if ( ind >= 0 ) {
				emptyNode = this.getUnderlyingDataProvider().getItemAt(ind);
				
			} else {
				// Add a row - using the add row function if provided
				this.addRow();
				// Get the added row
				emptyNode = this.getUnderlyingDataProvider().getItemAt(this.getUnderlyingDataProvider().length-1);
			}
			
			// Now copy the sample annotations
			for each (var attribute:Object in newItem.attributes()) {
				if ( this._ignoredColumns != null ) {
					var aName:String = attribute.name();
					if (this._ignoredColumns.indexOf(aName) == -1) {
						emptyNode["@" + attribute.name()] = String(attribute);
					}
				} else {
					emptyNode["@" + attribute.name()] = String(attribute);
				}
			}
							
		}
		
		protected function getFirstEmptyRowIndex():int {
			for each (var row:Object in this.getUnderlyingDataProvider()) {
				var isBlank:Boolean = true;
				
				for each (var attribute:String in _importantFields) {
					if ( row["@" + attribute] != null && StringUtil.trim(row["@" + attribute]) != '' ) {
						isBlank = false;
					}
				}
				
				if ( isBlank ) {
					return this.getUnderlyingDataProvider().getItemIndex(row);
				}
			}
			return -1; 
		}
		
		// Setters
		// Custom Functions
		[Bindable]
		public function get pasteFunction():Function { return _pasteFunction; }
		public function set pasteFunction( value:Function ):void
		{
			_pasteFunction = value;
		}
		[Bindable]
		public function get insertRowFunction():Function { return _insertRowFunction; }
		public function set insertRowFunction( value:Function ):void
		{
			_insertRowFunction = value;
		}
		[Bindable]
		public function get addRowFunction():Function { return _addRowFunction; }
		public function set addRowFunction( value:Function ):void
		{
			_addRowFunction = value;
		}
		[Bindable]
		public function get deleteRowFunction():Function { return _deleteRowFunction; }
		public function set deleteRowFunction( value:Function ):void
		{
			_deleteRowFunction = value;
		}
		[Bindable]
		public function get clearAllFunction():Function { return _clearAllFunction; }
		public function set clearAllFunction( value:Function ):void
		{
			_clearAllFunction = value;
		}
		
		// Fields
		[Bindable]
		public function get pasteEnabled():Boolean { return _pasteEnabled; }
		public function set pasteEnabled( value:Boolean ):void
		{
			_pasteEnabled = value;
		}
		[Bindable]
		public function get addRowEnabled():Boolean { return _addRowEnabled; }
		public function set addRowEnabled( value:Boolean ):void
		{
			_addRowEnabled = value;
		}
		[Bindable]
		public function get rowOperationsAllowed():Boolean { return _rowOperationsAllowed; }
		public function set rowOperationsAllowed( value:Boolean ):void
		{
			_rowOperationsAllowed = value;
		}
		[Bindable]
		public function get insertRowEnabled():Boolean { return _insertRowEnabled; }
		public function set insertRowEnabled( value:Boolean ):void
		{
			_insertRowEnabled = value;
		}
		[Bindable]
		public function get dataType():String { return _dataType; }
		public function set dataType( value:String ):void
		{
			_dataType = value;
		}
		[Bindable]
		public function get ignoredColumns():Array { return _ignoredColumns; }
		public function set ignoredColumns( value:Array ):void
		{
			_ignoredColumns = value;
		}
		// These are the fields that are checked to determine if a row is empty or not.  If these
		// fields are all empty, the row is considered empty.
		[Bindable]
		public function get importantFields():Array { return _importantFields; }
		public function set importantFields( value:Array ):void
		{
			_importantFields = value;
		}
		// This field allows you to group rows by a particular field.  
		[Bindable]
		public function get colorRowsByField():String { return _colorRowsByField; }
		public function set colorRowsByField( value:String ):void
		{
			_colorRowsByField = value;
		}
		
		override protected function initializationComplete():void
		{
			addEventListener( Event.CHANGE, handleChange );
			addEventListener( MouseEvent.CLICK, handleClick );
			addEventListener( 'columnsChanged', addRowNumberColumn );
			
			systemManager.addEventListener( KeyboardEvent.KEY_DOWN, handleKeyPressed );
			systemManager.addEventListener( KeyboardEvent.KEY_UP, handleKeyReleased );
			
			createContextMenu();
			addRowColorFields();
		}
		
		override protected function commitProperties():void
		{
			super.commitProperties();
			createContextMenu();
			addRowColorFields();
		}
		
		// Special function for coloring rows in groups 
		override protected function drawRowBackground(s:Sprite, rowIndex:int, 
													  y:Number, height:Number, color:uint, dataIndex:int):void {
			if ( this.colorRowsByField != null && this.colorRowsByField != '' ) {
				if (dataProvider != null && dataProvider.length > 0 && !(dataProvider is HierarchicalCollectionView)) {
					
					var item:Object;
					
					if( dataIndex < dataProvider.length ) {
						item = dataProvider[dataIndex];
						if( item != null && item.@altColor != null && item.@altColor == "true" ) {
							color = 0xEEEEE0;
						} 
					}
				}
			}
			super.drawRowBackground(s,rowIndex,y,height,color,dataIndex);
		}
		// Special function for coloring rows in groups 	
		protected function addRowColorFields():void {
			if ( this.colorRowsByField == null || this.colorRowsByField == '' ) {
				return;
			}
			if (dataProvider != null && dataProvider.length > 0 && !(dataProvider is HierarchicalCollectionView)) {
				
				var ind:int;
				var alt:Boolean = true; 
				var prevVal:String = '';
				var currVal:String = '';
				
				for ( ind = 0; ind < dataProvider.length; ind++ ) {
					
					currVal = dataProvider[ind].@[this.colorRowsByField];
					
					if (currVal != prevVal) {
						alt = !alt;
					}
					dataProvider[ind].@altColor = new Boolean(alt).toString();
					prevVal = currVal;
				}
			}
		}
		
		/**
		 * Row number column
		 * A column that will show the row number for each item in the grid.
		 */
		protected var _showRowNumberCol:Boolean = false;
		protected var _rowNumberColExists:Boolean = false;
		protected var _rowNumberColumn:AdvancedDataGridColumn;
		
		[Bindable]
		public function get showRowNumberCol():Boolean { return _showRowNumberCol; }
		public function set showRowNumberCol( value:Boolean ):void
		{
			if ( showRowNumberCol != value ) {
				_rowNumberColExists = false;
				_showRowNumberCol = value;
				showHideRowNumberColumn();
			}
		}
		protected function addRowNumberColumn( e:Event = null ):void
		{
			showHideRowNumberColumn();
		}
		
		protected function showHideRowNumberColumn():void
		{
			if ( _rowNumberColumn == null ) {
				_rowNumberColumn = new AdvancedDataGridColumn("");
				_rowNumberColumn.editable = false;
				_rowNumberColumn.labelFunction = getGridRowNumber;
				_rowNumberColumn.width = 35;
				_rowNumberColumn.resizable = true;
				_rowNumberColumn.sortable = false
			}
			
			if ( !_rowNumberColExists && columns.length > 0 && _rowNumberColumn ) 
			{
				_rowNumberColExists = true;
				
				var cols:Array = columns;
				var index:int = cols.indexOf( _rowNumberColumn );
				
				if (index < 0)
				{
					// If it is not in the columns but we want it to be add it
					if ( _showRowNumberCol){
						cols.unshift( _rowNumberColumn );
					}
				} else {
					// Take it out 
					cols.splice( index, 1 );
					// Put it back in the beginning if we want
					if ( _showRowNumberCol){
						cols.unshift( _rowNumberColumn );
					}
					
				}
				columns = cols;
			}
		}
		
		public function getGridRowNumber(item:Object,col:int):String
		{
			if (getUnderlyingDataProvider()!= null) {
				var x:int = getUnderlyingDataProvider().getItemIndex(item) + 1;
				return String(x);     		
			} else {
				return "";
			}
		}
		
		override public function set dataProvider(value:Object):void
		{
			super.dataProvider = value;
			_rowNumberColExists = false;
			showHideRowNumberColumn();
			createContextMenu();
			addRowColorFields();
		}
		
		override public function set columns( value:Array ):void
		{
			super.columns = value;
			showHideRowNumberColumn();
		}
		
		
	}
}