package views.renderers
{
	import flash.xml.*;
	import mx.controls.advancedDataGridClasses.AdvancedDataGridGroupItemRenderer;
	
	import mx.collections.*;
	import mx.controls.AdvancedDataGrid;
	import mx.controls.listClasses.*;
	import mx.core.mx_internal;
	use namespace mx_internal; 
	

	public class PropertyGroupedItem extends AdvancedDataGridGroupItemRenderer 
	{

		public function PropertyGroupedItem () {
			super();
		}
	    
		

	   override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {

		   super.updateDisplayList(unscaledWidth, unscaledHeight);
	       if(super.data) {
			   	if (data.hasOwnProperty("@isActive") && data.@isActive == "N") { 
				   setStyle("fontStyle", "italic");
				   setStyle("fontWeight", "normal");
				   setStyle("fontColor", "#647478");
			   	} else if (data.hasOwnProperty("@isRequired") && data.@isRequired == "Y") {
	        		setStyle("fontStyle", "italic");
					setStyle("fontWeight", "bold");
					setStyle("fontColor", "#000000");
	        	} else {
					setStyle("fontStyle", "normal");
					setStyle("fontWeight", "normal");
					setStyle("fontColor", "#000000");
				}
		   }
	   }
	}

}