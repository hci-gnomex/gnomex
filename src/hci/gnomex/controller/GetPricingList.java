package hci.gnomex.controller;

import hci.dictionary.utility.DictionaryManager;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.model.Price;
import hci.gnomex.model.PriceCategory;
import hci.gnomex.model.PriceCriteria;
import hci.gnomex.model.PriceSheet;
import hci.gnomex.model.PriceSheetPriceCategory;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.apache.log4j.Logger;
public class GetPricingList extends GNomExCommand implements Serializable {

  private String showInactive = "N";
  private String showPrices = "Y";
  private String showPriceCriteria = "N";
  private String idCoreFacility = "";

  // the static field for logging in Log4J
  private static Logger LOG = Logger.getLogger(GetPricingList.class);

  public void validate() {
  }

  public void loadCommand(HttpServletRequest request, HttpSession session) {

    if (request.getParameter("showInactive") != null) {
      showInactive = request.getParameter("showInactive");
    }

    if (request.getParameter("showPrices") != null) {
      showPrices = request.getParameter("showPrices");
    }

    if (request.getParameter("showPriceCriteria") != null) {
      showPriceCriteria = request.getParameter("showPriceCriteria");
    }

    if (request.getParameter("idCoreFacility") != null) {
      idCoreFacility = request.getParameter("idCoreFacility");
    }

    if (isValid()) {
      setResponsePage(this.SUCCESS_JSP);
    } else {
      setResponsePage(this.ERROR_JSP);
    }

  }

  public Command execute() throws RollBackCommandException {

    try {

      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());

      if (this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_MANAGE_BILLING)) {

        Document doc = new Document(new Element("Pricing"));

        StringBuffer buf = new StringBuffer();
        buf.append("SELECT distinct p from PriceSheet p ");
        if (!this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_ADMINISTER_ALL_CORE_FACILITIES) || this.idCoreFacility.length() > 0) {
          buf.append("JOIN p.requestCategories rc ");
          buf.append("WHERE ");
          if (this.idCoreFacility.length() > 0) {
            buf.append("(rc.idCoreFacility = ").append(this.idCoreFacility).append(")");
          } else {
            this.getSecAdvisor().appendCoreFacilityCriteria(buf, "rc");
          }
          buf.append(" ");
        }
        buf.append("order by p.name");
        List priceSheets = sess.createQuery(buf.toString()).list();

        for (Iterator i = priceSheets.iterator(); i.hasNext();) {
          PriceSheet priceSheet = (PriceSheet) i.next();

          if (showInactive.equals("N")) {
            if (priceSheet.getIsActive() != null && priceSheet.getIsActive().equals("N")) {
              continue;
            }
          }

          priceSheet.excludeMethodFromXML("getPriceCategories");
          priceSheet.excludeMethodFromXML("getRequestCategories");

          Element priceSheetNode = priceSheet.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();
          doc.getRootElement().addContent(priceSheetNode);

          Set categories = priceSheet.getPriceCategories();
          if (categories != null) {
            for (Iterator i1 = categories.iterator(); i1.hasNext();) {
              PriceSheetPriceCategory x = (PriceSheetPriceCategory) i1.next();
              PriceCategory priceCat = x.getPriceCategory();

              if (showInactive.equals("N")) {
                if (priceCat.getIsActive() != null && priceCat.getIsActive().equals("N")) {
                  continue;
                }
              }

              priceCat.excludeMethodFromXML("getPrices");
              priceCat.excludeMethodFromXML("getSteps");

              Element categoryNode = priceCat.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();
              priceSheetNode.addContent(categoryNode);

              Set prices = priceCat.getPrices();

              if (prices != null && showPrices.equals("Y")) {
                for (Iterator i2 = prices.iterator(); i2.hasNext();) {
                  Price price = (Price) i2.next();

                  price.excludeMethodFromXML("getPriceCriterias");

                  // Exclude inactive prices unless requested otherwise.
                  if (showInactive.equals("N")) {
                    if (price.getIsActive() != null && price.getIsActive().equals("N")) {
                      continue;
                    }
                  }

                  Element priceNode = price.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();
                  priceNode.setAttribute("category", categoryNode.getAttributeValue("description"));
                  priceNode.setAttribute("codeBillingChargeKind", categoryNode.getAttributeValue("codeBillingChargeKind"));
                  categoryNode.addContent(priceNode);

                  if (showPriceCriteria.equals("Y")) {
                    for (Iterator i3 = price.getPriceCriterias().iterator(); i3.hasNext();) {
                      PriceCriteria priceCriteria = (PriceCriteria) i3.next();

                      Element priceCriteriaNode = priceCriteria.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();

                      String label = "";
                      if (priceCat.getDictionaryClassNameFilter1() != null && !priceCat.getDictionaryClassNameFilter1().equals("")) {
                        label = DictionaryManager.getDisplay(priceCat.getDictionaryClassNameFilter1(), priceCriteria.getFilter1());
                      }
                      if (priceCat.getDictionaryClassNameFilter1() != null && !priceCat.getDictionaryClassNameFilter1().equals("")) {
                        label += " " + DictionaryManager.getDisplay(priceCat.getDictionaryClassNameFilter2(), priceCriteria.getFilter2());
                      }
                      priceCriteriaNode.setAttribute("display", label);
                      priceNode.addContent(priceCriteriaNode);
                    }

                  }

                }
              }
            }
          }

        }

        org.jdom.output.XMLOutputter out = new org.jdom.output.XMLOutputter();
        this.xmlResult = out.outputString(doc);

        setResponsePage(this.SUCCESS_JSP);

      } else {
        this.addInvalidField("insufficient permission", "Insufficient permission to access pricing");
      }
    } catch (NamingException e) {
      LOG.error("An exception has occurred in GetPricingList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());

    } catch (SQLException e) {
      LOG.error("An exception has occurred in GetPricingList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (XMLReflectException e) {
      LOG.error("An exception has occurred in GetPricingList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e) {
      LOG.error("An exception has occurred in GetPricingList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        this.getSecAdvisor().closeReadOnlyHibernateSession();
      } catch (Exception e) {

      }
    }

    if (isValid()) {
      setResponsePage(this.SUCCESS_JSP);
    } else {
      setResponsePage(this.ERROR_JSP);
    }

    return this;
  }

}