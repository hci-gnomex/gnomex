package hci.flex.controls
{
	import flash.display.Graphics;
	import hci.flex.renderers.RendererFactory;
	import mx.core.IFactory;
	
	public class DropdownLabelHighlight extends DropdownLabel {
		
		public var highlight:Boolean = false;
		public var highlightColor:uint = RendererFactory.DEFAULT_HIGHLIGHT_BACKGROUND;
		
		public function DropdownLabelHighlight() {
			super();
		}
		
		public static function getFactory(
			dataProvider:XMLList, 
			labelField:String, 
			valueField:String, 
			dataField:String, 
			isRequired:Boolean=false, 
			missingRequiredFieldBackground:uint = 0xFFFFB9,
			highlight:Boolean=false,
			highlightColor:uint = 0xFFFF99):IFactory {			
			
			return RendererFactory.create(hci.flex.controls.DropdownLabelHighlight, {dataProvider: dataProvider,
													labelField: labelField,  
													valueField: valueField,
													dataField: dataField,
													isRequired: isRequired,
													missingRequiredFieldBackground: missingRequiredFieldBackground,
													highlight: highlight,
													highlightColor: highlightColor});				
		} 
		
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
        	super.updateDisplayList(unscaledWidth,unscaledHeight);
        	
        	if (highlight && data.@highlight == "Y") {
          		this.setStyle("fontWeight", "bold");			
				this.opaqueBackground = 0xFFFF99;
          	} else {
          		this.setStyle("fontWeight", "normal");	
				this.opaqueBackground = null;
          	}
	 	}
	 	
	}
}