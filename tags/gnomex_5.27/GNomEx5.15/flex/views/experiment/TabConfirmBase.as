package views.experiment
{
	
	import hci.flex.controls.ComboBox;
	import hci.flex.controls.DropdownLabel;
	
	import mx.collections.Grouping;
	import mx.collections.GroupingCollection;
	import mx.collections.GroupingField;
	import mx.collections.XMLListCollection;
	import mx.containers.Canvas;
	import mx.controls.AdvancedDataGrid;
	import mx.controls.DataGrid;
	import mx.controls.advancedDataGridClasses.AdvancedDataGridColumn;
	import mx.controls.dataGridClasses.DataGridColumn;
	
	import views.renderers.CheckBoxRenderer;
	import views.renderers.MultiselectRenderer;
	import views.renderers.URLRenderer;
	import views.util.AnnotationDataGridColumn;
	
	[Bindable]
	public class TabConfirmBase extends Canvas
	{
		
		public static function getConfirmTab(existingTab:TabConfirmBase, requestCategory:Object):TabConfirmBase {
			if (requestCategory.@codeRequestCategory == 'ISCAN') {
				if (existingTab is TabConfirmIScan) {
					return existingTab;
				} else {
					return new TabConfirmIScan();
				}
			} else {
				if (existingTab is TabConfirmView) {
					return existingTab;
				} else {
					return new TabConfirmView();
				}
			}
		}
		
		public function TabConfirmBase()
		{
			super();
		}
		
		protected function init():void {
			if (parentDocument != null) {
				setupView();
				showHideColumns();
				rebuildSamplesGrid();
			}
		} 
		
		protected function rebuildSamplesGrid():void {
			var grid:DataGrid = getSamplesGrid();
			var columns:Array = grid.columns;
			var newColumns:Array = new Array();
			if (grid != null) {
				var annotationsAdded:Boolean = false;
				for (var x:Number = 0; x < columns.length; x++) {
					var dc:DataGridColumn = grid.columns[x];
					if (dc.dataField == "@description") {
						// Push the annotations before the description property.
						addAnnotationProperties(columns, newColumns);
						annotationsAdded = true;
					}
					if (dc.dataField == null || dc.dataField.substr(0, 6) != "@ANNOT") {
						newColumns.push(dc);
					}
				}
				if ( !annotationsAdded ) {
					addAnnotationProperties(columns, newColumns);
				}
			}
			grid.columns = newColumns;
			grid.validateNow();
		}
		
		
		private function addAnnotationProperties(columns:Array, newColumns:Array):void {
			// Add real annotations.
			for each(var node:XML in parentDocument.propertyEntries) {
				addAnnotationProperty(columns, newColumns, node);
			}
		}
		
		private function addAnnotationProperty(columns:Array, newColumns:Array, node:XML):void {
			var dc:AnnotationDataGridColumn = findAnnotationGridColumn(columns, node);
			if (dc == null) {
				dc = buildNewAnnotationGridColumn(node);
			} else {
				refreshAnnotationGridColumn(node, dc);
			}
			newColumns.push(dc);
		}
		
		private function findAnnotationGridColumn(columns:Array, propertyNode:XML):AnnotationDataGridColumn {
			var dcFound:AnnotationDataGridColumn = null;
			for each (var dc:DataGridColumn in columns) {
				if (dc is AnnotationDataGridColumn && 
					(dc.dataField == "@ANNOT" + propertyNode.@idProperty)) {
					dcFound = dc as AnnotationDataGridColumn;
					break;
				}
			}
			
			return dcFound;
		}
		
		private function buildNewAnnotationGridColumn(propertyNode:XML):AnnotationDataGridColumn {
			var fieldName:String = "@ANNOT" + propertyNode.@idProperty;
			var newCol:AnnotationDataGridColumn = new AnnotationDataGridColumn(fieldName);
			refreshAnnotationGridColumn(propertyNode, newCol);
			return newCol;
		}
		
		private function refreshAnnotationGridColumn(propertyNode:XML, dc:AnnotationDataGridColumn):void {
			var property:XML = parentApplication.getSampleProperty(propertyNode.@idProperty);
			if (property == null) {
				dc.visible = false;
				dc.editable = false;
				return;
			}
			
			if ( parentDocument.isIScanState() ) {
				propertyNode.@isSelected = 'true';
			}
						
			var fieldName:String = "@ANNOT" + propertyNode.@idProperty;
			dc.dataField  = fieldName;
			dc.headerText = propertyNode.@name;
			if (propertyNode.@name == "Other" && parentDocument.request.@otherLabel != null && parentDocument.request.@otherLabel != '') {
				dc.headerText = parentDocument.request.@otherLabel;
			}
			dc.visible  = propertyNode.@isSelected == 'true' ? true : false;;
			dc.editable = false;
			if (property.@codePropertyType == 'MOPTION') {
				dc.itemRenderer =  MultiselectRenderer.create(false);
			} else if (property.@codePropertyType == 'URL') {
				dc.itemRenderer = URLRenderer.create(false);
			} else if (property.@codePropertyType == 'CHECK') {
				dc.itemRenderer = CheckBoxRenderer.create();
			} else if (property.@codePropertyType == 'OPTION') {
				dc.itemRenderer = views.renderers.DropdownLabel.create(
					parentApplication.getPropertyOptions(propertyNode.@idProperty, false), 
					'@option', 
					'@idPropertyOption', 
					fieldName,
					false, false);            					
			} 
		}
		
		public function getSamplesGrid():DataGrid {
			return null;
		}
		
		public function showHideColumns():void {
			
		}
		
		public function setupView():void {
			
		}
		
		public function setBarcodeColVisibility(visibility:Boolean):void {
			
		}
		
		public function setChannel2ColVisibility(visibility:Boolean):void {
			
		}
		
		public function setBillingGridRowCount(rowCount:int):void{
			
		}
		
		public function getEmptyTreeIcon(item:Object):Class {
			return null; 
		} 
		
		public function blankNLabelFunction(item:Object, column:DataGridColumn):String
		{
			if (item[column.dataField].toString() == "N") {
				return ""
			} else {
				return item[column.dataField].toString();
			}
		}
		
	}
}