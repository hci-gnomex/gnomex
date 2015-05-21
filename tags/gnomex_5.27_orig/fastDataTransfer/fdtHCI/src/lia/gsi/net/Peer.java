/*
 * $Id: Peer.java,v 1.1 2012-10-29 22:29:57 HCI\rcundick Exp $
 */
package lia.gsi.net;

import java.net.Socket;

import javax.security.auth.Subject;

import lia.gsi.authz.LocalMappingAuthorization;

/**
 * Extended Socket with an optional Subject attribute. The Subject field may contain additional Principal such as: GridID,UserLocalPrincipal
 * @author Adrian Muraru
 */
public class Peer {
	private Socket socket=null;
	private  LocalMappingAuthorization authorization = null;
	
	public Peer(Socket socket, LocalMappingAuthorization authz) {
		this.socket=socket;
		this.authorization = authz;		
	}
	public Socket getSocket() {
		return this.socket;
	}
	
	public Subject  getPeerSubject() {
		return this.authorization==null? null: this.authorization.getPeerSubject();
	}
	
}