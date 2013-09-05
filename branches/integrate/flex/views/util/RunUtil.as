package views.util
{
	[Bindable]
	public class RunUtil {
		public var idRun:String = "0";
		public var label:String = "";
		public var comments:String = "";
		public var creator:String = "";
		public var createDate:String = "";
		public var runDate:String = "";
		public var codeSealType:String = "";
		public var codeInstrumentRunStatus:String = "";
		public var codeReactionType:String = "";
		
		public function RunUtil() {
		}
		public function resetRun():void{
			idRun = "0";
			label = "";
			comments = "";
			creator = "";
			createDate = "";
			runDate = "";
			codeSealType = "";
			codeInstrumentRunStatus = "";
			codeReactionType = "";
		}
	}
}