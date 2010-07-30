package views.renderers
{
	import hci.flex.controls.DropdownLabel;
	import hci.flex.renderers.RendererFactory;

	import mx.core.IFactory;

	

	public class DropdownLabelBillingItem extends hci.flex.controls.DropdownLabel
	{ 	
		public static function create(dataProvider:Object, 
										  labelField:String,
										  valueField:String,
										  dataField:String 
								          ):IFactory {
			return RendererFactory.create(views.renderers.DropdownLabelBillingItem, {dataProvider: dataProvider, 
																		 labelField: labelField,
																		 valueField: valueField,
																		 dataField: dataField  
																		 });			
		}
			
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
            super.updateDisplayList(unscaledWidth, unscaledHeight);
 	    	if (data == null) {
	      		return;
	      	}
            this.styleName = data.@other == 'Y' ? "other" : "normal";
        }

			
	}

}