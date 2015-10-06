package views.util
{
	import flash.events.Event;
	
	public class ProductEvent extends Event
	{
		public static var DATA_REFRESHED:String = "CRefresh";

		public function ProductEvent(eventType:String)
		{
			super(eventType, true, false);
		}
	
	}
}