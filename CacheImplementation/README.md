
High performance scalable web applications often use a distributed in-memory data cache 
in front of or in place of robust persistent storage for some tasks. In Java Applications 
it is very common to use in Memory Cache for better performance. But what is “Cache?”

A cache is an area of local memory that holds a copy of frequently accessed data that is 
otherwise expensive to get or compute. Examples of such data include a result of a query 
to a database, a disk file or a report.

Lets look at creating and using a simple thread-safe Java in-memory cache.
Here are the characteristic of the program CrunchifyInMemoryCache.java.

    
    **Items will expire based on a time to live period.
    
    **Cache will keep most recently used items if you will try to add more items then max 
      specified. (apache common collections has a LRUMap, which, removes the least used 
      entries from a fixed sized map)
    
    **For the expiration of items we can timestamp the last access and in a separate thread 
      remove the items when the time to live limit is reached. This is nice for reducing memory 
      pressure for applications that have long idle time in between accessing the cached objects.
    
    **We will also create test class: InMemoryCacheTest.java
 
