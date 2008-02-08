package views
{
	public class ComboBoxWorkflowStatusExtraction extends ComboBoxWorkflowStatus
	{
		protected override function getCellAttributeName():String {
			return "@extractionStatus";
		}
	}
}