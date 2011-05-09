package views.renderers
{
	import flash.display.Graphics;
	
	import mx.controls.Label;
	import mx.core.IFactory;
	import hci.flex.renderers.RendererFactory;
	
	public class Label extends mx.controls.Label
	{   public var _dataField:String;	
		public var missingRequiredFieldBackground:uint = RendererFactory.DEFAULT_MISSING_REQUIRED_FIELD_BACKGROUND;
		public var missingRequiredFieldBorder:uint = RendererFactory.DEFAULT_MISSING_REQUIRED_FIELD_BORDER;
		public var missingRequiredFieldBorderThickness:uint = RendererFactory.DEFAULT_MISSING_REQUIRED_FIELD_BORDER_THICKNESS;
		
		public static function create(dataField:String):IFactory {
			return RendererFactory.create(views.renderers.Label,
				{_dataField: dataField});			
			
		}	
		
		public static function createCustom(dataField:String, 
											theMissingRequiredFieldBackground:uint,
											theMissingRequiredFieldBorder:uint,
											theMissingRequiredFieldBorderThickness:uint):IFactory {
			return RendererFactory.create(views.renderers.Label, 
				{ _dataField: dataField, 
					missingRequiredFieldBackground: theMissingRequiredFieldBackground,
					missingRequiredFieldBorder: theMissingRequiredFieldBorder,
					missingRequiredFieldBorderThickness: theMissingRequiredFieldBorderThickness});			
			
		}			 
		public function set dataField(dataField:String):void {
			this._dataField = dataField;	
		}
		
		override protected function initializationComplete():void {   
			initializeFields();
		}
		
		protected function initializeFields():void {        	
		}
		
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void
		{
			super.updateDisplayList(unscaledWidth,unscaledHeight);
			var g:Graphics = graphics;
			g.clear();
			if (data == null) {
				return;
			}
			if (!(data is XML)) {
				return;
			} 
			if (!data.hasOwnProperty(_dataField) || data[_dataField] == '') {
				g.beginFill(missingRequiredFieldBackground);
				g.lineStyle(missingRequiredFieldBorderThickness,
					missingRequiredFieldBorder);          	
				g.drawRect(0,0,unscaledWidth,unscaledHeight);
				g.endFill();
			}
			
		}
	}
}