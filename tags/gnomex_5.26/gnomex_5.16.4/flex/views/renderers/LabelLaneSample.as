package views.renderers
{
	import mx.controls.Label;
	import flash.display.Graphics;
	import hci.flex.renderers.RendererFactory;

	public class LabelLaneSample extends Label
	{
    	override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void
     	{
          super.updateDisplayList(unscaledWidth,unscaledHeight);
          if (data == null) {
          	return;
          } 
          var g:Graphics = graphics;
          g.clear();
          g.beginFill( data.@idSample == '0' ? RendererFactory.DEFAULT_MISSING_REQUIRED_FIELD_BACKGROUND : 0xffffff );
          if (data.@idSample == '0') {
	          g.lineStyle(RendererFactory.DEFAULT_MISSING_REQUIRED_FIELD_BORDER_THICKNESS,
	                      RendererFactory.DEFAULT_MISSING_REQUIRED_FIELD_BORDER );          	
          }
          g.drawRect(0,0,unscaledWidth,unscaledHeight);
          g.endFill();

	     }
	}
}