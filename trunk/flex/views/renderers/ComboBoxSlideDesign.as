package views.renderers
{
	import mx.collections.IList;
	import mx.events.CollectionEvent;
	import mx.events.ListEvent;
	import mx.controls.DataGrid;
	
	public class ComboBoxSlideDesign  extends ComboBoxBase
	{
			protected override function initializeFields():void {
		    	cellAttributeName            = "@idSlideDesign";
		    	choiceDisplayAttributeName   = "@name";
		    	choiceValueAttributeName     = "@idSlideDesign";
		    }
		    
		    override public function set data(o:Object):void
            {
                _data = o;
                if (parentApplication.submitRequestView.slideDesigns == null) {
                	return;
                }
                setDataProvider();
				selectItem(); 
            }
		    
		    protected override function setDataProvider():void {
				dataProvider = parentApplication.submitRequestView.slideDesigns;
				
				// This will detect changes to underlying data anc cause combobox to be selected based on value.
				IList(DataGrid(owner).dataProvider).addEventListener(CollectionEvent.COLLECTION_CHANGE, underlyingDataChange);
            }
            
            protected override function change(event:ListEvent):void {
            	super.change(event);
            	parentDocument.checkHybsCompleteness();
            }
            
			
            
	}

}