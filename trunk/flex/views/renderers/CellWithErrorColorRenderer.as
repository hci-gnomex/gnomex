package views.renderers
{
	import flash.display.Graphics;
	
	import hci.flex.renderers.RendererFactory;
	
	import mx.controls.AdvancedDataGrid;
	import mx.controls.Label;
	import mx.core.IFactory;
	
	public class CellWithErrorColorRenderer extends mx.controls.Label {   
		public var _dataField:String;	
		public var errorBackground:uint = 0xFFC1C1;
		
		[Bindable]
		public var _errorField:String;
		
		[Bindable]
		public var _errorValue:String;
		
		public static function create(dataField:String, errorField:String, errorValue:String = 'Y'):IFactory {
			return RendererFactory.create(views.renderers.CellWithErrorColorRenderer,
				{_dataField: dataField, _errorField: errorField, _errorValue: errorValue});			
			
		}	
		
		public static function createCustom(dataField:String,
											errorField:String,
											errorValue:String,
											theErrorBackground:uint):IFactory {
			return RendererFactory.create(views.renderers.CellWithErrorColorRenderer, 
				{ _dataField: dataField, _errorField: errorField, _errorValue: errorValue,
					errorBackground: theErrorBackground});			
			
		}
		
		public function set dataField(dataField:String):void {
			this._dataField = dataField;	
		}
		
		public function set errorField(errorField:String):void {
			this._errorField = errorField;	
		}
		
		public function set errorValue(errorValue:String):void {
			this._errorValue = errorValue;	
		}
		
		override protected function initializationComplete():void {   
			initializeFields();
		}
		
		protected function initializeFields():void {        	
		}
		
		private function getToolTip():String{
			var dg:AdvancedDataGrid = listData.owner as AdvancedDataGrid;
			var func:Function = dg.columns[listData.columnIndex].dataTipFunction;
			if(func != null){
				return func.call(this, this.data);
			}else{
				return "";
			}
		}
		
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void
		{
			super.updateDisplayList(unscaledWidth,unscaledHeight);
			var g:Graphics = graphics;
			g.clear();
			if (data == null) {
				return;
			}
			
			if (data[_errorField] == _errorValue) {
				g.beginFill(errorBackground);
				g.lineStyle(0,errorBackground);
				g.drawRect(0,0,unscaledWidth,unscaledHeight);
				g.endFill();
			}
			this.toolTip = getToolTip();
		}
	}
}