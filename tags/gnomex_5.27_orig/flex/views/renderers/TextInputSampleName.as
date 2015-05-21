package views.renderers
{
	import mx.controls.TextInput;
	import mx.core.IFactory;
	import flash.display.Graphics;
	import flash.events.Event;
	import hci.flex.renderers.RendererFactory;

	public class TextInputSampleName extends TextInput
	{
		public static function create(state:String):IFactory {
			var r:String = " -~";
			if (state == 'CapSeqState' || state == 'FragAnalState' || state == 'MitSeqState' || state == 'CherryPickState' || state == 'IScanState') {
				r = "A-Za-z0-9_\\-";
			} 
			return RendererFactory.create(views.renderers.TextInputSampleName, {restrict:r});			
		}		

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
          var g:Graphics = graphics;
          g.clear();
          g.beginFill( data.hasOwnProperty("@name") && data.@name == '' ? RendererFactory.DEFAULT_MISSING_REQUIRED_FIELD_BACKGROUND : 0xffffff );
          if (data.hasOwnProperty("@name") && data.@name == '' ) {
	          g.lineStyle(RendererFactory.DEFAULT_MISSING_REQUIRED_FIELD_BORDER_THICKNESS,
	                      RendererFactory.DEFAULT_MISSING_REQUIRED_FIELD_BORDER );          	
          }
          g.drawRect(0,0,unscaledWidth,unscaledHeight);
          g.endFill();
          
          if (data.hasOwnProperty("@canChangeSampleName") && data.@canChangeSampleName == "Y" || parentApplication.hasPermission("canWriteAnyObject")) {
	       	this.editable = true;
	      } else {
	       	this.editable = false;
	      }

	    }
	}
}