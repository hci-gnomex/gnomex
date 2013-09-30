package views.util
{
	import flash.events.Event;
	
	public class SampleSheetColumnEvent extends Event
	{
		public static const CHANGE:String = "change";
		private var _item:Object;
		private var _ssColumn:String;
		
		public function SampleSheetColumnEvent(type:String, item:Object, ssColumn:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
			_item = item;
			_ssColumn = ssColumn;
		}
		
		//getter function for item object
		public function get item():Object
		{
			return _item;
		}
		//getter function for ssColumn
		public function get ssColumn():String
		{
			return _ssColumn;
		}		
	}
}
