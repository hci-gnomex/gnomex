package views.renderers
{
	import flash.display.Graphics;
	
	import mx.controls.Text;
	import mx.core.IFactory;
	import hci.flex.renderers.RendererFactory;
	
	public class Text extends mx.controls.Text
	{   public var _dataField:String;	
		public var missingRequiredFieldBackground:uint = RendererFactory.DEFAULT_MISSING_REQUIRED_FIELD_BACKGROUND;
		public var missingRequiredFieldBorder:uint = RendererFactory.DEFAULT_MISSING_REQUIRED_FIELD_BORDER;
		public var missingRequiredFieldBorderThickness:uint = RendererFactory.DEFAULT_MISSING_REQUIRED_FIELD_BORDER_THICKNESS;
		public var missingFieldBackground:uint = RendererFactory.DEFAULT_MISSING_FIELD_BACKGROUND;
		public var isRequired:Boolean = false;
		
		public static function create(dataField:String):IFactory {
			return RendererFactory.create(views.renderers.Text,
				{_dataField: dataField});			
			
		}	
		
		public static function createCustom(dataField:String, 
											theMissingRequiredFieldBackground:uint,
											theMissingRequiredFieldBorder:uint,
											theMissingRequiredFieldBorderThickness:uint,
											theMissingFieldBackground:uint,
											isRequired:Boolean=true):IFactory {
			return RendererFactory.create(views.renderers.Text, 
				{ _dataField: dataField, 
					missingRequiredFieldBackground: theMissingRequiredFieldBackground,
					missingRequiredFieldBorder: theMissingRequiredFieldBorder,
					missingRequiredFieldBorderThickness: theMissingRequiredFieldBorderThickness,
					missingFieldBackground: theMissingFieldBackground,
					isRequired:isRequired});			
			
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
			if (data == null || !(data is XML)) {
				return;
			}
			if (this.text == null || this.text == '') {
				if ( !isRequired ) {
					g.beginFill(missingFieldBackground);
					g.lineStyle(missingRequiredFieldBorderThickness,
						missingFieldBackground);          	
					g.drawRect(0,0,unscaledWidth,unscaledHeight);
					g.endFill();
				} else {
					g.beginFill(missingRequiredFieldBackground);
					g.lineStyle(missingRequiredFieldBorderThickness,
						missingRequiredFieldBorder);          	
					g.drawRect(0,0,unscaledWidth,unscaledHeight);
					g.endFill();
				}
			} 
			
		}
	}
}