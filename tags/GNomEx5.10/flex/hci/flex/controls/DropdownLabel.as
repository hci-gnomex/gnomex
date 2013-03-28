package hci.flex.controls
{
	import flash.display.Graphics;
	import mx.controls.Label;
	import mx.core.IFactory;
	import hci.flex.renderers.RendererFactory;
	
	public class DropdownLabel extends mx.controls.Label
	{
		public var labelField:String;
		public var valueField:String;
		public var dataProvider:XMLList;
		public var isRequired:Boolean = false;
		public var missingRequiredFieldBackground:uint = RendererFactory.DEFAULT_MISSING_REQUIRED_FIELD_BACKGROUND;
	    public var missingRequiredFieldBorder:uint = RendererFactory.DEFAULT_MISSING_REQUIRED_FIELD_BORDER;
	    public var missingRequiredFieldBorderThickness:uint = RendererFactory.DEFAULT_MISSING_REQUIRED_FIELD_BORDER_THICKNESS; 		
		
		//For use in list
		public var dataField:String;	
		
		
		public function DropdownLabel()
		{
			super();
			
			//Defaults - Change as needed
			labelField = "@display";
			valueField = "@value";		
		}
		
		public function set value(val:String):void {
			if ( val != null ) {
				if (val == "") {
					this.text = val;
				}
				else {
					for (var i : int = 0; i < dataProvider.length(); i++) {
		            	var item:Object = dataProvider[i];
		            	if(item[valueField] == val) {
		                      this.text = item[labelField];
		                      break;
		                 }
					}
				}
			}
		}
		
        override public function set data(value:Object):void {
        	super.data = value;
        	
        	if (value && value.hasOwnProperty(dataField)) {
        		this.value = value[dataField];
        	} else {
        		this.value = '';
        	}
        }  
        
		public static function getFactory(
			dataProvider:XMLList, 
			labelField:String, 
			valueField:String, 
			dataField:String, 
			isRequired:Boolean=false, 
			missingRequiredFieldBackground:uint = 0xFFFFB9):IFactory {			
			
			return RendererFactory.create(hci.flex.controls.DropdownLabel, {dataProvider: dataProvider, 
													labelField: labelField,  
													valueField: valueField,
													dataField: dataField,
													isRequired: isRequired,
													missingRequiredFieldBackground: missingRequiredFieldBackground});				
		} 
		
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
        	super.updateDisplayList(unscaledWidth,unscaledHeight);
	    	if (data == null) {
	      		return;
	      	}
	      	if (!data.hasOwnProperty(dataField)) {
	      		return;
	      	}

          
        	if (isRequired) {
          		var g:Graphics = graphics;
	        	g.clear();	        
		  	  
	        	if (data[dataField] == '') {
	        		g.beginFill( data[dataField] == '' ? missingRequiredFieldBackground : 0xffffff );
	    	    	g.lineStyle(missingRequiredFieldBorderThickness, missingRequiredFieldBorder );          	
					g.drawRect(0,0,unscaledWidth,unscaledHeight);
		        	g.endFill();
          	  	}
          	}					        				
		}
	}
}