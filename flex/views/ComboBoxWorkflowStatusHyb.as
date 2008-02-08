package views
{
	public class ComboBoxWorkflowStatusHyb extends ComboBoxWorkflowStatus
	{
		protected override function getCellAttributeName():String {
			return "@hybStatus";
		}
	}
}