package views
{
	public class ComboBoxWorkflowStatusQC extends ComboBoxWorkflowStatus
	{
		protected override function getCellAttributeName():String {
			return "@qualStatus";
		}
	}
}