package views.util
{
	import flash.events.Event;
	
	public class DirtyEvent extends Event
	{
		public static var NOW_DIRTY:String = "NowDirty";
		public static var NOW_CLEAN:String = "NowClean";
		
		public function DirtyEvent(eventType:String) {
			super(eventType, true, false);
		}
	}
}