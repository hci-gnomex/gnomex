package views.renderers
{
	import hci.flex.renderers.RendererFactory;
	
	import mx.controls.TextInput;
	import mx.core.IFactory;
	
	public class TextInput extends mx.controls.TextInput
	{
		public var dataField:String;
		public var isRequired:Boolean = false;
		public var missingRequiredFieldBackground:uint = RendererFactory.DEFAULT_MISSING_REQUIRED_FIELD_BACKGROUND;
		public var missingFieldBackground:uint = RendererFactory.DEFAULT_MISSING_FIELD_BACKGROUND;
		
		
		public static function create(
			dataField:String, 
			isRequired:Boolean = false,
			restrict:String = null,
			missingRequiredFieldBackground:uint = 0xFFFFB9,
			missingFieldBackground:uint = 0xeaeaea):IFactory {
			
			return RendererFactory.create(views.renderers.TextInput, { 
				dataField: dataField,
				isRequired: isRequired, 
				restrict: restrict,
				missingRequiredFieldBackground: missingRequiredFieldBackground,
				missingFieldBackground: missingFieldBackground});							  
		}			
		
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			super.updateDisplayList(unscaledWidth,unscaledHeight);
			
			if (data == null || dataField == null || !(data is XML)) {
				editable = false;
				return;
			}
			editable = true;
			if ( data[dataField] == '' ){
				this.setStyle("backgroundColor",isRequired ? missingRequiredFieldBackground : missingFieldBackground);
			} else {
				this.clearStyle("backgroundColor");
			}
		}
	}
}