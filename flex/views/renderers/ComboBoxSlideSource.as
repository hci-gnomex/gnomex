package views.renderers
{
	import mx.events.ListEvent;
	
	public class ComboBoxSlideSource extends ComboBoxBase
	{
			protected override function initializeFields():void {
		    	dictionaryClassName          = "hci.gnomex.model.SlideSource";
		    	cellAttributeName            = "@codeSlideSource";
		    	choiceDisplayAttributeName   = "@display";
		    	choiceValueAttributeName     = "@value";
		    }            
		    
		    protected override function change(event:ListEvent):void {
            	_data[cellAttributeName] = this.selectedItem[this.choiceValueAttributeName];
            	_data.@isDirty = "Y";
            	parentDocument.propagateSlideSourceToHybsSameSlide(_data);
            	parentDocument.checkHybsCompleteness();
            }
	}

}