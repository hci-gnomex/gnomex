package views.renderers
{
	import flash.events.Event;
	
	import hci.flex.renderers.RendererFactory;
	
	import mx.core.IFactory;
	
	import views.admin.PropertyEvent;

	public class DropdownLabelAnnotation extends DropdownLabel
	{
		private var _idProperty:String;
		private var _parentApp:Object;
		
		public static function create(labelField:String,
									  valueField:String,
									  dataField:String,
									  idProperty:String,
									  parentApp:Object,
									  isRequired:Boolean=false,
									  isEditable:Boolean=false,
									  missingRequiredFieldBackground:uint = 0xeaeaea
		):IFactory {
			return RendererFactory.create(views.renderers.DropdownLabelAnnotation, { 
				labelField: labelField,
				valueField: valueField,
				dataField: dataField,
				idProperty: idProperty,
				parentApp: parentApp,
				isRequired:isRequired,
				isEditable:isEditable,
				missingRequiredFieldBackground: missingRequiredFieldBackground
			});			
		}

		public function get parentApp():Object {
			return _parentApp;
		}
		
		public function set parentApp(value:Object):void {
			_parentApp = value;
			setDataProvider();
		}
		
		public function get idProperty():String
		{
			return _idProperty;
		}
		
		public function set idProperty(value:String):void
		{
			_idProperty = value;
			setDataProvider();
		}
		
		protected function setDataProvider():void {
			dataProvider = new XMLList();
			
			if ( idProperty != null &&  parentApp != null){
				dataProvider = parentApp.getPropertyOptions(idProperty, true);
				parentApp.removeEventListener(PropertyEvent.DATA_REFRESHED, onPropertyRefreshed);
				parentApp.addEventListener(PropertyEvent.DATA_REFRESHED, onPropertyRefreshed);
			}
		}

		public function DropdownLabelAnnotation()
		{
			super();
		}
		
		private function onPropertyRefreshed(event:Event):void {
			setDataProvider();
		}
	}
}