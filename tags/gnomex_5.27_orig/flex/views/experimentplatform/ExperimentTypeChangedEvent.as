package views.experimentplatform
{
	import flash.events.Event;
	
	public class ExperimentTypeChangedEvent extends Event
	{
		private static var _DATA_CHANGED:String = "ExperimentTypeChangedEvent"; 
		
		public function ExperimentTypeChangedEvent()
		{
			super(_DATA_CHANGED);
		}
		
		public static function get DATA_CHANGED():String {
			return _DATA_CHANGED;
		}
	}
}