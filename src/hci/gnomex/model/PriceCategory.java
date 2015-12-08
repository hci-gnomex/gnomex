package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.hibernate.Session;



public class PriceCategory extends DictionaryEntry implements Comparable, Serializable {
  
  private Integer  idPriceCategory;
  private String   name;
  private String   description;
  private String   pluginClassName;
  private String   codeBillingChargeKind;
  private String   dictionaryClassNameFilter1;
  private String   dictionaryClassNameFilter2;
  private String   isActive;
  private Set      prices = new TreeSet();
  private Set      steps;
  
  public String getDisplay() {
    String display = this.getNonNullString(getName());
    return display;
  }

  public String getValue() {
    return getIdPriceCategory().toString();
  }


  public Integer getIdPriceCategory() {
    return idPriceCategory;
  }

  
  public void setIdPriceCategory(Integer idPriceCategory) {
    this.idPriceCategory = idPriceCategory;
  }
  
  public String getDescription() {
    return description;
  }

  
  public void setDescription(String description) {
    this.description = description;
  }

  
  public String getPluginClassName() {
    return pluginClassName;
  }

  
  public void setPluginClassName(String pluginClassName) {
    this.pluginClassName = pluginClassName;
  }

  
  public String getCodeBillingChargeKind() {
    return codeBillingChargeKind;
  }

  
  public void setCodeBillingChargeKind(String codeBillingChargeKind) {
    this.codeBillingChargeKind = codeBillingChargeKind;
  }

  
  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }

  
  public String getName() {
    return name;
  }

  
  public void setName(String name) {
    this.name = name;
  }

  
  public String getDictionaryClassNameFilter1() {
    return dictionaryClassNameFilter1;
  }

  
  public void setDictionaryClassNameFilter1(String dictionaryClassNameFilter1) {
    this.dictionaryClassNameFilter1 = dictionaryClassNameFilter1;
  }

  
  public String getDictionaryClassNameFilter2() {
    return dictionaryClassNameFilter2;
  }

  
  public void setDictionaryClassNameFilter2(String dictionaryClassNameFilter2) {
    this.dictionaryClassNameFilter2 = dictionaryClassNameFilter2;
  }

  
  public Set getPrices() {
    return prices;
  }

  
  public void setPrices(Set prices) {
    this.prices = prices;
  }

  public Set getSteps() {
    return steps;
  }
  public void setSteps(Set steps) {
    this.steps = steps;
  }


  public int compareTo(Object o) {
    if (o instanceof PriceCategory) {
      PriceCategory other = (PriceCategory)o;
      return this.getIdPriceCategory().compareTo(other.getIdPriceCategory());
    } else {
      return -1;
    }
  }

  public static List getPriceSheetPriceCategoryForPriceCategory( PriceCategory priceCategory, Session sess ) {
    StringBuffer buf = new StringBuffer();
    buf.append( "SELECT pspc from PriceSheetPriceCategory pspc " );
    buf.append( "WHERE  pspc.idPriceCategory = " + priceCategory.getIdPriceCategory().toString() );
    List pspcList = sess.createQuery( buf.toString() ).list();
    return pspcList;
  }

  public static void deletePriceSheetPriceCategoryEntries( PriceCategory priceCategory, Session sess ) {
    for(Iterator i = PriceCategory.getPriceSheetPriceCategoryForPriceCategory(priceCategory, sess ).iterator(); i.hasNext();) {
      PriceSheetPriceCategory x = (PriceSheetPriceCategory)i.next();
      sess.delete( x );
    }
    sess.flush();
  }

}