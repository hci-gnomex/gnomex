package views.renderers
{
	import mx.controls.CheckBox;
	import flash.events.Event;
	import flash.display.DisplayObject;
	import flash.text.TextField;
	import mx.core.IFactory; 
	import hci.flex.renderers.RendererFactory;

	public class CheckBoxIsSelectedSeqLibTreatment extends CheckBox
	{
		  private var _data:Object;
		  

          [Bindable]
          override public function get data():Object {
          	return _data;
          }
          override public function set data(o:Object):void {
          	_data = o;
          	if (o != null && o.hasOwnProperty("@isSelected") && o.@isSelected != null && o.@isSelected == "true") {
          		this.selected = true;
          	} else {
          		this.selected = false;
          	}
          }
          override protected function initializationComplete():void {
          	this.addEventListener(Event.CHANGE, change);
          }   
          
          private function change(event:Event):void {
          	if (_data == null || _data.@isSelected == null) {
          		return;
          	}
          	if (this.selected) {
          		_data.@isSelected = "true";
          		
          	} else {
          		_data.@isSelected = "false";
          	}
			parentDocument.selectSeqLibTreatment(null);
          	parentDocument.checkSeqAppSetupCompleteness();          	
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
				
				// Select or de-select checkbox based on @isSelected value
 				if (_data != null && _data.hasOwnProperty("@isSelected") && _data.@isSelected == "true") {
          			this.selected = true;
	          	} else {
    	      		this.selected = false;
        	  	}
        	  	
        	  	// Only allow admins to edit the seq lib treatments
        	  	if (parentDocument.currentState == "EditState") {
	        	  	if (parentApplication.hasPermission("canWriteAnyObject")) {
	        	  		this.enabled = true;
	        	  	} else {
	        	  		this.enabled = false;
	        	  	}
        	  	}
 
			}          
	}
}