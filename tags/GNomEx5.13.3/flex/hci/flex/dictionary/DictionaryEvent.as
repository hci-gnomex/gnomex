package hci.flex.dictionary
{
	import flash.events.Event;
	
	public class DictionaryEvent extends flash.events.Event 
	{
		public static var DICTIONARY_WINDOW_INITIALIZED:String          = "dictionaryWindowInitialized";
		public static var DICTIONARY_WINDOW_DICTIONARY_SELECTED:String  = "dictionaryWindowDictionarySelected";
		public static var DICTIONARY_LOADED:String                      = "dictionaryLoaded";
		public static var DICTIONARY_RELOADED:String                    = "dictionaryReloaded";
		public static var DICTIONARY_ENTRY_ADDED:String                 = "dictionaryEntryAdded";
		public static var DICTIONARY_ENTRY_SAVED:String                 = "dictionaryEntrySaved";
		public static var DICTIONARY_ENTRY_DELETED:String               = "dictionaryEntryDeleted";
		public static var DICTIONARY_METADATA_LOADED:String             = "dictionaryMetaDataLoaded";
		
		public function DictionaryEvent(eventType:String)
		{
			super(eventType);
		}

	}
}