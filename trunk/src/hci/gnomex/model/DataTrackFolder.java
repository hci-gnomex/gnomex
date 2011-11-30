package hci.gnomex.model;

import hci.framework.model.DetailObject;
import hci.framework.security.UnknownPermissionException;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DataTrackUtil;
import hci.gnomex.utility.DictionaryHelper;

import java.io.Serializable;
import java.sql.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;


public class DataTrackFolder extends DetailObject implements Serializable {

  private Integer            idDataTrackFolder;
  private String             name;
  private String             description;
  private Integer            idParentDataTrackFolder;
  private DataTrackFolder    parentFolder;
  private Set                folders;
  private Set                dataTracks;
  private Integer            idLab;
  private Integer            idGenomeBuild;
  private String             createdBy;
  private Date               createDate;


  public Integer getIdDataTrackFolder() {
    return idDataTrackFolder;
  }
  public void setIdDataTrackFolder(Integer idDataTrackFolder) {
    this.idDataTrackFolder = idDataTrackFolder;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public Set getFolders() {
    return folders;
  }
  public void setFolders(Set folders) {
    this.folders = folders;
  }
  public Integer getIdParentDataTrackFolder() {
    return idParentDataTrackFolder;
  }
  public void setIdParentDataTrackFolder(Integer idParentDataTrackFolder) {
    this.idParentDataTrackFolder = idParentDataTrackFolder;
  }
  public Set getDataTracks() {
    return dataTracks;
  }
  public void setDataTracks(Set dataTracks) {
    this.dataTracks = dataTracks;
  }
  public DataTrackFolder getParentFolder() {
    return parentFolder;
  }
  public void setParentFolder(DataTrackFolder parentFolder) {
    this.parentFolder = parentFolder;
  }


  public boolean hasVisibility(String codeVisibility) {
    boolean hasVisibility = false;
    for(Iterator<?> i = this.dataTracks.iterator(); i.hasNext();) {
      DataTrack a = DataTrack.class.cast(i.next());
      if (a.getCodeVisibility().equals(codeVisibility)) {
        hasVisibility = true;
        break;
      }
    }
    return hasVisibility;
  }

  public Integer getIdGenomeBuild() {
    return idGenomeBuild;
  }
  public void setIdGenomeBuild(Integer idGenomeBuild) {
    this.idGenomeBuild = idGenomeBuild;
  }
  public Integer getIdLab() {
    return idLab;
  }
  public void setIdLab(Integer idLab) {
    this.idLab = idLab;
  } 

  /*
   * Get the type name (no genome version in path)
   */
   public String getQualifiedTypeName() {
    if (getIdParentDataTrackFolder() == null) {
      return "";
    }

    String typeName = getName();
    typeName = recurseGetParentNameExcludingRoot(typeName);			 		
    return typeName;
   }

   /*
    * Get the fully qualifed type name (with genome version in path)
    */
   public String getQualifiedName() {
     String qualifiedName = getName();
     qualifiedName = recurseGetParentName(qualifiedName);			 		
     return qualifiedName;
   }

   private String recurseGetParentName(String qualifiedName) {
     DataTrackFolder parent = this.getParentFolder();

     if (parent != null) {
       if (parent.getName() != null) {
         qualifiedName = parent.getName() + "/" + qualifiedName;

         qualifiedName = parent.recurseGetParentName(qualifiedName);
       }
     }
     return qualifiedName;
   }

   private String recurseGetParentNameExcludingRoot(String typeName) {
     DataTrackFolder parent = this.getParentFolder();


     if (parent != null) {
       if (parent.getName() != null) {
         // Stop before the root dataTrack grouping
         if (parent.getIdParentDataTrackFolder() != null) {
           typeName = parent.getName() + "/" + typeName;

           typeName = parent.recurseGetParentNameExcludingRoot(typeName);

         }
       }
     }
     return typeName;
   }



   public void recurseGetChildren(List<Object> descendents) {
     for(Iterator<?> i = this.getFolders().iterator(); i.hasNext();) {        
       DataTrackFolder ag = DataTrackFolder.class.cast(i.next());
       descendents.add(ag);
       ag.recurseGetChildren(descendents);
     }
     for(Iterator<?> i = this.getDataTracks().iterator(); i.hasNext();) {
       DataTrack a = DataTrack.class.cast(i.next());
       descendents.add(a);
     }
   }

   public String getCreatedBy() {
     return createdBy;
   }
   public void setCreatedBy(String createdBy) {
     this.createdBy = createdBy;
   }
   public Date getCreateDate() {
     return createDate;
   }
   public void setCreateDate(Date createDate) {
     this.createDate = createDate;
   }

   public Document getXML(SecurityAdvisor secAdvisor, DictionaryHelper dictionaryHelper) throws UnknownPermissionException {
     Document doc = DocumentHelper.createDocument();
     Element root = doc.addElement("DataTrackFolder");

     GenomeBuild genomeBuild = dictionaryHelper.getGenomeBuildObject(this.getIdGenomeBuild());

     root.addAttribute("label", this.getName());	
     root.addAttribute("idDataTrackFolder", this.getIdDataTrackFolder().toString());	
     root.addAttribute("idGenomeBuild", genomeBuild.getIdGenomeBuild().toString());	
     root.addAttribute("genomeBuild", genomeBuild.getGenomeBuildName());	
     root.addAttribute("name", this.getName().toString());	
     root.addAttribute("description", this.getDescription() != null ? this.getDescription() : "");	
     root.addAttribute("lab", this.getIdLab() != null ? dictionaryHelper.getLabObject(this.getIdLab()).getName() : "");
     root.addAttribute("idLab",this.getIdLab() != null ? this.getIdLab().toString() : "");
     root.addAttribute("createdBy", this.getCreatedBy() != null ? this.getCreatedBy() : "");
     root.addAttribute("createDate", this.getCreateDate() != null ? DataTrackUtil.formatDate(this.getCreateDate()) : "");

     root.addAttribute("canWrite",    secAdvisor.canUpdate(this) ? "Y" : "N");

     return doc;
   }
   
   public boolean hasPublicDataTracks() {
     boolean hasPublicAnalysis = false;
     for (Iterator i2 = this.getDataTracks().iterator(); i2.hasNext();) {
       DataTrack dt = (DataTrack)i2.next();
       if (dt.getCodeVisibility().equals(Visibility.VISIBLE_TO_PUBLIC)) {
         hasPublicAnalysis = true;
         break;
       }
     }  
     return hasPublicAnalysis;
   }  
   public void registerMethodsToExcludeFromXML() {
     this.excludeMethodFromXML("getDataTracks");
     this.excludeMethodFromXML("getParentFolder");
     this.excludeMethodFromXML("getFolder");     
     this.excludeMethodFromXML("getExcludedMethodsMap");

   }
   

}
