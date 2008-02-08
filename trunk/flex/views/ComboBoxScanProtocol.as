package views
{
	import mx.controls.ComboBox;
	import flash.events.MouseEvent;
	import mx.collections.XMLListCollection;
	import flash.events.Event;
	import mx.events.ListEvent;

	public class ComboBoxScanProtocol extends ComboBox
	{
		    private var _data:Object;
		  
		    
            override public function set data(o:Object):void
            {
                _data = o;


				dataProvider = new XMLListCollection(parentApplication.manageDictionaries.lastResult.Dictionary.(@className == 'hci.gnomex.model.ScanProtocol').DictionaryEntry.(@value == "" || @codeRequestCategory == _data.@codeRequestCategory1));
 

                this.selectedIndex = 0;
                for(var i:Number = 0; i < this.dataProvider.length; i++) {
                	if(dataProvider[i].@value == o.attribute('idScanProtocol')) {
                          this.selectedIndex = i;
                          break;
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
            		return _data.@idScanProtocol;
            	} else {
            		return null;
            	} 
            	
            }
            


            override protected function initializationComplete():void
            {   
                this.addEventListener(ListEvent.CHANGE, change);
            	labelField = "@display";
            }
            
            
            private function change(event:ListEvent):void {
            	_data.@idScanProtocol = this.selectedItem.@value;
            	_data.@isDirty = "Y";
            }
            
	}
}