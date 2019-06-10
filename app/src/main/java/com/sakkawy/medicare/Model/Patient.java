package com.sakkawy.medicare.Model;

public class Patient {
    private String Name;
    private String UserName;
    private String Email;
    private String Password;
    private String BirthOfDate;
    private String Gender;
    private String ImageUri;

    public Patient(String name, String userName, String email, String password, String birthOfDate, String gender, String imageUri) {
        Name = name;
        UserName = userName;
        Email = email;
        Password = password;
        BirthOfDate = birthOfDate;
        Gender = gender;
        ImageUri = imageUri;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getBirthOfDate() {
        return BirthOfDate;
    }

    public void setBirthOfDate(String birthOfDate) {
        BirthOfDate = birthOfDate;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getImageUri() {
        return ImageUri;
    }

    public void setImageUri(String imageUri) {
        ImageUri = imageUri;
    }

    @Override
    public String toString() {
        return getImageUri();

    }
}
