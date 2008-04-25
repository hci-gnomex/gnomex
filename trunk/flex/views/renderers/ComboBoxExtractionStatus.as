package views.renderers
{
	import mx.events.ListEvent;
	import mx.controls.Alert;
	
	public class ComboBoxExtractionStatus extends ComboBoxStatus
	{
		protected override function initializeFields():void {
			super.initializeFields();
	    	cellAttributeName            = "@extractionStatus";
	    }
	   	protected override function change(event:ListEvent):void {
	     	if (parentApplication.hasPermission("canWriteAnyObject")) {
            	super.change(event);
	     	} else {
	     		selectItem();
	     		Alert.show("Extraction status cannot be changed.");
	     	}
        }            

	    

	}
}