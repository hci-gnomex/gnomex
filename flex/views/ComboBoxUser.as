package views
{
	import mx.controls.ComboBox;
	import flash.events.MouseEvent;
	import mx.collections.XMLListCollection;
	import flash.events.Event;
	import mx.events.ListEvent;

	public class ComboBoxUser extends ComboBox
	{
		    private var _data:Object;
		  
		    
            [Bindable]
            override public function set data(o:Object):void
            {
                _data = o;
                this.selectedIndex = 0;
                for(var i:Number = 0; i < this.dataProvider.length; i++) {
                       if(dataProvider[i].@idAppUser == o.@idAppUser) {
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

            

            override protected function initializationComplete():void
            {   
            	this.addEventListener(ListEvent.CHANGE, change);
            	dataProvider = parentApplication.getAppUserList.lastResult.AppUser;
            	labelField = "@displayName";
            }
            
            private function change(event:ListEvent):void {
            	_data.@idAppUser = this.selectedItem.@idAppUser;
            }
            
	}
}