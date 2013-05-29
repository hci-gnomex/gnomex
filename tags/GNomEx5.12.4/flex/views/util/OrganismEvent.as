package views.util
{
	import flash.events.Event;
	
	public class OrganismEvent extends Event
	{
		public static var DATA_REFRESHED:String = "ORefresh";

		public function OrganismEvent(eventType:String)
		{
			super(eventType, true, false);
		}
	
	}
}