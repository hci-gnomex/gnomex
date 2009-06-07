package views.renderers
{
	import mx.controls.TextInput;
	import flash.display.Graphics;
	import flash.events.Event;
	import hci.flex.renderers.RendererFactory; 

	public class TextInputSeqPrepByCore extends TextInput
	{
    	override protected function initializationComplete():void
        {   
            this.addEventListener(Event.CHANGE, change);
        }
        
        
        protected function change(event:Event):void {
        	if (this.text != "N" && this.text != "Y") {
        		mx.controls.Alert.show("Please enter Y or N in this field.");
        	} 
        }
            		
    	override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void
     	{
          super.updateDisplayList(unscaledWidth,unscaledHeight);
          if (data == null) {
          	return;
          }
          var g:Graphics = graphics;
          g.clear();
          g.beginFill( data.@idSampleChannel1 == '0' ? RendererFactory.DEFAULT_MISSING_REQUIRED_FIELD_BACKGROUND : 0xffffff );
          g.drawRect(0,0,unscaledWidth,unscaledHeight);
          g.endFill();
          
          if (data.@canChangeSeqPrepByCore == "Y" || parentApplication.hasPermission("canWriteAnyObject")) {
	       	this.editable = true;
	      } else {
	       	this.editable = false;
	      }

	    }
	}
}