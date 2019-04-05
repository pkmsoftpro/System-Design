
public class InMemoryCacheTest {
 
    public static void main(String[] args) throws InterruptedException {
 
        InMemoryCacheTest cache = new InMemoryCacheTest();
 
        System.out.println("\n\n==========Test1: cacheTestAddRemoveObjects ==========");
        cache.cacheTestAddRemoveObjects();
        System.out.println("\n\n==========Test2: cacheTestExpiredCacheObjects ==========");
        cache.cacheTestExpiredCacheObjects();
        System.out.println("\n\n==========Test3: cacheTestObjectsCleanupTime ==========");
        cache.cacheTestObjectsCleanupTime();
    }
 
    private void cacheTestAddRemoveObjects() {
 
        // Test with cacheTimeToLive = 200 seconds
        // cacheTimerInterval = 500 seconds
        // maxItems = 6
        InMemoryCache<String, String> cache = new InMemoryCache<String, String>(200, 500, 6);
 
        cache.put("eBay", "eBay");
        cache.put("Paypal", "Paypal");
        cache.put("Google", "Google");
        cache.put("Microsoft", "Microsoft");
        cache.put("IBM", "IBM");
        cache.put("Facebook", "Facebook");
 
        System.out.println("6 Cache Object Added.. cache.size(): " + cache.size());
        cache.remove("IBM");
        System.out.println("One object removed.. cache.size(): " + cache.size());
 
        cache.put("Twitter", "Twitter");
        cache.put("SAP", "SAP");
        System.out.println("Two objects Added but reached maxItems.. cache.size(): " + cache.size());
 
    }
 
    private void cacheTestExpiredCacheObjects() throws InterruptedException {
 
        // Test with cacheTimeToLive = 1 second
        // cacheTimerInterval = 1 second
        // maxItems = 10
        InMemoryCache<String, String> cache = new InMemoryCache<String, String>(1, 1, 10);
 
        cache.put("eBay", "eBay");
        cache.put("Paypal", "Paypal");
        // Adding 3 seconds sleep.. Both above objects will be removed from
        // Cache because of timeToLiveInSeconds value
        Thread.sleep(3000);
 
        System.out.println("Two objects are added but reached timeToLive. cache.size(): " + cache.size());
 
    }
 
    private void cacheTestObjectsCleanupTime() throws InterruptedException {
        int size = 500000;
 
        // Test with timeToLiveInSeconds = 100 seconds
        // timerIntervalInSeconds = 100 seconds
        // maxItems = 500000
 
        InMemoryCache<String, String> cache = new InMemoryCache<String, String>(100, 100, 500000);
 
        for (int i = 0; i < size; i++) {
            String value = Integer.toString(i);
            cache.put(value, value);
        }
 
        Thread.sleep(200);
 
        long start = System.currentTimeMillis();
        cache.cleanup();
        double finish = (double) (System.currentTimeMillis() - start) / 1000.0;
 
        System.out.println("Cleanup times for " + size + " objects are " + finish + " s");
 
    }
}
