package hci.gnomex.utility;

import hci.gnomex.model.RequestStatus;

import java.util.Comparator;

public class RequestStatusComparator implements Comparator<String> {

	@Override
	public int compare(String rs1, String rs2) {
		int value1 = determineWorkflowValue(rs1);
		int value2 = determineWorkflowValue(rs2);
		
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
	
	public boolean isTerminationStatus(String status) {
		if (status != null) {
			return status.equals(RequestStatus.COMPLETED) || status.equals(RequestStatus.FAILED);
		}
		
		return false;
	}
	
	private int determineWorkflowValue(String requestStatus) {
		int value = -1;
		
		if (requestStatus != null && !requestStatus.trim().equals("")) {
			if (requestStatus.equals(RequestStatus.NEW)) {
				value = 0;
			} else if (requestStatus.equals(RequestStatus.SUBMITTED)) {
				value = 1;
			} else if (requestStatus.equals(RequestStatus.PROCESSING)) {
				value = 2;
			} else if (requestStatus.equals(RequestStatus.COMPLETED)) {
				value = 3;
			} else if (requestStatus.equals(RequestStatus.FAILED)) {
				value = 3;
			}
		}
		
		return value;
	}

}
