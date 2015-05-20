/*
 * Created on Jun 20, 2006
 * 
 * $Id: XDRNamedPipe.java,v 1.1 2012-10-29 22:29:47 HCI\rcundick Exp $
 */
package lia.util.net.copy.monitoring.lisa.xdr;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 
 * @author Adrian Muraru
 */
public class XDRNamedPipe extends XDRGenericComm {

	protected File pipeIn;
	protected File pipeOut;

	public XDRNamedPipe(String pipeNameIn, String pipeNameOut) throws IOException {
		super("XDRNamedPipe for [ " + pipeNameIn + " - " + pipeNameOut + " ] ", new XDROutputStream(new FileOutputStream(new File(pipeNameOut))), new XDRInputStream(
				new FileInputStream(new File(pipeNameIn))));
	}

	//create a pipe (not open by default)
	public XDRNamedPipe(File pipeIn, File pipeOut) {
		super("XDRNamedPipe for [ " + pipeIn + " - " + pipeOut + " ] ");
		this.pipeIn = pipeIn;
		this.pipeOut=pipeOut;
	}
	
	@Override
	protected void initSession() throws Exception {
		this.xdris = new XDRInputStream(new FileInputStream(this.pipeIn));
		this.xdros = new XDROutputStream(new FileOutputStream(pipeOut));
		super.closed = false;
	}

	@Override
	protected void notifyXDRCommClosed() {

	}

	@Override
	protected void xdrSession() throws Exception {
		// nothing to do on pipes

	}

}