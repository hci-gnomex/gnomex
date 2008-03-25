package views.renderers
{
	import mx.events.ListEvent;
	import mx.controls.Alert;
	
	public class ComboBoxSampleType  extends ComboBoxBase
	{
			protected override function initializeFields():void {
		    	dictionaryClassName          = "hci.gnomex.model.SampleType";
		    	cellAttributeName            = "@idSampleType";
		    	choiceDisplayAttributeName   = "@display";
		    	choiceValueAttributeName     = "@value";
		    }
		    
		     protected override function change(event:ListEvent):void {
		     	if (_data.@canChangeSampleType == "Y" || parentApplication.hasPermission("canWriteAnyObject")) {
	            	_data[cellAttributeName] = this.selectedItem[this.choiceValueAttributeName];
    	        	_data.@isDirty = "Y";		     		
		     	} else {
		     		selectItem();
		     		Alert.show("Sample type cannot be changed.");
		     	}
            }
	}

}