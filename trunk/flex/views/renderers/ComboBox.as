package views.renderers
{
	import flash.events.Event;
	
	import mx.collections.IList;
	import mx.collections.XMLListCollection;
	import mx.controls.Alert;
	import mx.controls.ComboBox;
	import mx.controls.DataGrid;
	import mx.events.CollectionEvent;
	import mx.events.ListEvent;

	public class ComboBox extends mx.controls.ComboBox
	{ 
		    protected var _data:Object;
		    protected var _dictionary:String;
		    protected var _dataField:String;
		    protected var _dictionaryDisplayField:String = "@display";
		    protected var _dictionaryValueField:String   = "@value";
		    protected var _securityDataField:String;
		    protected var _isRequired:Boolean = true;
		    protected var _canChangeByAdminOnly:Boolean = false;
		    
		    protected  function initializeFields():void {
		    }
		    
		    public function set dictionary(dictionary:String):void {
		    	this._dictionary = dictionary;
		    }
		    
		    public function set dictionaryDisplayField(dictionaryDisplayField:String):void {
		    	this._dictionaryDisplayField = dictionaryDisplayField;
		    }

		    public function set dictionaryValueField(dictionaryValueField:String):void {
		    	this._dictionaryValueField = dictionaryValueField;
		    }

		    public function set dataField(dataField:String):void {
		    	this._dataField = dataField;	
		    }
		    
		    public function set securityDataField(securityDataField:String):void {
		    	_securityDataField = securityDataField;
		    }
		    
		    public function set isRequired(isRequired:Boolean):void {
		    	_isRequired = isRequired;
		    }
		    
		    public function set canChangeByAdminOnly(canChangeByAdminOnly:Boolean):void {
		    	_canChangeByAdminOnly = canChangeByAdminOnly;
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
                if (_data == null || _data[_dataField] == null) {
                	return;
                }
                for(var i:Number = 0; i < this.dataProvider.length; i++) {
                	var item:Object = dataProvider[i];
                	if(item[_dictionaryValueField] == _data[_dataField]) {
                          this.selectedIndex = i;
                          break;
                     }
                }
            	
            }
            
            protected function setDataProvider():void {
				dataProvider = new XMLListCollection(parentApplication.manageDictionaries.lastResult.Dictionary.(@className == _dictionary).DictionaryEntry);
				
				// This will detect changes to underlying data anc cause combobox to be selected based on value.
				IList(DataGrid(owner).dataProvider).addEventListener(CollectionEvent.COLLECTION_CHANGE, underlyingDataChange);
            }
            
           

            override public function get value():Object {
             	if (_data != null) {
            		return _data[_dataField];
            	} else {
            		return null;
            	} 
            	
            }
            


            override protected function initializationComplete():void
            {   
            	initializeFields();
            	setDataProvider();
                this.addEventListener(ListEvent.CHANGE, change);
            	labelField = _dictionaryDisplayField;
            	
            }
            
            
            protected function change(event:ListEvent):void {
            	if (_securityDataField == null) {
            		if (_canChangeByAdminOnly) {
            			if (parentApplication.hasPermission("canWriteAnyObject")) {
            		 		assignData();	
            		 	} else {
			     			selectItem();
			     			Alert.show("This field cannot be changed.  Please ask Microarray Core facility for assistance.");
            		 	}
		     		} else {
	            		assignData();            		
            		}
            	} else {
			     	if (_data[_securityDataField] == "Y" || parentApplication.hasPermission("canWriteAnyObject")) {
			     		assignData();
		     		} else {
		     			selectItem();
		     			Alert.show("This field cannot be changed.  Please ask Microarray Core facility for assistance.");
		     		}
            	}
            }
            
            protected function assignData():void {
            	_data[_dataField] = this.selectedItem[this._dictionaryValueField];
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
		          
		          if (_isRequired) {
				      var colors:Array = new Array();
			          if(_data[_dataField] == '') {
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