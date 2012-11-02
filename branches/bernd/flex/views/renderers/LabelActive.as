package views.renderers
{
	import hci.flex.renderers.RendererFactory;
	
	import mx.controls.Label;
	import mx.core.IFactory;
	
	public class LabelActive extends mx.controls.Label
	{   public var _dataField:String;	
		
		public static function create(dataField:String):IFactory {
			return RendererFactory.create(views.renderers.LabelActive,
				{_dataField: dataField});			
			
		}	
		
		public function set dataField(dataField:String):void {
			this._dataField = dataField;	
		}
		
		override public function set data(value:Object):void {
			if(value != null) {
				super.data = value;
				if(data.hasOwnProperty('@isActive') && value['@isActive'] != 'Y') {
					setStyle('color', 0x666666);
					setStyle('fontStyle', 'italic');
				}
				else {
					setStyle('color', 0x000000);
					setStyle('fontStyle', 'normal');
				}
			}
		}

	}
}