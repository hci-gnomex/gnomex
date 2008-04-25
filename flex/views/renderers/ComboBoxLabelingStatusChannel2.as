package views.renderers
{
	import mx.events.ListEvent;
	import mx.controls.Alert;
	
	public class ComboBoxLabelingStatusChannel2 extends ComboBoxStatus
	{
		protected override function initializeFields():void {
			super.initializeFields();
	    	cellAttributeName            = "@labelingStatusChannel2";
	    }
	   	protected override function change(event:ListEvent):void {
	     	if (parentApplication.hasPermission("canWriteAnyObject")) {
            	super.change(event);
	     	} else {
	     		selectItem();
	     		Alert.show("Labeling status cannot be changed.");
	     	}
        }            

	    

	}
}