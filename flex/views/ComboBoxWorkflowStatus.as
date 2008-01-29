package views
{
	import mx.controls.ComboBox;
	import flash.events.MouseEvent;
	import mx.collections.XMLListCollection;
	import flash.events.Event;
	import mx.events.ListEvent;

	public class ComboBoxWorkflowStatus extends ComboBox
	{
		    [Bindable]
            private var statusDictionary: Array = [ {label:"", data:""}, {label:"Completed", data:"Completed"}, 
                {label:"Terminated", data:"Terminated"}, {label:"Bypassed", data:"Bypassed"} ];

		    private var _data:Object;
		  
		    
            override public function set data(o:Object):void
            {
                _data = o;
                this.selectedIndex = 0;
                for(var i:Number = 0; i < this.dataProvider.length; i++) {
                	if (dataProvider[i].data == o.@labelingStatus) {
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
            	dataProvider = statusDictionary;
            }
            
            private function change(event:ListEvent):void {
            	_data.data = this.selectedItem.data;
            }
            
	}
}