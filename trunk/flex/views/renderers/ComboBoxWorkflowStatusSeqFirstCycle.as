package views.renderers
{
	import mx.events.ListEvent;
	import mx.events.CollectionEvent;
	import mx.collections.IList;
	import mx.controls.DataGrid;
	
	public class ComboBoxWorkflowStatusSeqFirstCycle extends ComboBoxWorkflowStatus
	{
		private var statusDictionary:XML =    
	 			<statusDictionary>
                    <dictionary value="" display="" />
                    <dictionary value="In Progress"  display="In Progress" />
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
	    

	}
}