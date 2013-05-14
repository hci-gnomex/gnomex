package views.util
{
	import mx.skins.halo.HaloBorder;
	import mx.core.mx_internal;
	import flash.display.Graphics;       
    
	use namespace mx_internal;         
 
	public class SmallToolTip extends HaloBorder
	{
		public function SmallToolTip()
		{
			super();
		}
 
		override mx_internal function drawBorder(w:Number, h:Number):void{
			super.drawBorder(w,h);
			// Draw a small tail at the bottom left side of the tooltip
			var gr:Graphics = graphics;
			gr.beginFill(getStyle('borderColor'), 1);
			gr.moveTo(x + 7, y + h);
			gr.lineTo(x + 10, y + h + 4);
			gr.lineTo(x + 13, y + h)
			gr.moveTo(x + 7, y + h);
			gr.endFill();
		}	
	}
}