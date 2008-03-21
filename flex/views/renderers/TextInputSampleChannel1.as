package views.renderers
{
	import mx.controls.TextInput;
	import flash.display.Graphics;
	import flash.events.Event;

	public class TextInputSampleChannel1 extends TextInput
	{
    	override protected function initializationComplete():void
        {   
            this.addEventListener(Event.CHANGE, change);
        }
        
        
        protected function change(event:Event):void {
        	if (this.text == "") {
	        	data["@idSampleChannel1"] = "0";
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
          g.beginFill( data.@idSampleChannel1 == '0' ? parentApplication.REQUIRED_FIELD_BACKGROUND : 0xffffff );
          g.drawRect(0,0,unscaledWidth,unscaledHeight);
          g.endFill();

	     }
	}
}