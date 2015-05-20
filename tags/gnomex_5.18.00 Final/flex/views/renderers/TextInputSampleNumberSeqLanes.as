package views.renderers
{
	import flash.display.Graphics;
	import flash.events.Event;
	
	import hci.flex.renderers.RendererFactory;
	
	import mx.controls.TextInput;

	public class TextInputSampleNumberSeqLanes extends TextInput
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
          if (data == null || !(data is XML)) {
			this.editable = false;
          	return;
          }
		  this.editable = true;
          var g:Graphics = graphics;
          g.clear();
          g.beginFill( data.@numberSequencingLanes == '' ? RendererFactory.DEFAULT_MISSING_REQUIRED_FIELD_BACKGROUND : 0xffffff );
          if (data.@numberSequencingLanes == '' ) {
	          g.lineStyle(RendererFactory.DEFAULT_MISSING_REQUIRED_FIELD_BORDER_THICKNESS,
	                      RendererFactory.DEFAULT_MISSING_REQUIRED_FIELD_BORDER );          	
          }
          g.drawRect(0,0,unscaledWidth,unscaledHeight);
          g.endFill();
          


	    }
	}
}