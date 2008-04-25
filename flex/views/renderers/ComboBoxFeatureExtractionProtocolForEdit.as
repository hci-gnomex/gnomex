package views.renderers
{
	import mx.events.ListEvent;
	import mx.controls.Alert;
	
	public class ComboBoxFeatureExtractionProtocolForEdit  extends ComboBoxBase
	{
			protected override function initializeFields():void {
		    	dictionaryClassName          = "hci.gnomex.model.FeatureExtractionProtocol";
		    	cellAttributeName            = "@idFeatureExtractionProtocol";
		    	choiceDisplayAttributeName   = "@display";
		    	choiceValueAttributeName     = "@value";
		    }		
		   	protected override function change(event:ListEvent):void {
		     	if (parentApplication.hasPermission("canWriteAnyObject")) {
	            	super.change(event);
		     	} else {
		     		selectItem();
		     		Alert.show("Feature extraction protocol cannot be changed.");
		     	}
	        }            


	}

}