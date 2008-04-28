package views.renderers
{
	import mx.events.ListEvent;
	import mx.controls.Alert;
	
	public class ComboBoxFundingAgency extends ComboBoxBase
	{
			protected override function initializeFields():void {
		    	dictionaryClassName          = "hci.gnomex.model.FundingAgency";
		    	cellAttributeName            = "@idFundingAgency";
		    	choiceDisplayAttributeName   = "@display";
		    	choiceValueAttributeName     = "@value";
		    }
		    
		    protected override function change(event:ListEvent):void {
		        super.change(event);
	        }

	}

}