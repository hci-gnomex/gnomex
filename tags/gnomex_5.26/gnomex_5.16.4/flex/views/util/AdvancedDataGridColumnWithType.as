package views.util
{
	import mx.controls.advancedDataGridClasses.AdvancedDataGridColumn;
	
	public class AdvancedDataGridColumnWithType extends AdvancedDataGridColumn
	{
		/**
		 *  Field Type: TEXT (default), CHECK, MOPTION, OPTION, or URL
		 * 
		 *  
		 *  @default false
		 */
		public var propertyType:String = 'Text';
		
		public function AdvancedDataGridColumnWithType(columnName:String=null)
		{
			super(columnName);
		}
	}
}