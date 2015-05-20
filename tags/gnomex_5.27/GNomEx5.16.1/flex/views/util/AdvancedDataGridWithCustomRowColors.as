package views.util
{
	import flash.display.Sprite;
	
	import mx.collections.ArrayCollection;
	import mx.controls.AdvancedDataGrid;
	
	public class AdvancedDataGridWithCustomRowColors extends AdvancedDataGrid
	{
		override protected function drawRowBackground(s:Sprite, rowIndex:int, 
													  y:Number, height:Number, color:uint, dataIndex:int):void {
			
			if (dataProvider != null && dataProvider.length > 0) {
				
				var item:Object;
				
				color = 0xFFFFFF;
				if( dataIndex < dataProvider.length ) {
					item = dataProvider[dataIndex];
					if( item != null && item.@customColor != null && item.@customColor != '') {
						var color1:uint = uint(item.@customColor);
						if (color1 != 0) {
							color = color1;
						}
					}
				}
			}
			
			super.drawRowBackground(s,rowIndex,y,height,color,dataIndex);
		}
	}
}