package views.renderers
{
	import mx.events.ListEvent;
	import mx.controls.Alert;
	import mx.events.CollectionEvent;
	import mx.collections.IList;
	import mx.controls.DataGrid;
		
	
	public class ComboBoxSeqFirstCycleStatus extends ComboBoxStatus
	{
		private var statusDictionary:XML =    
	 			<statusDictionary>
                    <dictionary value="" display="" />
                    <dictionary value="Completed"  display="Completed" />
                    <dictionary value="Terminated" display="Terminated" />
                </statusDictionary>;
		
	    
	    protected override function setDataProvider():void {
			dataProvider = new XMLList(statusDictionary.dictionary);
			
			// This will detect changes to underlying data anc cause combobox to be selected based on value.
			IList(DataGrid(owner).dataProvider).addEventListener(CollectionEvent.COLLECTION_CHANGE, underlyingDataChange);
        }				
		protected override function initializeFields():void {
			super.initializeFields();
	    	cellAttributeName            = "@firstCycleStatus";
	    }
	   	protected override function change(event:ListEvent):void {
	     	if (parentApplication.hasPermission("canWriteAnyObject")) {
            	super.change(event);
	     	} else {
	     		selectItem();
	     		Alert.show("First cycle status cannot be changed.");
	     	}
        }            

	    

	}
}