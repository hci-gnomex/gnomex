package views.util
{
	import flash.events.Event;

	public class ContextHelpEditEnableDisableEvent extends Event
	{
		public static var DATA_REFRESHED:String = "ContextHelpEditEnableDisableRefresh";
		
		public function ContextHelpEditEnableDisableEvent(eventType:String) {
			super(eventType, true, false);
		}
	}
}