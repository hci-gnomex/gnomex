package views.renderers
{
	import flash.display.Graphics;
	import flash.events.Event;
	
	import hci.flex.renderers.RendererFactory;
	
	import mx.controls.TextInput;

	public class TextInputSampleConcentration extends TextInput
	{
    	override protected function initializationComplete():void
        {   
            this.addEventListener(Event.CHANGE, change);
        }
        
        
        protected function change(event:Event):void {
        }
            		
    	override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void
     	{
          super.updateDisplayList(unscaledWidth,unscaledHeight);
          if (data == null) {
          	return;
          }
		  if (!(data is XML)) {
			  this.editable = false;
			  return;
		  }

          if (data.@canChangeSampleConcentration == "Y" || parentApplication.hasPermission("canWriteAnyObject")) {
	       	this.editable = true;
	      } else {
	       	this.editable = false;
	      }

	    }
	}
}