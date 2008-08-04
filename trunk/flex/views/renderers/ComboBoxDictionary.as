package views.renderers
{
	import flash.events.Event;
	
	import hci.flex.util.DictionaryManager;
	import hci.flex.renderers.ComboBoxDictionary;	 
	import mx.collections.IList;
	import mx.controls.Alert;
	import mx.controls.DataGrid;
	import mx.core.IFactory;
	import mx.events.CollectionEvent;
	import mx.events.ListEvent; 
	import hci.flex.renderers.RendererFactory; 


	public class ComboBoxDictionary extends hci.flex.renderers.ComboBoxDictionary
	{ 
		    public var securityDataField:String;
		    public var canChangeByAdminOnly:Boolean = false;
		    
			public static function create(dictionaryManager:DictionaryManager, 
									      dictionary:String, 
								          dataField:String, 
								          securityDataField:String,
									      isRequired:Boolean=false, 
								          canChangeByAdminOnly:Boolean=false,
									      missingRequiredFieldBackground:uint=0xffd8bb):IFactory {
				return RendererFactory.create(views.renderers.ComboBoxDictionary, {dictionaryManager: dictionaryManager, 
																		         dictionary: dictionary, 
																		         dataField: dataField,  
																		         securityDataField: securityDataField,
																		         canChangeByAdminOnly: canChangeByAdminOnly,
																		         isRequired: isRequired, 
																		         missingRequiredFieldBackground: missingRequiredFieldBackground});			
			}
		    

            override protected function initializationComplete():void
            {   
            	super.initializationComplete();
                this.addEventListener(ListEvent.CHANGE, change);
            }
                        
            override protected function change(event:ListEvent):void {
            	if (securityDataField == null) {
            		if (canChangeByAdminOnly) {
            			if (parentApplication.hasPermission("canWriteAnyObject")) {
            		 		assignData();	
            		 	} else {
			     			selectItem();
			     			Alert.show("This field cannot be changed.  Please ask Microarray Core facility for assistance.");
            		 	}
		     		} else {
	            		assignData();            		
            		}
            	} else {
			     	if (_data[securityDataField] == "Y" || parentApplication.hasPermission("canWriteAnyObject")) {
			     		assignData();
		     		} else {
		     			selectItem();
		     			Alert.show("This field cannot be changed.  Please ask Microarray Core facility for assistance.");
		     		}
            	}
            }


            protected override function assignData():void {
            	super.assignData();
            	_data.@isDirty = "Y";            	
            }
            
  
            

		    
		    
	}

}