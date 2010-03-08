package views.renderers
{
	import hci.flex.renderers.RendererFactory;
	
	import mx.controls.Alert;
	import mx.core.IFactory;
	import mx.events.ListEvent;
	

	public class ComboBoxBillingPeriod extends views.renderers.ComboBox
	{ 		
    	private static const millisecondsPerDay:int = 1000 * 60 * 60 * 24;
    	

			public static function create(dataProvider:Object, 
										  labelField:String,
										  valueField:String,
										  dataField:String, 
								          securityDataField:String,
								          updateData:Boolean,
									      isRequired:Boolean=false, 
								          canChangeByAdminOnly:Boolean=false):IFactory {
				return RendererFactory.create(views.renderers.ComboBoxBillingPeriod, {dataProvider: dataProvider, 
																		 labelField: labelField,
																		 valueField: valueField,
																		 dataField: dataField,  
																		 updateData: true,
					 													 securityDataField: securityDataField,
																		 canChangeByAdminOnly: canChangeByAdminOnly,
																		 isRequired: isRequired});			
			}
			
			
			
            protected override function change(event:ListEvent):void {
            	//super.change(event);
            	// TODO
            	// Need to figure out a way to compare previous selected
            	// billing period to this selection.  Warn if
            	// billing period > or < 2 months.
            	var oldValue:String = data[dataField];
            	var oldDate:Date = null;
            	var acceptableDateFrom:Date = null;
            	var acceptableDateTo:Date = null;
            	var newDate:Date = dateStringToObject(this.selectedItem.@startDate);
            	var oldDateDisplay:String = "";
            	for each(var item:Object in dataProvider) {
            		if (item.@value == oldValue) {
            			oldDate = dateStringToObject(item.@startDate);
            			oldDateDisplay = item.@display;
            			acceptableDateFrom = new Date(oldDate.getTime() - (millisecondsPerDay * 30 * 3));
            			acceptableDateTo = new Date(oldDate.getTime() + (millisecondsPerDay * 30 * 3));
            			break;
            		}
            	}
            	
            	if (newDate > acceptableDateTo || newDate < acceptableDateFrom) {
            		if (oldDate < newDate) {
	            		Alert.show("The billing period " + this.selectedItem.@display + " is significantly later than the previous selected billing period " + oldDateDisplay + ".  Are you sure this is the correct billing period?", "Warning");            			
            		} else {
	            		Alert.show("The billing period " + this.selectedItem.@display + " is significantly earlier than the previous selected billing period " + oldDateDisplay + ".  Are you sure this is the correct billing period?", "Warning");            			
            		}
            	}
            }
            
            private function dateStringToObject(dateString:String):Date{
    			var date_ar:Array = dateString.split("/");
    			return new Date(date_ar[2], date_ar[0]-1, date_ar[1]);
            }

	    
 }

}