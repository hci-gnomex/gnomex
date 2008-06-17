package views.renderers
{
	import flash.display.DisplayObject;
	import flash.events.Event;
	import flash.text.TextField;
	
	import mx.controls.CheckBox;
	

	public class CheckBox extends mx.controls.CheckBox
	{
	  protected var _data:Object;
	  protected var _dataField:String = "@isSelected";
	  
	  public function set dataField(dataField:String):void {
	  	this._dataField = dataField;	
	  }
	  
      [Bindable]
      override public function get data():Object {
      	return _data;
      }
      override public function set data(o:Object):void {
      	_data = o;
      	if (o != null && o.hasOwnProperty(_dataField) && o[_dataField] == "true") {
      		this.selected = true;
      	} else {
      		this.selected = false;
      	}
      }
      override protected function initializationComplete():void {
      	this.addEventListener(Event.CHANGE, change);
      }   
      
      protected function change(event:Event):void {
      	if (this.selected) {
      		_data[_dataField] = "true";
      	} else {
      		_data[_dataField] = "false";
      	}
      }
      
      /**
	   * center the checkbox and refresh the checkbox based on changed data
	   * values due to toggling off other selections.
	   */
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
			
			// Select or de-select checkbox based on @isSelected value
 			if (_data != null && _data.hasOwnProperty(_dataField) && _data[_dataField] == "true") {
      			this.selected = true;
          	} else {
	      		this.selected = false;
    	  	}
 
		}          
	}
}