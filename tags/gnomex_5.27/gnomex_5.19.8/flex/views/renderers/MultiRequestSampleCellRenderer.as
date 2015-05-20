package views.renderers
{
	import flash.display.Graphics;
	
	import hci.flex.renderers.RendererFactory;
	
	import mx.controls.AdvancedDataGrid;
	import mx.controls.Label;
	import mx.core.IFactory;
	
	public class MultiRequestSampleCellRenderer extends mx.controls.Label {   
		public var _dataField:String;	
		public var errorBackground:uint = 0xFFC1C1;
		
		[Bindable]
		public var _errorList:XMLList;
		
		public static function create(dataField:String, errorList:XMLList):IFactory {
			return RendererFactory.create(views.renderers.MultiRequestSampleCellRenderer,
				{_dataField: dataField, _errorList: errorList});			
			
		}	
		
		public static function createCustom(dataField:String,
											errorList:XMLList,
											theErrorBackground:uint):IFactory {
			return RendererFactory.create(views.renderers.MultiRequestSampleCellRenderer, 
				{ _dataField: dataField, _errorList: errorList,
					errorBackground: theErrorBackground});			
			
		}
		
		public function set dataField(dataField:String):void {
			this._dataField = dataField;	
		}
		
		public function set errorList(errorList:XMLList):void {
			this._errorList = errorList;	
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
			if (!data.hasOwnProperty(_dataField) || !data.hasOwnProperty('@rowOrdinal') || _dataField.substr(0,2) != '@n') {
				return;
			}
			
			var hasError:Boolean = false;
			var dataRowOrdinal:String = data['@rowOrdinal'].toString();
			var dataColOrdinal:String = _dataField.substr(2);
			for each (var err:XML in _errorList) {
				if ((err['@rowOrdinal'] == dataRowOrdinal || err['@rowOrdinal'] == '') 
					&& (err['@columnOrdinal'] == dataColOrdinal || err['@columnOrdinal'] == '')) {
					hasError = true;
					break;
				}
			}

			if (hasError) {
				g.beginFill(errorBackground);
				g.lineStyle(0,errorBackground);
				g.drawRect(0,0,unscaledWidth,unscaledHeight);
				g.endFill();
			}
			this.toolTip = getToolTip();
		}
	}
}