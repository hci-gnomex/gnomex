package views.renderers
{
	public class ComboBoxWorkflowStatusHyb extends ComboBoxWorkflowStatus
	{
		protected override function initializeFields():void {
			super.initializeFields();
	    	cellAttributeName            = "@hybStatus";
	    }
	}
}