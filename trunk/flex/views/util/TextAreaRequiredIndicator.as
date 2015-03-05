package views.util
{
	import flash.events.Event;
	import flash.events.FocusEvent;
	
	import hci.flex.renderers.RendererFactory;
	
	import mx.controls.TextArea;
	
	public class TextAreaRequiredIndicator extends TextArea
	{
		private var missingRequiredFieldBackground:uint = RendererFactory.DEFAULT_MISSING_REQUIRED_FIELD_BACKGROUND;
		private var defaultColor:uint = 0xFFFFFF;
		private var _isRequired:Boolean;
		
		
		public function get isRequired(): Boolean {
			return _isRequired;
		}		

		public function set isRequired(value:Boolean):void {
			_isRequired = value;
		}			
		
		public function TextAreaRequiredIndicator():void {
			super();
		}
		
		public function SetBackgroundColor():void {
			change(null);
		}		
		
		override public function set text(value:String):void {
			super.text = value;
			callLater(resizeToText);
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
			if(this.getStyle("backgroundColor") != missingRequiredFieldBackground) {
				defaultColor = this.getStyle("backgroundColor");
			}		
			change(null);
		}


		protected function change(event:Event):void {
			if (this.textField.text != null && this.textField.text == "" && this._isRequired) {
				if(this.getStyle("backgroundColor") != missingRequiredFieldBackground) {
					defaultColor = this.getStyle("backgroundColor");
				}	
				this.setStyle("backgroundColor", missingRequiredFieldBackground);
			} else {
				this.setStyle("backgroundColor", defaultColor);
			}
		}
		
		public function resizeToText():void {
			if ( this.editable ) {
				// For some reason textheight is way huge for editable text area. 
				this.height = this.text != '' ? Math.max(this.textHeight - 150, 30) : 30;
			} else {
				this.height = this.text != '' ? this.textHeight + 10 : 30;
			}
		}
	}
}