package views.renderers
{
	import mx.events.ListEvent;
	import mx.controls.Alert;
	
	public class ComboBoxNumberSequencingCycles  extends ComboBoxBase
	{
			protected override function initializeFields():void {
		    	dictionaryClassName          = "hci.gnomex.model.NumberSequencingCycles";
		    	cellAttributeName            = "@idNumberSequencingCycles";
		    	choiceDisplayAttributeName   = "@display";
		    	choiceValueAttributeName     = "@value";
		    }
		    
		     protected override function change(event:ListEvent):void {
		     	if ((_data.@canChangeNumberSequencingCycles != null && _data.@canChangeNumberSequencingCycles == "Y")
		     	     || parentApplication.hasPermission("canWriteAnyObject")) {
	            	_data[cellAttributeName] = this.selectedItem[this.choiceValueAttributeName];
    	        	_data.@isDirty = "Y";		     		
		     	} else {
		     		selectItem();
		     		Alert.show("Number sequencing cycles cannot be changed.");
		     	}
            }
	}

}