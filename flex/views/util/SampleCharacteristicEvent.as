package views.util
{
	import flash.events.Event;
	
	public class SampleCharacteristicEvent extends Event
	{
		public static var DATA_REFRESHED:String = "SCRefresh";

		public function SampleCharacteristicEvent(eventType:String)
		{
			super(eventType, true, false);
		}
	
	}
}