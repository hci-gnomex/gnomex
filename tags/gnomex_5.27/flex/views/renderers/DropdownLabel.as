package views.renderers
{
	import flash.display.Graphics;
	
	import hci.flex.controls.DropdownLabel;
	import hci.flex.renderers.RendererFactory;
	
	import mx.core.IFactory;
	
	public class DropdownLabel extends hci.flex.controls.DropdownLabel
	{
		public static function create(dataProvider:Object, 
										  labelField:String,
										  valueField:String,
										  dataField:String,
										  isRequired:Boolean=false,
										  isEditable:Boolean=false,
										  missingRequiredFieldBackground:uint = 0xeaeaea
								          ):IFactory {
			return RendererFactory.create(views.renderers.DropdownLabel, {dataProvider: dataProvider, 
																		 labelField: labelField,
																		 valueField: valueField,
																		 dataField: dataField,
																		 isRequired:isRequired,
																		 isEditable:isEditable,
																		 missingRequiredFieldBackground: missingRequiredFieldBackground
																		 });			
		}
			
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
            super.updateDisplayList(unscaledWidth,unscaledHeight);
			
			var g:Graphics = graphics;
			g.clear();
			
			if (data == null || !(data is XML)) {
	      		return;
	      	}
        	if (isEditable) {
          		        
				if (this.text == null || this.text == '') {
					if ( !isRequired ) {
						g.beginFill(missingFieldBackground);
						g.lineStyle(missingRequiredFieldBorderThickness,
							missingFieldBackground);          	
						g.drawRect(0,0,unscaledWidth,unscaledHeight);
						g.endFill();
					} else {
						g.beginFill(missingRequiredFieldBackground);
						g.lineStyle(missingRequiredFieldBorderThickness,
							missingRequiredFieldBorder);          	
						g.drawRect(0,0,unscaledWidth,unscaledHeight);
						g.endFill();
					}
				} 
          	} 				        				
		
        }	
	}

}