package views.renderers
{
	import mx.events.ListEvent;
	import mx.controls.Alert;
	
	public class ComboBoxQCStatus extends ComboBoxStatus
	{
		protected override function initializeFields():void {
			super.initializeFields();
	    	cellAttributeName            = "@qualStatus";
	    }
	   	protected override function change(event:ListEvent):void {
	     	if (parentApplication.hasPermission("canWriteAnyObject")) {
            	super.change(event);
	     	} else {
	     		selectItem();
	     		Alert.show("QC status cannot be changed.");
	     	}
        }            

	    

	}
}