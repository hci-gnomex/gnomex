package views.renderers
{
	import flash.events.FocusEvent;
	
	import hci.flex.renderers.RendererFactory;
	import hci.flex.controls.ComboBox;
	
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
			var codeApplication:String = "";
			if (data != null) {
				codeApplication = data.@qualCodeApplication;
			}
			
			if ( parentApp != null && codeApplication != null && codeApplication != '' ){
				
				this.enabled = true;
				
				var types:XMLListCollection= new XMLListCollection(XMLList(parentApp.dictionaryManager.xml.Dictionary.(@className == 'hci.gnomex.model.BioanalyzerChipType').DictionaryEntry.(@value != '' && @isActive == 'Y' && @codeApplication == codeApplication)).copy());
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
		
		public function get parentApp():Object
		{
			return _parentApp;
		}
		
		public function set parentApp(value:Object):void
		{
			_parentApp = value;
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
		
		public static function getFactory(parentApp:Object,
		    updateData:Boolean = false):IFactory {			
			return new ComboBoxBioanalyzerChipTypeFactory({parentApp: parentApp,
										updateData: updateData});	
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
		
		return cb;	
	}			
}
