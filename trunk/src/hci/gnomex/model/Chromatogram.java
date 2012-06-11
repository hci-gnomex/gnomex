package hci.gnomex.model;

import java.util.Date;

import hci.hibernate3utils.HibernateDetailObject;


public class Chromatogram extends HibernateDetailObject {
  
  private Integer    idChromatogram;
  private Integer    idPlateWell;
  private PlateWell  plateWell;
  private Integer    idRequest;
  private Request    request;
  private String     qualifiedFilePath;
  private String     displayName;
  private Integer    readLength;
  private Integer    trimmedLength;
  private Integer    q20;
  private Integer    q40;
  private Integer    aSignalStrength;
  private Integer    cSignalStrength;
  private Integer    gSignalStrength;
  private Integer    tSignalStrength;
  private Date       releaseDate;
  
  
  
  public Integer getIdChromatogram()
  {
    return idChromatogram;
  }

  public void setIdChromatogram(Integer idChromatogram)
  {
    this.idChromatogram = idChromatogram;
  }

  public Integer getIdPlateWell()
  {
    return idPlateWell;
  }

  public void setIdPlateWell(Integer idPlateWell)
  {
    this.idPlateWell = idPlateWell;
  }

  public PlateWell getPlateWell()
  {
    return plateWell;
  }

  public void setPlateWell(PlateWell plateWell)
  {
    this.plateWell = plateWell;
  }

  public Integer getIdRequest()
  {
    return idRequest;
  }

  public void setIdRequest(Integer idRequest)
  {
    this.idRequest = idRequest;
  }

  public Request getRequest()
  {
    return request;
  }

  public void setRequest(Request request)
  {
    this.request = request;
  }

  public void setQualifiedFilePath(String qualifiedFilePath)
  {
    this.qualifiedFilePath = qualifiedFilePath;
  }

  public String getQualifiedFilePath()
  {
    return qualifiedFilePath;
  }

  public String getDisplayName()
  {
    return displayName;
  }

  public void setDisplayName(String displayName)
  {
    this.displayName = displayName;
  }

  public Integer getReadLength()
  {
    return readLength;
  }

  public void setReadLength(Integer readLength)
  {
    this.readLength = readLength;
  }

  public Integer getTrimmedLength()
  {
    return trimmedLength;
  }

  public void setTrimmedLength(Integer trimmedLength)
  {
    this.trimmedLength = trimmedLength;
  }

  public Integer getQ20()
  {
    return q20;
  }

  public void setQ20(Integer q20)
  {
    this.q20 = q20;
  }

  public Integer getQ40()
  {
    return q40;
  }

  public void setQ40(Integer q40)
  {
    this.q40 = q40;
  }

  public Integer getaSignalStrength()
  {
    return aSignalStrength;
  }

  public void setaSignalStrength(Integer aSignalStrength)
  {
    this.aSignalStrength = aSignalStrength;
  }

  public Integer getcSignalStrength()
  {
    return cSignalStrength;
  }

  public void setcSignalStrength(Integer cSignalStrength)
  {
    this.cSignalStrength = cSignalStrength;
  }

  public Integer getgSignalStrength()
  {
    return gSignalStrength;
  }

  public void setgSignalStrength(Integer gSignalStrength)
  {
    this.gSignalStrength = gSignalStrength;
  }

  public Integer gettSignalStrength()
  {
    return tSignalStrength;
  }

  public void settSignalStrength(Integer tSignalStrength)
  {
    this.tSignalStrength = tSignalStrength;
  }

  public Date getReleaseDate()
  {
    return releaseDate;
  }

  public void setReleaseDate(Date releaseDate)
  {
    this.releaseDate = releaseDate;
  }
    
}