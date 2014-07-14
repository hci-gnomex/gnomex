package gui;

public class Request {
	
	//fields
	private String serverIPAddress;
	private String serverDirectory;
	private boolean isUpload;
	private int port = 54321;
	
	public Request (String serverIPAddress, String serverDirectory, boolean isUpload){
		this.serverIPAddress = serverIPAddress;
		this.serverDirectory = serverDirectory;
		this.isUpload = isUpload;
	}

	public String getServerIPAddress() {
		return serverIPAddress;
	}

	public String getServerDirectory() {
		return serverDirectory;
	}

	public boolean isUpload() {
		return isUpload;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}
