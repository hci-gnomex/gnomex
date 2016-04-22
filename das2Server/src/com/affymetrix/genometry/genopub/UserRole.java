// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometry.genopub;

public class UserRole
{
    private Integer idUserRole;
    private Integer idUser;
    private String userName;
    private String roleName;
    
    public Integer getIdUserRole() {
        return this.idUserRole;
    }
    
    public void setIdUserRole(final Integer idUserRole) {
        this.idUserRole = idUserRole;
    }
    
    public Integer getIdUser() {
        return this.idUser;
    }
    
    public void setIdUser(final Integer idUser) {
        this.idUser = idUser;
    }
    
    public String getRoleName() {
        return this.roleName;
    }
    
    public void setRoleName(final String roleName) {
        this.roleName = roleName;
    }
    
    public String getUserName() {
        return this.userName;
    }
    
    public void setUserName(final String userName) {
        this.userName = userName;
    }
}
