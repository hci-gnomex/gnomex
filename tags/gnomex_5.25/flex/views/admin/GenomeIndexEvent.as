package views.admin
{
	import flash.events.Event;
	
	public class GenomeIndexEvent extends Event
	{
		public static var DATA_REFRESHED:String = "GnIdxRefresh";

		public function GenomeIndexEvent(eventType:String)
		{
			super(eventType, true, false);
		}
	
	}
}