package hci.flex.util
{
	
	import flash.events.Event;
	import hci.flex.dictionary.DictionaryEvent;
	import mx.collections.XMLListCollection;
	import mx.controls.Alert;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.http.mxml.HTTPService;

	 	
	public class DictionaryManager
	{
	
		protected var service:HTTPService;
		protected var manageDictionaryCommand:String; 
		protected var xmlMetaData:Object = null;
				
		[Bindable]
		public var xml:Object = null;	

		public function DictionaryManager(manageDictionaryCommand:String)
		{
			this.manageDictionaryCommand = manageDictionaryCommand;
		}
		
		public function load(className:String=""):void {
			call("load", className);
		}
		
		public function reload(className:String=""):void {
			call("reload", className);
		}
		
		public function call(action:String, className:String=""):void {
			var parameters:Object = new Object();
			parameters.action = action;
			parameters.className = className;
			
            callWithParameters(parameters);
        }

		public function callWithParameters(parameters:Object):void {
            
            service = new  mx.rpc.http.mxml.HTTPService(); 
            service.url = manageDictionaryCommand;
			service.useProxy = false;
			service.resultFormat = "e4x";
            service.method = "POST"; 
            service.addEventListener("result", httpResult);
            service.addEventListener("fault", httpFault);
            service.showBusyCursor = true;


            var call:Object = service.send(parameters); 
            call.action = parameters.action;
            call.className = parameters.className; 
        }
        
        public function httpResult(event:ResultEvent):void {
           var call:Object = event.token;
		   if (event.message.body.toString().indexOf("<ERROR message=") != -1) {
        		var errXml:XML = new XML(event.message.body.toString());
				if (errXml.ERROR.hasOwnProperty("@message")) {
					Alert.show(errXml.ERROR.@message.toString(),"Error");
				} else {
					Alert.show("An error occurred when loading the dictionaries.");
				}
				return;
        	} else {
        		if (call.action == "load") {       
        			if (call.className == null || call.className == "") {
	        			xml = service.lastResult;
        			} 			
        		} else if (call.action == "metadata") {
        			xmlMetaData = service.lastResult;
        		} 
        	} 

			if (call.action == "reload") {
				var reloadEvent:Event = new Event(DictionaryEvent.DICTIONARY_RELOADED);
		        dispatchEvent(reloadEvent);
		        
		        // Load all of the dictionaries after a reload
		        this.load();
			}
			if (call.action == "delete") {
				var deleteEvent:Event = new Event(DictionaryEvent.DICTIONARY_ENTRY_DELETED);
		        dispatchEvent(deleteEvent);
		        
		        // Load all of the dictionaries after a delete
		        this.load();
			} 
        	
			if (call.action == "load") {
				var loadEvent:Event = new Event(DictionaryEvent.DICTIONARY_LOADED);
		        dispatchEvent(loadEvent);
			}
			
			if (call.action == "add") {
				var addedEvent:Event = new Event(DictionaryEvent.DICTIONARY_ENTRY_ADDED);
		        dispatchEvent(addedEvent);
		        
		        // Load all of the dictionaries after an add
		        this.load();
			} 
			
			if (call.action == "save") {
				var savedEvent:Event = new Event(DictionaryEvent.DICTIONARY_ENTRY_SAVED);
		        dispatchEvent(savedEvent);
			}        	

			if (call.action == "metadata") {
				var metadataEvent:Event = new Event(DictionaryEvent.DICTIONARY_METADATA_LOADED);
		        dispatchEvent(metadataEvent);
			}        	
        } 

        public function httpFault(event:FaultEvent):void {
	    	if (event.message.body is String) {
	        	var startPos:int = event.message.body.indexOf("TEXT=");
	        	var endPos:int   = event.message.body.indexOf("TYPE=");
	        	if (startPos != -1 && endPos != -1) {
		        	Alert.show(event.message.body.toString().substring(startPos + 5, endPos), "Manage Dictionaries error");    		
	        	} else {
	        		Alert.show(event.message.body.toString(), "Manage Dictionaries error");
	        	}        
	    		
	    	} else {
	    		Alert.show(event.fault.faultCode + "\n\n" + event.fault.faultString + "\n\n" + event.fault.faultDetail, "Manage Dictionaries error");
	    		
	    	}
        }
        
        public function getEditableDictionaries():XMLListCollection {
			return new XMLListCollection(xml.Dictionary.(@canWrite=="Y"));        	
        }
        public function getEntries(dictionaryClassName:String):XMLList {
        	return xml.Dictionary.(@className == dictionaryClassName).DictionaryEntry;
        }
        
        public function getEntriesExcludeBlank(dictionaryClassName:String):XMLList {
        	return xml.Dictionary.(@className == dictionaryClassName).DictionaryEntry.(@value != '');        	
        }
        
        public function getFilter(dictionaryClassName:String, dataField:String):XMLList {
       		return xml.Dictionary.(@className == dictionaryClassName).Filters.filter.(@filterField==dataField.replace("@",""));
        }
        
        public function getEntry(dictionaryClassName:String, value:String):Object {
			return xml.Dictionary.(@className == dictionaryClassName).DictionaryEntry.(@value == value);        	
        }
        
        public function getEntryDisplay(dictionaryClassName:String, dataFieldValue:String):String {
       		return xml.Dictionary.(@className == dictionaryClassName).DictionaryEntry.(@value == dataFieldValue).@display.toString();
        }
        
        public function getDictionaryMetaDataFields():XMLList {
        	if (xmlMetaData != null) {
	        	return xmlMetaData.Dictionary.Field;       		
        	} else {
        		return null;
        	}
        }

	}
}