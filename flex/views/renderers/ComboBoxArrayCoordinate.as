package views.renderers
{
	import mx.controls.ComboBox;
	import flash.events.MouseEvent;
	import mx.collections.XMLListCollection;
	import flash.events.Event;
	import mx.events.ListEvent;
	import mx.events.CollectionEvent;
	import mx.controls.DataGrid;
	import mx.collections.IList;

	public class ComboBoxArrayCoordinate extends ComboBoxBase
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
			

			protected override function initializeFields():void {
		    	cellAttributeName              = "@arrayCoordinate";
		    	choiceValueAttributeName       = "@value";
		    	choiceDisplayAttributeName     = "@display";
				showMissingDataBackground      = false;
		    }
		    

		    
		    protected override function setDataProvider():void {
				dataProvider = new XMLList(coordDictionary.dictionary);
				
				// This will detect changes to underlying data anc cause combobox to be selected based on value.
				IList(DataGrid(owner).dataProvider).addEventListener(CollectionEvent.COLLECTION_CHANGE, underlyingDataChange);
            }
            
            override protected function initializationComplete():void
            {   
            	initializeFields();
            	setDataProvider();
                this.addEventListener(ListEvent.CHANGE, change);
            	labelField = choiceDisplayAttributeName;
            }

		    protected override function change(event:ListEvent):void {
		        parentDocument.workList.filterFunction = null;
		        super.change(event);
	        }

            
		    override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void
		    {
		        super.updateDisplayList(unscaledWidth,unscaledHeight);
		        if (_data == null) {
		          	return;
		        }
		        var slideProduct:Object = parentApplication.getSlideProductList.lastResult..SlideDesign.(@idSlideDesign == _data.@idSlideDesign).parent().parent();
					if (slideProduct.@arraysPerSlide == "" || slideProduct.@arraysPerSlide == "1") {
					this.enabled = false;
				} else {
					this.enabled = true;
				}
		    }
            
	}
}