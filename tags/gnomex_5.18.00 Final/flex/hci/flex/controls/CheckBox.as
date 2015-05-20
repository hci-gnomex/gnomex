package hci.flex.controls
{
	import hci.flex.renderers.RendererFactory;	
	import mx.controls.CheckBox;
	import mx.core.IFactory;
	import flash.events.Event;
	import flash.display.DisplayObject;
	import flash.text.TextField;
	
	/**
	 * Standalone Usage:
	 * 		set checkedValue to appropriate string
	 * 		set uncheckedValue to appropriate string
	 * 		set value for checkbox (use databinding)
	 * 
	 * Grid Usage:
	 * 		set GRID's data to XML (use databinding)
	 * 		column.itemRenderer = CheckBox.getFactory(checkedValue, uncheckeValue, dataValueField)
	 * 		checkedValue is string representing checked,  unchecked value is string representing not checked
	 * 		dataValueField is attribute in GRID's XML containing the value
	 */	

	public class CheckBox extends mx.controls.CheckBox
	{
		public var isRequired:Boolean = false;
		public var updateData:Boolean = false;
		public var missingRequiredFieldBackground:uint = RendererFactory.DEFAULT_MISSING_REQUIRED_FIELD_BACKGROUND;
		public var checkedValue:String;
		public var uncheckedValue:String;
		
		//For use in grids
		public var dataField:String;
		
		public function CheckBox()
		{
			super();
		}
		
		public function set value(val:String):void {
			if (val != null && val == checkedValue) {
				this.selected = true;
			}
			else {
				this.selected = false;
			}
		}
		
		public function get value():String {
			if (selected) {
				return checkedValue;
			}
			else {
				return uncheckedValue;
			}
		}
		
		override public function set data(o:Object):void {
			super.data = o;
        	this.value = o[dataField];
        }	
        
        override protected function initializationComplete():void { 
        	this.addEventListener(Event.CHANGE, change);
        }
        
        protected function change(event:Event):void {
			assignData();
        }    
        
        private function assignData():void {
        	if (this.updateData) {
		      	if (this.selected) {
		      		super.data[dataField] = checkedValue;
		      	} else {
		      		super.data[dataField] = uncheckedValue;
		      	}        		
        	}
        }        	
        
		public static function getFactory(
			checkedValue:String, 
			uncheckedValue:String, 
			dataField:String, 
			updateData:Boolean = false,
			isRequired:Boolean = false,		
			missingRequiredFieldBackground:uint = 0xFFFFB9):IFactory {	
						
			return RendererFactory.create(hci.flex.controls.CheckBox, {
													checkedValue: checkedValue,  
													uncheckedValue: uncheckedValue,
													dataField: dataField,
													updateData: updateData,
													isRequired: isRequired,
													missingRequiredFieldBackground: missingRequiredFieldBackground});	
		}
		

		override protected function updateDisplayList(w:Number, h:Number):void
		{
			super.updateDisplayList(w, h);
	
			  if (super.data == null) {
	          	return;
	          }
	          
          	  var n:int = numChildren;
			  for (var i:int = 0; i < n; i++)
			  {
				var c:DisplayObject = getChildAt(i);
				if (!(c is TextField))
				{
				  c.x = (w - c.width) / 2;
				  c.y = 0;
				}
			  }
	          
	          if (isRequired) {
			      var colors:Array = new Array();
		          if(! this.selected) {
			          colors.push(missingRequiredFieldBackground);		          
			          colors.push("0xCCCCCC");		          
			          this.setStyle("fillColors", colors);
		          	
		          } else {
			          colors.push("0xFFFFFF");
			          colors.push("0xCCCCCC");		          
			          this.setStyle("fillColors", colors);
		          }
	          	
	          }
 
		} 					        		
	}
}