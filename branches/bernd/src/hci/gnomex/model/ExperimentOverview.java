package hci.gnomex.model;



import java.sql.Date;

import hci.hibernate3utils.HibernateDetailObject;



public class ExperimentOverview extends HibernateDetailObject {
  
	  // Criteria / Argumented mx:request variables
  private String				workflow;
  private String				expType;
  private String				requestUser;
  private String				seqType;
  private String				seqLength;
  private String				seqInstrument;
  private String				experimentId;
  private String				lab;
  private String				coreFacilityId;
  
  
	public String getWorkflow() {
		return workflow;
	}
	
	public void setWorkflow(String Workflow) {
		this.workflow = Workflow;
	}	

	public String getExpType() {
		return expType;
	}
	
	public void setExpType(String ExpType) {
		this.expType = ExpType;
	}
	
	public String getRequester() {
		return requestUser;
	}

	public void setRequester(String Requester) {
		this.requestUser = Requester;
	}
	
	public String getSeqType() {
		return seqType;
	}
	
	public void setSeqType(String SeqType) {
		this.seqType = SeqType;
	}

	public String getSeqLength() {
		return seqLength;
	}

	public void setSeqLength(String SeqLength) {
		this.seqLength = SeqLength;
	}
	
	public String getSeqInstrument() {
		return seqInstrument;
	}

	public void setSeqInstrument(String SeqInstrument) {
		this.seqInstrument = SeqInstrument;
	}

	public String getExperimentId() {
		return experimentId;
	}

	public void setExperimentId(String ExperimentId) {
		this.experimentId = ExperimentId;
	}

	public String getLab() {
		return lab;
	}

	public void setLab(String Lab) {
		this.lab = Lab;
	}

	public String getCoreFacilityId() {
		return coreFacilityId;
	}

	public void setCoreFacilityId(String coreFacilityId) {
		this.coreFacilityId = coreFacilityId;
	}

  /*private Integer         idWorkItem;
  private String          codeStepNext;
  private Date            createDate;
  private Integer         idRequest;
  private Request         request;
  private Sample          sample;
  private LabeledSample   labeledSample;
  private Hybridization   hybridization;
  private SequenceLane    sequenceLane;
  private FlowCellChannel flowCellChannel;
  private String          status;
  private Integer         idCoreFacility; */
  
 
 
}