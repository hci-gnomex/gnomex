package views.util
{
	import flash.display.DisplayObject;
	import flash.events.Event;
	import flash.events.MouseEvent;
	
	import mx.collections.XMLListCollection;
	import mx.controls.AdvancedDataGrid;
	import mx.events.CollectionEvent;
	import mx.events.CollectionEventKind;
	import mx.events.ScrollEvent;
	import mx.events.ScrollEventDetail;
	
	
	public class AdvancedDataGridLargeDataSet extends AdvancedDataGrid
	{
		
		private var dataLength:int = 0; 
				
		public function AdvancedDataGridLargeDataSet()
		{
			super();
		}
		
		override protected function mouseWheelHandler(event:MouseEvent):void {
			event.preventDefault();
			var delta:int = -event.delta;
			this.scrollAndExpandByIndex(delta);
		}
		
		override protected function scrollHandler(event:Event):void {
			event.preventDefault();
			event.stopImmediatePropagation();
			if (event is ScrollEvent){
				var se:ScrollEvent = ScrollEvent(event);
				var delta:Number = se.delta;
				if ( se.detail == ScrollEventDetail.PAGE_DOWN ) {
					delta = Math.round( this.dataLength/10 );
				} else if ( se.detail == ScrollEventDetail.PAGE_UP ) {
					delta = Math.round( -this.dataLength/10 );
				}
				this.scrollAndExpandByIndex(delta);
			}
		}
		
		
		override protected function collectionChangeHandler(event:Event):void{
			
			super.collectionChangeHandler( event );
			if ( event is CollectionEvent ) {
				var cEvent:CollectionEvent = CollectionEvent(event);
				if ( cEvent.kind == CollectionEventKind.RESET  ){
					callLater(this.expandVisibleNodes);
				}
			}
			event.stopImmediatePropagation();
		}
		
		
		private function expandNodes(topIndex:int, bottomIndex:int, dataCollection:XMLListCollection):void {
			
			if ( dataCollection == null || dataCollection.length == 0 ) {
				return;
			}
			
			for ( var index:int = 0; index < dataCollection.length; index ++ ) {
				var node:Object = dataCollection[index];
				if ( index >= topIndex-1 && index <= bottomIndex ) {
					this.expandItem( node, true );
				} else {
					this.expandItem( node, false );
				}
				
			}
			
		}
		
		private function expandVisibleNodes():void {
			
			if ( dataProvider == null || dataProvider.source == null || dataProvider.source.source == null ) {
				return; 
			}
			
			var dataCollection:XMLListCollection = this.dataProvider.source.source;
			
			this.dataLength = dataCollection.length;
			
			if ( dataCollection.length == 0 ) {
				return;
			}
			
			var firstItem:XML = this.firstVisibleItem as XML;
			if ( firstItem.name() == 'BillingItem' && firstItem.parent() != null ) {
				firstItem = firstItem.parent();
			}
			
			var topIndex:int = 0;
			if ( firstItem != null && dataCollection.getItemIndex( firstItem ) >= 0 ) {
				topIndex = dataCollection.getItemIndex( firstItem );
			}
			
			var bottomIndex:int = this.findBottomIndex(dataCollection, topIndex, this.rowCount); 
			
			this.expandNodes(topIndex, bottomIndex, dataCollection);

		}
		
		public function scrollToItemAndExpand(item:Object):void {
			
			if ( item == null || dataProvider == null || dataProvider.source == null || dataProvider.source.source == null ) {
				return; 
			}
			
			var dataCollection:XMLListCollection = this.dataProvider.source.source;
			
			this.dataLength = dataCollection.length;
			
			if ( dataCollection.length == 0 ) {
				return;
			}
			
			var index:int = dataCollection.getItemIndex(item);
			
			if ( index == -1 ) {
				return;
			}
			
			var maxTopIndex:int = this.findMaxIndex(dataCollection,this.rowCount);
			var topIndex:int = Math.min(index, maxTopIndex);
			
			this.firstVisibleItem = dataCollection.getItemAt(topIndex);
				
			var bottomIndex:int = this.findBottomIndex(dataCollection, topIndex, this.rowCount); 
			
			this.expandNodes(topIndex, bottomIndex, dataCollection);
			
			this.selectedItem = item;
			
		}
		
		
		private function scrollAndExpandByIndex(delta:int):void {
			
			if ( dataProvider == null || dataProvider.source == null || dataProvider.source.source == null ) {
				return; 
			}
			
			var dataCollection:XMLListCollection = this.dataProvider.source.source;
			
			if ( dataCollection.length == 0 ) {
				return;
			}
			
			var maxTopIndex:int = this.findMaxIndex(dataCollection,this.rowCount);
			
			if ( this.firstVisibleItem != null && dataCollection.getItemIndex( this.firstVisibleItem ) == maxTopIndex && delta > 0 ) {
				return;
			}
			
			var topIndex:int = 0;
			
			var firstItem:XML = this.firstVisibleItem as XML;
			if ( firstItem.name() == 'BillingItem' && firstItem.parent() != null ) {
				firstItem = firstItem.parent();
			}
			
			if ( firstItem != null && dataCollection.getItemIndex( firstItem ) >= 0 ) {
				topIndex = (dataCollection.getItemIndex( firstItem ) + delta) < 0 ? 0 : (dataCollection.getItemIndex( firstItem ) + delta);
			}
			
			topIndex = Math.min(topIndex, maxTopIndex);
			
			this.firstVisibleItem =  dataCollection.getItemAt(topIndex);
			
			var bottomIndex:int = this.findBottomIndex(dataCollection, topIndex, this.rowCount); 
			
			this.expandNodes(topIndex, bottomIndex, dataCollection);
			
		}
		
		private function findMaxIndex(theListCollection:XMLListCollection, rowCount:int):int{
			var index:int = theListCollection.length;
			var itemCount:int = 0;
			
			while (itemCount < rowCount && index > 0) {
				index--;
				var item:XML = theListCollection.getItemAt(index) as XML;
				itemCount += item.children().length() + 1;
			}
			
			if ( itemCount >= rowCount ) {
				index++;
			}
			
			return index;
		}
		
		private function findBottomIndex(theListCollection:XMLListCollection, startIndex:int, rowCount:int):int{
			var index:int = startIndex;
			var itemCount:int = 0;
			
			while (itemCount < rowCount && index < theListCollection.length-1) {
				index++;
				var item:XML = theListCollection.getItemAt(index) as XML;
				itemCount += item.children().length() + 1;
			}
						
			return index;
		}
	}
}