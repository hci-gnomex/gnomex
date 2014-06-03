package views.renderers
{
	import mx.controls.Label;
	import flash.display.Graphics;
	import hci.flex.renderers.RendererFactory;
  
	public class LabelSampleChannel2 extends Label 
	{
    	override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void
     	{
          super.updateDisplayList(unscaledWidth,unscaledHeight);
          if (data == null) {
          	return;
          }
          var g:Graphics = graphics;
          g.clear();
          
          if (data.@idSampleChannel2 == '0') {
	          g.beginFill(parentApplication.cy5Color);
		      g.drawRect(0,0,unscaledWidth,unscaledHeight);
    		  g.endFill();          	
          } else {
          	g.beginFill(0xffffff);
		    g.drawRect(0,0,unscaledWidth,unscaledHeight);
    		g.endFill();  
          	this.setStyle("color","red");
          	this.setStyle("fontWeight", "bold");
          }


	     }
	}
}