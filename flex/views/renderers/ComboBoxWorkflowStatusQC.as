package views.renderers
{
	public class ComboBoxWorkflowStatusQC extends ComboBoxWorkflowStatus
	{
		protected override function initializeFields():void {
			super.initializeFields();
	    	cellAttributeName            = "@qualStatus";
	    }
	}
}