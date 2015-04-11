package views.renderers
{
	import hci.flex.controls.ComboBox;
	import hci.flex.renderers.RendererFactory;
	import hci.flex.util.DictionaryManager;
	
	import mx.collections.IList;
	import mx.collections.XMLListCollection;
	import mx.controls.DataGrid;
	import mx.core.IFactory;
	import mx.events.CollectionEvent;
	
	public class ComboBoxOligoBarcode extends hci.flex.controls.ComboBox
	{
			public static function create():IFactory {
				return RendererFactory.create(ComboBoxOligoBarcode, 
				{ dataField: '@idOligoBarcode',  
				  updateData: true
				});	 						  
			}	 
			
            override protected function initializationComplete():void
            {   
            	dataProvider = parentDocument.barcodes;         	
				super.initializationComplete();				
            }
            
		    override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void
		    {
		        super.updateDisplayList(unscaledWidth,unscaledHeight);
		        if (data == null) {
		          	return;
		        }
		        
		        // The oligo barcode dropdown should be enabled if the core is prepping the library
		        // and multiplexed samples are being used.  
		        // Also, enable the dropdown if an oligobarcode has been assigned already.
				if (data.@multiplexGroupNumber != "" && data.@seqPrepByCore == "Y") {
					this.enabled = true;
				} else if (data.@idOligoBarcode != '' || data.@seqPrepByCore != "N"){
					this.enabled = true;
				} else{
					this.enabled = false;
				}
		    }            						 		            
	}

}