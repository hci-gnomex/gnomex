package views.renderers
{
	import mx.events.ListEvent;
	import mx.controls.Alert;
	
	public class ComboBoxSolexaLibraryType  extends ComboBoxBase
	{
			protected override function initializeFields():void {
		    	dictionaryClassName          = "hci.gnomex.model.SolexaLibraryType";
		    	cellAttributeName            = "@idSolexaLibraryType";
		    	choiceDisplayAttributeName   = "@display";
		    	choiceValueAttributeName     = "@value";
		    }
		    
		     protected override function change(event:ListEvent):void {
		     	if (_data.@canChangeSampleType == "Y" || parentApplication.hasPermission("canWriteAnyObject")) {
	            	_data[cellAttributeName] = this.selectedItem[this.choiceValueAttributeName];
    	        	_data.@isDirty = "Y";		
    	        	parentDocument.propagateSolexaLibraryType(_data[cellAttributeName]);     		
		     	} else {
		     		selectItem();
		     		Alert.show("Library type cannot be changed.");
		     	}
            }
	}

}