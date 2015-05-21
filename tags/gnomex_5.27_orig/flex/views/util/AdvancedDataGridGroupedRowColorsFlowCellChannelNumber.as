package views.util
{
	import mx.controls.AdvancedDataGrid;
	import flash.display.Sprite;
	
	//import mx.collections.ArrayCollection;
	

	public class AdvancedDataGridGroupedRowColorsFlowCellChannelNumber extends AdvancedDataGrid
	{
		private var colorA:uint = 0xEEEEE0; //grey-blue
		private var colorB:uint = 0xFFFFFF; //white
		private var alternatingColor:uint = colorB;
		private var identifier:String = '';
		
		override protected function drawRowBackground(s:Sprite, rowIndex:int, 
													  y:Number, height:Number, color:uint, dataIndex:int):void {
			// if this is a refresh, reset the first color so it the pattern stays the same.
			if(dataIndex == 0) {
				alternatingColor = colorB; // start with colorA, code will swap to colorA below
				identifier = '';
			}
			if (dataProvider != null && dataProvider.length > 0) {	// do we have a dataProvider or are we initializing an empty grid?			
				var item:Object;				
				
				if( dataIndex < dataProvider.length ) { 
					item = dataProvider[dataIndex];					
					var tempIdentifier:String = "";
					// is this the grid for the flowCell on the assemble or finalize page?
					if(item.hasOwnProperty("@flowCellChannelNumber") && item.@flowCellChannelNumber != ''){
						tempIdentifier = item.@flowCellChannelNumber;
						testForNewCategory(tempIdentifier);
					}
					
				} else { // we have a dataProvider but there are more rows in the grid than items in the dataProvider, just alternate for the rest
					defaultScheme();
				}
			} else { // no dataProvider (might be initializing before loading dataProvider or may be empty grid)				
				defaultScheme();
			}
			
			super.drawRowBackground(s,rowIndex,y,height,alternatingColor,dataIndex);
		}
		
		internal function defaultScheme():void {
			if(alternatingColor == colorA) {
				alternatingColor = colorB;
			} else {
				alternatingColor = colorA;
			}
		}
		
		internal function testForNewCategory(tempIdentifier:String):void {
			if(identifier != tempIdentifier) { // new category, alternate the color
				identifier = tempIdentifier;
				if(alternatingColor == colorA) {
					alternatingColor = colorB;
				}else {
					alternatingColor = colorA;
				}
			} else {
				// not a new category, leave color the same.
				alternatingColor = alternatingColor;
			}
		}
	}
}



