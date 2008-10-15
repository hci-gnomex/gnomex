package views.util
{
	import mx.controls.TextInput;
	import mx.validators.NumberValidator;
	import mx.validators.ValidationResult;

	public class FragmentSizeRangeValidator extends NumberValidator
	{
        // Define Array for the return value of doValidation().
        private var results:Array;
        
        [Bindable]
        public var sizeRangeLowField:TextInput;

        [Bindable]
        public var sizeRangeHiField:TextInput;

		public function FragmentSizeRangeValidator()
		{
			super();
		}

    
        // Define the doValidation() method.
        override protected function doValidation(value:Object):Array {

            // Clear results Array.
            results = [];

            // Call base class doValidation().
            results = super.doValidation(value);        
            // Return if there are errors.
            if (results.length > 0) {
                return results;            	
            }
        
            if (sizeRangeLowField.text != '' && sizeRangeHiField.text != '') {
            	
            	var hi:Number = Number(this.sizeRangeHiField.text);
            	var lo:Number = Number(this.sizeRangeLowField.text);
            	if (lo >= hi) {
                	results.push(new ValidationResult(true, null, "invalidRange", 
                    	"Please enter a valid range.  The fragment size 'from' should be lower than the fragment size 'to'."));
                	return results;
             	}
            }        

            
            
            return results;
        }
		
	}
}