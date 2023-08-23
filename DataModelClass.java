package com.tuition.createcontactform;

public class DataModelClass {
    String firstName="fName";
    String lastName="lName";
    String companyName="cNAme";
    String phoneNumber="pNumber";
    String emailId="eId";
    String calender="cId";
    String image="imageValue";
    boolean isSelected = false;

    public DataModelClass(String firstName, String lastName, String companyName, String phoneNumber, String emailId, String calender, String image) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.companyName = companyName;
        this.phoneNumber = phoneNumber;
        this.emailId = emailId;
        this.calender = calender;
        this.image= image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getCalender() {
        return calender;
    }

    public void setCalender(String calender) {
        this.calender = calender;
    }
}
