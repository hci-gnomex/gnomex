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
	
	
	public class CustomAdvancedDataGridColumn extends AdvancedDataGridColumn
	{
		
		public var isLocked:Boolean = false;
		public var mappingField:String = "";
		public var mappingFunction:Function = null;
		
		public function CustomAdvancedDataGridColumn()
		{
			super();
		}		
		
		
		public function setLock( value:Boolean ):void
		{
			isLocked = value;			
		}
		
		public function setMappingField ( value:String ):void
		{
			mappingField = value;
		}
		
		public function setMappingFunction ( value:Function ):void
		{
			mappingFunction = value;
		}
	
	}
}