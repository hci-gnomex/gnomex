package views.renderers
{
	import mx.controls.Label;
	import flash.display.Graphics;

	public class LabelPercentage extends Label
	{
    	override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void
     	{
          super.updateDisplayList(unscaledWidth,unscaledHeight);
          if (data == null) {
          	return;
          }
          
          if (data.@other == 'Y') {
          	styleName = "other";
          } else if (data.@percentageDisplay != "" && data.@percentageDisplay != "100%") {
          	styleName = "percentageEmphasis";
          } else {
          	styleName = "normal";
          }
        
	    }
	}
}