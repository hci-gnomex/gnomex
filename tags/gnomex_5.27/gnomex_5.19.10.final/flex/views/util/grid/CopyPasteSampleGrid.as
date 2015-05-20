package views.util.grid
{
	import flash.events.TextEvent;
	
	import mx.collections.XMLListCollection;
	
	import views.util.grid.CopyPasteDataGrid;
	import views.util.grid.SampleDataGridUtil;
	
	public class CopyPasteSampleGrid extends CopyPasteDataGrid
	{
		protected var _hasPlates:Boolean;
		
		public function CopyPasteSampleGrid()
		{
			super();
			setParameters();
		}
		
		private function setParameters():void
		{
			dataType="Sample";
			ignoredColumns= ['idSample','number', 'wellName', 'idPlateWell'];
			importantFields=['name','concentration'];
			horizontalScrollPolicy="auto";
			variableRowHeight=true;
			sortableColumns=false;
			sortExpertMode=true;
			displayItemsExpanded=true; 
			iconFunction=getSampleTreeIcon;
			selectionMode="multipleRows";
			this.setStyle("selectionColor","#DDF3FB");
			this.setStyle("defaultLeafIcon",null);
		}
		
		override protected function initializationComplete():void
		{
			pasteEnabled=parentApplication.allowPaste;
			super.initializationComplete();
		}
		
		public function getSampleTreeIcon(item:Object):Class {
			if (item == null) {
				return parentApplication.iconGroup;
			} else {
				return null; 
			}  
		}  
		
		override protected function handleTextPasted( event:TextEvent ):void
		{
			if (!_textArea)
			{
				return;
			}
			
			if (!_pasteEnabled && _pasteFunction == null)
			{
				return;
			}
			
			if (_pasteFunction != null)
			{
				_pasteFunction( event );
			}
			else
			{
				// Extract values from TSV format and populate the DataGrid
				var items:XMLListCollection = SampleDataGridUtil.getItemsFromText( event.text, this, parentApplication );
				
				for each (var item:XML in items)
				{
					addItemToDataProvider( item );
				}
				this.propagateIndexTagSequence();
				if ( this._hasPlates ) {
					updateWellNames();
				} 
			}
		}
		
		private function propagateIndexTagSequence():void {
			for each(var s:XML in this.getUnderlyingDataProvider()) {
				var bc:Object = parentApplication.dictionaryManager.getEntry('hci.gnomex.model.OligoBarcode', s.@idOligoBarcode);
				s.@barcodeSequence = bc != null ? bc.@barcodeSequence : "";
				bc = parentApplication.dictionaryManager.getEntry('hci.gnomex.model.OligoBarcode', s.@idOligoBarcodeB);
				s.@barcodeSequenceB = bc != null ? bc.@barcodeSequence : "";
			}
		}
		
		// Duplicates selected rows and adds them to the dataprovider
		override public function duplicateSelectedRows():void
		{
			saveDataProvider();
			var selectedItems:XMLListCollection = SampleDataGridUtil.getSelectedRows( this,  _dataType, _ignoredColumns );
			for each ( var item:XML in selectedItems ) {
				addItemToDataProvider(item);
			}
			if ( this._hasPlates ) {
				updateWellNames();
			} 
		}
				
		protected function getFirstEmptyWellIndex():int {
			for each (var sample:Object in this.getUnderlyingDataProvider()) {
				if ( sample.@name == '' ) {
					return this.getUnderlyingDataProvider().getItemIndex(sample);
				}
			}
			return -1; 
		}
		
		protected function getWellName(idx:int):String {
			// Since we are using naming by column convention (instead of by row)
			// we default to that
			var wellName:String = "";
			var y:int = idx % 96;
			wellName = parentApplication.wellNamesByColumn[y];
			
			return wellName;
		}
		
		public function updateWellNames():void {
			if ( !this._hasPlates ) {
				return;
			}
			for each (var sample:Object in this.getUnderlyingDataProvider()) {
				sample.@plateName = parentDocument != null ? parentDocument.getPlateName(this.getUnderlyingDataProvider().getItemIndex(sample)) : "";
				sample.@wellName = getWellName(this.getUnderlyingDataProvider().getItemIndex(sample));
			}
			this.addRowColorFields();
		}
		
		[Bindable]
		public function get hasPlates():Boolean { return _hasPlates; }
		public function set hasPlates( value:Boolean ):void
		{
			_hasPlates = value;
		}
	}
}