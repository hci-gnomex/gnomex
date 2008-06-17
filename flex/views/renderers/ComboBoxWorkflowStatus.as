package views.renderers
{
	import mx.collections.IList;
	import mx.collections.XMLListCollection;
	import mx.controls.DataGrid;
	import mx.events.CollectionEvent;
	import mx.events.ListEvent;

	public class ComboBoxWorkflowStatus extends views.renderers.ComboBox
	{
		protected var _showInProgress:Boolean = false;
		protected var _showCompleted:Boolean  = true;
		protected var _showTerminated:Boolean = true;
		protected var _showBypassed:Boolean   = true;
		
		private var statusDictionary:XML =    
	 			<statusDictionary>
                    <dictionary value="" display="" />
                    <dictionary value="In Progress"  display="In Progress" />
                    <dictionary value="Completed"  display="Completed" />
                    <dictionary value="Terminated" display="Terminated" />
                    <dictionary value="Bypassed"   display="Bypassed" />
                </statusDictionary>;
		
		public function set showInProgress(showInProgress:Boolean):void {
		    	this._showInProgress = showInProgress;	
		}
		public function set showCompleted(showCompleted:Boolean):void {
		    	this._showCompleted = showCompleted;	
		}
		public function set showTerminated(showTerminated:Boolean):void {
		    	this._showTerminated = showTerminated;	
		}
		public function set showBypassed(showBypassed:Boolean):void {
		    	this._showBypassed = showBypassed;	
		}
	    
	    protected override function setDataProvider():void {
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
        
        override protected function initializationComplete():void
        {   
        	initializeFields();
        	setDataProvider();
            this.addEventListener(ListEvent.CHANGE, change);
        	labelField = this._dictionaryDisplayField;
        }



            
	}
}