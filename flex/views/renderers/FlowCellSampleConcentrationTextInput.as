package views.renderers
{
	import hci.flex.renderers.RendererFactory;	
	import mx.core.IFactory;
	import hci.flex.controls.TextInput;

	public class FlowCellSampleConcentrationTextInput extends hci.flex.controls.TextInput
	{
		public static function create(dataField:String):IFactory {
				return RendererFactory.create(FlowCellSampleConcentrationTextInput, 
				{ dataField: dataField});							  
		}	
		
		
        
    	override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void
     	{
          super.updateDisplayList(unscaledWidth,unscaledHeight);
          if (data == null) {
          	return;
          }
          this.setStyle("backgroundColor", data.name() == "WorkItem" && data[dataField] == '' ? missingRequiredFieldBackground : "0xffffff");
          
          this.editable = true;

	    }
	}
}