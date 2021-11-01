package com.example.chatic;

public class User {
    private String name;
    private String emaile;
    private String id;
    private int avatarMockUpResource;
    public User() {
    }

    public User(String name, String emaile, String id,int avatarMockUpResource) {
        this.name = name;
        this.emaile = emaile;
        this.id = id;
        this.avatarMockUpResource=avatarMockUpResource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmaile() {
        return emaile;
    }

    public void setEmaile(String emaile) {
        this.emaile = emaile;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAvatarMockUpResource() {
        return avatarMockUpResource;
    }

    public void setAvatarMockUpResource(int avatarMockUpResource) {
        this.avatarMockUpResource = avatarMockUpResource;
    }
}
