package views.util.grid
{
	import flash.events.KeyboardEvent;
	import flash.events.TextEvent;
	import flash.net.URLRequest;
	import flash.net.URLRequestMethod;
	import flash.net.URLVariables;
	import flash.net.navigateToURL;
	import flash.system.Capabilities;
	import flash.system.System;
	import flash.text.TextField;
	import flash.text.TextFieldType;
	
	import mx.collections.ArrayCollection;
	import mx.collections.HierarchicalCollectionView;
	import mx.collections.ICollectionView;
	import mx.collections.IViewCursor;
	import mx.collections.XMLListCollection;
	import mx.controls.AdvancedDataGrid;
	import mx.controls.DataGrid;
	import mx.controls.advancedDataGridClasses.AdvancedDataGridColumn;
	import mx.core.ClassFactory;
	import mx.utils.StringUtil;
	
	import views.util.AdvancedDataGridColumnWithType;
	import views.util.AnnotationAdvancedDataGridColumn;
	
	// based on http://coockbooks.adobe.com/post_Copying_a_datagrid_data_to_the_clipboard_for_Excel-9883.html
	// Modified for GNomEx by MBowler Feb 2014
	// Assumes data provider is XMLListCollection, accounts for special typed and annotation columns
	public class DataGridUtil
	{		
		protected static var tabDelimiter:String = "\t";
		protected static var commaDelimiter:String = ","; 
		
		public static const TYPE_CSV:String = "typeCsv";
		public static const TYPE_TSV:String = "typeTsv";
		
		public static const EXPORT_ALL:String 		= "exportAll";
		public static const EXPORT_VISIBLE:String	= "exportVisible";
		public static const EXPORT_SELECTED:String 	= "exportSelected";		
		
		/**
		 * This will convert the data shown in the DataGrid to a CSV/TSV string
		 */
		public static function copyData( dataGrid:AdvancedDataGrid, fileType:String, exportType:String, parentApplication:Object, includeHeaders:Boolean=true ):String
		{
			var str:String 	 		= "";
			var value:String 		= "";
			var skipColumns:Array	= [];
			
			for (var i:int = 0;i<dataGrid.columns.length;i++) 
			{
				if (dataGrid.columns[i].headerText != undefined) 
				{
					value = dataGrid.columns[i].headerText;					
				} 
				else 
				{
					value = dataGrid.columns[i].dataField;
				}
				
				// we won't include columns which don't have titles or are not visible
				if (value == null || value.length == 0 || !dataGrid.columns[i].visible)
				{
					skipColumns.push( i );
					continue;
				}
				else
				{
					if (fileType == TYPE_CSV)
					{
						str += '"' + value + '"';
					}
					else
					{
						str += value;
					}
				}
				
				if (i < dataGrid.columns.length-1)
				{
					str += fileType == TYPE_CSV ? commaDelimiter : tabDelimiter;
				}
			}
			
			str += lineEnding; 
			
			if ( !includeHeaders ) {
				str = "";
			}
			
			//Loop through the records in the dataprovider and 
			//insert the column information into the table			
			var data:Array;
			
			if (exportType == EXPORT_ALL)
			{
				data = new Array();
				if ( dataGrid.dataProvider is HierarchicalCollectionView ) {
					var listItem:Object;
					for each ( listItem in dataGrid.dataProvider.source.source ) {
						data.push( listItem );
					}
				} else {
					for each ( listItem in dataGrid.dataProvider ) {
						data.push( listItem );
					}
				}
			}
			else
			{
				data = getSelectedItemsInOrder(dataGrid);
			}
			
			for each (var item:Object in data)
			{					
				for(var k:int=0; k < dataGrid.columns.length; k++) 
				{
					// check if we're skipping this column
					if (skipColumns.indexOf( k ) >= 0)
					{
						continue;
					}
					
					//Check to see if the profile specified a labelfunction which we must
					//use instead of the dataField; don't use if it just returns an empty string
					if (dataGrid.columns[k].labelFunction != undefined && dataGrid.columns[k].labelFunction(item, dataGrid.columns[k])!='' ) 
					{
						value = dataGrid.columns[k].labelFunction(item, dataGrid.columns[k]);			        					
					} 
					else 
					{
						//Our dataprovider contains the real data
						//We need the column information (dataField)
						//to specify which key to use.
						var col:AdvancedDataGridColumn = dataGrid.columns[k];
						var dataField:String = col.dataField;
						if ( (col is AdvancedDataGridColumnWithType) || (col is AnnotationAdvancedDataGridColumn) )
						{
							var fieldType:String = "TEXT";
							if(col is AnnotationAdvancedDataGridColumn) {
								fieldType = AnnotationAdvancedDataGridColumn(col).propertyType;
							}
							if(col is AdvancedDataGridColumnWithType) {
								fieldType = AdvancedDataGridColumnWithType(col).propertyType;
							}
							value = getTextForType(item[ dataField ], fieldType, col, parentApplication);
							
						} else {
							value = item[ dataField ];
						}
					}
					
					
					if (value)
					{
						var pattern:RegExp = /["]/g;
						value = value.replace( pattern, "" );
						
						if (fileType == TYPE_CSV)
						{
							value = '"' + value + '"';
						}
					}
					else
					{
						if (fileType == TYPE_CSV)
						{
							value = '""';
						}
					}
					
					str += value;
					
					if (k < dataGrid.columns.length - 1)
					{
						str += fileType == TYPE_CSV ? commaDelimiter : tabDelimiter;
					}
				}
				
				str += lineEnding;
			}
			
			return str;
		}
		
		// Copies the selected rows in the grid and returns an XMLListCollection containing the copied items 
		public static function getSelectedRows( dataGrid:AdvancedDataGrid, dataType:String,  ignoredAttributes:Array = null):XMLListCollection {
			var copiedItems:XMLListCollection = new XMLListCollection();
			
			var selectedData:Array =  getSelectedItemsInOrder(dataGrid);
			for each(var itemToCopy:XML in selectedData) {	
				var emptyNode:XML = new XML("<"+dataType+"/>");
				
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
		
		// Returns a comma or tab separated string 
		public static function getTextFromXMLList( dataGrid:AdvancedDataGrid, data:XMLListCollection, fileType:String, parentApplication:Object ):String
		{
			var str:String 	 		= "";
			var value:String 		= "";
			var skipColumns:Array	= [];
			
			for (var i:int = 0;i<dataGrid.columns.length;i++) 
			{
				if (dataGrid.columns[i].headerText != undefined) 
				{
					value = dataGrid.columns[i].headerText;					
				} 
				else 
				{
					value = dataGrid.columns[i].dataField;
				}
				
				// we won't include columns which don't have titles or are not visible
				if (value == null || value.length == 0 || !dataGrid.columns[i].visible)
				{
					skipColumns.push( i );
					continue;
				}
			}
			
			//Loop through the records in the dataprovider and 
			//insert the column information into the table			
						
			for each (var item:Object in data)
			{					
				for(var k:int=0; k < dataGrid.columns.length; k++) 
				{
					// check if we're skipping this column
					if (skipColumns.indexOf( k ) >= 0)
					{
						continue;
					}
					
					//Check to see if the profile specified a labelfunction which we must
					//use instead of the dataField
					if (dataGrid.columns[k].labelFunction != undefined) 
					{
						value = dataGrid.columns[k].labelFunction(item, dataGrid.columns[k]);			        					
					} 
					else 
					{
						//Our dataprovider contains the real data
						//We need the column information (dataField)
						//to specify which key to use.
						var col:AdvancedDataGridColumn = dataGrid.columns[k];
						var dataField:String = col.dataField;
						if ( (col is AdvancedDataGridColumnWithType) || (col is AnnotationAdvancedDataGridColumn) )
						{
							var fieldType:String = "TEXT";
							if(col is AnnotationAdvancedDataGridColumn) {
								fieldType = AnnotationAdvancedDataGridColumn(col).propertyType;
							}
							if(col is AdvancedDataGridColumnWithType) {
								fieldType = AdvancedDataGridColumnWithType(col).propertyType;
							}
							value = getTextForType(item[ dataField ], fieldType, col, parentApplication);
							
						} else {
							value = item[ dataField ];
						}
					}
					
					if (value)
					{
						var pattern:RegExp = /["]/g;
						value = value.replace( pattern, "" );
						
						if (fileType == TYPE_CSV)
						{
							value = '"' + value + '"';
						}
					}
					else
					{
						if (fileType == TYPE_CSV)
						{
							value = '""';
						}
					}
					
					str += value;
					
					if (k < dataGrid.columns.length - 1)
					{
						str += fileType == TYPE_CSV ? commaDelimiter : tabDelimiter;
					}
				}
				
				str += lineEnding;
			}
			
			return str;
		}
		
		// Returns an array of the selected items in the order they appear in the grid (instead of
		// in the order they were selected)
		private static function getSelectedItemsInOrder( dataGrid:AdvancedDataGrid ):Array {
			
			var sortedItemList:Array = new Array();
			if ( dataGrid.dataProvider is HierarchicalCollectionView ) {
				var listItem:Object;
				for each ( listItem in dataGrid.dataProvider.source.source ) {
					if ( dataGrid.selectedItems.indexOf( listItem ) >=0 ) {
						sortedItemList.push( listItem );
					}
				}
			} else {
				for each ( listItem in dataGrid.dataProvider ) {
					if ( dataGrid.selectedItems.indexOf( listItem ) >=0 ) {
						sortedItemList.push( listItem );
					}
				}
			}
			
			return sortedItemList;
		}
		
		// Returns an XMLListCollection containing XML items given a string (tab delimited) containing the attributes that 
		// correspond with the columns of the grid.
		public static function getItemsFromText( text:String, dataGrid:AdvancedDataGrid, parentApplication:Object, dataType:String="ListItem" ):XMLListCollection
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
				var itemxml:XML = new XML("<"+dataType+"/>");
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
		
		// Get the label correlating to an id or other input for special typed or annotation columns
		private static function getTextForType(inputString:String, fieldType:String, col:AdvancedDataGridColumn, parentApplication:Object):String {
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
								if( dataProviderItem.@[thisValueField] == inputString ) {
									value = dataProviderItem.@[thisLabelField];
									optionFound = true;
									break;
								}
							}												
						}											
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
							if(optionItem.@idPropertyOption == thisParam) {
								mOptionFound = true;
								if(paramsFoundCnt > 0) {
									value = value + ",";
								}
								value = value + optionItem.@option;
								paramsFoundCnt++;
								break;
							}
						}
					}	
				}
			} 
			return value;
		}
		
		// Get the id or other datafield value given the label of a typed or annotation column
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
								if( dataProviderItem.@[thisLabelField].toLowerCase() == value.toLowerCase() ) {
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
		
		// Create an empty xml with empty attributes corresponding to the columns of the grid
		public static function getEmptyRow( dataGrid:AdvancedDataGrid, dataType:String):XML {
			
			var emptyNode:XML = new XML("<"+dataType+"/>");
			
			for(var k:int=0; k < dataGrid.columns.length; k++) 
			{
				var col:AdvancedDataGridColumn = dataGrid.columns[k];
				var colName:String = col.dataField;
				
				if (colName!=null && colName.charAt(0)=="@") {
					colName = colName.substr(1,colName.length);
				}
				if (colName!=null && colName!='null'){
					emptyNode.@[colName] = '';
				}
			}
			
			return emptyNode;
		}
				
		// Returns the index of the next column that is visible and has a header
		protected static function findNextVisibleColIndex(startIndex:int,columns:Array):int
		{
			if ( startIndex >= columns.length ) {
				return -1;
			} else if ( columns[startIndex].visible && columns[startIndex].headerText != null && columns[startIndex].headerText != "") {
				return startIndex;
			} else {
				startIndex++;
				return findNextVisibleColIndex(startIndex, columns);
			}
		}
		
		// Get line end character 
		protected static function get lineEnding():String
		{
			if (Capabilities.os.indexOf( "Mac" ) >= 0)
			{
				return "\r";
			}
			else
			{
				return "\n";
			}
		}
		
		
	}
}