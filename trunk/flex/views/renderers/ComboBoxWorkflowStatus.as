package views.renderers
{
	import hci.flex.renderers.RendererFactory;
	
	import mx.collections.XMLListCollection;
	import mx.core.IFactory;


	public class ComboBoxWorkflowStatus extends views.renderers.ComboBox
	{
		public var _showInProgress:Boolean = false;
		public var _showCompleted:Boolean  = true;
		public var _showTerminated:Boolean = true;
		public var _showBypassed:Boolean   = true;
		public var _showOnHold:Boolean     = true;
		
		private var statusDictionary:XML =    
	 			<statusDictionary>
                    <dictionary value="" display="" />
                    <dictionary value="In Progress"  display="In Progress" />
                    <dictionary value="Completed"  display="Complete" />
                    <dictionary value="Terminated" display="Terminate" />
                    <dictionary value="Bypassed"   display="Bypass" />
                    <dictionary value="On Hold"   display="On Hold" />
                </statusDictionary>;
		
		public static function create(dataField:String,
									  showInProgress:Boolean=false,
						 			  showCompleted:Boolean=true,
						 			  showTerminated:Boolean=true,
						 			  showBypassed:Boolean=true,
						 			  showOnHold:Boolean=true
						 			  ):IFactory {
				return RendererFactory.create(ComboBoxWorkflowStatus, 
				{ dataField: dataField,
				  _showInProgress: showInProgress,
				  _showCompleted: showCompleted,
				  _showTerminated: showTerminated,
				  _showBypassed: showBypassed,  
				  _showOnHold: showOnHold,
				  updateData: true,
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
			
			if (!_showOnHold) {
				for each(status in dp) {
					if (status.@value == "On Hold") {
						dp.removeItemAt(dp.getItemIndex(status));
					}
				}
			}				
        }
	}
}