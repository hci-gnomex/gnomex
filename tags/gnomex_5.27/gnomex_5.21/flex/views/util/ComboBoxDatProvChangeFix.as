package views.util
{
	import mx.controls.ComboBox;
	
	public class ComboBoxDatProvChangeFix extends ComboBox
	{
		public function ComboBoxDatProvChangeFix()
		{
			super();
		}
		
		override public function set dataProvider(value:Object):void
		{
			// The dropdown will not be properly reset unless it is currently shown.
			open();
			super.dataProvider = value;
		}
	}
}