package hci.flex.controls
{
	import hci.flex.renderers.RendererFactory;
	
	import mx.controls.TextInput;
	import mx.core.IFactory;

	public class TextInput extends mx.controls.TextInput
	{
		public var dataField:String;
		public var isRequired:Boolean = false;
		public var missingRequiredFieldBackground:uint = RendererFactory.DEFAULT_MISSING_REQUIRED_FIELD_BACKGROUND;
	   
		public static function create(
			dataField:String, 
			isRequired:Boolean = false,
			missingRequiredFieldBackground:uint = 0xFFFFB9):IFactory {
				
				return RendererFactory.create(hci.flex.controls.TextInput, { 
					dataField: dataField,
					isRequired: isRequired, 
					missingRequiredFieldBackground: missingRequiredFieldBackground });							  
		}			
        
    	override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
        	super.updateDisplayList(unscaledWidth,unscaledHeight);
          	
          	if (data == null || dataField == null) {
          		return;
          	}
          	this.setStyle("backgroundColor", data[dataField] == '' ? missingRequiredFieldBackground : "0xffffff");
	     }
	}
}