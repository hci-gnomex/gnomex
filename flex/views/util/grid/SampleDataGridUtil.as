package views.util.grid
{
	import mx.collections.XMLListCollection;
	import mx.controls.AdvancedDataGrid;
	import mx.controls.advancedDataGridClasses.AdvancedDataGridColumn;
	import mx.core.ClassFactory;
	import mx.utils.StringUtil;
	
	import views.util.AdvancedDataGridColumnWithType;
	import views.util.AnnotationAdvancedDataGridColumn;
	import views.util.grid.DataGridUtil;
	
	public class SampleDataGridUtil extends DataGridUtil
	{
		
		// Special handling for Samples
		public static function getItemsFromText( text:String, dataGrid:AdvancedDataGrid, parentApplication:Object ):XMLListCollection
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
						copiedValue = getValueForType(copiedValue, fieldType, col, parentApplication);
						
						itemxml.@[colName] = copiedValue;
						colIndex++;
					}
					
				}
				
				itemsFromTextXML.addItem(itemxml);
			}
			
			return itemsFromTextXML;
		}
				
		private static function getValueForType(inputString:String, fieldType:String, col:AdvancedDataGridColumn, parentApplication:Object):String {
			var value:String = inputString;
			
			if(fieldType == "OPTION") {
				if(inputString.length > 0) {
					// If option field then need to find dropdown value corresponding to the label that has been stored in the spreadsheet
					var optionFound:Boolean = false;
					var thisItemRenderer:mx.core.ClassFactory = mx.core.ClassFactory(col.itemRenderer);
					if(thisItemRenderer != null) {
						var thisDataProvider:XMLList = thisItemRenderer.properties.dataProvider;
						var thisLabelField:String = thisItemRenderer.properties.labelField;	
						if(thisLabelField.length > 0 && thisLabelField.charAt(0) == '@') {
							// Strip off @ if it's there -- not needed for this action
							thisLabelField = thisLabelField.substr(1);
						}
						var thisValueField:String = thisItemRenderer.properties.valueField;
						if(thisValueField.length > 0 && thisValueField.charAt(0) == '@') {
							// Strip off @ if it's there -- not needed for this action
							thisValueField = thisValueField.substr(1);
						}
						if(thisDataProvider != null) {
							for each (var dataProviderItem:XML in thisDataProvider) {
								if(dataProviderItem.@[thisLabelField].toLowerCase() == value.toLowerCase() ||
									(col.dataField.toLocaleLowerCase().substr(0, 15) == "@idoligobarcode" && dataProviderItem.@name.toString().toLowerCase() == value.toLowerCase())) {
									value = dataProviderItem.@[thisValueField];
									optionFound = true;
									break;
								}
							}												
						}											
					}
					if (!optionFound) {
						value = "";
					}
				}
			} else if(fieldType == "MOPTION") {
				// If multiple option field then need to find value corresponding
				// to the label(s) that has/have been stored in the spreadsheet
				if(inputString.length > 0) {
					var params:Array = inputString.split(",");
					var paramsFoundCnt:int = 0;								
					var options:XMLList = parentApplication.getPropertyOptions(col.dataField.substr(6));
					value = "";
					for each (var thisParam:String in params) {
						// Loops through one or multiple labels stored in the spreadsheet
						thisParam = StringUtil.trim(thisParam);
						var mOptionFound:Boolean = false;
						for each (var optionItem:XML in options) {
							// Compares against Multi Select options and matches up with corresponding values
							if(optionItem.@option.toLowerCase() == thisParam.toLowerCase()) {
								mOptionFound = true;
								if(paramsFoundCnt > 0) {
									value = value + ",";
								}
								value = value + optionItem.@idPropertyOption;
								paramsFoundCnt++;
								break;
							}
						}
					}
				}
			} else if(fieldType == "CHECK") {
				if(inputString != null) {
					if(inputString.length > 0) {
						if(inputString != "Y" && inputString != "N") {
							value = "";
						}
					}
				}
			}
			
			return value;;
		}
		
	}
}