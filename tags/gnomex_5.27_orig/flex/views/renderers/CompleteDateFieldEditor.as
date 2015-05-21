package views.renderers
{
	import mx.controls.DateField;
	import flash.events.Event;
	import mx.collections.IList;
	import mx.events.CalendarLayoutChangeEvent;

	public class CompleteDateFieldEditor extends DateField
	{
		    protected var _data:Object;
		    protected var cellAttributeName:String = "@completeDateOther";
		    
		    protected  function initializeFields():void {
				this.yearNavigationEnabled = true;
				this.editable = true;
		    }
		    		
		    override public function set data(o:Object):void
            {
                _data = o; 
            }
            
            [Bindable]           
            override public function get data():Object 
            {
            	return _data;
            }
            
			[Bindable]
            override public function get selectedDate():Date {
             	if (_data != null) {
            		var theDate:Date = new Date(_data[cellAttributeName].toString());
            		return theDate;
            	} else {
            		return null;
            	} 
            	
            }
            
            override public function set selectedDate(value:Date):void {}
            
            protected function change(event:CalendarLayoutChangeEvent):void {
				if (event.newDate == null)
				{
					_data[cellAttributeName] = "";
				}
				else
				{
					_data[cellAttributeName] = this.text;
				}
            }
                  
            override protected function initializationComplete():void
            {   initializeFields();
                this.addEventListener( mx.events.CalendarLayoutChangeEvent.CHANGE, change);
            }
	}
}