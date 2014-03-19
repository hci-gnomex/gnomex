package views.util
{
	import mx.controls.advancedDataGridClasses.AdvancedDataGridColumn;
	import mx.controls.advancedDataGridClasses.AdvancedDataGridColumnGroup;
	
	public class Traversal
	{
		private var handler:LeafHandler;
		
		public function Traversal(handler:LeafHandler)
		{
			this.handler = handler;
		}
		
		public function traverseAndGetHandler(clms:Array):LeafHandler {
			traverse(clms);
			return handler;
		}
		
		private function traverse(clms:Array):void {
			for (var i:int = 0; i < clms.length; i++) {
				if (clms[i] is AdvancedDataGridColumnGroup) {
					traverse(AdvancedDataGridColumnGroup(clms[i]).children);
				} else {
					handler.handleLeaf(AdvancedDataGridColumn(clms[i]));
				}
			}
		}
	}
}