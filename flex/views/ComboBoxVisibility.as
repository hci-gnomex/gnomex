package views
{
	import mx.controls.ComboBox;
	import flash.events.MouseEvent;
	import mx.collections.XMLListCollection;
	import flash.events.Event;
	import mx.events.ListEvent;

	public class ComboBoxVisibility extends ComboBox
	{
		    private var _data:Object;
		  
		    
            override public function set data(o:Object):void
            {
                _data = o;

 

                this.selectedIndex = 0;
                if (o != null && o.@codeVisibility != null) {
	                for(var i:Number = 0; i < this.dataProvider.length; i++) {
	                	if(dataProvider[i].@value == o.@codeVisibility) {
	                          this.selectedIndex = i;
	                          break;
	                     }
	                }
                	
                }
            }
            
            [Bindable]           
            override public function get data():Object 
            {
            	return _data;
            }
            

            override public function get value():Object {
             	if (_data != null) {
            		return _data.@codeVisibility;
            	} else {
            		return null;
            	} 
            	
            }

            override protected function initializationComplete():void
            {   
				dataProvider = new XMLListCollection(parentApplication.manageDictionaries.lastResult.Dictionary.(@className == 'hci.gnomex.model.Visibility').DictionaryEntry);            	
                this.addEventListener(ListEvent.CHANGE, change);
            	labelField = "@display";
            }
            
            
            private function change(event:ListEvent):void {
            	_data.@codeVisibility = this.selectedItem.@value;
            	_data.@isDirty = "Y";
            }
            
	}
}