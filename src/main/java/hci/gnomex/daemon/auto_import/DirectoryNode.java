package hci.gnomex.daemon.auto_import;

public class DirectoryNode {
	private int depth;
	private String name;
	
	public DirectoryNode(int depth, String name){
		this.depth = depth;
		this.name = name;
	}
	
	public int getDepth() {
		return depth;
	}
	public String getName() {
		return name;
	}
	public void setDepth(int depth) {
		this.depth = depth;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	
	
}
