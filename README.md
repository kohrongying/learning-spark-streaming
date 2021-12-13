# Learning Spark Streaming

## 0: Using Spark Context
From [spark website](https://spark.apache.org/docs/latest/streaming-programming-guide.html)

## Output Modes
### 1. Append
Default mode is append (New rows are added to result table).
Supported for only those queries where rows added will never change:
- select
- where
- flatMap / map
- filter
- join etc
```
Append output mode not supported when there are 
streaming aggregations on streaming DataFrames/DataSets 
without watermark;
```
Guarantees that each row will be output only once (assuming fault-tolerant sink). 

#### Watermarking
Supports either update or append output modes

- Add option to include timestamp
```
spark.readStream.option("includeTimestamp",  true) 
```

- use withWatermark 
```
words.withWatermark("timestamp", "2 minutes") // late arrivals of up to 2 mins
```
Note: withWatermark must be called on the same column used in the aggregate for `append` output mode. Else will be invalid.

### 2. Complete
The whole Result Table will be outputted to the sink after every trigger. This is supported for aggregation queries.
```
Complete output mode not supported when there are no 
streaming aggregations on streaming DataFrames/Datasets;
```

### 3. Update
Only the rows in the Result Table that were updated since the last trigger will be outputted to the sink

## InputSource
### 1. Socket
For debugging. Listening on socket. Run this to open socket:
```
nc -lk 9999
```

### 2. File source
File Data Source Streaming Example using the concept of checkpointing
```
 Option 'basePath' must be a directory
 works when set `.load("data/pokemon.csv")`
```
Paths in structured streaming needs to be a directory and not a file
#### Checkpointing
To ensure queries are fault tolerant, enable query checkpointing.
How to execute:
1. Run part 6 first
2. Stop run
3. ./gen_pokemon.sh 
4. Run part 6 again

`chkpt` folder
- commits/
    - Gets incremented after stopped
- offsets/
- sources/
    - List of file paths
- state/
- metadata

## Kafka
Kafka Data Source Streaming example

# Resources
1. [Output Modes](https://spark.apache.org/docs/latest/structured-streaming-programming-guide.html#output-modes)
2. [Pokemon csv](https://gist.githubusercontent.com/armgilles/194bcff35001e7eb53a2a8b441e8b2c6/raw/92200bc0a673d5ce2110aaad4544ed6c4010f687/pokemon.csv)