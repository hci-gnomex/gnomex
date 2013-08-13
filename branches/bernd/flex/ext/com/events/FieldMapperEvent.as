package ext.com.events
{
	import flash.events.Event;
	
	public class FieldMapperEvent extends Event
	{
		public var data:Array;
		
		public static const FIELDS_MAPPED:String = "fieldsMapped";
		
		public function FieldMapperEvent( type:String, bubbles:Boolean=false, cancelable:Boolean=false )
		{
			super( type, bubbles, cancelable );
		}
		
		override public function clone():Event
		{
			var event:FieldMapperEvent = new FieldMapperEvent( type, bubbles, cancelable );
			event.data = data;
			
			return event;
		}
	}
}