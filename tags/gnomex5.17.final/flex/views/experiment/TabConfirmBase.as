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
	import views.util.AnnotationAdvancedDataGridColumn;
	import views.util.AnnotationDataGridColumn;
	
	[Bindable]
	public class TabConfirmBase extends Canvas
	{
		
		public static function getConfirmTab(existingTab:TabConfirmBase, requestCategoryType:Object):TabConfirmBase {
			if (requestCategoryType.@codeRequestCategoryType == 'ISCAN') {
				if (existingTab is TabConfirmIScan) {
					return existingTab;
				} else {
					return new TabConfirmIScan();
				}
			} else if (requestCategoryType.@isIllumina == 'Y') {
				if (existingTab is TabConfirmIllumina) {
					return existingTab;
				} else {
					return new TabConfirmIllumina();
				}
			}  else if (requestCategoryType.@codeRequestCategoryType == 'ISOLATION') {
				if (existingTab is TabConfirmIsolation) {
					return existingTab;
				} else {
					return new TabConfirmIsolation();
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
			var grid:Object = getSamplesGrid();
			var columns:Array = getSamplesGridColumns();
			var newColumns:Array = new Array();
			if (grid != null) {
				var annotationsAdded:Boolean = false;
				for (var x:Number = 0; x < columns.length; x++) {
					var dc:Object = getSamplesGridColumn(grid, x);
					if (getSampleDataField(dc) == "@description") {
						// Push the annotations before the description property.
						addAnnotationProperties(columns, newColumns);
						annotationsAdded = true;
						if (isDescriptionEnabled()) {
							setSampleVisibility(dc, true);
						} else {
							setSampleVisibility(dc, false);
						}
					}
					if (getSampleDataField(dc) == null || getSampleDataField(dc).substr(0, 6) != "@ANNOT") {
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
		
		private function getSamplesGridColumns():Array {
			var grid:Object = getSamplesGrid();
			if (grid is AdvancedDataGrid) {
				return AdvancedDataGrid(grid).columns;
			} else if (grid is DataGrid) {
				return DataGrid(grid).columns;
			} else {
				return null;
			}
		}
		
		private function getSamplesGridColumn(grid:Object, x:Number):Object {
			if (grid is AdvancedDataGrid) {
				return AdvancedDataGrid(grid).columns[x];
			} else if (grid is DataGrid) {
				return DataGrid(grid).columns[x];
			} else {
				return null;
			}
		}
		
		private function getSampleDataField(dc:Object):String {
			if (dc is AdvancedDataGridColumn) {
				return AdvancedDataGridColumn(dc).dataField;
			} else if (dc is DataGridColumn) {
				return DataGridColumn(dc).dataField;
			} else {
				return null;
			}
		}
		
		private function setSampleVisibility(dc:Object, vis:Boolean):void {
			if (dc is AdvancedDataGridColumn) {
				AdvancedDataGridColumn(dc).visible = vis;
			} else if (dc is DataGridColumn) {
				DataGridColumn(dc).visible = vis;
			}
		}
		private function isDescriptionEnabled():Boolean {
			var enabled:Boolean = false;
			for each(var node:XML in parentDocument.propertyEntries) {
				if (node.@idProperty == "-1") {
					if (node.@isSelected == 'true') {
						enabled = true;
						break;
					}
				}
			}
			return enabled;
		}

		private function addAnnotationProperties(columns:Array, newColumns:Array):void {
			// Add real annotations.
			for each(var node:XML in parentDocument.propertyEntries) {
				addAnnotationProperty(columns, newColumns, node);
			}
		}
		
		private function addAnnotationProperty(columns:Array, newColumns:Array, node:XML):void {
			var dc:Object = findAnnotationGridColumn(columns, node);
			var fieldName:String = "@ANNOT" + node.@idProperty;
			if(columns[0] is AdvancedDataGridColumn) {
				if (dc == null) {
					dc = buildNewAnnotationAdvancedGridColumn(node, fieldName);
				} else {
					refreshAnnotationAdvancedGridColumn(node, AnnotationAdvancedDataGridColumn(dc));
				}
			} else if (columns[0] is DataGridColumn) {
				if (dc == null) {
					dc = buildNewAnnotationGridColumn(node, fieldName);
				} else {
					refreshAnnotationGridColumn(node, AnnotationDataGridColumn(dc));
				}
			}
			newColumns.push(dc);
		}
		
		private function findAnnotationGridColumn(columns:Array, propertyNode:XML):Object {
			var dcFound:Object = null;
			for each (var dc:Object in columns) {
				if ((dc is AnnotationDataGridColumn || dc is AnnotationAdvancedDataGridColumn) && 
					(getSampleDataField(dc) == "@ANNOT" + propertyNode.@idProperty)) {
					dcFound = dc;
					break;
				}
			}
			
			return dcFound;
		}
		
		private function buildNewAnnotationGridColumn(propertyNode:XML, fieldName:String):AnnotationDataGridColumn {
			var newCol:AnnotationDataGridColumn = new AnnotationDataGridColumn(fieldName);
			refreshAnnotationGridColumn(propertyNode, newCol);
			return newCol;
		}
		
		private function buildNewAnnotationAdvancedGridColumn(propertyNode:XML, fieldName:String):AnnotationAdvancedDataGridColumn {
			var newCol:AnnotationAdvancedDataGridColumn = new AnnotationAdvancedDataGridColumn(fieldName);
			refreshAnnotationAdvancedGridColumn(propertyNode, newCol);
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
		
		private function refreshAnnotationAdvancedGridColumn(propertyNode:XML, dc:AnnotationAdvancedDataGridColumn):void {
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
		
		public function getSamplesGrid():Object {
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