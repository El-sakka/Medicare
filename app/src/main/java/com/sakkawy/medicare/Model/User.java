package com.sakkawy.medicare.Model;

public class User {
    int flag = 0;
    private String Name = "";
    private String UserName = "";
    private String Email = "";
    private String Password = "";
    private String BirthOfDate = "";
    private String Gender = "";
    private String ImageUri = "";
    private String userId = "";
    private String Address = "";
    private String Speciality = "";
    private String UserType = "";
    private String Status = "";

    public User() {
    }

    public User(String userId, String name, String userName, String email, String password, String birthOfDate, String gender, String imageUri, String userType) {
        this.userId = userId;
        Name = name;
        UserName = userName;
        Email = email;
        Password = password;
        BirthOfDate = birthOfDate;
        Gender = gender;
        ImageUri = imageUri;
        this.UserType = userType;
    }

    public User(String userId, String name, String userName, String Email, String Password, String address, String speciality, String imageUri, String userType, int flag) {
        this.userId = userId;
        this.Name = name;
        this.UserName = userName;
        this.Email = Email;
        this.Password = Password;
        this.Address = address;
        this.Speciality = speciality;
        this.ImageUri = imageUri;
        this.UserType = userType;
    }

    public User(String name, String userName, String email, String password, String birthOfDate, String gender, String imageUri, String userType) {
        Name = name;
        UserName = userName;
        Email = email;
        Password = password;
        BirthOfDate = birthOfDate;
        Gender = gender;
        ImageUri = imageUri;
        this.UserType = userType;
    }

    public User(String name, String userName, String Email, String Password, String address, String speciality, String imageUri, String userType, int flag) {
        this.Name = name;
        this.UserName = userName;
        this.Email = Email;
        this.Password = Password;
        this.Address = address;
        this.Speciality = speciality;
        this.ImageUri = imageUri;
        this.UserType = userType;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        this.Status = status;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getSpeciality() {
        return Speciality;
    }

    public void setSpeciality(String speciality) {
        Speciality = speciality;
    }

    public String getUserType() {
        return UserType;
    }

    public void setUserType(String userType) {
        UserType = userType;
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
