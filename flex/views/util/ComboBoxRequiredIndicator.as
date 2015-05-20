package views.util
{
	import mx.controls.ComboBox;
	import flash.events.Event;
	import flash.events.FocusEvent;
	import hci.flex.renderers.RendererFactory;
	
	public class ComboBoxRequiredIndicator extends ComboBox
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
		
		public function ComboBoxRequiredIndicator() {
			super();
		}		
		
		override protected function focusInHandler(event:FocusEvent):void {
			super.focusInHandler(event);
			this.addEventListener(Event.CHANGE, change);
		}
		
		override protected function focusOutHandler(event:FocusEvent):void {
			super.focusOutHandler(event);
			this.removeEventListener(Event.CHANGE, change);
		}
		
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			
			var currentFillColors:Array = getStyle("fillColors");
			if(currentFillColors[0] != missingRequiredFieldBackground) {
				defaultFillColors = getStyle("fillColors");
			}		
			change(null);
		}


		protected function change(event:Event):void {
			if (this.selectedIndex == 0 && this._isRequired) {
				var currentFillColors:Array = getStyle("fillColors");
				if(currentFillColors[0] != missingRequiredFieldBackground) {
					defaultFillColors = getStyle("fillColors");
				}		
				this.setStyle("fillColors", [missingRequiredFieldBackground, "white"]);
			} else {
				this.setStyle("fillColors", defaultFillColors);
			}
		}
	}
}