package views.renderers
{
	import flash.events.FocusEvent;
	
	import hci.flex.renderers.RendererFactory;
	
	import mx.collections.XMLListCollection;
	import mx.core.IFactory;
	import mx.managers.PopUpManager;
	
	import views.renderers.FilterComboBox;
	import views.util.AnnotationOptionAddEvent;

	public class FilterComboBoxAnnotation extends FilterComboBox
	{
		public var dataField:String;
		public var valueField:String;
		public var addOptionOnFly:Boolean;
		public var idProperty:String;
		public var includeInactiveOptions:Boolean;
		
		private var addOptionOpen:Boolean = false;
		private var dataProviderSet:Boolean = false;
		
		public static function create(labelField:String,
									  valueField:String,
									  dataField:String,
									  idProperty:String,
									  includeInactiveOptions:Boolean = false,
									  addOptionOnFly:Boolean=false,
									  prompt:String=null):IFactory {
			return RendererFactory.create(views.renderers.FilterComboBoxAnnotation, { 
				labelField: labelField,
				valueField: valueField,
				dataField: dataField,
				idProperty: idProperty,
				includeInactiveOptions: includeInactiveOptions,
				prompt: prompt,
				addOptionOnFly: addOptionOnFly});			
		}

		public function FilterComboBoxAnnotation()
		{
			super();
		}
		
		protected function setDataProvider():void {
			dataProvider = new XMLListCollection();
			
			this.enabled = true;
			this.editable = true;
			this.removeFilterOnSelection = false;
			this.removeInputOnFocusOut = false;
			if ( idProperty != null ){
				dataProvider = parentApplication.getPropertyOptions(idProperty, includeInactiveOptions);
				selectTheItem();
			} else {
				this.enabled = false;
				this.editable = false;
				selectedItem = null;
			}
			dataProviderSet = true;
		}

		public function set value(val:Object):void {
			if ( val != null ) {
				for (var i : int = 0; i < dataProvider.length; i++) {
					var item:Object = dataProvider[i];
					if(item[valueField] == val) {
						this.selectedIndex = i;
						
						break;
					}
				}
			}
		}
		
		override public function get value():Object {			
			if (this.selectedItem != null) {
				return this.selectedItem[valueField];
			}
			else {
				return null;
			}
		}

		private function addOption(oName:String):void {
			var w:AddAnnotationOptionWindow = AddAnnotationOptionWindow(PopUpManager.createPopUp(parentApplication.theBody, AddAnnotationOptionWindow, true));
			w.oName.text = oName;
			w.idProperty = this.idProperty;
			PopUpManager.centerPopUp(w);
			
			this.systemManager.removeEventListener(AnnotationOptionAddEvent.ANNOTATION_OPTION_ADD_EVENT, onOptionRefreshed);		                                  
			this.systemManager.addEventListener(AnnotationOptionAddEvent.ANNOTATION_OPTION_ADD_EVENT, onOptionRefreshed);		                                  
			
			addOptionOpen = false;
		}
		
		private function onOptionRefreshed(event:AnnotationOptionAddEvent):void {
			this.setDataProvider();
			this.systemManager.removeEventListener(AnnotationOptionAddEvent.ANNOTATION_OPTION_ADD_EVENT, onOptionRefreshed);		                                  
			
			data[dataField] = '';
			for each (var o:Object in this.dataProvider) {
				if (o[labelField] == event.nameAdded) {
					selectedItem = o;
					data[dataField] = selectedItem[valueField];
					break;
				}
			}
		}

		override protected function focusOutHandler(event:FocusEvent):void
		{
			super.focusOutHandler(event);

			// kludge because focus out called twice.
			if (addOptionOpen) {
				return;
			}
			
			if (this.selectedItem == null && this.addOptionOnFly && this.text != null && this.text.length > 0 && this.text != this.prompt) {
				addOptionOpen = true;
				var oName:String = this.text;
				this.text = '';
				callLater(addOption,[oName]);
				return;
			}
			
			if( this.selectedItem!=null ){
				data[dataField] = this.selectedItem[valueField];
			}  else {
				data[dataField] = '';
			}
		}
		
		override protected function focusInHandler(event:FocusEvent):void
		{
			if (!this.dataProviderSet) {
				this.setDataProvider();
			}
			if (this.data != null) {
				selectTheItem();
			}
		}
		
		protected function selectTheItem():void {
			for (var i : int = 0; i < dataProvider.length; i++) {
				var item:Object = dataProvider[i];
				if(item[valueField] == data[dataField]) {
					this.selectedIndex = i;
					
					break;
				}
			}
			
		}
	}
}