package views.renderers
{
	import mx.collections.XMLListCollection;
	import mx.events.ListEvent;
	
	public class ComboBoxVisibility extends ComboBox
	{
            protected override function change(event:ListEvent):void {
		        parentDocument.parentDocument.clearFilter();
		        super.change(event);
	        }
	}

}