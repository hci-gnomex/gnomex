package views.util
{
	import flash.events.Event;
	
	public class AnalysisProtocolsEvent extends Event
	{
		public static var DATA_REFRESHED:String = "AnalysisProtocolRefresh";

		public function AnalysisProtocolsEvent(eventType:String)
		{
			super(eventType, true, false);
		}
	
	}
}