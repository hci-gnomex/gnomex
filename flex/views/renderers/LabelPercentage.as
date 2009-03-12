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
          if (data.@percentageDisplay != "" && data.@percentageDisplay != "100%") {
	      	this.setStyle("color", 0xAA082A);
	      	this.setStyle("fontWeight", "bold");
          } else {
          	this.setStyle("color", 0x000000);
	      	this.setStyle("fontWeight", "normal");
          }
          /*
	          var g:Graphics = graphics;
    	      g.clear();
        	  g.beginFill(0xffff99);
          	  g.drawRect(0,0,unscaledWidth,unscaledHeight);
          	  g.endFill();           	
          } else {
	          var g:Graphics = graphics;
    	      g.clear();
        	  g.beginFill(owner.);
          	  g.drawRect(0,0,unscaledWidth,unscaledHeight);
          	  g.endFill();           	
          	
          }
          */

	     }
	}
}