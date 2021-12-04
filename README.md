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

#### Watermarking?

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

#### Checkpointing

## Kafka
Kafka Data Source Streaming example

# Resources
1. [Output Modes](https://spark.apache.org/docs/latest/structured-streaming-programming-guide.html#output-modes)