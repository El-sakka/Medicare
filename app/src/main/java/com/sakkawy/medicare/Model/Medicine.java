package com.sakkawy.medicare.Model;

public class Medicine {

    private String name ;
    private String description ;

    // 6years - 12years
    private int kidsDose ;
    private String kidsUnit ;    // mlg , cabsul , tablet
    private int kidsPortion ;  // this is <number> per days

    // 13years - 18years
    private int teenDose ;
    private String teenUnit ;
    private int teenPortion ;

    // +18years
    private int adultDose ;
    private String adultUnit ;
    private int adultPortion ;

    public Medicine(String name, String description,
                        int kidsDose, String kidsUnit, int kidsPortion,
                        int teenDose, String teenUnit, int teenPortion,
                        int adultDose, String adultUnit, int adultPortion) {

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

    public int getKidsDose() {
        return kidsDose;
    }

    public void setKidsDose(int kidsDose) {
        this.kidsDose = kidsDose;
    }

    public String getKidsUnit() {
        return kidsUnit;
    }

    public void setKidsUnit(String kidsUnit) {
        this.kidsUnit = kidsUnit;
    }

    public int getKidsPortion() {
        return kidsPortion;
    }

    public void setKidsPortion(int kidsPortion) {
        this.kidsPortion = kidsPortion;
    }

    public int getTeenDose() {
        return teenDose;
    }

    public void setTeenDose(int teenDose) {
        this.teenDose = teenDose;
    }

    public String getTeenUnit() {
        return teenUnit;
    }

    public void setTeenUnit(String teenUnit) {
        this.teenUnit = teenUnit;
    }

    public int getTeenPortion() {
        return teenPortion;
    }

    public void setTeenPortion(int teenPortion) {
        this.teenPortion = teenPortion;
    }

    public int getAdultDose() {
        return adultDose;
    }

    public void setAdultDose(int adultDose) {
        this.adultDose = adultDose;
    }

    public String getAdultUnit() {
        return adultUnit;
    }

    public void setAdultUnit(String adultUnit) {
        this.adultUnit = adultUnit;
    }

    public int getAdultPortion() {
        return adultPortion;
    }

    public void setAdultPortion(int adultPortion) {
        this.adultPortion = adultPortion;
    }
}
