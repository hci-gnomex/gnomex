package views.renderers
{
	import mx.controls.TextInput;
	import flash.display.Graphics;
	import flash.events.Event;
	import hci.flex.renderers.RendererFactory;

	public class TextInputSampleFragSizeTo extends TextInput
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
          var g:Graphics = graphics;
          g.clear();
          g.beginFill( data.@fragmentSizeTo == '' ? RendererFactory.DEFAULT_MISSING_REQUIRED_FIELD_BACKGROUND : 0xffffff );
          if (data.@fragmentSizeTo == '' ) {
	          g.lineStyle(RendererFactory.DEFAULT_MISSING_REQUIRED_FIELD_BORDER_THICKNESS,
	                      RendererFactory.DEFAULT_MISSING_REQUIRED_FIELD_BORDER );          	
          }
          g.drawRect(0,0,unscaledWidth,unscaledHeight);
          g.endFill();
          
          if (data.@canChangeSampleType == "Y" || parentApplication.hasPermission("canWriteAnyObject")) {
	       	this.editable = true;
	      } else {
	       	this.editable = false;
	      }

	    }
	}
}