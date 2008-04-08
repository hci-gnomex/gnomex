package views.renderers
{
	import mx.events.ListEvent;
	import mx.controls.Alert;
	
	public class ComboBoxFlowCellType  extends ComboBoxBase
	{
			protected override function initializeFields():void {
		    	dictionaryClassName          = "hci.gnomex.model.FlowCellType";
		    	cellAttributeName            = "@idFlowCellType";
		    	choiceDisplayAttributeName   = "@display";
		    	choiceValueAttributeName     = "@value";
		    }
		    
		     protected override function change(event:ListEvent):void {
		     	if ((_data.@canChangeFlowCellType != null && _data.@canChangeFlowCellType == "Y")
		     	     || parentApplication.hasPermission("canWriteAnyObject")) {
	            	_data[cellAttributeName] = this.selectedItem[this.choiceValueAttributeName];
    	        	_data.@isDirty = "Y";	
    	        	parentDocument.propagateFlowCellType(_data[cellAttributeName]);	     		
		     	} else {
		     		selectItem();
		     		Alert.show("Flow cell type cannot be changed.");
		     	}
            }
	}

}