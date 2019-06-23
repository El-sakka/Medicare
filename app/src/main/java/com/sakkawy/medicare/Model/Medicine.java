package com.sakkawy.medicare.Model;

public class Medicine {

    private String name ;
    private String description ;

    // 6years - 12years
    private String kidsDose ;
    private String kidsUnit ;    // mlg , cabsul , tablet
    private String kidsPortion ;  // this is <number> per days

    // 13years - 18years
    private String teenDose ;
    private String teenUnit ;
    private String teenPortion ;

    // +18years
    private String adultDose ;
    private String adultUnit ;
    private String adultPortion ;

    public Medicine() {
    }

    public Medicine(String name, String description,
                    String kidsDose, String kidsUnit, String kidsPortion,
                    String teenDose, String teenUnit, String teenPortion,
                    String adultDose, String adultUnit, String adultPortion) {

        this.name = name;
        this.description = description;
        this.kidsDose = kidsDose;
        this.kidsUnit = kidsUnit;
        this.kidsPortion = kidsPortion;
        this.teenDose = teenDose;
        this.teenUnit = teenUnit;
        this.teenPortion = teenPortion;
        this.adultDose = adultDose;
        this.adultUnit = adultUnit;
        this.adultPortion = adultPortion;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKidsDose() {
        return kidsDose;
    }

    public void setKidsDose(String kidsDose) {
        this.kidsDose = kidsDose;
    }

    public String getKidsUnit() {
        return kidsUnit;
    }

    public void setKidsUnit(String kidsUnit) {
        this.kidsUnit = kidsUnit;
    }

    public String getKidsPortion() {
        return kidsPortion;
    }

    public void setKidsPortion(String kidsPortion) {
        this.kidsPortion = kidsPortion;
    }

    public String getTeenDose() {
        return teenDose;
    }

    public void setTeenDose(String teenDose) {
        this.teenDose = teenDose;
    }

    public String getTeenUnit() {
        return teenUnit;
    }

    public void setTeenUnit(String teenUnit) {
        this.teenUnit = teenUnit;
    }

    public String getTeenPortion() {
        return teenPortion;
    }

    public void setTeenPortion(String teenPortion) {
        this.teenPortion = teenPortion;
    }

    public String getAdultDose() {
        return adultDose;
    }

    public void setAdultDose(String adultDose) {
        this.adultDose = adultDose;
    }

    public String getAdultUnit() {
        return adultUnit;
    }

    public void setAdultUnit(String adultUnit) {
        this.adultUnit = adultUnit;
    }

    public String getAdultPortion() {
        return adultPortion;
    }

    public void setAdultPortion(String adultPortion) {
        this.adultPortion = adultPortion;
    }
}
