package views
{
	import mx.controls.CheckBox;
	import flash.events.Event;
	import flash.display.DisplayObject;
	import flash.text.TextField;
	
	public class CheckBoxIsSelectedChipType extends CheckBox
	{
		  private var _data:Object;
		  

          [Bindable]
          override public function get data():Object {
          	return _data; 
          }
          override public function set data(o:Object):void {
          	_data = o;
          	if (o != null && o.hasOwnProperty("@isSelected") && o.@isSelected == "true") {
          		this.selected = true;
          	} else {
          		this.selected = false;
          	}
          }
          override protected function initializationComplete():void {
          	this.addEventListener(Event.CHANGE, change);
          }   
          
          private function change(event:Event):void {
          	if (this.selected) {
          		_data.@isSelected = "true";
          		// toggle all other selections off
	          	parentDocument.toggleOtherChipTypeSelections(_data.@value);
          	} else {
          		_data.@isSelected = "false";
          	}   
          	// initialize the samples grid
          	parentDocument.parentDocument.samplesView.initializeSamplesGrid();
          	// propagate selected chip type to samples
          	parentDocument.propagateChipType();       	
          	// check for sampleSetup completeness
          	parentDocument.checkSampleSetupCompleteness();
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
 				if (_data != null && _data.hasOwnProperty("@isSelected") && _data.@isSelected == "true") {
          			this.selected = true;
	          	} else {
    	      		this.selected = false;
        	  	}
 
			}

	}
}