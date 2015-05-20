package views.util
{
	/**
	 * 
	 * WORKAROUND FOR MAC COPY/PASTE BUG WHERE NEWLINES ARE STRIPPED. 
	 * OVERRIDE THE DEFAULT FLASH COPY/PASTE IMPLEMENTATION
	 * AND HANDLE DIRECTLY IN EVENT HANDLER.
	 * 
	 * see http://www.actionscript.org/forums/showthread.php3?t=70375
	 * 
	 */	
	import mx.controls.TextArea;
	import flash.events.KeyboardEvent;
	import flash.events.TextEvent;
	
	public class TextAreaWorkaround extends TextArea
	{
		private var isPasteCommand:Boolean = false;
		
		public function TextAreaWorkaround()
		{
			super();
			this.addEventListener(TextEvent.TEXT_INPUT, onTextInput); 
			this.addEventListener(KeyboardEvent.KEY_DOWN, onKeyDown); 

		}
		
		
	
		private function onKeyDown(event:KeyboardEvent):void {
			if(event.ctrlKey == true && event.keyCode == 17){ 
				isPasteCommand = true; 
			} 
		}
		
		private function onTextInput(event:TextEvent):void { 
			if(isPasteCommand){ 
				event.preventDefault(); 
				
				// If you paste into this box we need to complete the paste work. 
				var prefix:String = this.text.substr(0, this.selectionBeginIndex); 
				var suffix:String = this.text.substr(this.selectionEndIndex, this.length);
				var newString:String = prefix + event.text + suffix; 
				this.text = newString; 
				var newCaretIndex:int = prefix.length + event.text.length; 
				this.setSelection(newCaretIndex, newCaretIndex); 
				
				isPasteCommand = false; // reset flag. 
			}
		}
	}
}