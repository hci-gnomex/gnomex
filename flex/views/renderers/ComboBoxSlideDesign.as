package views.renderers
{
	import mx.collections.IList;
	import mx.events.CollectionEvent;
	import mx.events.ListEvent;
	import mx.controls.DataGrid;
	import mx.controls.Alert;
	
	public class ComboBoxSlideDesign  extends ComboBox
	{

		    
		    override public function set data(o:Object):void
            {
                _data = o;
                if (parentDocument.slideDesigns == null) {
                	return;
                }
                setDataProvider();
				selectItem(); 
            }
		    
		    protected override function setDataProvider():void {
				dataProvider = parentDocument.slideDesigns;
				
				// This will detect changes to underlying data anc cause combobox to be selected based on value.
				IList(DataGrid(owner).dataProvider).addEventListener(CollectionEvent.COLLECTION_CHANGE, underlyingDataChange);
            }
            
		    protected override function change(event:ListEvent):void {
		     	if (_data.@canChangeSlideDesign == "Y" || parentApplication.hasPermission("canWriteAnyObject")) {
	            	super.change(event);
    	        	parentDocument.checkHybsCompleteness();
		     	} else {
		     		selectItem();
		     		Alert.show("Slide cannot be changed.");
		     	}
            }            
            
			
            
	}

}