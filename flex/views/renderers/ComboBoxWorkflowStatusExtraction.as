package views.renderers
{
	public class ComboBoxWorkflowStatusExtraction extends ComboBoxWorkflowStatus
	{
		protected override function initializeFields():void {
			super.initializeFields();
	    	cellAttributeName            = "@extractionStatus";
	    }
	}
}