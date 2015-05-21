package views.renderers
{
	import mx.collections.IList;
	import mx.controls.DataGrid;
	import mx.events.CollectionEvent;
	import mx.events.ListEvent;
	import mx.core.IFactory;
	import views.renderers.ComboBox;
	import hci.flex.renderers.RendererFactory;
	

	public class ComboBoxArrayCoordinate extends views.renderers.ComboBox
	{
			private var coordDictionary:XML =    
		 			<coordDictionary>
                        <dictionary value="" display="" />
                        <dictionary value="1_1"  display="1_1" />
                        <dictionary value="1_2"  display="1_2" />
                        <dictionary value="1_3"  display="1_3" />
                        <dictionary value="1_4"  display="1_4" />
                        <dictionary value="2_1"  display="2_1" />
                        <dictionary value="2_2"  display="2_2" />
                        <dictionary value="2_3"  display="2_3" />
                        <dictionary value="2_4"  display="2_4" />
                    </coordDictionary>;
			
			public static function create(dataField:String):IFactory {
				return RendererFactory.create(ComboBoxArrayCoordinate, 
				{ dataField: dataField,  
				updateData: true});			
				  
			}	 
					    
		    override protected function initializationComplete():void {
            
            
				dataProvider = new XMLList(coordDictionary.dictionary);
            	super.initializationComplete();
            }
            

            
		    override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void
		    {
		        super.updateDisplayList(unscaledWidth,unscaledHeight);
		        if (data == null) {
		          	return;
		        }
				if (data.@arraysPerSlide == "" || data.@arraysPerSlide == "1") {
					this.enabled = false;
				} else {
					this.enabled = true;
				}
		    }
            
	}
}