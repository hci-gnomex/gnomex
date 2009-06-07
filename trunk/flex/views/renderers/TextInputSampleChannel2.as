package views.renderers
{
	import mx.controls.TextInput;
	import flash.display.Graphics;
	import flash.events.Event;
	import hci.flex.renderers.RendererFactory;

	public class TextInputSampleChannel2 extends TextInput
	{
    	override protected function initializationComplete():void
        {   
            this.addEventListener(Event.CHANGE, change);
        }
        
        
        protected function change(event:Event):void {
        	if (this.text == "") {
	        	data["@idSampleChannel2"] = "0";
	        	parentDocument.sampleDragGrid.invalidateDisplayList();
	 	        parentDocument.sampleDragGrid.invalidateList();
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
          g.beginFill( data.@idSampleChannel2 == '0' ? parentApplication.cy5ColorD : 0xffffff );
		  if (data.@idSampleChannel2 == '0' ) {
	          g.lineStyle(RendererFactory.DEFAULT_MISSING_REQUIRED_FIELD_BORDER_THICKNESS,
	                      parentApplication.cy5Color );          	
          }         
          g.drawRect(0,0,unscaledWidth,unscaledHeight);
          g.endFill();
          
          if (data.@canChangeSampleDesignations == "Y" || parentApplication.hasPermission("canWriteAnyObject")) {
	       	this.editable = true;
	      } else {
	       	this.editable = false;
	      }
	     }
	}
}