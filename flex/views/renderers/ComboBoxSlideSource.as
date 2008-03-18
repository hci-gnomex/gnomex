package views.renderers
{
	public class ComboBoxSlideSource extends ComboBoxBase
	{
			protected override function initializeFields():void {
		    	dictionaryClassName          = "hci.gnomex.model.SlideSource";
		    	cellAttributeName            = "@codeSlideSource";
		    	choiceDisplayAttributeName   = "@display";
		    	choiceValueAttributeName     = "@value";
		    }
	}

}