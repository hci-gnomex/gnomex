package hci.gnomex.model;

import java.io.File;

public class UCSCLinkFiles {
	//fields
	private File[] filesToLink;
	private boolean converting;
	private boolean stranded;
	
	//constructor
	public UCSCLinkFiles(File[] filesToLink, boolean converting, boolean stranded){
		this.filesToLink = filesToLink;
		this.converting = converting;
		this.setStranded(stranded);
	}

	public File[] getFilesToLink() {
		return filesToLink;
	}

	public void setFilesToLink(File[] filesToLink) {
		this.filesToLink = filesToLink;
	}

	public boolean isConverting() {
		return converting;
	}

	public void setConverting(boolean converting) {
		this.converting = converting;
	}

	public void setStranded(boolean stranded) {
		this.stranded = stranded;
	}

	public boolean isStranded() {
		return stranded;
	}
	
}
