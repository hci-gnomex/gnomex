package ch.ethz.ssh2.signature;

import java.math.BigInteger;

/**
 * RSAPublicKey.
 * 
 * @author Christian Plattner, plattner@inf.ethz.ch
 * @version $Id: RSAPublicKey.java,v 1.1 2012-10-29 22:30:11 HCI\rcundick Exp $
 */
public class RSAPublicKey
{
	BigInteger e;
	BigInteger n;

	public RSAPublicKey(BigInteger e, BigInteger n)
	{
		this.e = e;
		this.n = n;
	}

	public BigInteger getE()
	{
		return e;
	}

	public BigInteger getN()
	{
		return n;
	}
}