package views.util {
	
	import mx.collections.XMLListCollection;
	import mx.events.ValidationResultEvent;
	import mx.validators.NumberValidator;
	import mx.validators.ValidationResult;
	
	public class SampleQCValidator {
			
		private static var _instance:SampleQCValidator = new views.util.SampleQCValidator();
		
		private var validator:NumberValidator = null;
		
		/*
		* Note that flex does not allow private constructors. Don't call this constructor -- use SampleQCValidator.instance instead.
		*/
		public function SampleQCValidator() {
			if (instance) {
				throw new Error("Please do not instantiate SampleQCValidator. Use SampleQCValidator.instance instead.");
			}
			
			validator = new NumberValidator();
			validator.domain = "real";
			validator.minValue = "0";
			validator.maxValue = "9.99";
			validator.precision = 2;
			validator.allowNegative = false;
			validator.required = false;
			validator.exceedsMaxError = "exceeds max.";
			validator.invalidCharError = "contains invalid characters. Please enter a numeric value.";
			validator.negativeError = "cannot be negative.";
			validator.precisionError = "has too many digits beyond the decimal point.";
		}
		
		public static function get instance():SampleQCValidator {
			return _instance;
		} 
		
		/*
		* If all well returns a blank string, otherwise returns text for the error.
		*/
		public function validateSampleQC(samples:XMLListCollection):String {
			var ret:String = "";
			for each (var s1:Object in samples) {
				var vr:ValidationResultEvent = null;
				var ratio:String = null;
				
				if (s1.hasOwnProperty("@qual260nmTo280nmRatio") && s1.@qual260nmTo280nmRatio != "") {
					vr = validator.validate(s1.@qual260nmTo280nmRatio);
					ratio = s1.@qual260nmTo280nmRatio;
				} else if (s1.hasOwnProperty("@qual260nmTo230nmRatio") && s1.@qual260nmTo230nmRatio != "") {
					vr = validator.validate(s1.@qual260nmTo230nmRatio);
					ratio = s1.@qual260nmTo230nmRatio;
				}
				
				if (vr != null && vr.type == ValidationResultEvent.INVALID && ratio != null) {
					ret += "Invalid sample QC '" + ratio + "'.  Please ensure value is numeric with no more than 1 place to the left and 2 places to the right of the decimal."; 
					break;
				}
			}
			
			return ret;
		}	
	}
	
}