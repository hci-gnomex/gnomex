package views.renderers
{
	import mx.events.ListEvent;
	import mx.controls.Alert;
	
	public class ComboBoxSlideSource extends ComboBoxBase
	{
			protected override function initializeFields():void {
		    	dictionaryClassName          = "hci.gnomex.model.SlideSource";
		    	cellAttributeName            = "@codeSlideSource";
		    	choiceDisplayAttributeName   = "@display";
		    	choiceValueAttributeName     = "@value";
		    }            
		    
		    protected override function change(event:ListEvent):void {
		     	if (_data.@canChangeSlideSource == "Y" || parentApplication.hasPermission("canWriteAnyObject")) {
	            	_data[cellAttributeName] = this.selectedItem[this.choiceValueAttributeName];
	            	_data.@isDirty = "Y";
	            	parentDocument.propagateSlideSourceToHybsSameSlide(_data);
	            	parentDocument.checkHybsCompleteness();
		     	} else {
		     		selectItem();
		     		Alert.show("Slide source cannot be changed.");
		     	}
            }
            
	}

}