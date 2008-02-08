package views
{
	import mx.controls.ComboBox;
	import flash.events.MouseEvent;
	import mx.collections.XMLListCollection;
	import flash.events.Event;
	import mx.events.ListEvent;
	
	public class ComboBoxArrayCoordinate extends ComboBox
	{
		[Bindable]
		private var coordinateDictionary: Array = [ 
		      {label:"", data:""}, 
		      {label:"1_1", data:"1_1"}, 
		      {label:"1_2", data:"1_2"}, 
		      {label:"1_3", data:"1_3"},
		      {label:"1_4", data:"1_4"},
		      {label:"2_1", data:"2_1"}, 
		      {label:"2_2", data:"2_2"}, 
		      {label:"2_3", data:"2_3"},
		      {label:"2_4", data:"2_4"},
		     ];
		
		private var _data:Object;
	
		override public function set data(o:Object):void {
		    _data = o;
 
				var slideProduct:Object = parentApplication.getSlideProductList.lastResult..SlideDesign.(@idSlideDesign == _data.@idSlideDesign).parent().parent();
				if (slideProduct.@arraysPerSlide == "" || slideProduct.@arraysPerSlide == "1") {
					this.enabled = false;
				}

		    this.selectedIndex = 0;
		    for(var i:Number = 0; i < this.dataProvider.length; i++) {
		    	if (dataProvider[i].data == o.@arrayCoordinate) {
		              this.selectedIndex = i;
		              break;
		         }
		    }
		}
		
		[Bindable]           
		override public function get data():Object 
		{
			return _data;
		}
		
		override protected function initializationComplete():void {   
			this.addEventListener(ListEvent.CHANGE, change);
			dataProvider = coordinateDictionary;
		}
		
		private function change(event:ListEvent):void {
			_data.data = this.selectedItem.data;
		}
	        
	}
}