package views.renderers
{
	import mx.collections.IList;
	import mx.collections.XMLListCollection;
	import mx.controls.DataGrid;
	import mx.events.CollectionEvent;
	import mx.events.ListEvent;
	import mx.core.IFactory;
	import hci.flex.renderers.RendererFactory;


	public class ComboBoxWorkflowStatus extends views.renderers.ComboBoxDictionary
	{
		public var _showInProgress:Boolean = false;
		public var _showCompleted:Boolean  = true;
		public var _showTerminated:Boolean = true;
		public var _showBypassed:Boolean   = true;
		
		private var statusDictionary:XML =    
	 			<statusDictionary>
                    <dictionary value="" display="" />
                    <dictionary value="In Progress"  display="In Progress" />
                    <dictionary value="Completed"  display="Complete" />
                    <dictionary value="Terminated" display="Terminate" />
                    <dictionary value="Bypassed"   display="Bypass" />
                </statusDictionary>;
		
		public static function create(dataField:String,
									  showInProgress:Boolean=false,
						 			  showCompleted:Boolean=true,
						 			  showTerminated:Boolean=true,
						 			  showBypassed:Boolean=true):IFactory {
				return RendererFactory.create(ComboBoxWorkflowStatus, 
				{ dataField: dataField,
				  _showInProgress: showInProgress,
				  _showCompleted: showCompleted,
				  _showTerminated: showTerminated,
				  _showBypassed: showBypassed,
				  canChangeByAdminOnly: true});			
				  
		}	
		
        override protected function initializationComplete():void
        {   
        	super.initializationComplete();
        	setDataProvider();
            this.addEventListener(ListEvent.CHANGE, change);
        }
    
	    override protected function setDataProvider():void {
			dataProvider = new XMLListCollection(statusDictionary.dictionary);
			
			var dp:XMLListCollection = XMLListCollection(dataProvider);
			var status:Object;
			if (!_showInProgress) {
				for each(status in dp) {
					if (status.@value == "In Progress") {
						dp.removeItemAt(dp.getItemIndex(status));
					}
				}
			}

			if (!_showBypassed) {
				for each(status in dp) {
					if (status.@value == "Bypassed") {
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
						
			if (!_showTerminated) {
				for each(status in dp) {
					if (status.@value == "Terminated") {
						dp.removeItemAt(dp.getItemIndex(status));
					}
				}
			}				
			// This will detect changes to underlying data anc cause combobox to be selected based on value.
			IList(DataGrid(owner).dataProvider).addEventListener(CollectionEvent.COLLECTION_CHANGE, underlyingDataChange);
        }
 
 
 


	}
}