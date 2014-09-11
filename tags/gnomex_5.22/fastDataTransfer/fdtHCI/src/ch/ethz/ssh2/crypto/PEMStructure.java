
package ch.ethz.ssh2.crypto;

/**
 * Parsed PEM structure.
 * 
 * @author Christian Plattner, plattner@inf.ethz.ch
 * @version $Id: PEMStructure.java,v 1.1 2012-10-29 22:29:35 HCI\rcundick Exp $
 */

public class PEMStructure
{
	int pemType;
	String dekInfo[];
	String procType[];
	byte[] data;
}