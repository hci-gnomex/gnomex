package views.renderers
{
	import mx.controls.CheckBox;
	import flash.events.Event;
	import flash.display.DisplayObject;
	import flash.text.TextField;
	import mx.controls.DataGrid;
	import mx.controls.dataGridClasses.DataGridListData;

	public class CheckBoxCy3 extends CheckBox
	{
		  private var _data:Object;
		  

          [Bindable]
          override public function get data():Object {
          	return _data;
          }
          override public function set data(o:Object):void {
          	_data = o;
          	if (o != null && o.hasOwnProperty("@cy3") && o.@cy3 != null && o.@cy3 == "true") {
          		this.selected = true;
          	} else {
          		this.selected = false;
          	}
          }
          override protected function initializationComplete():void {
          	this.addEventListener(Event.CHANGE, change);
          }   
          
          private function change(event:Event):void {
          	if (_data == null || _data.@cy3 == null) {
          		return;
          	}
          	if (this.selected) {
          		_data.@cy3 = "true";
          		_data.@cy5 = "false";
				var grid:DataGrid = DataGrid(DataGridListData(listData).owner);     
				grid.invalidateDisplayList();     		
          	} else {
          		_data.@cy3 = "false";
          		
          	}
          }
			override protected function updateDisplayList(w:Number, h:Number):void
			{
				super.updateDisplayList(w, h);
		
				var n:int = numChildren;
				for (var i:int = 0; i < n; i++)
				{
					var c:DisplayObject = getChildAt(i);
					if (!(c is TextField))
					{
						c.x = (w - c.width) / 2;
						c.y = 0;
					}
				}
				
				// Select or de-select checkbox based on @cy3 value
 				if (_data != null && _data.hasOwnProperty("@cy3") && _data.@cy3 == "true") {
          			this.selected = true;
	          	} else {
    	      		this.selected = false;
        	  	}
 
			}          
	}
}