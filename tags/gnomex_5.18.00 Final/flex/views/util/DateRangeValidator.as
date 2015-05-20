package views.util
{
	import mx.validators.ValidationResult;
	import mx.validators.Validator;
	
	
	public class DateRangeValidator extends Validator {
		
		private var results:Array;
		private var _fromDate:Date;
		private var _toDate:Date;
		
		public function get fromDate():Date{
			return _fromDate
		}
		public function set fromDate(value:Date):void{
			_fromDate = value;
		}
		
		public function get toDate():Date{
			return _toDate
		}
		public function set toDate(value:Date):void{
			_toDate = value;
		}
		
		public function DateRangeValidator() {
			super();
		}
		
		override protected function doValidation(rangeSelected:Object):Array {
			
			results = [];
			
			var startDate:Date = new Date();
			startDate = fromDate;
			
			var endDate:Date = new Date();
			endDate = toDate;
			
			if(startDate == null){
				results.push(new ValidationResult(true, "", "noStartDate", "You must choose a start date."));
				return results;
			}
			
			if(endDate == null){
				results.push(new ValidationResult(true, "", "invalidEndDate", "You must choose an end date."));
				return results;
			}
			
			if ( endDate.getTime() <= startDate.getTime() ) {
				results.push(new ValidationResult(true, "", "invalidEndDate", "The end date must be later than the start date."));
				return results;
			}
			
			return results;
		}
		
	}
}