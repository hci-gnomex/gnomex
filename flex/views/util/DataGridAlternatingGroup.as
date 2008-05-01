package views.util
{
	import mx.controls.DataGrid;
	import flash.display.Sprite;
	import mx.collections.ArrayCollection;

	public class DataGridAlternatingGroup extends DataGrid
	{
		override protected function drawRowBackground(s:Sprite, rowIndex:int, 
		y:Number, height:Number, color:uint, dataIndex:int):void {
			
			if (dataProvider != null && dataProvider.length > 0) {
			   
			    var item:Object;
			
		      	if( dataIndex < dataProvider.length ) {
			      	item = dataProvider[dataIndex];
			      	if( item != null && item.@altColor != null && item.@altColor == "true" ) {
			      	} else {
				      	color = 0xEEEEE0;
			      	}
		      	}
		  }
				
	      super.drawRowBackground(s,rowIndex,y,height,color,dataIndex);
	     }


	}
}