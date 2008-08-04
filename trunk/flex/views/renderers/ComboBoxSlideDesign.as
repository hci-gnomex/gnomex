package views.renderers
{
	import mx.collections.IList;
	import mx.controls.Alert;
	import mx.controls.DataGrid;
	import mx.core.IFactory;
	import mx.events.CollectionEvent;
	import mx.events.ListEvent;
	import hci.flex.renderers.RendererFactory;
	
	public class ComboBoxSlideDesign  extends views.renderers.ComboBoxDictionary
	{
			public static function create(dataField:String, dictionaryValueField:String, dictionaryDisplayField:String, securityDataField:String):IFactory {
				return RendererFactory.create(ComboBoxSlideDesign, 
				{ dataField: dataField,
				  dictionaryValueField: dictionaryValueField,
				  dictionaryDisplayField: dictionaryDisplayField,
				  securityDataField: securityDataField});			
				  
			}	
		    
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
            
            override protected function initializationComplete():void
            {   
            	setDataProvider();
                this.addEventListener(ListEvent.CHANGE, change);
            	labelField = this.dictionaryDisplayField;
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