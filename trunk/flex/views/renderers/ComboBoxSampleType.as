package views.renderers
{
	public class ComboBoxSampleType  extends ComboBoxBase
	{
			protected override function initializeFields():void {
		    	dictionaryClassName          = "hci.gnomex.model.SampleType";
		    	cellAttributeName            = "@idSampleType";
		    	choiceDisplayAttributeName   = "@display";
		    	choiceValueAttributeName     = "@value";
		    }
	}

}