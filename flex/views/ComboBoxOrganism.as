package views
{
	import mx.controls.ComboBox;
	import flash.events.MouseEvent;
	import mx.collections.XMLListCollection;
	import flash.events.Event;
	import mx.events.ListEvent;

	public class ComboBoxOrganism extends ComboBox
	{
		    private var _data:Object;
		  
		    
            override public function set data(o:Object):void
            {
                _data = o;


				dataProvider = new XMLListCollection(parentApplication.manageDictionaries.lastResult.Dictionary.(@className == 'hci.gnomex.model.Organism').DictionaryEntry);
 

                this.selectedIndex = 0;
                for(var i:Number = 0; i < this.dataProvider.length; i++) {
                	if(dataProvider[i].@value == o.@idOrganism) {
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
            		return _data.@idOrganism;
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
            	_data.@idOrganism = this.selectedItem.@value;
            	_data.@isDirty = "Y";
            }
            
		    override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void
		    {
		          super.updateDisplayList(unscaledWidth,unscaledHeight);
		          if (_data == null) {
		          	return;
		          }
		          
			      var colors:Array = new Array();
		          if(_data.@idOrganism == '') {
			          colors.push(parentApplication.REQUIRED_FIELD_BACKGROUND);		          
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