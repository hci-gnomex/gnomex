package views.util
{
	import flash.events.Event;
	
	public class IScanChipEvent extends Event
	{
		public static var DATA_REFRESHED:String = "CRefresh";

		public function IScanChipEvent(eventType:String)
		{
			super(eventType, true, false);
		}
	
	}
}