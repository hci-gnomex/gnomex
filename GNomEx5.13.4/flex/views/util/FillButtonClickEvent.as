package views.util
{
	import flash.events.Event;
	
	import mx.controls.dataGridClasses.DataGridListData;
	
	public class FillButtonClickEvent extends Event
	{
		public var listData:DataGridListData = null;
		public var value:String = null;

		public function FillButtonClickEvent(_listData:DataGridListData, _value:String)
		{
			listData = _listData;
			value = _value;
			super("FillButtonClickEvent", true, false);
		}
	}
}