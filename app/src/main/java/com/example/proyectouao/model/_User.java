package com.example.proyectouao.model;

import java.util.Objects;

public class _User {

    private String nameUser;
    private String lastNameUser;
    private String email;
    private String facultyUser;
    private String program;
    private String selectRole;
    private String selectSite;
    private String userStatus;


    public _User(String nameUser, String lastNameUser, String email, String facultyUser, String program, String selectRole, String selectSite, String userStatus) {
        this.nameUser = nameUser;
        this.lastNameUser = lastNameUser;
        this.email = email;
        this.facultyUser = facultyUser;
        this.program = program;
        this.selectRole = selectRole;
        this.selectSite = selectSite;
        this.userStatus = userStatus;
    }

    public _User() {
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getLastNameUser() {
        return lastNameUser;
    }

    public void setLastNameUser(String lastNameUser) {
        this.lastNameUser = lastNameUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFacultyUser() {
        return facultyUser;
    }

    public void setFacultyUser(String facultyUser) {
        this.facultyUser = facultyUser;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getSelectRole() {
        return selectRole;
    }

    public void setSelectRole(String selectRole) {
        this.selectRole = selectRole;
    }

    public String getSelectSite() {
        return selectSite;
    }

    public void setSelectSite(String selectSite) {
        this.selectSite = selectSite;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        _User user = (_User) o;
        return Objects.equals(nameUser, user.nameUser) && Objects.equals(lastNameUser, user.lastNameUser) && Objects.equals(email, user.email) && Objects.equals(facultyUser, user.facultyUser) && Objects.equals(program, user.program) && Objects.equals(selectRole, user.selectRole) && Objects.equals(selectSite, user.selectSite);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameUser, lastNameUser, email, facultyUser, program, selectRole, selectSite);
    }
}


