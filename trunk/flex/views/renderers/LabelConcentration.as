package views.renderers
{
	import mx.controls.Label;
	import flash.display.Graphics;

	public class LabelConcentration extends Label
	{
    	override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void
     	{
          super.updateDisplayList(unscaledWidth,unscaledHeight);
          if (data == null) {
          	return;
          }
          var g:Graphics = graphics;
          g.clear();
          g.beginFill( data.@concentration == '' ? parentApplication.REQUIRED_FIELD_BACKGROUND : 0xffffff );
          g.drawRect(0,0,unscaledWidth,unscaledHeight);
          g.endFill();

	     }
	}
}