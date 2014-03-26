package views.util
{
	import mx.core.UIComponent;
	
	public class ColoredDot extends UIComponent
	{
		private var radius:Number = 5;
		public var expId:String;
		private var fillColor:uint;
		private static var requestColor:uint = 0x66CC66;
		private static var analysisColor:uint = 0xFFFF66;
		private static var dataTrackColor:uint = 0x3399FF;
		private static var flowCellColor:uint = 0x99FFFF;
		private static var topicColor:uint = 0xFF9966;
		
		public function ColoredDot()
		{
			super();
			
		}
		
		override protected function updateDisplayList(x:Number, y:Number):void
		{
			if(expId.indexOf("A") == 0){
				fillColor = analysisColor;
			} else if(expId.indexOf("DT") == 0){
				fillColor = dataTrackColor;
			} else if(expId.indexOf("T") == 0){
				fillColor = topicColor;
			} else if(expId.indexOf("FC") == 0){
				fillColor = flowCellColor;
			} else if(expId.indexOf("R") != -1){
				fillColor = requestColor;
			}
			
			super.updateDisplayList(x, y);
			graphics.clear();
			graphics.beginFill(fillColor, .5);
			graphics.drawCircle(x, y, radius);
		}
	}
}