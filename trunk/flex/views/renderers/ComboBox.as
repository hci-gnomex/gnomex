package views.renderers
{
	import flash.events.Event;
	import hci.flex.renderers.ComboBox;
	import mx.events.ListEvent;
	import mx.controls.Alert;
	import hci.flex.renderers.RendererFactory;
	import mx.core.IFactory;
	

	public class ComboBox extends hci.flex.renderers.ComboBox
	{ 		public var canChangeByAdminOnly:Boolean = false;

			public static function create(dataField:String, 
								          securityDataField:String,
									      isRequired:Boolean=false, 
								          canChangeByAdminOnly:Boolean=false,
									      missingRequiredFieldBackground:uint=0xffd8bb):IFactory {
				return RendererFactory.create(views.renderers.ComboBox, {dataField: dataField,  
																		 securityDataField: securityDataField,
																		 canChangeByAdminOnly: canChangeByAdminOnly,
																		 isRequired: isRequired, 
																		 missingRequiredFieldBackground: missingRequiredFieldBackground});			
			}
			
            protected override function change(event:ListEvent):void {
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
			     	if (data[securityDataField] == "Y" || parentApplication.hasPermission("canWriteAnyObject")) {
			     		assignData();
		     		} else {
		     			selectItem();
		     			Alert.show("This field cannot be changed.  Please ask Microarray Core facility for assistance.");
		     		}
            	}
            }
            
            protected override function assignData():void {
            	data[dataField] = this.selectedItem[this.listValueField];
            	data.@isDirty = "Y";            	
            }
            
		    
		    
	}

}