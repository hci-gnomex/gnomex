package views.renderers
{
	public class ComboBoxSampleSource extends ComboBoxBase
	{
			protected override function initializeFields():void {
		    	dictionaryClassName          = "hci.gnomex.model.SampleSource";
		    	cellAttributeName            = "@idSampleSource";
		    	choiceDisplayAttributeName   = "@display";
		    	choiceValueAttributeName     = "@value";
		    }
	}

}