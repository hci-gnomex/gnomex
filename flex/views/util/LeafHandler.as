package views.util
{
	import mx.controls.advancedDataGridClasses.AdvancedDataGridColumn;
	
	public interface LeafHandler
	{
		function handleLeaf(leaf:AdvancedDataGridColumn):void;
	}
}