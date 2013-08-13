package views.util
{
	import flash.events.ContextMenuEvent;
	import flash.events.Event;
	import flash.system.System;
	import flash.ui.ContextMenu;
	import flash.ui.ContextMenuItem;
	
	import mx.controls.Alert;
	import mx.controls.DataGrid;
	import mx.controls.dataGridClasses.DataGridColumn;
	import mx.core.EventPriority;
	import mx.events.CloseEvent;
	import mx.events.ListEvent;
	
	public class CustomDataGrid extends DataGrid
	{
		[Bindable] public var enableCopy : Boolean = true;
		// for creating conext menu item for coping functionality				
		private var copyContextItem:ContextMenuItem;		
		// for storing the header text at only once.
		private var headerString : String = '';
		private var dataToCopy:String = '';
		
		public function CustomDataGrid()
		{
			super();
		}
		
	
		
	}
}