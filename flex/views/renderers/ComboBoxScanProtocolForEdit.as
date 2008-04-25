package views.renderers
{
	import mx.events.ListEvent;
	import mx.controls.Alert;
	
	public class ComboBoxScanProtocolForEdit  extends ComboBoxBase
	{
			protected override function initializeFields():void {
		    	dictionaryClassName          = "hci.gnomex.model.ScanProtocol";
		    	cellAttributeName            = "@idScanProtocol";
		    	choiceDisplayAttributeName   = "@display";
		    	choiceValueAttributeName     = "@value";
		    }
		   	protected override function change(event:ListEvent):void {
		     	if (parentApplication.hasPermission("canWriteAnyObject")) {
	            	super.change(event);
		     	} else {
		     		selectItem();
		     		Alert.show("Scan protocol cannot be changed.");
		     	}
	        }            


	}

}