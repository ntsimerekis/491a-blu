package com.blu.auth.Dto;
/*
    Stores Registered Device in a JSON
 */
public class RegisteredDeviceDto {
    private String ipAddress;

    private String macAddress;

    public String getIpAddress() {return ipAddress;}

    public String getMacAddress() {return macAddress;}
}
