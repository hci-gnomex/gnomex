package views.renderers
{
	import hci.flex.renderers.RendererFactory;
	
	import mx.collections.HierarchicalCollectionView;
	import mx.collections.IList;
	import mx.collections.Sort;
	import mx.collections.XMLListCollection;
	import mx.controls.AdvancedDataGrid;
	import mx.controls.Alert;
	import mx.controls.DataGrid;
	import mx.core.IFactory;
	import mx.events.CollectionEvent;
	import mx.events.ListEvent;
	
	public class FilterComboBoxNumberIScanSamples extends views.renderers.FilterComboBox
	{
		private var _iScanChip:Object;
		
		public function FilterComboBoxNumberIScanSamples()
		{
			super();
			setDataProvider();
		}
				
		protected function setDataProvider():void {
			dataProvider = new XMLListCollection();
			
			if ( iScanChip == null ) {
				return;
			}
			
			var dp:XMLListCollection = XMLListCollection(dataProvider);
			var samplesPerChip:int = iScanChip.@samplesPerChip != null ? iScanChip.@samplesPerChip : 0;
			var chipsPerKit:int = iScanChip.@chipsPerKit != null ? iScanChip.@chipsPerKit : 0;
			
			if ( samplesPerChip == 0    || chipsPerKit == 0 ) {
				return;
			}
			
			var numberOfSamples:int = samplesPerChip * chipsPerKit;
			var numberOfChips:int = chipsPerKit;
			var numberOfKits:int = 1;
			
			do {
				var item:XML = new XML("<IScanSampleNumberItem/>");
				item.@numberOfSamples = numberOfSamples;
				item.@numberOfChips = numberOfChips;
				item.@numberOfKits = numberOfKits;
				item.@display = numberOfSamples + " samples (" + numberOfKits + " kits)";
				dp.addItem(item);
				numberOfKits += 1; 
				numberOfChips += chipsPerKit;
				numberOfSamples = numberOfChips*samplesPerChip;
			} while ( numberOfSamples <= 500);
			
			item = new XML("<IScanSampleNumberItem/>");
			item.@numberOfSamples = 1;
			item.@numberOfChips = 0;
			item.@numberOfKits = 0;
			item.@display = "Other/Custom number of samples";
			dp.addItem(item);
		}
		
		
		public function get iScanChip():Object
		{
			return _iScanChip;
		}
		
		public function set iScanChip(value:Object):void
		{
			_iScanChip = value;
			setDataProvider();
		}
				
	}
}