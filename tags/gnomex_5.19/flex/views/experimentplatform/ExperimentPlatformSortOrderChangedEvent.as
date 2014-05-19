package views.experimentplatform
{
	import flash.events.Event;
	
	public class ExperimentPlatformSortOrderChangedEvent extends Event
	{
		private static var _DATA_CHANGED:String = "ExperimentPlatformSortOrderChangedEvent"; 
		
		public function ExperimentPlatformSortOrderChangedEvent()
		{
			super(_DATA_CHANGED);
		}
		
		public static function get DATA_CHANGED():String {
			return _DATA_CHANGED;
		}
	}
}