package views.util
{
	import mx.controls.advancedDataGridClasses.AdvancedDataGridColumn;
	 
	public class AnnotationAdvancedDataGridColumn extends AdvancedDataGridColumn
	{
		/**
		 *  Field Type: TEXT (default), CHECK, MOPTION, OPTION, or URL
		 * 
		 *  
		 *  @default false
		 */
		public var propertyType:String = 'Text';
		
		public function AnnotationAdvancedDataGridColumn(name:String)
		{
			super(name);
		}

	}
}