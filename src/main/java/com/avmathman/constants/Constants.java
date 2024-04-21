package com.avmathman.constants;

public class Constants {
    public static final long DEFAULT_EPOCH = 1420070400000L;
    public static final long DEFAULT_DATACENTER_ID = 1L;
    public static final int SIGN_BIT = 1; // Sign bit, Unused (always set to 0)
    public static final int EPOCH_BITS = 41;
    public static final int DATACENTER_ID_BITS = 5;
    public static final int MACHINE_ID_BITS = 5;
    public static final int SEQUENCE_BITS = 12;
    public static final long MAX_DATACENTER_ID = (1L << DATACENTER_ID_BITS) - 1;
    public static final long MAX_MACHINE_ID = (1L << MACHINE_ID_BITS) - 1;
    public static final long MAX_SEQUENCE = (1L << SEQUENCE_BITS) - 1;
}
