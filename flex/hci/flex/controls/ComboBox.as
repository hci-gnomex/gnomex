package hci.flex.controls
{
	import hci.flex.renderers.RendererFactory;
	
	import mx.collections.ArrayCollection;
	import mx.collections.XMLListCollection;
	import mx.controls.ComboBox;
	import mx.core.IFactory;
	import mx.events.ListEvent;

	
	/**
	 * Standalone Usage:
	 * 		set dataProvider to XML
	 * 		set labelField to label attribute in XML
	 * 		set valueField to value attribute in XML
	 * 		set value for combobox (use databinding)
	 * 
	 * Grid Usage:
	 * 		set GRID's data to XML (use databinding)
	 * 		column.itemRenderer = ComboBox.getFactory(dataProvider, labelField, valueField, dataValueField)
	 * 		dataProvider is XML for dropdown, labelField is label attribute in XML, valueField is value attribute in XML
	 * 		dataValueField is attribute in GRID's XML containing the value
	 */
	
	public class ComboBox extends mx.controls.ComboBox
	{
		public var missingRequiredFieldBackground:uint = RendererFactory.DEFAULT_MISSING_REQUIRED_FIELD_BACKGROUND;
		public var valueField:String;
		public var updateData:Boolean = false;
		public var isRequired:Boolean = false;
		public var appendBlankRow:Boolean = false;
		
		private var _unfilteredDataProvider:Object;
		public var filterAttribute:String;
		private var _filterValue:String; 
		
		//For use in list
		public var dataField:String;
		
		public function ComboBox()
		{
			super();

			//Defaults - Change as needed
			labelField = "@display";
			valueField = "@value";
		}
		
		public function set value(val:Object):void {
			filterDataProvider(val);
			
			if ( val != null ) {
				for (var i : int = 0; i < dataProvider.length; i++) {
	            	var item:Object = dataProvider[i];
	            	if(item[valueField] == val) {
	                      this.selectedIndex = i;
	                      
	                      break;
	                 }
				}
			}
		}
		
		override public function get value():Object {			
			if (this.selectedItem != null) {
				return this.selectedItem[valueField];
			}
			else {
				return null;
			}
		}
		
        override public function set data(value:Object):void {
        	super.data = value;
        	this.value = value[dataField];
        }
        
        override protected function initializationComplete():void { 
        	super.initializationComplete();
        	this.addEventListener(ListEvent.CHANGE, change);
        }
        
        protected function change(event:ListEvent):void {
			if (updateData) {
				assignData();
			}
        }   
        
        protected function assignData():void {
        	if (super.data) {
       			super.data[dataField] = this.selectedItem[this.valueField];
       		}
        }            
       
       	override public function set dataProvider(dp:Object):void {          		    		
       		if (dp is XMLList) {
       			var coll:XMLListCollection = new XMLListCollection(XMLList(dp).copy());
       			if (appendBlankRow) {
       				coll.addItemAt(new XML("<BlankOption " + labelField.substr(1) + "='' " + valueField.substr(1) + "=''/>"), 0);
       			}       			
       			this._unfilteredDataProvider = coll;
       		}	
       		else if (dp is XMLListCollection) {
       			var theCollection:XMLListCollection = new XMLListCollection(XMLListCollection(dp).copy());
       			if (appendBlankRow) {
       				theCollection.addItemAt(new XML("<BlankOption " + labelField.substr(1) + "='' " + valueField.substr(1) + "=''/>"), 0);
       			}       			
       			this._unfilteredDataProvider = theCollection;
       		}	       		
       		else if (dp is ArrayCollection) {
       			if (appendBlankRow) {
       				ArrayCollection(dp).addItemAt(new XML("<BlankOption " + labelField + "='' " + valueField + "=''/>"), 0);
       			}
       			this._unfilteredDataProvider = dp;   		
       		}
       		else {
       			this._unfilteredDataProvider = dp;
       		}
       		
       		filterDataProvider(null);
       	}
       	
       	
       	public function get filterValue():String {
       		return this._filterValue;
       	}
       	
       	public function set filterValue(filterValue:String):void {
       		this._filterValue = filterValue;
       		filterDataProvider(this.value);
       	}
       	
       	
       	private function filterDataProvider(value:Object):void { 
       		if (this._unfilteredDataProvider is XMLListCollection) {
       			var filteredDataProvider:XMLListCollection = new XMLListCollection((XMLListCollection(this._unfilteredDataProvider).copy()));
       			
       			var i:int = 0;
				while (i < filteredDataProvider.length) {
	            	var item:Object = filteredDataProvider[i];
	            	
	            	//Optional filtering on final XML node of DataProvider
	            	if (filterAttribute && item[valueField] != "" && item[filterAttribute] != filterValue) {
	            		filteredDataProvider.removeItemAt(i);
	            		i--;
	            	}	 
	            	//@isActive filtering           	
	            	else if(item.@isActive && item.@isActive == "N") {
	            		if (value == null || item[valueField] != value) {
	            			filteredDataProvider.removeItemAt(i);
	            			//The removal happens instantly; everything else is moved up, so we continue with the same index
	            			i--;
	            		} 		
	            	}	            	
	            	
	            	i++;       			
    			}    			
       			
       			super.dataProvider = filteredDataProvider;
       		}
       		else {
       			super.dataProvider = this._unfilteredDataProvider;	
       		}       		
       	}
       	
       	override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
	          super.updateDisplayList(unscaledWidth,unscaledHeight);
	          if (super.data == null) {
	          	return;
	          }
	          	          		          
	    }
        
		public static function getFactory(
			dataProvider:Object, 
			labelField:String, 
			valueField:String, 
			dataField:String, 
			updateData:Boolean = false,
			isRequired:Boolean = false,
			appendBlankRow:Boolean = false,
			missingRequiredFieldBackground:uint = 0xFFFFB9):IFactory {			
				return new ComboBoxFactory({dataProvider: dataProvider, 
													labelField: labelField,  
													valueField: valueField,
													dataField: dataField,
													updateData: updateData,
													isRequired: isRequired,
													appendBlankRow: appendBlankRow,
													missingRequiredFieldBackground: missingRequiredFieldBackground});	
		}	
			
	}
	
}

import hci.flex.controls.ComboBox;

import mx.core.IFactory;

class ComboBoxFactory implements mx.core.IFactory {
	private var properties:Object;
	
	public function ComboBoxFactory(properties:Object) {
		this.properties = properties;
	}
	
	public function newInstance():* {
		var cb:hci.flex.controls.ComboBox = new hci.flex.controls.ComboBox();
		
		cb.labelField = properties.labelField;
		cb.valueField = properties.valueField;
		cb.dataField = properties.dataField;
		cb.isRequired = properties.isRequired;
		cb.updateData = properties.updateData;
		cb.appendBlankRow = properties.appendBlankRow;
		cb.missingRequiredFieldBackground = properties.missingRequiredFieldBackground;
		cb.dataProvider = properties.dataProvider;
		
		return cb;	
	}			
}
