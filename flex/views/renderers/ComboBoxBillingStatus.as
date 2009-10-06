package views.renderers
{
	import mx.collections.IList;
	import mx.collections.XMLListCollection;
	import mx.controls.DataGrid;
	import mx.events.CollectionEvent;
	import mx.events.ListEvent;
	import mx.core.IFactory;
	import hci.flex.renderers.RendererFactory;
	import mx.collections.HierarchicalCollectionView;
	import mx.controls.AdvancedDataGrid;
	import mx.controls.Alert;
	
	public class ComboBoxBillingStatus extends views.renderers.ComboBox
	{
		public var _showPending:Boolean = false;
		public var _showCompleted:Boolean  = true;
		public var _showApproved:Boolean = true;
		
		private var statusDictionary:XML =    
	 			<statusDictionary>
                    <dictionary value="" display="" />
                    <dictionary value="PENDING"  display="Pending" />
                    <dictionary value="COMPLETE"  display="Completed" />
                    <dictionary value="APPROVED" display="Approved" />
                </statusDictionary>;
		
		public static function create(dataField:String,
									  showPending:Boolean=false,
						 			  showCompleted:Boolean=true,
						 			  showApproved:Boolean=true):IFactory {
				return RendererFactory.create(ComboBoxBillingStatus, 
				{ dataField: dataField,
				  _showPending: showPending,
				  _showCompleted: showCompleted,
				  _showApproved: showApproved,
				  updateData:true,
				  canChangeByAdminOnly: true});			
				  
		}	
		
        override protected function initializationComplete():void
        {   
        	super.initializationComplete();
        	setDataProvider();
        }
    
	    protected function setDataProvider():void {
			dataProvider = new XMLListCollection(statusDictionary.dictionary);
			
			var dp:XMLListCollection = XMLListCollection(dataProvider);
			var status:Object;
			if (!_showPending) {
				for each(status in dp) {
					if (status.@value == "In Progress") {
						dp.removeItemAt(dp.getItemIndex(status));
					}
				}
			}

			if (!_showCompleted) {
				for each(status in dp) {
					if (status.@value == "Completed") {
						dp.removeItemAt(dp.getItemIndex(status));
					}
				}
			}	
						
			if (!_showApproved) {
				for each(status in dp) {
					if (status.@value == "Terminated") {
						dp.removeItemAt(dp.getItemIndex(status));
					}
				}
			}

        }
 
       override protected function change(event:ListEvent):void {
			if (parentApplication.hasPermission("canManageBilling")) {
		 		assignData();	
		 	} else {
     			Alert.show("This field cannot be changed.  Please ask Microarray Core facility for assistance.");
		 	}
        } 
	}
}