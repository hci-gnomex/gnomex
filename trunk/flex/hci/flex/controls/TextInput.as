package hci.flex.controls
{
	import hci.flex.renderers.RendererFactory;
	
	import ext.com.Consts;
	
	import mx.controls.TextInput;
	import mx.core.IFactory;

	public class TextInput extends mx.controls.TextInput
	{
		public var dataField:String;
		public var isRequired:Boolean = false;
		public var missingRequiredFieldBackground:uint = RendererFactory.DEFAULT_MISSING_REQUIRED_FIELD_BACKGROUND;
		public var missingFieldBackground:uint = RendererFactory.DEFAULT_MISSING_FIELD_BACKGROUND;
		public var maxChars1:int = Consts.MAX_CHARS ;
		
		
		public static function create(
			dataField:String, 
			isRequired:Boolean = false,
			missingRequiredFieldBackground:uint = 0xFFFFB9,
			missingFieldBackground:uint = 0xeaeaea):IFactory {
			
			return RendererFactory.create(hci.flex.controls.TextInput, { 
				dataField: dataField,
				isRequired: isRequired, 
				missingRequiredFieldBackground: missingRequiredFieldBackground,
				missingFieldBackground: missingFieldBackground});							  
		}
		
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			this.maxChars = maxChars1;
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