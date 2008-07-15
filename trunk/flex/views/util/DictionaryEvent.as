package views.util
{
	import flash.events.Event;
	
	public class DictionaryEvent extends flash.events.Event 
	{
		public static var DICTIONARY_WINDOW_INITIALIZED:String          = "dictionaryWindowInitialzed";
		public static var DICTIONARY_WINDOW_DICTIONARY_SELECTED:String  = "dictionaryWindowDictionarySelected";
		public static var DICTIONARY_WINDOW_ENTRY_ADDED:String          = "dictionaryWindowEntryAdded";
		public static var DICTIONARY_LOADED:String                      = "dictionaryLoadCompleted";
		public static var DICTIONARY_ENTRY_ADDED:String                 = "dictionaryEntryAdded";
		
		public function DictionaryEvent(eventType:String)
		{
			super(eventType);
		}

	}
}