// RadioButtonGroupBox.as
package views.renderers

{
	import flash.display.DisplayObject;
	import flash.display.Graphics;
	import flash.events.Event;
	
	import hci.flex.renderers.RendererFactory;
	
	import mx.binding.utils.*;
	import mx.collections.ArrayCollection;
	import mx.containers.Box;
	import mx.controls.RadioButton;
	import mx.controls.RadioButtonGroup;
	import mx.controls.dataGridClasses.DataGridListData;
	import mx.controls.listClasses.BaseListData;
	import mx.controls.listClasses.IDropInListItemRenderer;
	import mx.core.IDataRenderer;
	import mx.core.mx_internal;
	import mx.events.FlexEvent;
	use namespace mx_internal;
	

	public class RadioButtonGroupSampleLabel extends Box implements  IDataRenderer,  IDropInListItemRenderer
	{
		private var group:RadioButtonGroup=null ;
		
		private var opt:ArrayCollection = new ArrayCollection([
	        {data:"cy3", label:"cy3", fillColors:[0x23b12d, 0x23b12d, 0x23b12d, 0x23b12d], color:"green", borderColor:"green", fillAlphas:[1.0, 0.15, 0.15, 0.15]},
	        {data:"cy5", label:"cy5", fillColors:[0xda2020, 0xda2020, 0xda2020, 0xda2020], color:0xFF0000, borderColor:0xFF0000, fillAlphas:[1.0, 0.15, 0.15, 0.15]}]);
	         
		private var rb_cy3:RadioButton = null;
		private var rb_cy5:RadioButton = null;

		public function RadioButtonGroupSampleLabel() {
			super();
			verticalScrollPolicy = "off";
			horizontalScrollPolicy = "off";

			direction = "horizontal";
			setStyle("paddingLeft", "5");
			
			group = new RadioButtonGroup();
			group.addEventListener(Event.CHANGE,
				function (event:Event):void { 
					value = event.target.selectedValue;
					dispatchEvent(event);
				}
			);
			
			rb_cy3 = new RadioButton();
			setRadioButtonStyle(opt[0], rb_cy3);
			BindingUtils.bindSetter(onSelectCy3, rb_cy3, "selected");				
			addChild(rb_cy3);

			rb_cy5 = new RadioButton();
			setRadioButtonStyle(opt[1], rb_cy5);
			BindingUtils.bindSetter(onSelectCy5, rb_cy5, "selected");				
			addChild(rb_cy5);
			
		}
		
		private function setRadioButtonStyle(option:Object, rb:RadioButton):void {
			rb.label = option.label;
			rb.value = option.data;
			rb.setStyle("fillColors", option.fillColors);
			rb.setStyle("color", option.color);
			rb.setStyle("borderColor", option.borderColor);
			rb.setStyle("fillAlphas", option.fillAlphas);
			rb.setStyle("horizontalGap", 0);
		} 
		
		private function onSelectCy3(isSelected:String):void {			
       		rb_cy3.setStyle("fontWeight", isSelected == "true" ? "bold" : "normal");
       		rb_cy3.setStyle("color", isSelected == "true" ? "green" : "grey");
       		if (isSelected == "true") {
	       		rb_cy3.setStyle("fillAlphas", [1.0, 0.7, 0.7, 0.7]);       			
       		} else {
	       		rb_cy3.setStyle("fillAlphas", [.4, 0.2, 0.2, 0.2]);       			       			
       		}
       		
        }

		private function onSelectCy5(isSelected:String):void {
       		rb_cy5.setStyle("fontWeight", isSelected == "true" ? "bold" : "normal");               
       		rb_cy5.setStyle("color", isSelected == "true" ? "red" : "grey");
       		if (isSelected == "true") {
	       		rb_cy5.setStyle("fillAlphas", [1.0, 0.7, 0.7, 0.7]);       			
       		} else {
	       		rb_cy5.setStyle("fillAlphas", [.4, 0.2, 0.2, 0.2]);       			       			
       		}
        }

		override public function addChild(child:DisplayObject):DisplayObject {
			if (child is RadioButton) {
				(child as RadioButton).group = group;
				group.addInstance(child as RadioButton);
			}
			return super.addChild(child);
		}

		override public function set data(item:Object):void	{
			super.data = item;
			if( item != null ) {
				group.selectedValue = item[DataGridListData(listData).dataField];
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
			return group.selectedValue;
		}
		public function set value(v:Object) : void {
			group.selectedValue = v;
			if (listData) {
				data[DataGridListData(listData).dataField] = group.selectedValue;
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
             
          	var g:Graphics = this.graphics;
          	g.clear();
          
             if (data == null) {
             	return;
             } 
             		  
          	if (!data.hasOwnProperty("@label") || data.@label == '') {
	        	g.beginFill(RendererFactory.DEFAULT_MISSING_REQUIRED_FIELD_BACKGROUND);
	        	g.lineStyle(RendererFactory.DEFAULT_MISSING_REQUIRED_FIELD_BORDER_THICKNESS,
	            	        RendererFactory.DEFAULT_MISSING_REQUIRED_FIELD_BORDER);          	
	            g.drawRect(0,0,unscaledWidth,unscaledHeight);
    	        g.endFill();	
    	        

      
            }


        }  

	}
	
}
