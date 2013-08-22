package views.util
{
	import ext.com.utils.AdvancedDataGridUtils;
	
	import flash.events.ContextMenuEvent;
	import flash.events.Event;
	import flash.events.KeyboardEvent;
	import flash.events.MouseEvent;
	import flash.events.TextEvent;
	import flash.system.System;
	import flash.ui.ContextMenu;
	import flash.ui.ContextMenuItem;
	
	import mx.collections.ArrayCollection;
	import mx.controls.AdvancedDataGrid;
	import mx.controls.TextArea;
	import mx.controls.advancedDataGridClasses.AdvancedDataGridColumn;
	import mx.core.UIComponent;
	import mx.managers.FocusManager;
	import mx.utils.ObjectUtil;
	
	
	public class AdvancedDataGridCopyPaste extends AdvancedDataGrid
	{
		private var dragScrollingInterval:int = 0;
		
		private var _textArea:TextArea;
		
		public var _pasteFunction:Function;
		public var _pasteEnabled:Boolean;
		
		public var _mode:String = "row";
		public var _selectedCell:Object;
		
		private var _contextMenu:ContextMenu;
		private var _copyMenuItem:ContextMenuItem;
		private var _pasteMenuItem:ContextMenuItem;
		
		public function AdvancedDataGridCopyPaste()
		{
			super();
		}		
		
		public function init():void
		{
			addEventListener( Event.CHANGE, handleChange );
			addEventListener( MouseEvent.CLICK, handleClick );
			
			systemManager.addEventListener( KeyboardEvent.KEY_DOWN, handleKeyPressed );
			systemManager.addEventListener( KeyboardEvent.KEY_UP, handleKeyReleased );
			
			// note: the label can not be "Copy" (if it is, the menu item won't appear)
			_copyMenuItem = new ContextMenuItem( "Copy item" );
			_copyMenuItem.addEventListener( ContextMenuEvent.MENU_ITEM_SELECT, handleCopySelect );

			_contextMenu = new ContextMenu();
			_contextMenu.hideBuiltInItems();
			_contextMenu.customItems = [ _copyMenuItem ];
			
			this.contextMenu = _contextMenu;										
		}
		
		private function handleChange( event:Event ):void
		{
			_copyMenuItem.caption = selectedItems.length > 1 ? "Copy items" : "Copy item";
		}
		
		private function handleCopySelect( event:Event ):void
		{
			System.setClipboard( getTextFromItems() );
		}
		
		public function set pasteFunction( value:Function ):void
		{
			_pasteFunction = value;
		}
		
		public function set pasteEnabled( value:Boolean ):void
		{
			_pasteEnabled = value;
		}
		
		private function handleClick( event:Event ):void
		{
			setFocus();
			drawFocus( true );
		}
		
		private function handleKeyPressed( event:KeyboardEvent ):void
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
		
		private function handleKeyReleased( event:KeyboardEvent ):void
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
				var items:Array = AdvancedDataGridUtils.getItemsFromText( event.text );
				
				for each (var item:Object in items)
				{
					dataProvider.addItem( item );
				}
			}
		}
		
		public function getTextFromItems():String
		{
			return AdvancedDataGridUtils.copyData( this, AdvancedDataGridUtils.TYPE_TSV, AdvancedDataGridUtils.EXPORT_SELECTED );
		}
		
		
	}
}