package views.admin
{
	import flash.events.Event;
	
	public class PropertyEvent extends Event
	{
		public static var DATA_REFRESHED:String = "SCRefresh";

		public function PropertyEvent(eventType:String)
		{
			super(eventType, true, false);
		}
	
	}
}