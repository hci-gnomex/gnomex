package views.renderers
{
	import mx.controls.Label;
	import flash.display.Graphics;

	public class LabelBase extends mx.controls.Label
	{   protected var cellAttributeName:String;

        override protected function initializationComplete():void {   
	        initializeFields();
        }
        
        protected function initializeFields():void {        	
        }
        
    	override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void
     	{
          super.updateDisplayList(unscaledWidth,unscaledHeight);
          if (data == null) {
          	return;
          }
          var g:Graphics = graphics;
          g.clear();
          g.beginFill( data[cellAttributeName] == '' ? parentApplication.REQUIRED_FIELD_BACKGROUND : 0xffffff );
          g.drawRect(0,0,unscaledWidth,unscaledHeight);
          g.endFill();

	     }
	}
}