package com.avmathman.utils;

import com.avmathman.constants.Constants;

import java.net.NetworkInterface;
import java.security.SecureRandom;
import java.util.Enumeration;

public class MachineUtils {
    public long createMachineId() {
        long machineId;
        try {
            StringBuilder sb = new StringBuilder();
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                byte[] mac = networkInterface.getHardwareAddress();
                if (mac != null) {
                    for(byte macPort: mac) {
                        sb.append(String.format("%02X", macPort));
                    }
                }
            }
            machineId = sb.toString().hashCode();
        } catch (Exception ex) {
            machineId = (new SecureRandom().nextInt());
        }
        machineId = machineId & Constants.MACHINE_ID_BITS;
        return machineId;
    }
}
