package views.util
{
	import mx.controls.advancedDataGridClasses.AdvancedDataGridColumn;
	
	public class HideShowHandler implements LeafHandler
	{
		private var item:AdvancedDataGridColumn = null;
		private var headerToLookFor:String;    
		
		
		public function HideShowHandler(headerToLookFor:String)
		{
			this.headerToLookFor = headerToLookFor;
		}
		
		public function handleLeaf(leaf:AdvancedDataGridColumn):void
		{
			if (item != null) return;
			if (leaf.headerText == headerToLookFor) item = leaf;
		}
		
		public function getLeaf():AdvancedDataGridColumn {
			return item;
		}
	}
}