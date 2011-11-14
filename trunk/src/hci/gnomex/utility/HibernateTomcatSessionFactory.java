package hci.gnomex.utility;

import java.util.Hashtable;
import java.util.Enumeration;
import javax.naming.Name;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.RefAddr;
import javax.naming.spi.ObjectFactory;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
 
public class HibernateTomcatSessionFactory implements ObjectFactory{
   public Object getObjectInstance(Object obj, Name name, Context cntx, Hashtable env) 
                 throws NamingException{
 
      SessionFactory sessionFactory = null;
      RefAddr addr = null;
      
      try{
         Enumeration addrs = ((Reference)(obj)).getAll();
            
         while(addrs.hasMoreElements()){
            addr = (RefAddr) addrs.nextElement();
            if("configuration".equals((String)(addr.getType()))){
               sessionFactory = (new Configuration())
                    .configure((String)addr.getContent()).buildSessionFactory();
            }
         }
      }catch(Exception ex){
         throw new javax.naming.NamingException(ex.getMessage());
      }
 
      return sessionFactory;
   }
}

