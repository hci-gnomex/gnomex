package views
{
	import mx.controls.ComboBox;
	import flash.events.MouseEvent;
	import mx.collections.XMLListCollection;
	import flash.events.Event;
	import mx.events.ListEvent;

	public class ComboBoxConcentrationUnit extends ComboBox
	{
		    private var _data:Object;
		  
		    
            override public function set data(o:Object):void
            {
                _data = o;


				dataProvider = new XMLListCollection(parentApplication.manageDictionaries.lastResult.Dictionary.(@className == 'hci.gnomex.model.ConcentrationUnit').DictionaryEntry);
 

                this.selectedIndex = 0;
                for(var i:Number = 0; i < this.dataProvider.length; i++) {
                	if(dataProvider[i].@value == o.@codeConcentrationUnit) {
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
            		return _data.@codeConcentrationUnit;
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
            	_data.@codeConcentrationUnit = this.selectedItem.@value;
            	_data.@isDirty = "Y";
            }
            
	}
}