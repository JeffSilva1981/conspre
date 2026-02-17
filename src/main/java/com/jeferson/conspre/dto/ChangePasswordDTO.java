package com.jeferson.conspre.dto;

public class ChangePasswordDTO {

    private String currentPassword;
    private String newPassword;
    private String confirmPassword;

    public ChangePasswordDTO() {
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }
}
