package views.util
{
	import flash.events.Event;
	
	public class DateSetEvent extends Event 
	{
		public static var DATE_SET:String = "DateSet";
		
		public function DateSetEvent(eventType:String) {
			super(eventType, true, false);
		}
	}
}