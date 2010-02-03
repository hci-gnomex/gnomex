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
            	dataProvider = parentDocument.samplePrepMethods;         	
				super.initializationComplete();				
            }						 		            
	}

}