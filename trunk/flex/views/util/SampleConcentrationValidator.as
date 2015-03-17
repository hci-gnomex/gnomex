package views.util
{
	import mx.collections.XMLListCollection;
	import mx.events.ValidationResultEvent;
	import mx.validators.NumberValidator;
	import mx.validators.ValidationResult;
	
	import views.util.SampleConcentrationValidator;

	public class SampleConcentrationValidator
	{
		private static var _instance:SampleConcentrationValidator = new views.util.SampleConcentrationValidator();
		
		private var concentrationValidator:NumberValidator = null;
		
		/*
		 * Note that flex does not allow private constructors.  Don't call this constructor -- use SampleConcentrationValidator.instance instead.
		 */
		public function SampleConcentrationValidator() {
			if (instance) {
				throw new Error("Please do not instantiate SampleConcentrationValidator.  Use SampleConcentrationValidator.instance instead.");
			}
			concentrationValidator = new NumberValidator();
			concentrationValidator.domain = "real";
			concentrationValidator.minValue = "0";
			concentrationValidator.maxValue = "99999";
			concentrationValidator.precision = 3;
			concentrationValidator.allowNegative = false;
			concentrationValidator.required = false;
			concentrationValidator.exceedsMaxError = "exceeds max.";
			concentrationValidator.invalidCharError = "contains invalid characters. Please enter a numeric value.";
			concentrationValidator.negativeError = "cannot be negative.";
			concentrationValidator.precisionError = "has too many digits beyond the decimal point.";
		}
			
		public static function get instance():SampleConcentrationValidator {
			return _instance;
		} 
		
		/*
		 * If all well returns a blank string, otherwise returns text for the error.
		 */
		public function validateSampleConcentration(samples:XMLListCollection):String {
			var ret:String = "";
			for each (var s1:Object in samples) {
				var vr:ValidationResultEvent = concentrationValidator.validate(s1.@concentration);
				if (vr.type == ValidationResultEvent.INVALID) {
					ret += "Invalid sample concentration '" + s1.@concentration + "'.  Please insure value is numeric with no more than 3 places to the right of the decimal."; 
					break;
				}
			}
			
			return ret;
		}

	}
}