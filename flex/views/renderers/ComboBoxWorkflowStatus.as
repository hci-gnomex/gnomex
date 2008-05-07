package views.renderers
{
	import mx.controls.ComboBox;
	import flash.events.MouseEvent;
	import mx.collections.XMLListCollection;
	import flash.events.Event;
	import mx.events.ListEvent;
	import mx.events.CollectionEvent;
	import mx.controls.DataGrid;
	import mx.collections.IList;

	public class ComboBoxWorkflowStatus extends ComboBoxBase
	{
			private var statusDictionary:XML =    
		 			<statusDictionary>
                        <dictionary value="" display="" />
                        <dictionary value="Completed"  display="Completed" />
                        <dictionary value="Terminated" display="Terminated" />
                        <dictionary value="Bypassed"   display="Bypassed" />
                    </statusDictionary>;
			

			protected override function initializeFields():void {
		    	cellAttributeName              = "@status";
		    	choiceValueAttributeName       = "@value";
		    	choiceDisplayAttributeName     = "@display";
				showMissingDataBackground      = false;
		    }
		    

		    
		    protected override function setDataProvider():void {
				dataProvider = new XMLList(statusDictionary.dictionary);
				
				// This will detect changes to underlying data anc cause combobox to be selected based on value.
				IList(DataGrid(owner).dataProvider).addEventListener(CollectionEvent.COLLECTION_CHANGE, underlyingDataChange);
            }
            
            override protected function initializationComplete():void
            {   
            	initializeFields();
            	setDataProvider();
                this.addEventListener(ListEvent.CHANGE, change);
            	labelField = choiceDisplayAttributeName;
            }

		    protected override function change(event:ListEvent):void {
		        parentDocument.workList.filterFunction = null;
		        super.change(event);
	        }

            
	}
}