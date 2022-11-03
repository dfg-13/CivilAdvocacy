package com.example.civiladvocacyapp;

import java.io.Serializable;

public class Official implements Serializable {
    //Basic official data
    private String name, governmentTitle, party;
    //Contact info given it is provided
    private String officeAddress, phoneNum, email, website;
    private String ytLink, twitLink, fbLink;
    private String photoLink;

    public Official(String name, String governmentTitle, String party) {
        setName(name);
        setGovernmentTitle(governmentTitle);
        setParty(party);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGovernmentTitle() {
        return governmentTitle;
    }

    public void setGovernmentTitle(String governmentTitle) {
        this.governmentTitle = governmentTitle;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getOfficeAddress() {
        return officeAddress;
    }

    public void setOfficeAddress(String officeAddress) {
        this.officeAddress = officeAddress;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getYtLink() {
        return ytLink;
    }

    public void setYtLink(String ytLink) {
        this.ytLink = ytLink;
    }

    public String getTwitLink() {
        return twitLink;
    }

    public void setTwitLink(String twitLink) {
        this.twitLink = twitLink;
    }

    public String getFbLink() {
        return fbLink;
    }

    public void setFbLink(String fbLink) {
        this.fbLink = fbLink;
    }

    public String getPhotoLink() {
        return photoLink;
    }

    public void setPhotoLink(String photoLink) {
        this.photoLink = photoLink;
    }
}
