package views.renderers
{
	import mx.events.ListEvent;
	
	public class ComboBoxOrganism extends ComboBoxBase
	{ 
			protected override function initializeFields():void {
		    	dictionaryClassName          = "hci.gnomex.model.Organism";
		    	cellAttributeName            = "@idOrganism";
		    	choiceDisplayAttributeName   = "@display";
		    	choiceValueAttributeName     = "@value";
		    }
		    protected override function change(event:ListEvent):void {
	     		_data[cellAttributeName] = this.selectedItem[this.choiceValueAttributeName];
            	parentDocument.propagateOrganism(_data[cellAttributeName]);
	        	_data.@isDirty = "Y";		     		
		     	
            }
		    
	}

}