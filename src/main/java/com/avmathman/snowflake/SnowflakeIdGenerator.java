package com.avmathman.snowflake;

import com.avmathman.snowflake.constants.Constants;
import com.avmathman.snowflake.enums.KeyEnum;
import com.avmathman.snowflake.utils.MachineUtils;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of logic for generating identification similar to
 * Twitter's snowflake id generation algorithm.
 */
public class SnowflakeIdGenerator {
    private long datacenterId;;
    private long machineId;;
    private long epoch;

    private volatile long lastTimestamp = -1L;
    private volatile long sequence = 0L;

    private MachineUtils machineUtils;

    public SnowflakeIdGenerator(long datacenterId, long machineId, long epoch) {
        if (this.machineId < 0 || this.machineId > Constants.MAX_MACHINE_ID) {
            throw new IllegalArgumentException(String.format("MachineId must be between %d and %d", 0, Constants.MAX_MACHINE_ID));
        }
        if (this.datacenterId < 0 || this.datacenterId > Constants.MAX_DATACENTER_ID) {
            throw new IllegalArgumentException(String.format("DatacenterId must be between %d and %d", 0, Constants.MAX_DATACENTER_ID));
        }

        this.datacenterId = datacenterId;
        this.machineId = machineId;
        this.epoch = epoch;
    }

    public SnowflakeIdGenerator(long datacenterId, long machineId) {
        this(datacenterId, machineId, Constants.DEFAULT_EPOCH);
    }

    public SnowflakeIdGenerator(long machineId) {
        this(Constants.DEFAULT_DATACENTER_ID, machineId, Constants.DEFAULT_EPOCH);
    }

    public SnowflakeIdGenerator() {
        this.machineUtils = new MachineUtils();

        this.machineId = this.machineUtils.createMachineId();
        this.datacenterId = Constants.DEFAULT_DATACENTER_ID;
        this.epoch = Constants.DEFAULT_EPOCH;
    }

    public synchronized long generate() {
        long currentTimestamp = this.timestamp();

        if(currentTimestamp < lastTimestamp) {
            throw new IllegalStateException("Invalid System Clock!");
        }

        if (currentTimestamp == lastTimestamp) {
            sequence = (sequence + 1) & Constants.MAX_SEQUENCE;

            if(sequence == 0) {
                // Sequence Exhausted, wait till next millisecond.
                currentTimestamp = this.waitNextMillis(currentTimestamp);
            }
        } else {
            // reset sequence to start with zero for the next millisecond
            sequence = 0;
        }

        lastTimestamp = currentTimestamp;

        return currentTimestamp << (Constants.DATACENTER_ID_BITS + Constants.MACHINE_ID_BITS + Constants.SEQUENCE_BITS)
                | this.datacenterId << (Constants.MACHINE_ID_BITS + Constants.SEQUENCE_BITS)
                | this.machineId << (Constants.SEQUENCE_BITS)
                | this.sequence;
    }

    public Map<KeyEnum, Long> parse(long id) {
        long maskDatacenterId = ((1L << Constants.DATACENTER_ID_BITS) - 1) << (Constants.MACHINE_ID_BITS + Constants.SEQUENCE_BITS);
        long maskMachineId = ((1L << Constants.MACHINE_ID_BITS) - 1) << Constants.SEQUENCE_BITS;
        long maskSequence = 1L << Constants.SEQUENCE_BITS - 1;

        long timestamp = (id >> (Constants.DATACENTER_ID_BITS + Constants.MACHINE_ID_BITS + Constants.SEQUENCE_BITS)) + this.epoch;
        long datacenterId = (id & maskDatacenterId) >> (Constants.MACHINE_ID_BITS + Constants.SEQUENCE_BITS);
        long machineId = (id & maskMachineId) >> Constants.SEQUENCE_BITS;
        long sequence = id & maskSequence;

        Map<KeyEnum, Long> result = new HashMap<>();
        result.put(KeyEnum.TIMESTAMP, timestamp);
        result.put(KeyEnum.DATACENTER, datacenterId);
        result.put(KeyEnum.MACHINE, machineId);
        result.put(KeyEnum.SEQUENCE, sequence);

        return result;
    }

    private long timestamp() {
        return Instant.now().toEpochMilli() - this.epoch;
    }

    private long waitNextMillis(long currentTimestamp) {
        while(currentTimestamp == lastTimestamp) {
            currentTimestamp = timestamp();
        }

        return currentTimestamp;
    }
}
