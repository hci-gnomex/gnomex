package views.renderers
{
	import hci.flex.util.DictionaryManager;
	import mx.events.ListEvent;
	import hci.flex.controls.ComboBox;
	import mx.core.IFactory;
	import hci.flex.renderers.RendererFactory;
	import mx.controls.Alert;
	
	public class ComboBoxVisibility extends views.renderers.ComboBox
	{					
            protected override function change(event:ListEvent):void {
		        parentDocument.parentDocument.clearFilter();
		        
		     	if (data.@canUpdateVisibility == "Y" || parentApplication.hasPermission("canWriteAnyObject")) {
	            	super.change(event);
		     	} else {
		     		this.selectTheItem();
		     		Alert.show("Visibility can only be changed by owner, lab manager, or GNomEx admins.");
		     	}
	        }
	        
		public static function getFactory(
			dataProvider:Object, 
			labelField:String, 
			valueField:String, 
			dataField:String, 
			updateData:Boolean = false,
			isRequired:Boolean = false,
			appendBlankRow:Boolean = false,
			missingRequiredFieldBackground:uint = 0xFFFFB9):IFactory {			
			return RendererFactory.create(ComboBoxVisibility, {dataProvider: dataProvider, 
													labelField: labelField,  
													valueField: valueField,
													dataField: dataField,
													updateData: updateData,
													isRequired: isRequired,
													appendBlankRow: appendBlankRow,
													updateData: true,
													missingRequiredFieldBackground: missingRequiredFieldBackground});
			}
	}		        

}