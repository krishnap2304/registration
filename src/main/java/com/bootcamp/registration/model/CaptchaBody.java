package com.bootcamp.registration.model;

public class CaptchaBody {
    private String captchaEntered;
    private String captchaGenerated;

    public String getCaptchaEntered() {
        return captchaEntered;
    }

    public void setCaptchaEntered(String captchaEntered) {
        this.captchaEntered = captchaEntered;
    }

    public String getCaptchaGenerated() {
        return captchaGenerated;
    }

    public void setCaptchaGenerated(String captchaGenerated) {
        this.captchaGenerated = captchaGenerated;
    }
    public boolean isEquals(){
        return this.getCaptchaEntered().equals(this.getCaptchaGenerated());
    }
}
