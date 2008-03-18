package views.renderers
{
	import mx.collections.XMLListCollection;
	
	public class ComboBoxVisibility extends ComboBoxBase
	{
			protected override function initializeFields():void {
		    	dictionaryClassName          = "hci.gnomex.model.Visibility";
		    	cellAttributeName            = "@codeVisibility";
		    	choiceDisplayAttributeName   = "@display";
		    	choiceValueAttributeName     = "@value";
		    	this.showMissingDataBackground = false;
		    }
		    
		    protected override function setDataProvider():void {
				dataProvider = new XMLListCollection(parentApplication.manageDictionaries.lastResult.Dictionary.(@className == dictionaryClassName).DictionaryEntry);
            }
	}

}