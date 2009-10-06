package views.renderers
{
	import hci.flex.controls.ComboBox;
	import hci.flex.renderers.RendererFactory;
	
	import mx.controls.Alert;
	import mx.core.IFactory;
	import mx.events.ListEvent;
	

	public class ComboBox extends hci.flex.controls.ComboBox
	{ 		public var canChangeByAdminOnly:Boolean = false;
			public var securityDataField:String;

			public static function create(dataProvider:Object, 
										  labelField:String,
										  valueField:String,
										  dataField:String, 
								          securityDataField:String,
								          updateData:Boolean,
									      isRequired:Boolean=false, 
								          canChangeByAdminOnly:Boolean=false):IFactory {
				return RendererFactory.create(views.renderers.ComboBox, {dataProvider: dataProvider, 
																		 labelField: labelField,
																		 valueField: valueField,
																		 dataField: dataField,  
																		 updateData: true,
					 													 securityDataField: securityDataField,
																		 canChangeByAdminOnly: canChangeByAdminOnly,
																		 isRequired: isRequired});			
			}
			
			protected function selectTheItem():void {
				for (var i : int = 0; i < dataProvider.length; i++) {
	            	var item:Object = dataProvider[i];
	            	if(item[valueField] == data[dataField]) {
	                      this.selectedIndex = i;
	                      
	                      break;
	                 }
				}
							
			}
			
            protected override function change(event:ListEvent):void {
            	if (securityDataField == null) {
            		if (canChangeByAdminOnly) {
            			if (parentApplication.hasPermission("canWriteAnyObject")) {
            		 		assignData();
            		 		data.@isDirty="Y";
            		 	} else {
            		 		selectTheItem();
			     			Alert.show("This field cannot be changed.  Please ask Microarray Core facility for assistance.");
            		 	}
		     		} else {
	            		assignData();
            		 	data.@isDirty="Y";         		
            		}
            	} else {
			     	if (data[securityDataField] == "Y" || parentApplication.hasPermission("canWriteAnyObject")) {
            		 	assignData();
            		 	data.@isDirty="Y";
		     		} else {		
            		 	selectTheItem();
		     			Alert.show("This field cannot be changed.  Please ask Microarray Core facility for assistance.");
		     		}
            	}
            }	    
	}

}