package com.mg.apidaaalumni.alumnus;

import jakarta.persistence.*;

@Entity
@Table(name="alumni")
public class Alumnus {
    @Id
    @Column(name = "alumnusid", unique = true)
    private Integer alumnusId;
    private String name;
    private String pronouns;
    private String email;
    private String headshot;
    private Integer year;
    private String pastRoles;
    private String industry;

    public Alumnus() {
    }

    public Alumnus(Integer alumnusId, String name, String pronouns, String email, Integer year, String headshot, String pastRoles, String industry) {
        this.alumnusId = alumnusId;
        this.name = name;
        this.pronouns = pronouns;
        this.email = email;
        this.year = year;
        this.headshot = headshot;
        this.pastRoles = pastRoles;
        this.industry = industry;
    }

    public Integer getAlumnusId() {
        return alumnusId;
    }

    public void setAlumnusId(Integer alumnusId) {
        this.alumnusId = alumnusId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPronouns() {
        return pronouns;
    }

    public void setPronouns(String pronouns) {
        this.pronouns = pronouns;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHeadshot() {
        return headshot;
    }

    public void setHeadshot(String headshot) {
        this.headshot = headshot;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getPastRoles() {
        return pastRoles;
    }

    public void setPastRoles(String pastRoles) {
        this.pastRoles = pastRoles;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }
}
