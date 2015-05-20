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
	
	public class FilterComboBoxOligoBarcode extends views.renderers.FilterComboBox
	{
		private var _idSeqLibProtocol:String;
		private var _parentApp:Object;
		
		public function FilterComboBoxOligoBarcode()
		{
			super();
			setDataProvider();
		}
		
		protected function setSelectedIndex():void {
			if ( this.data != null ) { 
				this.selectedItem = this.getBarcode(data);
			}
		}
			
		protected function getBarcode(item:Object):Object {
			var barcode:XMLList = parentApp.dictionaryManager.getEntry("hci.gnomex.model.OligoBarcode", item.@idOligoBarcode);
			if (barcode.length() == 1) {
				return barcode[0];
			} else {
				return new Object();
			}
		}
		
		protected function setDataProvider():void {
			dataProvider = new XMLListCollection();
			var dp:XMLListCollection = XMLListCollection(dataProvider);
			
			if ( parentApp != null && idSeqLibProtocol != null){
				for each(var barcodeScheme:Object in parentApp.dictionaryManager.xml.Dictionary.(@className == 'hci.gnomex.model.OligoBarcodeScheme').DictionaryEntry) {
					// Only use scheme if it is allowed for this seq lib protocol
					var keepScheme:Boolean = false;
					for each (var x:XML in parentApp.dictionaryManager.xml.Dictionary.(@className == 'hci.gnomex.model.OligoBarcodeSchemeAllowed').DictionaryEntry.(@value != '' && @idOligoBarcodeScheme == barcodeScheme.@idOligoBarcodeScheme)) {
						if (idSeqLibProtocol == '' || x.@idSeqLibProtocol == idSeqLibProtocol) {
							keepScheme = true;
							break;
						}
					}
					if (!keepScheme) {
						continue;
					}
					
					var theBarcodes:XMLListCollection = new XMLListCollection(XMLList(parentApp.dictionaryManager.xml.Dictionary.(@className == 'hci.gnomex.model.OligoBarcode').DictionaryEntry.(@value != '' && @isActive != 'N' && @idOligoBarcodeScheme == barcodeScheme.@value)).copy());
					
					// Sort barcodes by sortOrder
					var barcodesSort:Sort = new Sort();
					barcodesSort.compareFunction = this.sortBarcodes;
					theBarcodes.sort = barcodesSort;
					theBarcodes.refresh();
					
					dp.addAll(theBarcodes);
					
				}
			}
			setSelectedIndex();	
		}
		
		private function sortBarcodes(obj1:Object, obj2:Object, fields:Array=null):int {
			if (obj1 == null && obj2 == null) {
				return 0;
			} else if (obj1 == null) {
				return 1;
			} else if (obj2 == null) {
				return -1;
			} else {
				var order1:int = obj1.@sortOrder;
				var order2:int = obj2.@sortOrder;
				
				if (obj1.@value == '') {
					return -1;
				} else if (obj2.@value == '') {
					return 1;
				} else {
					if (order1 < order2) {
						return -1;
					} else if (order1 > order2) {
						return 1;
					} else {
						return 0;
					}
				}
			}			
		} 
		
		public function get idSeqLibProtocol():String
		{
			return _idSeqLibProtocol;
		}
		
		public function set idSeqLibProtocol(value:String):void
		{
			_idSeqLibProtocol = value;
			setDataProvider();
		}
		
		public function get parentApp():Object
		{
			return _parentApp;
		}
		
		public function set parentApp(value:Object):void
		{
			_parentApp = value;
		}
		
	}
}