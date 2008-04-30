package views.renderers
{
	import mx.controls.TextInput;
	import flash.display.Graphics;
	import mx.controls.Alert;
	import flash.events.Event;

	public class TextInputSampleName extends TextInput
	{
        
    	override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void
     	{
          super.updateDisplayList(unscaledWidth,unscaledHeight);
          if (data == null) {
          	return;
          }
          this.setStyle("backgroundColor", data.@name == '' ? parentApplication.REQUIRED_FIELD_BACKGROUND : "0xffffff");
          
          if (data.@canChangeSampleName == "Y" || parentApplication.hasPermission("canWriteAnyObject")) {
	       	this.editable = true;
	      } else {
	       	this.editable = false;
	      }

	     }
	}
}