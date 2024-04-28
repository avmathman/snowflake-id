# Welcome to Snowflake ID Generator!

This module allows to generate identification using concept of Twitter's snowflake identification generating algorithm. It generates at least 4000 IDs per millisecond. For more detailed information about snowflake id, you can read [here](https://en.wikipedia.org/wiki/Snowflake_ID)


# snowflake-id

## Create files and folders

The file explorer is accessible using the button in left corner of the navigation bar. You can create a new file by clicking the **New file** button in the file explorer. You can also create folders by clicking the **New folder** button.

## How to use it?

### Dependency Injection
```
<dependency>
    <groupId>com.avmathman.snowflake</groupId>
    <artifactId>snowflake-id</artifactId>
    <version>0.0.1</version>
</dependency>
```
### Initializing Object
```
SnowflakeIdGenerator generator = new SnowflakeIdGenerator(1,2);
```

## Sample Code
```
SnowflakeIdGenerator generator = new SnowflakeIdGenerator(long datacenterId, long machineId, long epoch);
```
OR
```
SnowflakeIdGenerator generator = new SnowflakeIdGenerator(long datacenterId, long machineId);
```
where ```DEFAULT_EPOCH = 1420070400000```
OR
```
SnowflakeIdGenerator generator = new SnowflakeIdGenerator(long machineId);
```
where ```DEFAULT_DATACENTER_ID = 1``` and  ```DEFAULT_EPOCH = 1420070400000```

After initializing generator you just make a call such as:
```
long snowflakeId = generator.generate();
```
