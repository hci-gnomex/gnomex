package views.renderers
{
	import flash.events.FocusEvent;
import flash.utils.Dictionary;

import hci.flex.renderers.RendererFactory;
	import hci.flex.controls.ComboBox;

import mx.collections.ArrayCollection;

import mx.collections.HierarchicalCollectionView;
	import mx.collections.IList;
	import mx.collections.Sort;
	import mx.collections.XMLListCollection;
	import mx.controls.AdvancedDataGrid;
	import mx.controls.Alert;
	import mx.controls.ComboBox;
	import mx.controls.DataGrid;
	import mx.core.IFactory;
	import mx.events.CollectionEvent;
	
	public class ComboBoxBioanalyzerChipType extends hci.flex.controls.ComboBox
	{
		private var _parentApp:Object;
		private var _selectedIdCoreFacility:String;
		private var _coreFacilityAppMap:Dictionary;
		
		public function ComboBoxBioanalyzerChipType()
		{
			super();
			labelField="@display";
			valueField="@value";
			dataField="@qualCodeBioanalyzerChipType";
			editable = false;
			isRequired=false;
			appendBlankRow = false;
			setDataProvider();
		}
		
		protected function setSelectedIndex():void {
			if ( this.data != null ) { 
				this.selectedItem = this.getBioanalyzerChipType(data);
			}
		}
		
		protected function getBioanalyzerChipType(item:Object):Object {
			var id:String = item.@qualCodeBioanalyzerChipType;
			var types:XMLList = parentApp.dictionaryManager.getEntry("hci.gnomex.model.BioanalyzerChipType", id);
			if (types.length() == 1) {
				return types[0];
			} else {
				return null;
			}
		}
		
		protected function setDataProvider():void {
			dataProvider = new XMLListCollection();
			var dp:XMLListCollection = XMLListCollection(dataProvider);
						
			if ( parentApp != null ){
				this.enabled = true;
				var types:XMLListCollection= new XMLListCollection(XMLList(parentApp.dictionaryManager.xml.Dictionary.(@className == 'hci.gnomex.model.BioanalyzerChipType').DictionaryEntry.(@value != '' && @isActive == 'Y')).copy());
				types.filterFunction = filterAppList;
				types.refresh();
				dp.addAll(types);				
			} else {
				this.enabled = false;
				selectedItem = null;
			}
			if ( dp.length == 0 ) {
				this.enabled = false;
				selectedItem = null;
			} else {
				setSelectedIndex();
			}
		}

		private function filterAppList(item:Object):Boolean {
			var retVal:Boolean = false;
			if (item.@value == "") {
				retVal = true;
			} else {
				if (item.@isActive == 'Y' && selectedIdCoreFacility != null) {
					var appCodes:ArrayCollection = ArrayCollection(coreFacilityAppMap[selectedIdCoreFacility]);
					if (appCodes != null) {
						for each(var c:String in appCodes) {
							if (item.@codeApplication.toString() == c) {
								retVal = true;
								break;
							}
						}
					}
				}
			}
			return retVal;
		}
		
		public function get parentApp():Object
		{
			return _parentApp;
		}
		
		public function set parentApp(value:Object):void
		{
			_parentApp = value;
		}

		public function get selectedIdCoreFacility():String
		{
			return _selectedIdCoreFacility;
		}

		public function set selectedIdCoreFacility(value:String):void
		{
			_selectedIdCoreFacility = value;
		}

		public function get coreFacilityAppMap():Dictionary
		{
			return _coreFacilityAppMap;
		}

		public function set coreFacilityAppMap(value:Dictionary):void
		{
			_coreFacilityAppMap = value;
		}
		
		override protected function focusOutHandler(event:FocusEvent):void
		{
			super.focusOutHandler(event);
			if( this.selectedItem!=null ){
				data.@qualCodeBioanalyzerChipType = this.selectedItem.@codeBioanalyzerChipType;
				this.setSelectedIndex();
			}  else {
				data.@qualCodeBioanalyzerChipType = '';
			}
		}
		override protected function focusInHandler(event:FocusEvent):void
		{
			this.setSelectedIndex();
		}
		
		override public function set data(value:Object):void {
			super.data = value;
			setDataProvider();
		}
		
		public static function getFactory(parentApp:Object, coreFacilityAppMap:Dictionary, selectedIdCoreFacility:String, updateData:Boolean = false):IFactory {
			return new ComboBoxBioanalyzerChipTypeFactory({parentApp: parentApp, coreFacilityAppMap: coreFacilityAppMap, selectedIdCoreFacility: selectedIdCoreFacility, updateData: updateData});
		}	
	}
}

import mx.core.IFactory;

import views.renderers.ComboBoxBioanalyzerChipType;

class ComboBoxBioanalyzerChipTypeFactory implements mx.core.IFactory {
	private var properties:Object;
	
	public function ComboBoxBioanalyzerChipTypeFactory(properties:Object) {
		this.properties = properties;
	}
	
	public function newInstance():* {
		var cb:views.renderers.ComboBoxBioanalyzerChipType = new views.renderers.ComboBoxBioanalyzerChipType();
		
		cb.parentApp = properties.parentApp;
		cb.updateData = properties.updateData;
		cb.selectedIdCoreFacility = properties.selectedIdCoreFacility;
		cb.coreFacilityAppMap = properties.coreFacilityAppMap;
		
		return cb;	
	}			
}
