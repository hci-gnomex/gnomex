package views.experiment
{
	import flash.net.*;
	import flash.utils.Dictionary;
	
	import hci.flex.controls.TextInput;
	
	import mx.collections.XMLListCollection;
	import mx.containers.Canvas;
	import mx.controls.AdvancedDataGrid;
	import mx.controls.Alert;
	import mx.controls.TextInput;
	import mx.controls.advancedDataGridClasses.AdvancedDataGridColumn;
	import mx.core.IFactory;
	import mx.events.AdvancedDataGridEvent;
	import mx.events.CloseEvent;
	import mx.events.FlexEvent;
	import mx.formatters.NumberBaseRoundType;
	import mx.formatters.NumberFormatter;
	import mx.managers.PopUpManager;
	import mx.utils.ObjectUtil;
	
	import views.renderers.CheckBoxRenderer;
	import views.renderers.DropdownLabelAnnotation;
	import views.renderers.MultiselectRenderer;
	import views.renderers.TextInputSampleConcentration;
	import views.renderers.URLRenderer;
	import views.util.AnnotationAdvancedDataGridColumn;
	import views.util.CopySampleSheetColumnView;
	import views.util.UploadSampleSheetView;

	[Bindable]
	public class TabSamplesBase extends Canvas
	{
		public var isEditState:Boolean = false;
		
		protected var downloadRequest:URLRequest; 
		protected var downloadFileRef:FileReference;
		
		public var sampleConcentrationFormatter:NumberFormatter;

		public var filteredSampleTypeList:XMLListCollection;

		public static function getSamplesTab(existingTab:TabSamplesBase, requestCategory:Object, requestCategoryType:Object, isEditState:Boolean, isAmendState:Boolean):TabSamplesBase {
			if (requestCategory.@codeRequestCategory == 'MDMISEQ') {
				if (existingTab is TabSamplesMDMiSeq) {
					return existingTab;
				} else {
					return new TabSamplesMDMiSeq();
				}
			} else if (requestCategory.@codeRequestCategory == "DDPCR") {
				if (existingTab is TabSamplesDDPCR) {
					return existingTab;
				} else {
					return new TabSamplesDDPCR();
				}
			} else if (requestCategoryType.@isIllumina == 'Y') {
				if (existingTab is TabSamplesIllumina) {
					return existingTab;
				} else {
					return new TabSamplesIllumina();
				}
			} else if (requestCategoryType.@codeRequestCategoryType == 'SEQUENOM' || requestCategoryType.@codeRequestCategoryType == 'CLINSEQ') {
				if (existingTab is TabSamplesSequenom) {
					return existingTab;
				} else {
					return new TabSamplesSequenom();
				}
			} else if (requestCategoryType.@codeRequestCategoryType == 'NANOSTRING') {
				if (existingTab is TabSamplesNanoString) {
					return existingTab;
				} else {
					return new TabSamplesNanoString();
				}
			}else if (requestCategoryType.@codeRequestCategoryType == 'ISOLATION') {
				if (existingTab is TabSamplesIsolation) {
					return existingTab;
				} else {
					return new TabSamplesIsolation();
				}
			} else if ((requestCategory.@codeRequestCategory.toString()).substr( 0, 6 ) == 'IONTOR') {
				if (existingTab is TabSamplesIonTorrent) {
					return existingTab;
				} else {
					return new TabSamplesIonTorrent();
				}
			} else if (requestCategoryType.@codeRequestCategoryType == 'GENERIC') {
				if (existingTab is TabSamplesGeneric) {
					return existingTab;
				} else {
					return new TabSamplesGeneric();
				}
			} else {
				if (existingTab is TabSamplesView) {
					return existingTab;
				} else {
					return new TabSamplesView();
				}
			}
		}

		public function TabSamplesBase()
		{
			super();
			
			setupSampleConcentrationFormatter();
		}
		
		public function setupSampleConcentrationFormatter(requestCategory:Object = null):void {
			sampleConcentrationFormatter = new NumberFormatter();
			
			if (requestCategory != null && requestCategory.hasOwnProperty("@idCoreFacility")) {
				var concentrationPrecision:Number = new Number(parentApplication.getCoreFacilityProperty(requestCategory.@idCoreFacility, parentApplication.PROPERTY_SAMPLE_CONCENTRATION_PRECISION));
				if (!isNaN(concentrationPrecision)) {
					sampleConcentrationFormatter.precision = concentrationPrecision;
					sampleConcentrationFormatter.rounding = NumberBaseRoundType.NEAREST;
				}
			}
		}
		
		public function initializeSampleTypes():void {
			var sampleTypeList:XMLList = new XMLList();
			for each (var sampleType:Object in parentDocument.filteredSampleTypeList) {
				sampleTypeList += sampleType;
			}		
			this.filteredSampleTypeList = new XMLListCollection(sampleTypeList);
		}
		
		public function initializeSamplesGrid():void {
			
		}
		
		public function showHideColumns():void {
			
		}
		
		public function prepareCherryPickingSamplesForEdit(numDestinationWells:int):void {
			
		}
		
		public function hasPlates():Boolean {
			return false;
		}

		public function fillPlates():void {
			
		}
		
		public function initializeBarcoding():void {
			
		}
		
		public function propagateOrganism(idOrganism:String):void {
			
		}
		
		public function propagateNumberSequencingCycles(theSample:Object):void {
			
		}
		
		public function propagateContainerType(containerType:String):void {
			
		}
		
		public function propagateOtherOrganism(otherOrganism:String):void {
			for each (var sample:Object in parentDocument.samples) {
				sample.@otherOrganism = otherOrganism;
			}
		}
		
		public function getSamplesGrid():AdvancedDataGrid {
			return null;
		}
		
		public function checkSamplesCompleteness():void {
			
		}
		
		public function insertSample():Boolean {
			return true;
		}
		
		public function addSample():Boolean {
			return true;
		}
		
		public function setShowCCNumber(show:Boolean):void {
			
		}
		
		public function getShowCCNumber():Boolean {
			return false;
		}
		
		public function propagateBarcode():void {
			
		}
		
		public function areIndexTagsUnique():Boolean {
			return true;
		}
		
		protected function rebuildSamplesGrid():void {
			var grid:AdvancedDataGrid = getSamplesGrid();
			var columns:Array = grid.columns;
			var newColumns:Array = new Array();
			if (grid != null) {
				var found:Boolean = false;
				for (var x:Number = 0; x < columns.length; x++) {
					var dc:AdvancedDataGridColumn = grid.columns[x];
					if (dc.dataField == "@description") {
						// Push the annotations before the description property.
						addAnnotationProperties(columns, newColumns);
						if (isDescriptionEnabled()) {
							dc.visible = true;
						} else {
							dc.visible = false;
						}
					}
					if (dc.dataField == null || dc.dataField.substr(0, 6) != "@ANNOT") {
						newColumns.push(dc);
					}
				}
			}
			grid.columns = newColumns;
			grid.validateNow();
		}
		
		protected function isDescriptionEnabled():Boolean {
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
		
		protected function addAnnotationProperties(columns:Array, newColumns:Array):void {
			// Add real annotations.
			for each(var node:XML in parentDocument.propertyEntries) {
				// -1 check is because description is handled above.
				if (node.@idProperty != "-1") {
					addAnnotationProperty(columns, newColumns, node);
				}
			}
		}
		
		protected function addAnnotationProperty(columns:Array, newColumns:Array, node:XML):void {
			var dc:AnnotationAdvancedDataGridColumn = findAnnotationGridColumn(columns, node);
			if (dc == null) {
				dc = buildNewAnnotationGridColumn(node);
			} else {
				refreshAnnotationGridColumn(node, dc);
			}
			newColumns.push(dc);
		}
		
		protected function findAnnotationGridColumn(columns:Array, propertyNode:XML):AnnotationAdvancedDataGridColumn {
			var dcFound:AnnotationAdvancedDataGridColumn = null;
			for each (var dc:AdvancedDataGridColumn in columns) {
				if (dc is AnnotationAdvancedDataGridColumn && 
					(dc.dataField == "@ANNOT" + propertyNode.@idProperty)) {
					dcFound = dc as AnnotationAdvancedDataGridColumn;
					break;
				}
			}
			
			return dcFound;
		}
		
		protected function buildNewAnnotationGridColumn(propertyNode:XML):AnnotationAdvancedDataGridColumn {
			var fieldName:String = "@ANNOT" + propertyNode.@idProperty;
			var newCol:AnnotationAdvancedDataGridColumn = new AnnotationAdvancedDataGridColumn(fieldName);
			refreshAnnotationGridColumn(propertyNode, newCol);
			return newCol;
		}
		
		protected function refreshAnnotationGridColumn(propertyNode:XML, dc:AnnotationAdvancedDataGridColumn):void {
			var property:XML = parentApplication.getSampleProperty(propertyNode.@idProperty);
			if (property == null) {
				dc.visible = false;
				dc.editable = false;
				return;
			}
			
			if ( parentDocument.isIScanState() ) {
				propertyNode.@isSelected = 'true';
			}
			
			// We only show inactive options when it is edit state.
			var includeInactiveOptions:Boolean = parentDocument.isEditState() && !parentDocument.isIScanState();
			
			var fieldName:String = "@ANNOT" + propertyNode.@idProperty;
			dc.dataField  = fieldName;
			dc.headerText = propertyNode.@name;
			dc.headerWordWrap = true;
			if (propertyNode.@description != null && propertyNode.@description.toString() != "") {
				dc.headerToolTip = propertyNode.@description
			} else {
				dc.headerToolTip = propertyNode.@name;
			}
			if (propertyNode.@name == "Other" && parentDocument.request.@otherLabel != null && parentDocument.request.@otherLabel != '') {
				dc.headerText = parentDocument.request.@otherLabel;
			}
			dc.visible    = propertyNode.@isSelected == "true" ? true : false;
			dc.editable = false;
			dc.propertyType = property.@codePropertyType;
			if (property.@codePropertyType == 'MOPTION') {
				dc.itemRenderer =  MultiselectRenderer.create(true,propertyNode.@isRequired == 'true',true);
			} else if (property.@codePropertyType == 'URL') {
				dc.itemRenderer = URLRenderer.create(true,propertyNode.@isRequired == 'true');
			} else if (property.@codePropertyType == 'CHECK') {
				dc.itemRenderer = CheckBoxRenderer.create();
			} else if (property.@codePropertyType == 'OPTION') {
				dc.editable = true;
				dc.editorDataField = "value"
					/*
				dc.itemRenderer = views.renderers.DropdownLabel.create(
					parentApplication.getPropertyOptions(propertyNode.@idProperty, includeInactiveOptions), 
					'@option', 
					'@idPropertyOption', 
					fieldName,
					propertyNode.@isRequired == 'true',
					true,
				    parentApplication.annotationColor);
					*/
				dc.itemRenderer = views.renderers.DropdownLabelAnnotation.create(
					"@option",
					"@idPropertyOption",
					fieldName,
					propertyNode.@idProperty.toString(),
					parentApplication,
					propertyNode.@isRequired == 'true',
					true,
					parentApplication.annotationColor);
				/*dc.itemEditor   = views.renderers.GridColumnFillButton.create(views.renderers.ComboBox.create(
					parentApplication.getPropertyOptions(propertyNode.@idProperty, includeInactiveOptions),
					"@option",
					"@idPropertyOption",
					fieldName, 
					null,
					true,
					true,
					false,
					true).newInstance(), '');		*/				
				dc.itemEditor   = views.renderers.GridColumnFillButton.create(views.renderers.FilterComboBoxAnnotation.create(
					"@option",
					"@idPropertyOption",
					fieldName,
					propertyNode.@idProperty.toString(),
					includeInactiveOptions,
					true).newInstance(), '');						
			} else  {
				// Assume text
				dc.editable   = true;
				dc.itemRenderer = views.renderers.Text.createCustom(property, 
					parentApplication.annotationColor, 
					parentApplication.annotationColorBorder, 
					0, 
					parentApplication.nonReqAnnotationColor, 
					propertyNode.@isRequired == 'true');
				dc.itemEditor   = views.renderers.GridColumnFillButton.create(hci.flex.controls.TextInput.create(fieldName).newInstance(), '');
				dc.editorDataField="value";
				dc.wordWrap = true;
			}
		}
		
		
		protected function initButtons():void {
		}
		
		protected function init():void {
			if (parentDocument != null) {
				parentDocument.propertyEntries.refresh();
				showHideColumns();
				initButtons();
				rebuildSamplesGrid();
				initializeBarcoding();
				initializeSampleTypes();
			}
		}
		
		protected function getNumPlates():int {
			return 0;
		}
		
		protected function getNextPlate():int {
			return 0;
		}
		
		public function uploadSampleSheet():void {
			var uploadSampleSheetWindow:UploadSampleSheetView = UploadSampleSheetView(PopUpManager.createPopUp(parentApplication.theBody, UploadSampleSheetView, true));
			PopUpManager.centerPopUp(uploadSampleSheetWindow);
			var fieldList:Dictionary = getSampleSheetSpecifiedFieldList();
			var numPlates:int = getNumPlates();
			var nextPlate:int = getNextPlate();
			
			uploadSampleSheetWindow.init(this, numPlates, nextPlate, fieldList, parentDocument.getAnnotationView());
				
			uploadSampleSheetWindow.addEventListener(FlexEvent.VALUE_COMMIT, uploadComplete);
		}
		
		protected function uploadComplete(event:Event):void {
			if (parentDocument.isEditState()) {
				Alert.show("Samples grid has been updated.  Press 'Save' to update the samples in the database.");
			}
		}
		
		protected function getSampleSheetSpecifiedFieldList():Dictionary {
			return null;
		}
		
		protected function getSampleSheetFieldList():XMLListCollection {
			var fieldList:Dictionary = getSampleSheetSpecifiedFieldList();
			var addAllAnnotations:Boolean = getAddAllAnnotations();
			return UploadSampleSheetView.getFieldList(this, fieldList, null, parentDocument.getAnnotationView(), addAllAnnotations);
		}
		
		protected function getAddAllAnnotations():Boolean {
			return true;
		}
		
		
		public function downloadSampleSheet():void{
			var fieldList:XMLListCollection = getSampleSheetFieldList();
			var showUrl:URLRequest = new URLRequest('DownloadSampleSheet.gx');
			var uv:URLVariables = new URLVariables();
			showUrl.contentType = "application/x-www-form-urlencoded";
			var names:String = "<NameList>"
			for each (var node : XML in fieldList){
				names += node.toXMLString();
			}
			
			// clean up samples
			if (parentDocument.isEditState()) {
				parentDocument.request.replace("samples", <samples></samples>); 
				for each(var sample:Object in parentDocument.samples) {
					if (sample.@name != null && sample.@name != '') {
						parentDocument.request.samples.appendChild(sample);
					}
				}
			}
			
			names += "</NameList>";
			uv.names = names;
			//If this is a new request send back the lab id to create the file name for sample sheet.
			// Also make sure the code request category is set.
			if(parentDocument.request.@idRequest == "0" && parentDocument.setupView != null){
				parentDocument.request.@idLab = parentDocument.setupView.labCombo.selectedItem.@idLab;
				parentDocument.request.@codeRequestCategory = parentDocument.getRequestCategory().@codeRequestCategory;
			}
			uv.requestXMLString = parentDocument.request.toXMLString();
			showUrl.data = uv;
			showUrl.method = URLRequestMethod.POST;
			navigateToURL(showUrl, '_blank');
		}
		
		protected function downloadCompleteHandler(event:Event):void
		{
			mx.controls.Alert.show("Example sample sheet downloaded.");
		}
		
		protected function getNextSampleId():Number {
			var lastId:Number = -1;
			
			for each(var sample:Object in parentDocument.samples) {
				if (sample.@idSample.toString().indexOf("Sample") == 0) {
					var id:Number = sample.@idSample.toString().substr(6);
					if (id > lastId) {
						lastId = id;
					}
				}
			}
			
			lastId++;
			return lastId; 
		}
		
		protected function isEntered(sample:Object, fieldName:String):Boolean {
			if (!sample.hasOwnProperty(fieldName) || sample[fieldName] == '') {
				return false;
			} else {
				return true;
			}
		}
		
		protected function reqdAnnotationsEntered(sample:Object):Boolean {
		/*
			for each(var col:AdvancedDataGridColumn in getSamplesGrid().columns) {
				if (col is views.util.AnnotationAdvancedDataGridColumn && col.visible) {
					if (!sample.hasOwnProperty(col.dataField) || sample[col.dataField] == '') {
						var idProperty:String = col.dataField.substr(6);
						var property:XML = parentApplication.getSampleProperty(idProperty);
						if(property.@isRequired == "Y") {
							return false;	
						}
					}					
				}
			}
			*/
			return true;
		}
				
		public function deleteSample():void {
			var isExternal:Boolean = (parentDocument.isEditState() && parentDocument.request.@isExternal == 'Y') || (!parentDocument.isEditState() && !parentApplication.isInternalExperimentSubmission);
			parentDocument.dirty.setDirty();
			var deleteHappened:Boolean = false;
			if (getSamplesGrid().selectedItems.length > 0) {
				for each(var sample:Object in getSamplesGrid().selectedItems) {
					var isValid:Boolean = true;
					// Only administrators can delete samples
					if (sample.@idSample!=null && sample.@idSample!='' && sample.@idSample.toString().indexOf("Sample") < 0 && (parentDocument.request.@canDeleteSample != null && parentDocument.request.@canDeleteSample != 'Y')) {
						Alert.show("Existing sample " + sample.@number + " cannot be deleted from the experiment.");
						isValid = false;
						continue;
					}
					
					if (isValid) {
						isValid = checkDeleteValidity(sample);
					}
					
					if (isValid) {
						deleteHappened = deleteSingleSample(sample);
					} else {
						break;
					}
					
				}
			}

			postDeleteProcessing(deleteHappened);
			
			this.initializeBarcoding();
			
			
		}
		
		protected function checkDeleteValidity(sample:Object):Boolean {
			return true;
		}
		
		protected function deleteSingleSample(sample:Object):Boolean {
			var ind:int = parentDocument.samples.getItemIndex(sample);
			parentDocument.samples.removeItemAt(parentDocument.samples.getItemIndex(sample));
			return true;
		}
		
		protected function postDeleteProcessing(deleteHappened:Boolean):void {

		}
		
		
		public function promptToClearAllSamples():void {
			Alert.show("Remove all samples currently showing in list?",
				null, 
				(Alert.YES | Alert.NO), this, 
				onPromptToClearAllSamples);
			
		}
		
		protected function onPromptToClearAllSamples(event:CloseEvent):void {
			if (event.detail==Alert.YES) {
				parentDocument.samples.removeAll();
				parentDocument.lanes.removeAll();
				addSample(); 	// Add an initial blank sample to the grid
				checkSamplesCompleteness();
				this.initializeBarcoding();
			}
		}		
		
		protected function sampleCompareFunction(a:XML, b:XML):int
		{
			var aPersistFlag:Number = 0;
			var aPosition:Number = 0;
			if (a.@idSample.toString().indexOf("Sample") > -1) {
				aPosition = a.@idSample.toString().substr(6);
				aPersistFlag = 1;
			} else {
				aPosition = a.@idSample;
			}
			
			var bPersistFlag:Number = 0;
			var bPosition:Number = 0;
			if (b.@idSample.toString().indexOf("Sample") > -1) {
				bPosition = b.@idSample.toString().substr(6);
				bPersistFlag = 1; // non-persistent samples sort after the persistent ones
			} else {
				bPosition = b.@idSample;
			}
			
			if (aPersistFlag == bPersistFlag) {
				return ObjectUtil.numericCompare(aPosition, bPosition);				
			} else {
				return ObjectUtil.numericCompare(aPersistFlag, bPersistFlag);
			}
		}
		
		public function onEdit():void {
			parentDocument.dirty.setDirty();
		}
		
		private function highlightWhenMissing():Boolean {
			if (this.parentDocument.isEditState()) {
				return false;
			} else {
				return true;
			}
		}
		
		// Used for multi-select renderer. Get all options (include inactive if edit state)
		public function getPropertyOptions(idProperty:String):XMLList {
			var includeInactive:Boolean = true;
			if (parentDocument != null) {
				includeInactive = parentDocument.isEditState();
			}
			return parentApplication.getPropertyOptions(idProperty, includeInactive);
		}
		
		protected function addOrganism():void {
			var addOrganismWindow:AddOrganismWindow = AddOrganismWindow(PopUpManager.createPopUp(parentApplication.theBody, AddOrganismWindow, true));
			PopUpManager.centerPopUp(addOrganismWindow);
		}
		
		public function setCoreFacilityNoteVisibility(vis:Boolean):void {
			
		}
		
		public function setTopBoxVisibility(vis:Boolean):void {
			
		}
		
		public function setExternalNoteVisibility():void {
			if (!parentApplication.isInternalExperimentSubmission) {
				if (parentDocument.request.@corePrepInstructions == null || parentDocument.request.@corePrepInstructions == "") {
					setCoreFacilityNoteVisibility(false);
					setTopBoxVisibility(true);
				} else {
					setCoreFacilityNoteVisibility(true);
					setTopBoxVisibility(true);
				}
			} else {
				if (parentDocument.isEditState()) {
					setCoreFacilityNoteVisibility(false);
				} else {
					setCoreFacilityNoteVisibility(true);
				}
				setTopBoxVisibility(true);
			}
		}
		
		public function getPlateName(idx:int):String {
			return "";
		}
		
		public function getIdPlate(idx:int):String {
			return "";
		}
		
		public function getIsExternal():Boolean {
			return (parentDocument.isEditState() && parentDocument.request.@isExternal == 'Y') || (!parentDocument.isEditState() && !parentApplication.isInternalExperimentSubmission);
		}
		
		protected function getWellName(idx:int):String {
			var wellName:String = "";
			if (parentDocument.isFragAnalState()) {
				wellName = "ABCDEFGH".substr(idx / 12, 1);
				var fragColNumber:int = idx % 12 + 1;
				wellName += fragColNumber.toString();
			} else {
				var y:int = idx % 96;
				wellName = parentApplication.wellNamesByColumn[y];
			}
			return wellName;
		}
		
		protected function updateWellNames():void {
			for each (var sample:Object in parentDocument.samples) {
				sample.@wellName = getWellName(parentDocument.samples.getItemIndex(sample));
			}
		}
		
		public function validateData(event:AdvancedDataGridEvent):void {
			if (event.dataField == "@concentration") {
				var newSampleConc:String = event.currentTarget.itemEditorInstance.edtComponent.text;
				event.currentTarget.itemEditorInstance.edtComponent.text = sampleConcentrationFormatter.format(newSampleConc);
				event.currentTarget.itemEditorInstance.edtComponent.dispatchEvent(new Event(Event.CHANGE));
			}
		}
		
	}
}