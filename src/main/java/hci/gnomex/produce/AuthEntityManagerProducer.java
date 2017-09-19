package hci.gnomex.produce;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import hci.ri.auth.annotation.AuthDatasource;

/* GNomEx doesn't use the Auth model */
public class AuthEntityManagerProducer {

	@Produces
	@AuthDatasource
	private EntityManager getEm() {
		return null;
	}

}