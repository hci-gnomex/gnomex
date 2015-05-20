package hci.gnomex.controller;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface SBGNomExRequestProcessorHome extends EJBHome {
  public SBGNomExRequestProcessor create() throws RemoteException, CreateException;
}