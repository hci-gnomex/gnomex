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


				var slideDesigns:XMLListCollection = new XMLListCollection();
				for each(var sp:Object in parentApplication.slideProductList) {
					if (sp.@idSlideProduct == parentDocument.slideProductCombo.selectedItem.@idSlideProduct) {
						for each(var sd:Object in sp.slideDesigns.SlideDesign) {
							slideDesigns.addItem(sd);
						}
					}
				}
				dataProvider = slideDesigns;
 

                this.selectedIndex = 0;
                for(var i:Number = 0; i < this.dataProvider.length; i++) {
                	if(dataProvider[i].@idSlideDesign == o.@idSlideDesign) {
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
            
            
            private function change(event:ListEvent):void {
            	_data.@idSlideDesign = this.selectedItem.@idSlideDesign;
            	_data.@isDirty = "Y";
            }
            
	}
}