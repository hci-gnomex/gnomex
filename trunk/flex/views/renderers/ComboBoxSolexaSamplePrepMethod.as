package views.renderers
{
	import hci.flex.renderers.ComboBoxDictionary;
	import hci.flex.renderers.RendererFactory;
	import hci.flex.util.DictionaryManager;
	
	import mx.collections.IList;
	import mx.collections.XMLListCollection;
	import mx.controls.DataGrid;
	import mx.core.IFactory;
	import mx.events.CollectionEvent;
	
	public class ComboBoxSolexaSamplePrepMethod extends hci.flex.renderers.ComboBoxDictionary
	{
			public static function create(dictionaryManager:DictionaryManager):IFactory {
				return RendererFactory.create(ComboBoxSolexaSamplePrepMethod, 
				{ dictionaryManager: dictionaryManager,
				  dataField: '@idSamplePrepMethod',
				  dictionary: 'hci.gnomex.model.SamplePrepMethod' });	 		
				  
			}	 
					
            
            
	        protected override function setDataProvider():void {
	        	var samplePrepMethods:XMLListCollection = new XMLListCollection();

	        	var de:Object;
	        	// Remove non-solexa sample prep methods.
	        	for each(de in dictionaryManager.getEntries(dictionary)) {
	        		
		        	var doesMatchRequestCategory:Boolean = false;
        			var theSamplePrepMethods:XMLList = dictionaryManager.xml.Dictionary.(@className == 'hci.gnomex.model.SamplePrepMethodRequestCategory').DictionaryEntry.(@value != '' && @idSamplePrepMethod == de.@value.toString());
    	   			for each (var xref1:Object in theSamplePrepMethods) {
    	   				if (xref1.@codeRequestCategory.toString() == "SOLEXA") {
	   						doesMatchRequestCategory = true;
    	   		    		break;
    	   				}
    	   			}
    	   			if (doesMatchRequestCategory) {
    	   				samplePrepMethods.addItem(de);
    	   			}				
	        		
	        	}
				dataProvider = samplePrepMethods;
				
				// This will detect changes to underlying data anc cause combobox to be selected based on value.
				IList(DataGrid(owner).dataProvider).addEventListener(CollectionEvent.COLLECTION_CHANGE, underlyingDataChange);
            }
            
	}

}