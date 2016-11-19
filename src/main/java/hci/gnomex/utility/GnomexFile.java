package hci.gnomex.utility;

import hci.hibernate5utils.HibernateDetailObject;
import hci.gnomex.constants.Constants;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Date;

/**
 * Created by mbowler on 5/19/2016.
 */
public abstract class GnomexFile extends HibernateDetailObject {

    protected String fileName;
    protected String baseFilePath;
    protected String qualifiedFilePath;
    protected BigDecimal fileSize;
    protected Date createDate;
    protected String cloudURL;

    public String getFullPathName() {
        String fullPathName = getBaseFilePath();
        if ( getQualifiedFilePath() != null && !getQualifiedFilePath().equals("") ) {
            fullPathName += Constants.FILE_SEPARATOR + getQualifiedFilePath();
        }
        fullPathName += Constants.FILE_SEPARATOR + getFileName();

        return fullPathName;
    }

    public String getQualifiedFileName() {
        String fullPathName = "";
        if ( getQualifiedFilePath() != null && !getQualifiedFilePath().equals("") ) {
            fullPathName += getQualifiedFilePath() + Constants.FILE_SEPARATOR;
        }
        fullPathName += getFileName();
        return fullPathName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getBaseFilePath() {
        return baseFilePath;
    }

    public void setBaseFilePath(String baseFilePath) {
        this.baseFilePath = baseFilePath;
    }

    public String getQualifiedFilePath() {
        return qualifiedFilePath;
    }

    public void setQualifiedFilePath(String qualifiedFilePath) {
        this.qualifiedFilePath = qualifiedFilePath;
    }

    public BigDecimal getFileSize() {
        return fileSize;
    }

    public void setFileSize(BigDecimal fileSize) {
        this.fileSize = fileSize;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCloudURL() {
        return cloudURL;
    }

    public void setCloudURL(String cloudURL) {
        this.cloudURL = cloudURL;
    }
}
