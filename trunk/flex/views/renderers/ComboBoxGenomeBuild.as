package views.renderers
{
	import mx.events.ListEvent;
	import mx.controls.Alert;
	
	public class ComboBoxGenomeBuild  extends ComboBoxBase
	{
			protected override function initializeFields():void {
		    	dictionaryClassName          = "hci.gnomex.model.GenomeBuild";
		    	cellAttributeName            = "@idGenomeBuildAlignTo";
		    	choiceDisplayAttributeName   = "@display";
		    	choiceValueAttributeName     = "@value";
		    }
		    
		     protected override function change(event:ListEvent):void {
		     	if ((_data.@canChangeGenomeBuildAlignTo != null && _data.@canChangeGenomeBuildAlignTo == "Y")
		     	     || parentApplication.hasPermission("canWriteAnyObject")) {
	            	_data[cellAttributeName] = this.selectedItem[this.choiceValueAttributeName];
    	        	_data.@isDirty = "Y";		     		
    	        	parentDocument.propagateGenomeBuild(_data[cellAttributeName]);	     		
		     	} else {
		     		selectItem();
		     		Alert.show("Genome build cannot be changed.");
		     	}
            }
	}

}