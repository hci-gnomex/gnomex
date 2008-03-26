package views.renderers
{
	import mx.events.ListEvent;
	import mx.controls.Alert;
	
	public class ComboBoxFeatureExtractionProtocol  extends ComboBoxBase
	{
			protected override function initializeFields():void {
		    	dictionaryClassName          = "hci.gnomex.model.FeatureExtractionProtocol";
		    	cellAttributeName            = "@idFeatureExtractionProtocol";
		    	choiceDisplayAttributeName   = "@display";
		    	choiceValueAttributeName     = "@value";
		    }
		    
		    protected override function change(event:ListEvent):void {
		        parentDocument.workList.filterFunction = null;
		        super.change(event);
	        }

	}

}