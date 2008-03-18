package views.renderers
{
	public class ComboBoxConcentrationUnit  extends ComboBoxBase
	{
			protected override function initializeFields():void {
		    	dictionaryClassName          = "hci.gnomex.model.ConcentrationUnit";
		    	cellAttributeName            = "@codeConcentrationUnit";
		    	choiceDisplayAttributeName   = "@display";
		    	choiceValueAttributeName     = "@value";
		    }
	}

}