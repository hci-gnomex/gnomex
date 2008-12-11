package views.renderers
{
	import hci.flex.util.DictionaryManager;
	import mx.events.ListEvent;
	import hci.flex.renderers.ComboBoxDictionary;
	import mx.core.IFactory;
	import hci.flex.renderers.RendererFactory;
	import mx.controls.Alert;
	
	public class ComboBoxVisibility extends hci.flex.renderers.ComboBoxDictionary
	{
			public static function create(dictionaryManager:DictionaryManager, 
			                              dataField:String='@codeVisibility',
			                              dictionary:String='hci.gnomex.model.Visibility'):IFactory {
				return RendererFactory.create(ComboBoxVisibility, 
				{ dictionaryManager: dictionaryManager,
				  dataField: dataField,
				  dictionary:dictionary });	 		
				  
			}	 
					
            protected override function change(event:ListEvent):void {
		        parentDocument.parentDocument.clearFilter();
		        
		     	if (_data.@canUpdateVisibility == "Y" || parentApplication.hasPermission("canWriteAnyObject")) {
	            	super.change(event);
		     	} else {
		     		selectItem();
		     		Alert.show("Visibility can only be changed by experiment owner, lab manager, or GNomEx admins.");
		     	}
	        }
	        
			
	        
	}

}