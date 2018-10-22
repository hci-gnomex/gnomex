package hci.gnomex.daemon.auto_import;

public class PersonEntry {


	private String mrn;
	private String shadowId;
	private String personId;
	private String fullName;
	private String gender; // n
	private String ccNumber;
	private String slNumber;
	private String aliasType;
	private String tissueType; // tissueType
	public final static String AVATAR = "avatar";
	public final static String FOUNDATION = "foundation";
	public final static String TEMPEST = "tempest";



	private String sampleSubtype;  // sampleSubtype;
	private String submittedDiagnosis; // submittedDiagnosis;

	
	PersonEntry(){
	
	}
	PersonEntry(String mrn, String fullName, String gender,String ccNumber,String aliasType, String tissueType,String sampleSubtype, String submittedDiagnosis,String slNumber,String shadowId,String personId ){
		this.mrn = mrn;
		this.shadowId = shadowId;
		this.personId = personId;
		this.fullName = fullName;
		this.gender = gender;
		this.ccNumber = ccNumber;
		this.aliasType = aliasType;
		this.slNumber = slNumber;
		this.tissueType = tissueType;
		this.sampleSubtype = sampleSubtype;
		this.submittedDiagnosis = submittedDiagnosis;
	}
	

	public String getMrn() {
		return mrn;
	}
	public void setMrn(String mrn) {
		this.mrn = mrn;
	}
	
	public String getShadowId() {
		return shadowId;
	}
	public void setShadowId(String shadowId) {
		this.shadowId = shadowId;
	}
	public String getPersonId() {
		return personId;
	}
	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getFullName() {
		return fullName;
	}


	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getGender() {
		return gender;
	}


	public void setGender(String gender) {
		this.gender = gender;
	}


	public String getCcNumber() {
		return ccNumber;
	}

	public void setCcNumber(String ccNumber) {
		this.ccNumber = ccNumber;
	}
	
	public String getSlNumber() {
		return slNumber;
	}
	public void setSlNumber(String slNumber) {
		this.slNumber = slNumber;
	}

	public String getAliasType() {
		return aliasType;
	}

	public void setAliasType(String aliasType) {
		this.aliasType = aliasType;
	}
	public String getTissueType() {
		return tissueType;
	}
	public void setTissueType(String tissueType) {
		this.tissueType = tissueType;
	}
	public String getSampleSubtype() {
		return sampleSubtype;
	}
	public void setSampleSubtype(String sampleSubtype) {
		this.sampleSubtype = sampleSubtype;
	}
	public String getSubmittedDiagnosis() {
		return submittedDiagnosis;
	}

	public void setSubmittedDiagnosis(String submittedDiagnosis) {
		this.submittedDiagnosis = submittedDiagnosis;
	}


	public String setEmptyToNull(String val){
		if(val.equals("")){
			return "null";
		}
		return val;
	}

	public String toString(String personType){
		StringBuilder strBuild = new StringBuilder();
		//order matters
		if(PersonEntry.AVATAR.equals(personType)){
			strBuild.append(setEmptyToNull(this.mrn) +"\t");
			strBuild.append(setEmptyToNull(this.personId) +"\t");
			strBuild.append(setEmptyToNull(this.fullName) +"\t");
			strBuild.append(setEmptyToNull(this.gender) +"\t");
			strBuild.append(setEmptyToNull(this.ccNumber) +"\t");
			strBuild.append(setEmptyToNull(this.shadowId) +"\t");
			strBuild.append(setEmptyToNull(this.aliasType) +"\t");
			strBuild.append(setEmptyToNull(this.slNumber) +"\t");
			strBuild.append(setEmptyToNull(this.tissueType) +"\t");
			strBuild.append(setEmptyToNull(this.sampleSubtype) +"\t");
			strBuild.append(setEmptyToNull(this.submittedDiagnosis) +"\n");
		}
		else if (PersonEntry.FOUNDATION.equals(personType)){
			strBuild.append(setEmptyToNull(this.mrn) +"\t");
			strBuild.append(setEmptyToNull(this.personId) +"\t");
			strBuild.append(setEmptyToNull(this.fullName) +"\t");
			strBuild.append(setEmptyToNull(this.gender) +"\t");
			strBuild.append(setEmptyToNull(this.shadowId) +"\t");
			strBuild.append(setEmptyToNull(this.aliasType) +"\t");
			strBuild.append(setEmptyToNull(this.slNumber) +"\t");
			strBuild.append(setEmptyToNull(this.sampleSubtype) +"\t");
			strBuild.append(setEmptyToNull(this.tissueType) +"\t");
			strBuild.append(setEmptyToNull(this.submittedDiagnosis) +"\n");
		}

		return strBuild.toString();

	}
	
	
	

}
