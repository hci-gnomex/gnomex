package views.renderers
{
	import mx.controls.TextInput;
	import flash.display.Graphics;
	import flash.events.Event;

	public class TextInputFragmentSizeFrom extends TextInput
	{
    	override protected function initializationComplete():void
        {   
            this.addEventListener(Event.CHANGE, change);
        }
		
        protected function change(event:Event):void {
        	parentDocument.propagateFragmentSizeFrom(this.text);
        }
        		
    	override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void
     	{
          super.updateDisplayList(unscaledWidth,unscaledHeight);
          if (data == null) {
          	return;
          }
          this.setStyle("backgroundColor", data.@fragmentSizeFrom == '' ? parentApplication.REQUIRED_FIELD_BACKGROUND : "0xffffff");
          if (data.@canChangeSampleType == "Y" || parentApplication.hasPermission("canWriteAnyObject")) {
	       	this.editable = true;
	      } else {
	       	this.editable = false;
	      }
          
	    }
	}
}