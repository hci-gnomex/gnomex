package views.renderers
{
	import flash.events.FocusEvent;
	import flash.events.MouseEvent;
	
	import hci.flex.controls.ComboBox;
	
	import mx.collections.IViewCursor;
	import mx.containers.HBox;
	import mx.controls.Button;
	import mx.controls.dataGridClasses.DataGridColumn;
	import mx.controls.dataGridClasses.DataGridListData;
	import mx.controls.listClasses.BaseListData;
	import mx.controls.listClasses.IDropInListItemRenderer;
	import mx.effects.easing.Back;
	import mx.events.ListEvent;
	import mx.managers.IFocusManagerComponent;
	
	import views.util.DataGridAlternatingGroup;
	import views.util.FillButtonClickEvent;

	
	public class FillButtonComboBox extends HBox implements IDropInListItemRenderer, IFocusManagerComponent
	{
		
		// Child controls
		private var edtCombo:hci.flex.controls.ComboBox;
		private var fillButton:Button;
		
		// Define a property for returning the new value to the cell.
		[Bindable]
		public var value:Object;

		private var _listData:DataGridListData;
		private var grid:DataGridAlternatingGroup;		

		public function FillButtonComboBox() {
			super();
			this.horizontalScrollPolicy = "off";
			this.verticalScrollPolicy = "off";
			this.setStyle("horizontalGap", 0);
		}
				
		private function setInsert(event:FocusEvent):void{
			edtCombo.setFocus();
		}			
		
		override public function get data():Object {
			return super.data;			
		}
		
		override public function set data(value:Object):void {
			edtCombo.value = value[_listData.dataField];				
			edtCombo.data = value;
		}
		
		override protected function focusInHandler(event:FocusEvent):void
		{
			super.focusInHandler( event );
			edtCombo.setFocus();
			itemSelected();
		}
				
		public function get listData():BaseListData {
			return _listData;
		}
		
		public function set listData(value:BaseListData):void {
			
			_listData = DataGridListData(value);
			
			// Just in case instantiated without proper datafield
			edtCombo = new hci.flex.controls.ComboBox();
			
			var dictionaryClassName:String = "";
			if(_listData.dataField == "@qualCodeBioanalyzerChipType") {
				dictionaryClassName = "hci.gnomex.model.BioanalyzerChipType";
			}
			if(_listData.dataField == "@idLabelingProtocol") {
				dictionaryClassName = "hci.gnomex.model.LabelingProtocol";
			}
			if(_listData.dataField == "@codeLabelingReactionSize") {
				dictionaryClassName = "hci.gnomex.model.LabelingReactionSize";
			}
			if(_listData.dataField == "@idSeqLibProtocol") {
				edtCombo = new views.renderers.ComboBox();
				dictionaryClassName = "hci.gnomex.model.SeqLibProtocol";
			}
			if(_listData.dataField == "@seqPrepQualCodeBioanalyzerChipType") {
				edtCombo = new views.renderers.ComboBox();
				dictionaryClassName = "hci.gnomex.model.BioanalyzerChipType";
			}
			
			
			if(_listData.dataField == "@qualStatus" || _listData.dataField == "@labelingStatus"
				|| _listData.dataField == "@seqPrepStatus") {
				edtCombo = new views.renderers.ComboBoxWorkflowStatus();
				views.renderers.ComboBoxWorkflowStatus(edtCombo).canChangeByAdminOnly = true;
			}			
			
			hci.flex.controls.ComboBox(edtCombo).dataField = _listData.dataField;
			hci.flex.controls.ComboBox(edtCombo).updateData = true;	
			if(dictionaryClassName.length > 0) {
				edtCombo.dataProvider = parentApplication.dictionaryManager.xml.Dictionary.(@className==dictionaryClassName).DictionaryEntry;

			}			
			edtCombo.addEventListener(ListEvent.CHANGE, edtComboChanged);

			
			addChild(edtCombo);
			fillButton = new Button();
			fillButton.label = "Fill";
			fillButton.setStyle("paddingLeft", 0);
			fillButton.setStyle("paddingRight", 0);
			fillButton.setStyle("fontSize", 10);
			fillButton.addEventListener(MouseEvent.CLICK, fillButtonClicked);
			fillButton.toolTip="Click to propagate last entered value to all other fields in this request.";		
			addChild(fillButton);			
			
			grid = DataGridAlternatingGroup(_listData.owner);
			edtCombo.width = grid.columns[_listData.columnIndex].width-20;
			edtCombo.height = grid.rowHeight;
			fillButton.width = 20;
			fillButton.height = grid.rowHeight;
		}
		
		private function edtComboChanged(evt:ListEvent):void {
			itemSelected();
		}
		
		private function fillButtonClicked(evt:MouseEvent):void {
			itemSelected();
			fillButton.removeEventListener(MouseEvent.CLICK, fillButtonClicked);
			dispatchEvent(new FillButtonClickEvent(_listData, String(edtCombo.value)));
		}
		
		private function itemSelected():void {
			if(edtCombo.selectedItem != null) {
				value = edtCombo.value;				
			}
		}
		
	}
}