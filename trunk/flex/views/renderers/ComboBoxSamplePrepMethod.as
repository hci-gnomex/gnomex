package views.renderers
{
	import mx.events.ListEvent;
	import mx.controls.Alert;
	
	public class ComboBoxSamplePrepMethod extends ComboBoxBase
	{
			protected override function initializeFields():void {
		    	dictionaryClassName          = "hci.gnomex.model.SamplePrepMethod";
		    	cellAttributeName            = "@idSamplePrepMethod";
		    	choiceDisplayAttributeName   = "@display";
		    	choiceValueAttributeName     = "@value";
		    }
		    protected override function change(event:ListEvent):void {
		     	if (_data.@canChangeSamplePrepMethod == "Y" || parentApplication.hasPermission("canWriteAnyObject")) {
	            	_data[cellAttributeName] = this.selectedItem[this.choiceValueAttributeName];
    	        	_data.@isDirty = "Y";		     		
		     	} else {
		     		selectItem();
		     		Alert.show("Sample prep method cannot be changed.");
		     	}
            }
		    
	}
	

}