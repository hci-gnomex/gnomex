package views.renderers
{
import flash.events.Event;
import flash.events.FocusEvent;
import flash.events.MouseEvent;

import hci.flex.controls.ComboBox;
import hci.flex.renderers.RendererFactory;

import mx.collections.HierarchicalCollectionView;
import mx.collections.IViewCursor;
import mx.collections.XMLListCollection;
import mx.containers.HBox;
import mx.controls.AdvancedDataGrid;
import mx.controls.Button;
import mx.controls.DataGrid;
import mx.controls.TextInput;
import mx.controls.dataGridClasses.DataGridListData;
import mx.controls.listClasses.BaseListData;
import mx.controls.listClasses.IDropInListItemRenderer;
import mx.core.IFactory;
import mx.core.UIComponent;
import mx.events.ListEvent;
import mx.managers.IFocusManagerComponent;


public class GridColumnFillButton extends HBox implements IDropInListItemRenderer, IFocusManagerComponent
{

	// Child controls
	public var edtComponent:mx.core.UIComponent;
	public var fillColumnKey:String = '';

	private var fillButton:Button;

	private var aGrid:AdvancedDataGrid = null;
	private var grid:DataGrid = null;


	// Define a property for returning the new value to the cell.
	[Bindable]
	public var value:Object;

	private var _listData:DataGridListData;


	public static function create(edtComponent:mx.core.UIComponent,
								  fillColumnKey:String):IFactory {
		return RendererFactory.create(views.renderers.GridColumnFillButton, {edtComponent:edtComponent,
			fillColumnKey: fillColumnKey});
	}


	public function GridColumnFillButton() {
		super();
		this.horizontalScrollPolicy = "off";
		this.verticalScrollPolicy = "off";
		this.setStyle("horizontalGap", 0);
	}

	private function setInsert(event:FocusEvent):void{
		edtComponent.setFocus();
	}

	override public function get data():Object {
		return super.data;
	}

	override public function set data(value:Object):void {
		if(edtComponent is hci.flex.controls.ComboBox) {
			hci.flex.controls.ComboBox(edtComponent).value = value[_listData.dataField];
			hci.flex.controls.ComboBox(edtComponent).data = value;
		}
		if(edtComponent is views.renderers.FilterComboBoxAnnotation) {
			views.renderers.FilterComboBoxAnnotation(edtComponent).value = value[_listData.dataField];
			views.renderers.FilterComboBoxAnnotation(edtComponent).data = value;
		}
		if(edtComponent is mx.controls.TextInput) {
			mx.controls.TextInput(edtComponent).text = value[_listData.dataField];
			mx.controls.TextInput(edtComponent).data = value;
			if(edtComponent is views.renderers.TextInput) {
				views.renderers.TextInput(edtComponent).dataField = _listData.dataField;
			}
			// SetFocus is needed here to ensure that the GridColumnFillButton is re-focused on alt-tabbing out and back.
			// Otherwise, the component has a tendency to become null suddenly and unexpectedly.
			edtComponent.setFocus();
		}

		// This call is needed to ensure that multiple focus change events don't confuse the component.
		// See JIRA Ticket GNOM-2230 for details.
		// Easiest to see by commenting out, navigating to component, selecting an element and holding enter down.
		// Replaces most data with null.
        itemSelected();
	}

	override protected function focusInHandler(event:FocusEvent):void
	{
		super.focusInHandler( event );
		edtComponent.setFocus();
		itemSelected();
	}

	public function get listData():BaseListData {
		return _listData;
	}

	public function set listData(value:BaseListData):void {

		_listData = DataGridListData(value);

		//hci.flex.controls.ComboBox(edtCombo).dataField = _listData.dataField;
		//hci.flex.controls.ComboBox(edtCombo).updateData = true;
		if(edtComponent is hci.flex.controls.ComboBox) {
			edtComponent.addEventListener(ListEvent.CHANGE, edtComboChanged);
			//hci.flex.controls.ComboBox(edtComponent).editable = true;
		}
		if(edtComponent is views.renderers.FilterComboBoxAnnotation) {
			edtComponent.addEventListener(ListEvent.CHANGE, edtComboChanged);
		}
		if(edtComponent is mx.controls.TextInput) {
			edtComponent.addEventListener(Event.CHANGE, txtInputChanged);
			//mx.controls.TextInput(edtComponent).editable = true;
		}




		addChild(edtComponent);
		fillButton = new Button();
		fillButton.label = "Fill";
		fillButton.setStyle("paddingLeft", 0);
		fillButton.setStyle("paddingRight", 0);
		fillButton.setStyle("fontSize", 10);
		fillButton.addEventListener(MouseEvent.CLICK, fillButtonClicked);
		if(fillColumnKey == '') {
			fillButton.toolTip="Click to propagate this value to all other rows.";
		} else {
			fillButton.toolTip="Click to propagate this value to all other rows with the same " + fillColumnKey + ".";
		}
		addChild(fillButton);

		fillButton.width = 20;

		if (_listData.owner is DataGrid) {
			grid = DataGrid(_listData.owner);
			edtComponent.width = grid.columns[_listData.columnIndex].width-20;
			edtComponent.height = grid.rowHeight;
			fillButton.height = grid.rowHeight;
		}

		if (_listData.owner is 	AdvancedDataGrid) {
			aGrid = AdvancedDataGrid(_listData.owner);
			edtComponent.width = aGrid.columns[_listData.columnIndex].width-20;
			edtComponent.height = aGrid.rowHeight;
			fillButton.height = aGrid.rowHeight;
		}

	}

	private function edtComboChanged(evt:ListEvent):void {
		itemSelected();
	}

	protected function txtInputChanged(event:Event):void {
		itemSelected();
	}

	private function fillButtonClicked(evt:MouseEvent):void {
		itemSelected();

		var dataProvider:Object = null;
		var fillColumnValue:String = "";
		if (grid != null) {
			dataProvider = grid.dataProvider;
			if(fillColumnKey.length > 0) {
				fillColumnValue = grid.selectedItem.attribute(fillColumnKey)[0];
			}
		}
		if (aGrid != null) {
			dataProvider = aGrid.dataProvider;
			if(fillColumnKey.length > 0) {
				fillColumnValue = aGrid.selectedItem.attribute(fillColumnKey)[0];
			}
		}

		if (dataProvider != null) {
			var thisField:String = _listData.dataField.substr(1);
			var thisValue:String = "";
			var idSeqRunType:String = "";
			var idNumberSequencingCycles:String = "";
			if(edtComponent is hci.flex.controls.ComboBox) {
				thisValue = String(hci.flex.controls.ComboBox(edtComponent).value);
			}
			if(edtComponent is views.renderers.FilterComboBoxAnnotation) {
				thisValue = String(views.renderers.FilterComboBoxAnnotation(edtComponent).value);
			}
			if(edtComponent is mx.controls.TextInput) {
				thisValue = mx.controls.TextInput(edtComponent).text;
			}
			// We need the idSeqRunType and numbersequencingcycles to be updated as well or else there is a problem with multiplex grouping
			if(edtComponent is views.renderers.ComboBoxNumberSequencingCyclesAllowed){
				idSeqRunType = views.renderers.ComboBoxNumberSequencingCyclesAllowed(edtComponent).data.@idSeqRunType;
				idNumberSequencingCycles = views.renderers.ComboBoxNumberSequencingCyclesAllowed(edtComponent).data.@idNumberSequencingCycles;
			}

			if(dataProvider is XMLListCollection) {
				for each (var node : XML in dataProvider){
					if((fillColumnKey.length == 0) || (node.attribute(fillColumnKey)[0] == fillColumnValue)) {
						node.attribute(thisField)[0] = thisValue;
						node.attribute('isDirty')[0] = 'Y';
						// This check is for when we are dealing with a Num Sequencing Cycles allowed combo box fill operation
						if(idSeqRunType != "" && idNumberSequencingCycles != ""){
							node.attribute("idSeqRunType")[0] = idSeqRunType;
							node.attribute("idNumberSequencingCycles")[0] = idNumberSequencingCycles;
						}
					}
				}
			}

			if(dataProvider is HierarchicalCollectionView) {

				var cursor:IViewCursor=dataProvider.createCursor();

				while(!cursor.afterLast)
				{
					var hNode:XML = XML(cursor.current);
					if((fillColumnKey.length == 0) ||
							(hNode.attribute(fillColumnKey)[0] == fillColumnValue)) {
						hNode.attribute(thisField)[0] = thisValue;
						hNode.attribute('isDirty')[0] = 'Y';
					}
					cursor.moveNext();
				}
			}


			if (grid != null) {
				grid.invalidateList();
			}
			if (aGrid != null) {
				aGrid.invalidateList();
			}

		}

	}

	private function itemSelected():void {

		if(edtComponent is hci.flex.controls.ComboBox) {
			value = hci.flex.controls.ComboBox(edtComponent).value;
		}
		if(edtComponent is views.renderers.FilterComboBoxAnnotation) {
			value = views.renderers.FilterComboBoxAnnotation(edtComponent).value;
		}
		if(edtComponent is mx.controls.TextInput) {
			value = mx.controls.TextInput(edtComponent).text;
		}

	}

	override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void
	{
		super.updateDisplayList(unscaledWidth,unscaledHeight);
	}


}
}
