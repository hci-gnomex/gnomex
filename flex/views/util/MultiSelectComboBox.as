package views.util
{
	import flash.events.Event;
	import flash.events.KeyboardEvent;
	import flash.events.MouseEvent;
	
	import mx.controls.ComboBox;
	import mx.events.FlexMouseEvent;
	import mx.events.ListEvent;
	
	public class MultiSelectComboBox extends ComboBox
	{
		public function MultiSelectComboBox()
		{
			super();
		}
		
		override public function get selectedLabel():String{
			
			if( this.dropdown ) {
			var items:Array=[];
			var item:Object;
			 for each ( item in dropdown.selectedItems ) {
				 items.push(dropdown.itemToLabel(item));
			 }
			
			return items.length ? items.join(", ") : selectedIndices.toString();
			}
			return super.selectedLabel;
		}
		
		override public function open():void {
			dropdown.allowMultipleSelection = true;
			dropdown.addEventListener(MouseEvent.MOUSE_OUT,super.close);
			dropdown.addEventListener(FlexMouseEvent.MOUSE_DOWN_OUTSIDE,super.close);
		}
		
		/*private var ctrlKey:Boolean = false; //Control Key Pressed
		
		
		override protected function keyDownHandler( event:KeyboardEvent ):void {
			super.keyDownHandler( event );
			
			this.ctrlKey = event.ctrlKey;
			
			if( this.ctrlKey == true ) {
				dropdown.allowMultipleSelection = true;
			}
		}
		
		
		override protected function keyUpHandler( event:KeyboardEvent ):void {
			super.keyUpHandler( event );
			this.ctrlKey = event.ctrlKey;
			
			if ( this.ctrlKey == false ) {
				this.close(); 
				var changeEvent:ListEvent = new ListEvent( ListEvent.CHANGE );
				this.dispatchEvent( changeEvent );
				this.selectedIndex = -1;
			}			
		}*/
		
		/**
		 * This function prevents the ComboBox from closing if CtrlKey is pressed on a Close Event
		 */
		override public function close( trigger:Event=null ):void {
			
			/*if( this.ctrlKey == false )	{
				super.close( trigger );
				this.selectedIndex = -1;
			}*/
		}
		
		/**
		 * Setter function for selectedItems
		 */
		public function set selectedItems( values:Array ):void {
			if( this.dropdown ) {
				this.dropdown.selectedItems = values;
			}
		}
		
		
		/**
		 * Getter function for selectedItems
		 */
		[Bindable("change")]
		public function get selectedItems( ) : Array {
			if ( this.dropdown ) {
				return this.dropdown.selectedItems
			} else {
				return null;
			}	
		}
		
		/**
		 * Setter function for selectedIndices
		 */
		public function set selectedIndices( value:Array ) : void {
			if ( this.dropdown ) {
				this.dropdown.selectedIndices = value;
			}
		}
		
		
		/**
		 * Getter function for selectedIndices
		 */
		[Bindable("change")]
		public function get selectedIndices( ) : Array {
			if ( this.dropdown ) {
				return this.dropdown.selectedIndices;
			} else {
				return null;
			}
		}
	}
}