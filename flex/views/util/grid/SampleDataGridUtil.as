package views.util.grid
{
	import mx.collections.XMLListCollection;
	import mx.controls.AdvancedDataGrid;
	import mx.controls.advancedDataGridClasses.AdvancedDataGridColumn;
	
	import views.util.AdvancedDataGridColumnWithType;
	import views.util.AnnotationAdvancedDataGridColumn;
	
	public class SampleDataGridUtil extends DataGridUtil
	{
		
		// Special handling for Samples
		public static function getItemsFromText( text:String, dataGrid:AdvancedDataGrid ):XMLListCollection
		{
			var rows:Array = text.split( lineEnding );
			
			// check for a blank row
			if (rows.length > 0 && !rows[rows.length - 1])
			{
				rows.pop();
			}
			
			var itemsFromTextXML:XMLListCollection = new XMLListCollection();
			
			for each (var row:String in rows)
			{
				var fields:Array = row.split(tabDelimiter);
				var itemxml:XML = new XML("<Sample/>");
				itemxml.@idSample = "Sample" + rows.indexOf(row);
				
				var col:AdvancedDataGridColumn;
				var colName:String = "";
				var colIndex:int = 0;
				
				for (var i:int = 0; i < fields.length; i++)
				{
					if ( findNextVisibleColIndex(colIndex,dataGrid.columns) != -1 ) {
						colIndex = findNextVisibleColIndex(colIndex,dataGrid.columns);
						col = dataGrid.columns[colIndex];
						colName = col.dataField;
						
						if (colName!=null && colName.charAt(0)=="@") {
							colName = colName.substr(1,colName.length);
						}
						
						var copiedValue:String = fields[i];
						
						if (colName == "name") {
							// Only allow 30 characters for sample names
							if (copiedValue.length > 30) {
								copiedValue = copiedValue.substr(0, 30);
							}
						}
						if(colName == "label") {
							// Special handling for label field -- expect cy3 or cy5
							if(copiedValue.length > 0 && !(copiedValue == 'cy3' || copiedValue == 'cy5')) {
								copiedValue = "";								
							}
						}
						
						var fieldType:String = "TEXT";
						if(col is AnnotationAdvancedDataGridColumn) {
							fieldType = AnnotationAdvancedDataGridColumn(col).propertyType;
						}
						if(col is AdvancedDataGridColumnWithType) {
							fieldType = AdvancedDataGridColumnWithType(col).propertyType;
						}
						
						if(fieldType == "URL" || fieldType == "MOPTION") {
							// For these types strip beginning, ending quotation marks if present
							if(copiedValue.length > 0 && copiedValue.charAt(0) == '"') {
								copiedValue = copiedValue.substr(1);
							}
							if(copiedValue.length > 0 && copiedValue.charAt(copiedValue.length-1) == '"') {
								copiedValue = copiedValue.substr(0, copiedValue.length-1);
							}
						}

						itemxml.@[colName] = copiedValue;
						colIndex++;
					}
					
				}
				itemsFromTextXML.addItem(itemxml);
			}
			
			return itemsFromTextXML;
		}
		
		public static function getSelectedRows( dataGrid:AdvancedDataGrid, dataType:String,  ignoredAttributes:Array = null):XMLListCollection {
			var copiedItems:XMLListCollection = new XMLListCollection();
			
			var selectedData:Array =  DataGridUtil.getSelectedItemsInOrder(dataGrid);
			for each(var itemToCopy:XML in selectedData) {	
				var emptyNode:XML = new XML("<Sample/>");
				// Now copy the sample annotations
				for each (var attribute:Object in itemToCopy.attributes()) {
					if ( ignoredAttributes != null ) {
						var aName:String = attribute.name();
						if (ignoredAttributes.indexOf(aName) != -1) {
							emptyNode["@" + attribute.name()] = '';
						} else {
							emptyNode["@" + attribute.name()] = String(attribute);
						}
					} else {
						emptyNode["@" + attribute.name()] = String(attribute);
					}
				}
				copiedItems.addItem(emptyNode);
			}
			
			return copiedItems;
		}
	}
}