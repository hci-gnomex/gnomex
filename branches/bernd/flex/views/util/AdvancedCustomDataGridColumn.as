package views.util
{
	import mx.controls.AdvancedDataGrid;
	
	import mx.managers.FocusManager;
	import mx.collections.ArrayCollection;
	import mx.utils.ObjectUtil;
	import mx.controls.TextArea;
	import mx.core.UIComponent;
	import mx.controls.advancedDataGridClasses.AdvancedDataGridColumn;
	//import mx.controls.dataGridClasses.DataGridColumn;
	import flash.events.KeyboardEvent;
	import ext.com.utils.AdvancedDataGridUtils;
	import flash.ui.ContextMenu;
	import flash.ui.ContextMenuItem;
	import flash.events.MouseEvent;
	import flash.events.Event;
	import flash.events.ContextMenuEvent;
	import flash.events.TextEvent;
	import flash.system.System;
	
	
	public class AdvancedCustomDataGridColumn extends AdvancedDataGridColumn
	{
		
		private var isLocked:Boolean = false;
		
		public function AdvancedCustomDataGridColumn()
		{
			super();
		}		
		
		public function set isLocked( value:Boolean ):void
		{
			isLocked = value;			
		}
	
	}
}