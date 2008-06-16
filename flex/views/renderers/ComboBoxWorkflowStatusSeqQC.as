package views.renderers
{
	import mx.events.ListEvent;
	
	public class ComboBoxWorkflowStatusSeqQC extends ComboBoxWorkflowStatus
	{
		protected override function initializeFields():void {
			super.initializeFields();
	    	cellAttributeName            = "@qualStatus";
	    }
	    

	}
}