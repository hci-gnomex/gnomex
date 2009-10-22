package views.renderers
{
	import hci.flex.controls.ComboBox;
	import hci.flex.renderers.RendererFactory;
	import hci.flex.util.DictionaryManager;
	
	import mx.collections.IList;
	import mx.collections.XMLListCollection;
	import mx.controls.DataGrid;
	import mx.core.IFactory;
	import mx.events.CollectionEvent;
	
	public class ComboBoxSolexaSamplePrepMethod extends hci.flex.controls.ComboBox
	{
			public static function create():IFactory {
				return RendererFactory.create(ComboBoxSolexaSamplePrepMethod, 
				{ dataField: '@idSamplePrepMethod',  
				updateData: true
				});	 						  
			}	 
			
            override protected function initializationComplete():void
            {   
            	setDataProvider();         	
				super.initializationComplete();				
            }						 		            
            
	        protected function setDataProvider():void {
	        	var samplePrepMethods:XMLListCollection = new XMLListCollection();

	        	var de:Object;
	        	// Remove non-solexa sample prep methods.
	        	for each(de in parentApplication.dictionaryManager.getEntries('hci.gnomex.model.SamplePrepMethod')) {
	        		
		        	var doesMatchRequestCategory:Boolean = false;
        			var theSamplePrepMethods:XMLList = parentApplication.dictionaryManager.xml.Dictionary.(@className == 'hci.gnomex.model.SamplePrepMethodRequestCategory').DictionaryEntry.(@value != '' && @idSamplePrepMethod == de.@value.toString());
    	   			for each (var xref1:Object in theSamplePrepMethods) {
    	   				if (xref1.@codeRequestCategory.toString() == "SOLEXA") {
	   						doesMatchRequestCategory = true;
    	   		    		break;
    	   				}
    	   			}
    	   			if (doesMatchRequestCategory || de.@value == '') {
    	   				samplePrepMethods.addItem(de);
    	   			}				
	        		
	        	}
				dataProvider = samplePrepMethods;		
            }
            
	}

}