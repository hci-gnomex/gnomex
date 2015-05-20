package views.admin
{
	import flash.events.Event;
	
	public class AlignmentProfileEvent extends Event
	{
		public static var DATA_REFRESHED:String = "AlignmentProfileRefresh";

		public function AlignmentProfileEvent(eventType:String)
		{
			super(eventType, true, false);
		}
	
	}
}