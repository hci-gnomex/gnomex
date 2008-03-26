package views.renderers
{
	import mx.events.ListEvent;
	import mx.controls.Alert;
	
	public class ComboBoxScanProtocol  extends ComboBoxBase
	{
			protected override function initializeFields():void {
		    	dictionaryClassName          = "hci.gnomex.model.ScanProtocol";
		    	cellAttributeName            = "@idScanProtocol";
		    	choiceDisplayAttributeName   = "@display";
		    	choiceValueAttributeName     = "@value";
		    }
		    
		    protected override function change(event:ListEvent):void {
		        parentDocument.workList.filterFunction = null;
		        super.change(event);
	        }

	}

}