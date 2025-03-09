package com.blu.auth.Dto;
/*
    Stores Registered Device in a JSON (Done by Luke T.)
 */
public class RegisteredDeviceDto {
    private String ipAddress;

    private String macAddress;

    public String getIpAddress() {return ipAddress;}

    public String getMacAddress() {return macAddress;}
}
