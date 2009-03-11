package views.renderers
{
	import mx.collections.IList;
	import mx.controls.Alert;
	import mx.controls.DataGrid;
	import mx.core.IFactory;
	import mx.events.CollectionEvent;
	import mx.events.ListEvent;
	import hci.flex.renderers.RendererFactory;
	
	public class ComboBoxBillingAccount  extends views.renderers.ComboBoxDictionary
	{
			public static function create(dataField:String):IFactory {
				return RendererFactory.create(ComboBoxBillingAccount, 
				{ dataField: dataField,
				  dictionaryValueField: '@idBillingAccount',
				  dictionaryDisplayField: '@accountName'});			
				  
			}	
		    
		    override public function set data(o:Object):void
            {
                _data = o;
                if (parentDocument.billingAccounts == null) {
                	return;
                }
                setDataProvider();
				selectItem(); 
            }
		    
		    protected override function setDataProvider():void {
				dataProvider = parentDocument.billingAccounts;
				
				// This will detect changes to underlying data anc cause combobox to be selected based on value.
				IList(DataGrid(owner).dataProvider).addEventListener(CollectionEvent.COLLECTION_CHANGE, underlyingDataChange);
            }
            
            override protected function initializationComplete():void
            {   
            	setDataProvider();
                this.addEventListener(ListEvent.CHANGE, change);
            	labelField = this.dictionaryDisplayField;
            }
            
         
       
 			protected override function assignData():void {
 				super.assignData();
 				_data.@accountName  = this.selectedItem.@accountName;
            }           
			
            
	}

}