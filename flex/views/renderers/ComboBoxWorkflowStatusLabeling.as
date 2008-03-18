package views.renderers
{
	public class ComboBoxWorkflowStatusLabeling extends ComboBoxWorkflowStatus
	{
		protected override function initializeFields():void {
			super.initializeFields();
	    	cellAttributeName            = "@labelingStatus";
	    }
	}
}