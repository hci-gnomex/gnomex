package views
{
	import mx.controls.ComboBox;
	import flash.events.MouseEvent;
	import mx.collections.XMLListCollection;
	import flash.events.Event;
	import mx.events.ListEvent;

	public class ComboBoxSamplePrepMethod extends ComboBox
	{
		    private var _data:Object;
		  
		    
            override public function set data(o:Object):void
            {
                _data = o;


				dataProvider = new XMLListCollection(parentApplication.manageDictionaries.lastResult.Dictionary.(@className == 'hci.gnomex.model.SamplePrepMethod').DictionaryEntry);
 

                this.selectedIndex = 0;
                for(var i:Number = 0; i < this.dataProvider.length; i++) {
                	if(dataProvider[i].@value == o.@idSamplePrepMethod) {
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
            		return _data.@idSamplePrepMethod;
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
            	_data.@idSamplePrepMethod = this.selectedItem.@value;
            	_data.@isDirty = "Y";
            }
            
	}
}