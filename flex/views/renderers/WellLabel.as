package views.renderers 
{
	
	import mx.controls.Label;
	

	// Special Well label with a white background
	public class WellLabel extends Label
	{ 	
		
		public function WellLabel(name:String):void{
			super();
			text = name;
			setStyle('fontSize', 7);
			setStyle('textAlign','center');
		}
		
		override protected function updateDisplayList(
			unscaledWidth:Number, unscaledHeight:Number): void {
			super.updateDisplayList(unscaledWidth,unscaledHeight);
			graphics.lineStyle(1);
			graphics.beginFill(0xFFFFFF);
			graphics.drawRoundRect (0,0,unscaledWidth,unscaledHeight,3,3);
			graphics.endFill();
		}
				
	}

}