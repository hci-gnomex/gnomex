package views.util
{
	import flash.events.MouseEvent;
	import flash.events.FocusEvent;
	
	import hci.flex.renderers.RendererFactory;
	
	import mx.controls.CheckBox;
	
	public class CheckBoxRequiredIndicator extends CheckBox
	{
		private var missingRequiredFieldBackground:uint = RendererFactory.DEFAULT_MISSING_REQUIRED_FIELD_BACKGROUND;
		private var defaultFillColors:Array;
		private var _isRequired:Boolean;
		
		public function get isRequired(): Boolean {
			return _isRequired;
		}		
		
		public function set isRequired(value:Boolean):void {
			_isRequired = value;
		}

		public function CheckBoxRequiredIndicator() {
			super();
		}
		
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			
			var currentFillColors:Array = getStyle("fillColors");
			if(currentFillColors[0] != missingRequiredFieldBackground) {
				defaultFillColors = getStyle("fillColors");
			}		
		}
				
		override protected function clickHandler(event:MouseEvent):void {	
			super.clickHandler(event);
			setBackgroundColor();
		}
		
		public function setBackgroundColor():void {
			var isCheckBoxSelected:Boolean = false;
			var childObject:Object;
			for (var i:int = 0; i < parent.numChildren; i++) {
				childObject = parent.getChildAt(i);
				if(childObject is CheckBoxRequiredIndicator && CheckBoxRequiredIndicator(childObject).selected) {
					isCheckBoxSelected = true;
					break;
				}
			}
			if (!isCheckBoxSelected && this._isRequired) {
				var currentFillColors:Array = getStyle("fillColors");
				if(currentFillColors[0] != missingRequiredFieldBackground) {
					defaultFillColors = getStyle("fillColors");
				}
				this.setStyle("fillColors", [missingRequiredFieldBackground, "white"]);
				for (i = 0; i < parent.numChildren; i++) {
					childObject = parent.getChildAt(i);
					if(childObject is CheckBoxRequiredIndicator && childObject != this) {
						CheckBoxRequiredIndicator(childObject).setStyle("fillColors", [missingRequiredFieldBackground, "white"]);
					}
				}
				
			} else {
				this.setStyle("fillColors", defaultFillColors);
				for (i = 0; i < parent.numChildren; i++) {
					childObject = parent.getChildAt(i);
					if(childObject is CheckBoxRequiredIndicator && childObject != this) {
						CheckBoxRequiredIndicator(childObject).setStyle("fillColors", defaultFillColors);
					}
				}
			}
		}
		
	}
}