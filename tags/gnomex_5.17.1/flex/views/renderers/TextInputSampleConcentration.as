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
          var g:Graphics = graphics;
          g.clear();
          g.beginFill( data.@concentration == '' ? RendererFactory.DEFAULT_MISSING_REQUIRED_FIELD_BACKGROUND : 0xffffff );
          if (data.@concentration == '' ) {
	          g.lineStyle(RendererFactory.DEFAULT_MISSING_REQUIRED_FIELD_BORDER_THICKNESS,
	                      RendererFactory.DEFAULT_MISSING_REQUIRED_FIELD_BORDER );          	
          }
          g.drawRect(0,0,unscaledWidth,unscaledHeight);
          g.endFill();
          
          if (data.@canChangeSampleConcentration == "Y" || parentApplication.hasPermission("canWriteAnyObject")) {
	       	this.editable = true;
	      } else {
	       	this.editable = false;
	      }

	    }
	}
}