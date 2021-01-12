# GridGain Entry Compression Demo

This demo highlights the new data compression feature of GridGain 8.8.1

## Building

Please build this project with Maven. It will download the `gridgain-compress` module necessary to use entry compression feature, as well as `control-center-agent` to observe the compression benefit; as well as their dependencies.

## Running

Please run com.gridgain.demo.compress.CacheEntryCompressionDemo main class. Then you may proceed to GridGain Control Center using provided link, and check off-heap size of two data regions. After some time, data region with compressed cache will show significant reduction in usage compared to ordinary region, despite having the same data in cache.

