package views.util
{
	import views.util.AdvancedDataGridToolTipColumn;
	 
	public class AnnotationAdvancedDataGridColumn extends AdvancedDataGridToolTipColumn
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