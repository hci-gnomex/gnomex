package views.renderers
{
	import mx.events.ListEvent;
	import mx.controls.Alert;
	
	public class ComboBoxHybProtocolForEdit  extends ComboBoxBase
	{
			protected override function initializeFields():void {
		    	dictionaryClassName          = "hci.gnomex.model.HybProtocol";
		    	cellAttributeName            = "@idHybProtocol";
		    	choiceDisplayAttributeName   = "@display";
		    	choiceValueAttributeName     = "@value";
		    }
		   	protected override function change(event:ListEvent):void {
		     	if (parentApplication.hasPermission("canWriteAnyObject")) {
	            	super.change(event);
		     	} else {
		     		selectItem();
		     		Alert.show("Hyb protocol cannot be changed.");
		     	}
	        }            


	}

}