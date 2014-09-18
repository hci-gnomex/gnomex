package hci.gnomex.utility;

import java.io.Serializable;

import hci.gnomex.model.Topic;
import hci.gnomex.model.Visibility;

public class TopicTreeLinkInfo implements Serializable {
  private Integer idTopic;
  private String number;
  private Integer treeLevel;
  private String name;
  private String description;
  private Boolean isPublic;
  private String owner;
  private Boolean hasChildren;
  private Integer numExperiments;
  private Integer numAnalyses;
  private String lab;

  public TopicTreeLinkInfo(Topic topic, Integer treeLevel) {
    idTopic = topic.getIdTopic();
    number = topic.getNumber();
    this.treeLevel = treeLevel;
    name = topic.getName();
    description = topic.getDescription();
    isPublic = topic.getCodeVisibility().equals(Visibility.VISIBLE_TO_PUBLIC);
    owner = topic.getAppUser().getDisplayName();
    hasChildren = topic.getTopics().size() > 0;
    numExperiments = topic.getRequests() == null ? 0 : topic.getRequests().size();
    numAnalyses = topic.getAnalyses() == null ? 0 : topic.getAnalyses().size();
    lab = topic.getLab().getName();
  }
  
  public Integer getIdTopic() {
    return idTopic;
  }
  
  public String getNumber() {
    return number;
  }
  
  public Integer getTreeLevel() {
    return treeLevel;
  }
  
  public String getName() {
    return name;
  }
  
  public String getDescription() { 
    if (description == null || description.trim().length() == 0) {
      return "No Description";
    }  else {
      return description.replace("\"", "&quot;");
    }
  }
  
  public Boolean getIsPublic() {
    return isPublic;
  }
  
  public String getOwner() {
    return owner;
  }
  
  public Boolean getHasChildren() {
    return hasChildren;
  }
  
  public Integer getNumExperiments() {
    return numExperiments;
  }
  
  public Integer getNumAnalyses() {
    return numAnalyses;
  }
  
  public String getLab() {
    return lab;
  }
  
  public String getURL() {
    StringBuffer linkBuf = new StringBuffer();
    if (getIsPublic()) {
      linkBuf.append("/gnomex/gnomexGuestFlex.jsp?topicNumber=" + getIdTopic().toString());
    } else {
      linkBuf.append("/gnomex/gnomexFlex.jsp?topicNumber=" + getIdTopic().toString());
    }

    return linkBuf.toString();
  }
}
