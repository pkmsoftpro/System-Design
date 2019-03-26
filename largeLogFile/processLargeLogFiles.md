https://leetcode.com/discuss/interview-question/system-design/124603/how-to-handle-large-log-data-and-store-particular-info-to-sql

I was asked to answer a question in a phone interview for Amazon.
The question is as follow:
Image there is a website generates user access log in Terabytes scale every day. Each log contains many different information such as username, visiting time, location, page visited, and etc.
what's your solution if we want to only store certain info(for example, user, login time, location) to a sql database. How to optimize the process.

This is an open question. So I believe there is no correct answer. what is your thought?  

<div class="discuss-markdown-container"><p>Few constraints to consider:</p><p>
</p><ol>
<li>Asynchronous : you do not want user-experience to get blocked on log processing</li>
<li>Lossless (as much as possible) : for instance provide 99.9 guarantees</li>
<li>Latency : Are these logs being used for real-time analysis and alerting for site-down? If so &lt; 1min latency. Is this is used for offline reporting?  20min latency might be doable.</li>
<li>Security : Are you sending PII data-logs for the user. If so, they must be encrypted during transfer and every service must have the right keys to de-crypt before processing</li>
</ol>
<p></p><p>Services involved.</p><p>
</p><ol>
<li>Log-receiving service : This service can receive logs and perform basic validations - size, data-types, required fields like user-id etc. It then batches these logs in-memory (say: 30k or 3min) and stores this batch into a store say an S3 bucket file. (Depending on how fault-tolerant you need to be you can store each log too, since in-memory batches can be lost if the server crashes before a persist). This initial store ensures that you persist a raw copy of the log-data. (This is similar to the Producer service referred in Kafka answers)<br>
This service can also be preceded by a queue like SQS, to decouple the log-sending service/client from this log-receiving service. Scale-handling.</li>
<li>Log-Processing service/ worker : This can reads S3 file contents, transforms into the data-model needed for SQL and writes required rows into SQL. You can also drop this transformed data into another queue so you can decouple and scale writes-to-SQL indepdent of log transformation. SQL query engine will take care of updating indexes and atomic/consistent writes.<br>
Partitioning is something to discuss : you can either have timestamp be the partition key ( which likely means you'll have hot-partitions for recent logs but good range querying) or you can have user-id be partition key(less likelihood of hot parititions for an amazon user). Or have a hash of either ts /userid be partitionkey (good distribution poor range querying).</li>
</ol><p></p></div>
<br>

<strong>Solution 2: </strong><br>
<div class="discuss-markdown-container"><p>break the problem down to 3 main parts:</p><p>
</p><ul>
<li>Collection</li>
<li>Transport</li>
<li>Storage</li>
</ul>
<p></p><p>Discuss and nail down the specs:<br>
Discussion (with white board drawing) could be something like:<br>
Many things to clarify that affects the system design:</p><p>
</p><ol>
<li>
<p></p><p>Scale requirement is in terabytes/per day but:</p><p>
</p><ul>
<li>do we have a estimate on the peak load? Peak load defines the boundary for our system, we need to take them in to account.</li>
</ul>
</li>
<li>
<p></p><p>Availability requirements - what would be the acceptable uptime?</p><p>
</p></li>
<li>
<p></p><p>Security - should we be concerned about security?</p><p>
</p><ul>
<li>at collection side</li>
<li>at transport</li>
<li>storage</li>
</ul>
</li>
<li>
<p></p><p>Reliability requirements</p><p>
</p></li>
</ol>
<ul>
<li>Collection failures</li>
<li>Transport failures</li>
<li>Storage failures</li>
<li>Any acceptable data loss?</li>
<li>Redundancy / cost trade off</li>
</ul>
<ol start="5">
<li>
<p></p><p>Realtime requirements, how much lag is acceptable end-to-end?</p><p>
</p></li>
<li>
<p></p><p>Regional reqs ( wordwide deployments) - just to be clear that system does not need different specs for world wide deployments, if yes, we need to account for that.</p><p>
</p></li>
</ol>
<p></p><p>Proceed to design components:<br>
Collection discussion:</p><p>
</p><p>Will the collection agent parse the data and send it in a consumable form? (json or similar)</p><p>
</p><ul>
<li>parsing at source is a nice way to distributed the processing, trade off -&gt; using memory and CPU on application host. This way of distributing parsing load can be used catch offenders (those that spam logs)</li>
<li>parsing at a common backend location would lead to bottlenecks at large scales.</li>
</ul>
<p></p><p>Transport discussion:</p><p>
</p><ul>
<li>Kafka or other existing distributed queuing system should generally work</li>
<li>peak load factor is important here.</li>
<li>message size matters here, each message in kbs vs mbs. Gbs</li>
</ul>
<p></p><p>Storage:</p><p>
</p><ul>
<li>Ask how data in the DB is going to be queried?<br>
-Discuss trade offs using a full SQL DB vs pseudo-sql or no sql DB for the use case.</li>
<li>Propose aggregating data to some precision before storing:<br>
-user, login time, location</li>
<li>What is time scale at? seconds, milliseconds? nano seconds is going to be very hard</li>
<li>SQL db wont scale for seconds or milliseconds.</li>
<li>Propose aggregating at minute level - trade off some granularity</li>
<li>Ask what the user would do with the data, if they are only interested in higher level stats, aggregate up.</li>
<li>Propose aggregating  geo location to 1 decimal place - trade off in accuracy over miles.</li>
<li>Discuss TTL of fine grained data storage - trade off db storage performance vs ability to go back in time
<ul>
<li>propose, 30, 60, 90 day TTL and move to compressed archival.</li>
</ul>
</li>
<li>Does that data need additional processing?
<ul>
<li>filtering</li>
<li>date conversions/standardizations</li>
</ul>
</li>
<li>Design stream processing for aggregation to DB based on aggregation reqs.
<ul>
<li>discuss some stream processing algorithm cost/trade off like running median etc</li>
</ul>
</li>
<li>Discuss Db scaling
<ul>
<li>know internal workings of typical SQL , no SQL dbs</li>
</ul>
</li>
</ul><p></p></div>

<br>
<strong>Solution 3:</strong><br>
<div class="discuss-markdown-container"><p>Remember, SQL database was not designed for heavy writing. SQL database was designed for heavy relation data reading. No SQL would be a better solution at the first point. But still, if you want a SQL solution, you can create a pipeline with Apache Kafka and MemSQl. Here is the solution:</p><p>
</p><ol>
<li>Push your log data into Kafka Queue. Kafa use zookeeper for manager cluster nodes and absolute brilliant solution for heavy writing.</li>
<li>If you need to show the live data into your analytics, you can use memsql or any other similar solution.</li>
<li>Otherwise, connect your data to consume from the Apache Kafka queue. If you have really huge throughput pick, Kafka will hold and eventually will be updated into your database.</li>
</ol>
<p></p><p>Please add your valuable comments if I miss anything.</p></div>

<br>
<strong>Solution 4:</strong>
ELK stack

<br>
<strong>Solution 5:</strong><br>
<div class="discuss-markdown-container"><p>this is basically question of taking small projection of large number of columns and storing it in sql database from a  log which also has large amount of data per day. There is question of how to store the data and then what kind of execution system to use for processing the data to land in its final destination of sql db.</p><p>
</p><p>first of all use proper logging framework like log4j that can use appender to write to kafka topic.<br>
This way each logging message goes into a kafka topic and kafka cluster has to be sized according to size of data being ingested (few terabytes per day).<br>
Now you can have an execution engine like Presto or Spark read these messages from specific topic and do mapping operation to reduce number of columns from record to ingest into sql database .</p><p>
</p><p>This way you are decoupling the storage of logging from processing /ingestion of log data to sql database.</p></div>
