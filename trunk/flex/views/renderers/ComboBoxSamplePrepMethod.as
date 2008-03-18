package views.renderers
{
	public class ComboBoxSamplePrepMethod extends ComboBoxBase
	{
			protected override function initializeFields():void {
		    	dictionaryClassName          = "hci.gnomex.model.SamplePrepMethod";
		    	cellAttributeName            = "@idSamplePrepMethod";
		    	choiceDisplayAttributeName   = "@display";
		    	choiceValueAttributeName     = "@value";
		    }
	}

}