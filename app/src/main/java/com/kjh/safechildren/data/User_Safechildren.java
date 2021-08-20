package com.kjh.safechildren.data;

public class User_Safechildren {
    String Uid="";
    String email;   //email used to login
    String intro="";
    String name="";
    String type="student"; //parent or student
    String gender="";
    String location="";
    String schoolName="";
    String schoolAddr="";
    String childrenCSV="";
    String schoolsCSV="";
    String DateOfBirth="";
    String status="";
    String statusUpdateTime="";

    public User_Safechildren(){}
    public User_Safechildren(String Uid, String email){
        this.Uid = Uid;
        this.email = email;
    }
    public User_Safechildren(String Uid, String email, String intro, String name, String type, String gender, String location){
        this.Uid = Uid;
        this.email = email;
    }
    public String getUid(){
        return this.Uid;
    }
    public void setUid(String id){
        this.Uid = id;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public String getEmail(){
        return this.email;
    }

    public String getIntro(){
        return this.intro;
    }
    public void setIntro(String intro){
        this.intro = intro;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getType(){
        return this.type;
    }
    public void setType(String type){
        this.type = type;
    }

    public void setGender(String gender){ this.gender = gender; }
    public String getGender() {
        return this.gender;
    };

    public void setLocation(String loc){ this.location = loc; }
    public String getLocation() {
        return this.location;
    };

    public void setSchoolName(String name){ this.schoolName = name; }
    public String getSchoolName() {
        return this.schoolName;
    };

    public void setSchoolAddr(String addr){ this.schoolAddr = addr; }
    public String getSchoolAddr() {
        return this.schoolAddr;
    };


    public void setChildrenCSV(String childrenCSV){
        this.childrenCSV = childrenCSV;
    }
    public String getChildrenCSV() { return this.childrenCSV; };

    public void setSchoolsCSV(String schoolsCSV){
        this.schoolsCSV = schoolsCSV;
    }
    public String getSchoolsCSV() {
        return this.schoolsCSV;
    }

    public void setDateOfBirth(String dob){ this.DateOfBirth = dob; }
    public String getDateOfBirth() {
        return this.DateOfBirth;
    };

    public void setStatus(String status){this.status = status;}
    public String getStatus(){return this.status;}

    public void setLastStatusUpdate(String updateTime){ this.statusUpdateTime=updateTime;}
    public String getLastStatusUpdate(){return statusUpdateTime;}
}