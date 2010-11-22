package views.renderers

{
	import flash.display.DisplayObject;
	import flash.display.Graphics;
	
	import hci.flex.renderers.RendererFactory;
	
	import mx.binding.utils.*;
	import mx.collections.ArrayCollection;
	import mx.containers.Box;
	import mx.controls.dataGridClasses.DataGridListData;
	import mx.controls.listClasses.BaseListData;
	import mx.controls.listClasses.IDropInListItemRenderer;
	import mx.core.IDataRenderer;
	import mx.core.mx_internal;
	import mx.events.FlexEvent;
	import mx.controls.CheckBox;
	use namespace mx_internal;
	

	public class CheckBoxSampleLabel extends Box implements  IDataRenderer,  IDropInListItemRenderer
	{
		
		
		private var opt:ArrayCollection = new ArrayCollection([
	        {data:"cy3", label:"cy3", fillColors:[0x23b12d, 0x23b12d, 0x23b12d, 0x23b12d], color:"green", borderColor:"green", fillAlphas:[1.0, 0.15, 0.15, 0.15]},
	        {data:"cy5", label:"cy5", fillColors:[0xda2020, 0xda2020, 0xda2020, 0xda2020], color:0xFF0000, borderColor:0xFF0000, fillAlphas:[1.0, 0.15, 0.15, 0.15]}]);
	         
		private var cb_cy3:CheckBox = null;
		private var cb_cy5:CheckBox = null;

		public function CheckBoxSampleLabel() {
			super();
			verticalScrollPolicy = "off";
			horizontalScrollPolicy = "off";

			direction = "horizontal";
			setStyle("paddingLeft", "5");
			
			
			
			cb_cy3 = new CheckBox();
			setCheckBoxStyle(opt[0], cb_cy3);
			BindingUtils.bindSetter(onSelectCy3, cb_cy3, "selected");				
			addChild(cb_cy3);

			cb_cy5 = new CheckBox();
			setCheckBoxStyle(opt[1], cb_cy5);
			BindingUtils.bindSetter(onSelectCy5, cb_cy5, "selected");				
			addChild(cb_cy5);
			
		}
		
		private function setCheckBoxStyle(option:Object, cb:CheckBox):void {
			cb.label = option.label;
			//cb.value = option.data;
			cb.setStyle("fillColors", option.fillColors);
			cb.setStyle("color", option.color);
			cb.setStyle("borderColor", option.borderColor);
			cb.setStyle("fillAlphas", option.fillAlphas);
			cb.setStyle("horizontalGap", 0);
		} 
		
		private function onSelectCy3(isSelected:String):void {			
       		cb_cy3.setStyle("fontWeight", isSelected == "true" ? "bold" : "normal");
       		cb_cy3.setStyle("color", isSelected == "true" ? "green" : "grey");
       		if (isSelected == "true") {
	       		cb_cy3.setStyle("fillAlphas", [1.0, 0.7, 0.7, 0.7]);
	       		cb_cy5.selected = false;       			
       		} else {
	       		cb_cy3.setStyle("fillAlphas", [.4, 0.2, 0.2, 0.2]);       			       			
       		}
       		
        }

		private function onSelectCy5(isSelected:String):void {
       		cb_cy5.setStyle("fontWeight", isSelected == "true" ? "bold" : "normal");               
       		cb_cy5.setStyle("color", isSelected == "true" ? "red" : "grey");
       		if (isSelected == "true") {
	       		cb_cy5.setStyle("fillAlphas", [1.0, 0.7, 0.7, 0.7]);   
	       		cb_cy3.selected = false;    			
       		} else {
	       		cb_cy5.setStyle("fillAlphas", [.4, 0.2, 0.2, 0.2]);       			       			
       		}
        }

		override public function addChild(child:DisplayObject):DisplayObject {
			return super.addChild(child);
		}

		override public function set data(item:Object):void	{
			super.data = item;
			if( item != null ) {
				if (item[DataGridListData(listData).dataField] == "cy3") {
					cb_cy3.selected = true;
					cb_cy5.selected = false;
				} else if (item[DataGridListData(listData).dataField] == "cy5") {
					cb_cy3.selected = false;
					cb_cy5.selected = true;
				} else {
					cb_cy3.selected = false;
					cb_cy5.selected = false;
				}
			} else {
				cb_cy3.selected = false;
				cb_cy5.selected = false;
			}
		}
 
		[Bindable("valueCommit")]
		[Bindable("change")]
		public function get text():Object {
			return value;
		}

		public function set text(v:Object) : void { 
			value = v;
		}

		[Bindable("valueCommit")]
		[Bindable("change")]
		[Inspectable(category="General")]
		public function get value():Object {
			if (cb_cy3.selected) {
				return "cy3";
			} else if (cb_cy5.selected) {
				return "cy5";
			} else {
				return "";
			}
		}
		public function set value(v:Object) : void {
			if (listData) {
				if (cb_cy3.selected) {
					data[DataGridListData(listData).dataField] = "cy3";
				} else if (cb_cy5.selected) {
					data[DataGridListData(listData).dataField] = "cy5";
				} else {
					data[DataGridListData(listData).dataField] = "";
				}
				setMandatoryBackground();
			}
			dispatchEvent(new FlexEvent(FlexEvent.VALUE_COMMIT));
		}
	
		private var _listData:BaseListData=null;
		public function get listData():BaseListData	{
			return _listData;
		}
		public function set listData(value:BaseListData):void	{
			_listData = value;
		}

        override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void{
            super.updateDisplayList(unscaledWidth, unscaledHeight);
            
            setMandatoryBackground(); 
        }  
        
        private function setMandatoryBackground():void {
          	var g:Graphics = this.graphics;
          	g.clear();
             		   
          	if (data != null && (!data.hasOwnProperty("@label") || data.@label == '')) {
	        	g.beginFill(RendererFactory.DEFAULT_MISSING_REQUIRED_FIELD_BACKGROUND);
	        	g.lineStyle(RendererFactory.DEFAULT_MISSING_REQUIRED_FIELD_BORDER_THICKNESS,
	            	        RendererFactory.DEFAULT_MISSING_REQUIRED_FIELD_BORDER);          	
	            g.drawRect(0,0,unscaledWidth,unscaledHeight);
    	        g.endFill();	
            }        	
        }

	}
	
}
