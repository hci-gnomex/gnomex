package views
{
	import mx.controls.ComboBox;
	import flash.events.MouseEvent;
	import mx.collections.XMLListCollection;
	import mx.collections.ICollectionView;
	import flash.events.Event;
	import mx.events.ListEvent;

	public class ComboBoxBlankSelection extends ComboBox
	{
            override protected function initializationComplete():void
            {   
            }
            
            public override function set dataProvider(value:Object):void {
            	dataProvider = value;
            	var node:XML = <Lab idLab='' name=''/>;
            	
            	var data:XMLListCollection  = XMLListCollection(dataProvider);
            	data.addItemAt(node, 0);
            	dataProvider.refresh();
            }

	}
}