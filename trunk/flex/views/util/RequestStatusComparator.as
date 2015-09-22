package views.util {
	
	public class RequestStatusComparator {
		
		public function RequestStatusComparator() {
		}
		
		public function compare(rs1:String, rs2:String):int {
			var value1:int = determineWorkflowValue(rs1);
			var value2:int = determineWorkflowValue(rs2);
			
			if (value1 == -1 && value2 != -1) {
				return -1;
			} else if (value1 != -1 && value2 == -1) {
				return 1;
			} else if (value1 == -1 && value2 == -1) {
				return 0;
			} else {
				return value1 - value2;
			}
		}
		
		private function determineWorkflowValue(requestStatus:String):int {
			var value:int = -1;
			
			if (requestStatus != null && requestStatus != "") {
				if (requestStatus == "NEW") {
					value = 0;
				} else if (requestStatus == "SUBMITTED") {
					value = 1;
				} else if (requestStatus == "PROCESSING") {
					value = 2;
				} else if (requestStatus == "COMPLETE") {
					value = 3;
				} else if (requestStatus == "FAILED") {
					value = 3;
				}
			}
			
			return value;
		}
		
	}
	
}