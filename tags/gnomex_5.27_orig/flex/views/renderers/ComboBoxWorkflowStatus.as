package views.renderers
{
	import hci.flex.renderers.RendererFactory;
	
	import mx.collections.XMLListCollection;
	import mx.core.IFactory;


	public class ComboBoxWorkflowStatus extends views.renderers.ComboBox
	{
		public var _showInProgress:Boolean = true;
		public var _showCompleted:Boolean  = true;
		public var _showTerminated:Boolean = true;
		public var _showBypassed:Boolean   = true;
		public var _showOnHold:Boolean     = true;
		
		private var statusDictionary:XML =    
	 			<statusDictionary>
                    <dictionary value="" display="" />
                    <dictionary value="In Progress"  display="In Progress" />
                    <dictionary value="Completed"  display="Complete" />
                    <dictionary value="On Hold"   display="On Hold" />
                    <dictionary value="Terminated" display="Terminate" />
                    <dictionary value="Bypassed"   display="Bypass" />
                </statusDictionary>;
		
		
		
        override protected function initializationComplete():void
        {   
        	super.initializationComplete();
        	setDataProvider();
        }
    
	    protected function setDataProvider():void {
			var statusList:XMLListCollection = new XMLListCollection(statusDictionary.dictionary);
			
			var status:Object;
			if (!_showInProgress) {
				for each(status in statusList) {
					if (status.@value == "In Progress") {
						statusList.removeItemAt(statusList.getItemIndex(status));
						break;
					}
				}
			}

			if (!_showBypassed) {
				for each(status in statusList) {
					if (status.@value == "Bypassed") {
						statusList.removeItemAt(statusList.getItemIndex(status));
						break;
					}
				}
			}

			if (!_showCompleted) {
				for each(status in statusList) {
					if (status.@value == "Completed") {
						statusList.removeItemAt(statusList.getItemIndex(status));
						break;
					}
				}
			}	
						
			if (!_showTerminated) {
				for each(status in statusList) {
					if (status.@value == "Terminated") {
						statusList.removeItemAt(statusList.getItemIndex(status));
						break;
					}
				}
			}	
			
			if (!_showOnHold) {
				for each(status in statusList) {
					if (status.@value == "On Hold") {
						statusList.removeItemAt(statusList.getItemIndex(status));
						break;
					}
				}
			}			
			
			dataProvider = statusList.copy();
        }
		public static function create(dataField:String,
									  showInProgress:Boolean=true,
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
		
		
			
	}
}

