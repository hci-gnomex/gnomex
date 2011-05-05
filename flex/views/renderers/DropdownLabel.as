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
										  isRequired:Boolean=true
								          ):IFactory {
			return RendererFactory.create(views.renderers.DropdownLabel, {dataProvider: dataProvider, 
																		 labelField: labelField,
																		 valueField: valueField,
																		 dataField: dataField,
																		 isRequired:isRequired
																		 });			
		}
			
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
            super.updateDisplayList(unscaledWidth,unscaledHeight);
	    	if (data == null || !(data is XML)) {
	      		return;
	      	}
			
			if (!isRequired) {
				var g0:Graphics = graphics;
				g0.clear();
				return;
			}
          
        	if (!parentDocument.parentDocument.isEditState()) {
          		var g:Graphics = graphics;
	        	g.clear();	        
		  	  
	        	if (!data.hasOwnProperty(dataField) || data[dataField] == '') {
	        		g.beginFill(!data.hasOwnProperty(dataField) || data[dataField] == '' ? missingRequiredFieldBackground : 0xffffff );
	    	    	g.lineStyle(missingRequiredFieldBorderThickness, missingRequiredFieldBorder );          	
					g.drawRect(0,0,unscaledWidth,unscaledHeight);
		        	g.endFill();
          	  	}
          	} else {
          		var g1:Graphics = graphics;
	        	g1.clear();
          	}					        				
		
        }	
	}

}