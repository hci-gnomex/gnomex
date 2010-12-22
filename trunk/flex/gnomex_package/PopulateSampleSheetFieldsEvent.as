package gnomex_package
{
	import flash.events.Event;

	public class PopulateSampleSheetFieldsEvent extends Event
	{
		private var _sampleNameArray:Array;
		private var _sampleConcentrationArray:Array;
		private var _sampleDescriptionArray:Array;
		private var _sampleCCNumberArray:Array;
		
		public function PopulateSampleSheetFieldsEvent(
			sampleNameArray:Array,
			sampleConcentrationArray:Array,
			sampleDescriptionArray:Array,
			sampleCCNumberArray:Array)
		{
			super("PopulateSampleSheetFieldsEvent", true, true);
			_sampleNameArray = sampleNameArray;
			_sampleConcentrationArray = sampleConcentrationArray;
			_sampleDescriptionArray = sampleDescriptionArray;
			_sampleCCNumberArray = sampleCCNumberArray;
		}
		
		public function get sampleNameArray():Array{
           return _sampleNameArray;
		}
		
		public function get sampleConcentrationArray():Array{
           return _sampleConcentrationArray;
		}
		
		public function get sampleDescriptionArray():Array{
           return _sampleDescriptionArray;
		}
		
		public function get sampleCCNumberArray():Array{
           return _sampleCCNumberArray;
		}
		
		public override function clone():Event {      
     		return new PopulateSampleSheetFieldsEvent(
     			_sampleNameArray, _sampleConcentrationArray,
     			_sampleDescriptionArray, _sampleCCNumberArray);
    	}		
	}
}