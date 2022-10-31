package com.example.civiladvocacyapp;

public class Official {
    //Basic official data
    private String name, governmentTitle, party;
    //Contact info given it is provided
    //private String officeAddress, phoneNum, email, website;

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
}
