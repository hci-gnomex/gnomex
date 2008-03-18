package views.renderers
{
	public class ComboBoxChipType extends ComboBoxBase
	{
			protected override function initializeFields():void {
		    	dictionaryClassName          = "hci.gnomex.model.BioanalyzerChipType";
		    	cellAttributeName            = "@codeBioanalyzerChipType";
		    	choiceDisplayAttributeName   = "@display";
		    	choiceValueAttributeName     = "@value";
		    }
	}

}