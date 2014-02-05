package views.util
{
	import flash.events.Event;
	
	public class OrganismAddEvent extends Event
	{
		public var nameAdded:String;
		public static var ORGANISM_ADD_EVENT:String = "OrganismAddEvent";
		
		public function OrganismAddEvent(nameAdded:String)
		{
			this.nameAdded = nameAdded;
			super(ORGANISM_ADD_EVENT, true, false);
		}
		
	}
}