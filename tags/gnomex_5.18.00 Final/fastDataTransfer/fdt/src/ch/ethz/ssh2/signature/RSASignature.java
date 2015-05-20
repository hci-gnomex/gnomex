
package ch.ethz.ssh2.signature;

import java.math.BigInteger;


/**
 * RSASignature.
 * 
 * @author Christian Plattner, plattner@inf.ethz.ch
 * @version $Id: RSASignature.java,v 1.1 2012-10-29 22:30:09 HCI\rcundick Exp $
 */

public class RSASignature
{
	BigInteger s;

	public BigInteger getS()
	{
		return s;
	}

	public RSASignature(BigInteger s)
	{
		this.s = s;
	}
}