package views.util
{
	import flash.events.Event;
	
	public class AnnotationOptionAddEvent extends Event
	{
		public var nameAdded:String;
		public static var ANNOTATION_OPTION_ADD_EVENT:String = "AnnotationOptionAddEvent";
		
		public function AnnotationOptionAddEvent(nameAdded:String)
		{
			this.nameAdded = nameAdded;
			super(ANNOTATION_OPTION_ADD_EVENT, true, false);
		}
		
	}
}
