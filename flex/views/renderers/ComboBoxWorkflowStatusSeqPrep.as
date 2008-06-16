package views.renderers
{
	import mx.events.ListEvent;
	
	public class ComboBoxWorkflowStatusSeqPrep extends ComboBoxWorkflowStatus
	{
		protected override function initializeFields():void {
			super.initializeFields();
	    	cellAttributeName            = "@seqPrepStatus";
	    }
	    

	}
}