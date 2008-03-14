package views
{
	import mx.controls.ComboBox;
	import flash.events.MouseEvent;
	import mx.collections.XMLListCollection;
	import flash.events.Event;
	import mx.events.ListEvent;

	public class ComboBoxSlideDesign extends ComboBox
	{
		    private var _data:Object;
		  
		    
            override public function set data(o:Object):void
            {
                _data = o;
                
                dataProvider = parentDocument.slideDesigns;
				selectItem(); 
            }
            
            private function selectItem():void {
				this.selectedIndex = -1;
            	if (_data != null && dataProvider != null) {
	                for(var i:Number = 0; i < this.dataProvider.length; i++) {
	                	if(dataProvider[i].@idSlideDesign == _data.@idSlideDesign) {
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
            		return _data.@idSlideDesign;
            	} else {
            		return null;
            	} 
            	
            }
            


            override protected function initializationComplete():void
            {   
                this.addEventListener(ListEvent.CHANGE, change);
            	labelField = "@name";
            }
            
            private function onMyEvent(event:mx.events.FlexEvent):void {
            	selectItem();
            }
            
            
            private function change(event:ListEvent):void {
            	_data.@idSlideDesign = this.selectedItem.@idSlideDesign;
            	_data.@isDirty = "Y";
            	parentDocument.checkHybsCompleteness();
            }
            
	}
}