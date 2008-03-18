package views.renderers
{
	public class ComboBoxOrganism extends ComboBoxBase
	{ 
			protected override function initializeFields():void {
		    	dictionaryClassName          = "hci.gnomex.model.Organism";
		    	cellAttributeName            = "@idOrganism";
		    	choiceDisplayAttributeName   = "@display";
		    	choiceValueAttributeName     = "@value";
		    }
	}

}