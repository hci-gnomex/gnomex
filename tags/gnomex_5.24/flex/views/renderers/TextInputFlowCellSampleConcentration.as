package views.renderers
{
	import hci.flex.renderers.RendererFactory;	
	import mx.core.IFactory;
	import hci.flex.controls.TextInput;

	public class TextInputFlowCellSampleConcentration extends hci.flex.controls.TextInput
	{
		public static function create(dataField:String):IFactory {
				return RendererFactory.create(TextInputFlowCellSampleConcentration, 
				{ dataField: dataField});							  
		}	
		
		
        
    	override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void
     	{
          super.updateDisplayList(unscaledWidth,unscaledHeight);
          if (data == null) {
          	return;
          }
          this.setStyle("backgroundColor", data.name() == "WorkItem" && data[dataField] == '' && data['@editable'] == 'true' ? missingRequiredFieldBackground : "0xffffff");

		  // Set whether field is editable.  (Sequencing control is never editable)
		  // WorkItems are editable depending on row location.
		  if (data.name() == 'WorkItem') {
		  	  // Only first row in channel (for WorkItems) is editable)
			  if (data['@editable'] == 'true') {
			  	this.editable = true;
			  } else {
		  		this.editable = false;
		  	}
		  } else {
		  	this.editable = false;
		  }
	    }
	}
}