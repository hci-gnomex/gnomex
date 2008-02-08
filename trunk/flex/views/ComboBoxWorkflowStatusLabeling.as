package views
{
	public class ComboBoxWorkflowStatusLabeling extends ComboBoxWorkflowStatus
	{
		protected override function getCellAttributeName():String {
			return "@labelingStatus";
		}
	}
}