package hci.gnomex.controller;

import java.rmi.*;
import javax.ejb.*;

public interface SBGNomExRequestProcessorHome extends EJBHome {
  public SBGNomExRequestProcessor create() throws RemoteException, CreateException;
}