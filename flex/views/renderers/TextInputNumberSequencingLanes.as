package views.renderers
{
	import mx.controls.TextInput;
	import flash.display.Graphics;
	import mx.controls.Alert;
	import flash.events.Event;
	import mx.validators.NumberValidator;

	public class TextInputNumberSequencingLanes extends TextInput
	{
		
    	override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void
     	{
          super.updateDisplayList(unscaledWidth,unscaledHeight);
          if (data == null) {
          	return;
          }
          this.setStyle("backgroundColor", data.@name == '' ? parentApplication.REQUIRED_FIELD_BACKGROUND : "0xffffff");
          
          if ((data.@canChangeNumberSequencingLanes != null && data.@canChangeNumberSequencingLanes == "Y") || 
              parentApplication.hasPermission("canWriteAnyObject")) {
	       	this.editable = true;
	      } else {
	       	this.editable = false;
	      }

	     }
	}
}