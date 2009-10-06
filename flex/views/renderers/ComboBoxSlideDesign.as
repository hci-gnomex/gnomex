package views.renderers
{
	import mx.collections.IList;
	import mx.controls.Alert;
	import mx.controls.DataGrid;
	import mx.core.IFactory;
	import mx.events.CollectionEvent;
	import mx.events.ListEvent;
	import hci.flex.renderers.RendererFactory;
	
	public class ComboBoxSlideDesign  extends views.renderers.ComboBox
	{
			public static function create(dataField:String, 
										valueField:String, 
										displayField:String, 
										securityDataField:String):IFactory {
				return RendererFactory.create(ComboBoxSlideDesign, 
				{ dataField: dataField,
				  valueField: valueField,
				  labelField: displayField,
				  updateData: true,
				  securityDataField: securityDataField});			
				  
			}			   		    
            
            override protected function initializationComplete():void
            {   
            	this.dataProvider = parentDocument.slideDesigns;           	
				super.initializationComplete();				
            }
            
            
		    protected override function change(event:ListEvent):void {
		     	if (data.@canChangeSlideDesign == "Y" || parentApplication.hasPermission("canWriteAnyObject")) {
	            	super.change(event);
    	        	parentDocument.checkHybsCompleteness();
		     	} else {
		     		this.selectTheItem();
		     		Alert.show("Slide cannot be changed.");
		     	}
            }                
	}
}