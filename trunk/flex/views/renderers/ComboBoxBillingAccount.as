package views.renderers
{
	import mx.collections.IList;
	import mx.controls.Alert;
	import mx.controls.DataGrid;
	import mx.core.IFactory;
	import mx.events.CollectionEvent;
	import mx.events.ListEvent;
	import hci.flex.renderers.RendererFactory;
	
	public class ComboBoxBillingAccount  extends views.renderers.ComboBox
	{
			public static function create(dataField:String):IFactory {
				return RendererFactory.create(ComboBoxBillingAccount, 
				{ dataField: dataField,				
				  updateData: true,
				  valueField: '@idBillingAccount',
				  labelField: '@accountName'});							  
			}			    	    
            
            override protected function initializationComplete():void
            {   
            	super.initializationComplete();
            	this.dataProvider = parentDocument.billingAccounts;
            }                     
       
 			protected override function assignData():void {
 				super.assignData();
 				data.@accountName  = this.selectedItem.@accountName;
            }           
			
            
	}

}