package br.com.siecola.helloworldturbo.models;

import java.io.Serializable;
import java.util.Date;

public class Users implements Serializable {

    private long id;
    private String email;
    private String password;
    private String gcmRegId;
    private Date lastLogin;
    private Date lastGCMRegister;
    private String role;
    private boolean enabled;

    public Users(){
        this.email="Sem email";
        this.password=" ";
        this.id=0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGcmRegId() {
        return gcmRegId;
    }

    public void setGcmRegId(String gcmRegId) {
        this.gcmRegId = gcmRegId;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Date getLastGCMRegister() {
        return lastGCMRegister;
    }

    public void setLastGCMRegister(Date lastGCMRegister) {
        this.lastGCMRegister = lastGCMRegister;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }



}
