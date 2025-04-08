package com.example.pharmacies;

public class MedicalHistory {
    private String birthday;
    private String gender;
    private String diseases;
    private String medicines;
    private String complicationMedicines;
    private String foodAllergies;
    private String medicineAllergies;

    public MedicalHistory(){

    }
    public MedicalHistory(String birthday, String gender) {
        this.birthday = birthday;
        this.gender = gender;
        this.diseases = "";
        this.medicines = "";
        this.complicationMedicines = "";
        this.foodAllergies = "";
        this.medicineAllergies = "";
    }

    // Setters and Getters for birthday
    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    // Setters and Getters for gender
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    // Setters and Getters for diseases
    public String getDiseases() {
        return diseases;
    }

    public void setDiseases(String diseases) {
        this.diseases = diseases;
    }

    // Setters and Getters for medicines
    public String getMedicines() {
        return medicines;
    }

    public void setMedicines(String medicines) {
        this.medicines = medicines;
    }

    // Setters and Getters for complicationMedicines
    public String getComplicationMedicines() {
        return complicationMedicines;
    }

    public void setComplicationMedicines(String complicationMedicines) {
        this.complicationMedicines = complicationMedicines;
    }

    // Setters and Getters for foodAllergies
    public String getFoodAllergies() {
        return foodAllergies;
    }

    public void setFoodAllergies(String foodAllergies) {
        this.foodAllergies = foodAllergies;
    }

    // Setters and Getters for medicineAllergies
    public String getMedicineAllergies() {
        return medicineAllergies;
    }

    public void setMedicineAllergies(String medicineAllergies) {
        this.medicineAllergies = medicineAllergies;
    }
}
