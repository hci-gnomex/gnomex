package views.util
{
	import flash.events.Event;
	
	public class CoreFacilityEvent extends Event
	{
		public static var DATA_REFRESHED:String = "CoreFacilityRefresh";

		public function CoreFacilityEvent(eventType:String)
		{
			super(eventType, true, false);
		}
	
	}
}