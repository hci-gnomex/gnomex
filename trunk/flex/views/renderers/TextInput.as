package views.renderers
{
	import mx.controls.TextInput;
	import flash.display.Graphics;
	import mx.controls.Alert;
	import flash.events.Event;

	public class TextInput extends mx.controls.TextInput
	{
		protected var _dataField:String;
		protected var _securityDataField:String;
		
		public function set dataField(dataField:String):void {
			this._dataField = dataField;	
		}
        public function set securityDataField(securityDataField:String):void {
			this._securityDataField = securityDataField;	
		}
        
    	override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void
     	{
          super.updateDisplayList(unscaledWidth,unscaledHeight);
          if (data == null) {
          	return;
          }
          this.setStyle("backgroundColor", data[_dataField] == '' ? parentApplication.REQUIRED_FIELD_BACKGROUND : "0xffffff");
          
          if (_securityDataField != null) {
	          if (data[_securityDataField] == "Y" || parentApplication.hasPermission("canWriteAnyObject")) {
		       	this.editable = true;
	    	  } else {
	       		this.editable = false;
	      	  }
          }

	     }
	}
}