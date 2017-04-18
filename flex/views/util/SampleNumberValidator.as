package views.util
{
	import mx.collections.XMLListCollection;
	import mx.events.ValidationResultEvent;
	import mx.validators.NumberValidator;
	import mx.validators.ValidationResult;


	public class SampleNumberValidator
	{
		
		private var decimalValidator:NumberValidator;
        private var integerValidator:NumberValidator;

		public function SampleNumberValidator() {

            integerValidator = new NumberValidator();
            integerValidator.domain = "int";
            integerValidator.integerError = "Integers only permitted";
            integerValidator.allowNegative = false;
            integerValidator.invalidCharError ="Numerical characters only permitted";
            integerValidator.negativeError="Positive integers only permitted";
            integerValidator.maxValue = "99999"
			integerValidator.exceedsMaxError = "The number entered is too large"

			decimalValidator = new NumberValidator();
			decimalValidator.domain = "real";
            decimalValidator.allowNegative = false;
            decimalValidator.required = false;
            decimalValidator.invalidCharError = "Numerical characters only permitted";
			decimalValidator.exceedsMaxError = "The number entered is too large";


		}


		public function hasCommas(item:String):String{
			if(item.search(',') != -1){
				return "Commas are not allowed";
			}
			return null;
		}

		public function validateDecimal(item:String,maxSize:Number):String {
			decimalValidator.maxValue = maxSize;

            var commaMessage:String = hasCommas(item);
			if(commaMessage != null){
				return commaMessage;
			}
			var vr:ValidationResultEvent = decimalValidator.validate(item);
				//vr.message
			if (vr.type == ValidationResultEvent.INVALID) {
					return vr.message;
				}
			
			return null;
		}
        public function validateInteger(item:String,required:Boolean = false):String{
			integerValidator.required = required;
            var commaMessage:String = hasCommas(item);
            if(commaMessage != null){
                return commaMessage;
            }
            var vr: ValidationResultEvent = integerValidator.validate(item);
            if(vr.type == ValidationResultEvent.INVALID){
                return vr.message;
            }
			else{ // validator allows 8.00 as an integer
				var p:RegExp = /\./
				var errorMessage:String = item.search(p) != -1 ? "Integers only permitted" : null;
				return errorMessage;
			}
            return null;
        }

	}
}