package views.renderers
{
	import mx.controls.ComboBox;
	import flash.events.MouseEvent;
	import mx.collections.XMLListCollection;
	import flash.events.Event;
	import mx.events.ListEvent;
	import flash.display.Graphics;
	import mx.events.CollectionEvent;
	import mx.controls.DataGrid;
	import mx.collections.IList;

	public class ComboBoxBase extends ComboBox
	{
		    protected var _data:Object;
		    protected var dictionaryClassName:String;
		    protected var cellAttributeName:String;
		    protected var choiceDisplayAttributeName:String;
		    protected var choiceValueAttributeName:String;
		    protected var showMissingDataBackground:Boolean = true;
		    
		    protected  function initializeFields():void {
		    }
		    		
		    override public function set data(o:Object):void
            {
                _data = o; 
				selectItem();
            }
            
            [Bindable]           
            override public function get data():Object 
            {
            	return _data;
            }
            
            protected function selectItem():void {
                this.selectedIndex = -1;
                if (_data == null || _data[cellAttributeName] == null) {
                	return;
                }
                for(var i:Number = 0; i < this.dataProvider.length; i++) {
                	var item:Object = dataProvider[i];
                	if(item[choiceValueAttributeName] == _data[cellAttributeName]) {
                          this.selectedIndex = i;
                          break;
                     }
                }
            	
            }
            
            protected function setDataProvider():void {
				dataProvider = new XMLListCollection(parentApplication.manageDictionaries.lastResult.Dictionary.(@className == dictionaryClassName).DictionaryEntry);
				
				// This will detect changes to underlying data anc cause combobox to be selected based on value.
				IList(DataGrid(owner).dataProvider).addEventListener(CollectionEvent.COLLECTION_CHANGE, underlyingDataChange);
            }
            
           

            override public function get value():Object {
             	if (_data != null) {
            		return _data[cellAttributeName];
            	} else {
            		return null;
            	} 
            	
            }
            


            override protected function initializationComplete():void
            {   
            	initializeFields();
            	setDataProvider();
                this.addEventListener(ListEvent.CHANGE, change);
            	labelField = choiceDisplayAttributeName;
            	
            }
            
            
            protected function change(event:ListEvent):void {
            	_data[cellAttributeName] = this.selectedItem[this.choiceValueAttributeName];
            	_data.@isDirty = "Y";
            }

            protected function underlyingDataChange(event:Event):void {
            	selectItem();
            }


            
		    override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void
		    {
		          super.updateDisplayList(unscaledWidth,unscaledHeight);
		          if (_data == null) {
		          	return;
		          }
		          
		          if (showMissingDataBackground) {
				      var colors:Array = new Array();
			          if(_data[cellAttributeName] == '') {
				          colors.push(parentApplication.REQUIRED_FIELD_BACKGROUND);		          
				          colors.push("0xCCCCCC");		          
				          this.setStyle("fillColors", colors);
			          	
			          } else {
				          colors.push("0xFFFFFF");
				          colors.push("0xCCCCCC");		          
				          this.setStyle("fillColors", colors);
			          }
		          	
		          }
		          
		    }
	}

}