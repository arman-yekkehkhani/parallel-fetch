# parallel-fetch

This project is a simple program to demonstrate `how to fetch entities from a datasource in parallel`. 
You can run test cases in class `FetchTest`(located in test directory) to see the final results.
It is assumed the database contains 93 entities numbered from 0 to 92. This number can be changed by altering the private constructor in `DataDAO`.
Page size and number of threads are considered to be 5 and 2, repsectively. You can find and change these values(`PAGE_SIZE` and `THREAD_COUNTS`) in class `DataMgr`.
