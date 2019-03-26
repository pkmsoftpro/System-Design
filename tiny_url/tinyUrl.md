
<div class="post-area__3YJL"><div class="root__3bcS"><div class="user-info__2b-x"><a href="/shashibk11"><img alt="shashibk11's avatar" class="avatar__7D9c" src="https://www.gravatar.com/avatar/ced675a53998ec50af79cd40d88120e3.png?s=200"></a><span class="name__2jm2"><a href="/shashibk11" class="link__Lpjq">shashibk11</a></span><span class="reputation___jPr"><svg viewBox="0 0 24 24" width="1em" height="1em" class="icon__3Su4"><path fill-rule="evenodd" d="M12 17.27L18.18 21l-1.64-7.03L22 9.24l-7.19-.61L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21z"></path></svg>261</span></div><div class="post-info__1K06"><p>Last Edit: October 26, 2018 3:20 PM</p><p class="view-count__dBuq">46.8K VIEWS</p></div></div><div class="content-area__2vnF"><div class="discuss-markdown-container"><p><strong>Problem:</strong> Design a service like TinyURL, a URL shortening service, a web service that provides short aliases for redirection of long URLs.</p><p>
</p><p><strong>Solution</strong>: If you don't know about <a href="https://tinyurl.com/">TinyURL</a>, just check it. Basically we need a one to one mapping to get shorten URL which can retrieve original URL later. This will involve saving such data into database.<br>
We should check the following things:</p><p>
</p><ul>
<li>What's the traffic volume / length of the shortened URL?</li>
<li>What's the mapping function?</li>
<li>Single machine or multiple machines?</li>
</ul>
<p></p><p><strong>Traffic</strong>: Let's assume we want to serve more than 1000 billion URLs. If we can use 62 characters [A-Z, a-z, 0-9] for the short URLs having length n, then we can have total 62^n URLs. So, we should keep our URLs as short as possible given that it should fulfill the requirement. For our requirement, we should use n=7 i.e the length of short URLs will be 7 and we can serve 62^7 ~= 3500 billion URLs.</p><p>
</p><p><strong>Basic solution</strong>:<br>
To make things easier, we can assume the alias is something like <a href="http://tinyurl.com/">http://tinyurl.com/</a>&lt;alias_hash&gt; and alias_hash is a fixed length string.<br>
To begin with, let’s store all the mappings in a single database. A straightforward approach is using alias_hash as the ID of each mapping, which can be generated as a random string of length 7.</p><p>
</p><p>Therefore, we can first just store &lt;ID, URL&gt;. When a user inputs a long URL “<a href="http://www.google.com">http://www.google.com</a>”, the system creates a random 7-character string like “abcd123” as ID and inserts entry &lt;“abcd123”, “<a href="http://www.google.com">http://www.google.com</a>”&gt; into the database.</p><p>
</p><p>In the run time, when someone visits <a href="http://tinyurl.com/abcd123">http://tinyurl.com/abcd123</a>, we look up by ID “abcd123” and redirect to the corresponding URL “<a href="http://www.google.com">http://www.google.com</a>”.</p><p>
</p><p><strong>Problem with this solution</strong>:<br>
We can't generate unique hash values for the given long URL. In hashing, there may be collisions (2 long urls map to same short url) and we need a unique short url for every long url so that we can access long url back but hash is one way function.</p><p>
</p><p><strong>Better Solution</strong>:</p><p>
</p><p>One of the most simple but also effective one, is to have a database table set up this way:</p><p>
</p><p>Table Tiny_Url(<br>
ID : int PRIMARY_KEY AUTO_INC,<br>
Original_url : varchar,<br>
Short_url : varchar<br>
)<br>
Then the auto-incremental primary key ID is used to do the conversion: (ID, 10) &lt;==&gt; (short_url, BASE). Whenever you insert a new original_url, the query can return the new inserted ID, and use it to derive the short_url, save this short_url and send it to cilent.</p><p>
</p><p><strong>Code for methods</strong> (that are used to convert ID to short_url and short_url to ID):</p><p>
</p><pre><code>string idToShortURL(long <span class="hljs-type">int</span> n)
{
    // Map <span class="hljs-keyword">to</span> store <span class="hljs-number">62</span> possible characters
    <span class="hljs-type">char</span> map[] = "abcdefghijklmnopqrstuvwxyzABCDEF"
                 "GHIJKLMNOPQRSTUVWXYZ0123456789";
  
    string shorturl;
  
    // Convert given <span class="hljs-type">integer</span> id <span class="hljs-keyword">to</span> a base <span class="hljs-number">62</span> number
    <span class="hljs-keyword">while</span> (n)
    {
        shorturl.push_back(map[n%<span class="hljs-number">62</span>]);
        n = n/<span class="hljs-number">62</span>;
    }
  
    // <span class="hljs-keyword">Reverse</span> shortURL <span class="hljs-keyword">to</span> complete base <span class="hljs-keyword">conversion</span>
    reverse(shorturl.<span class="hljs-keyword">begin</span>(), shorturl.<span class="hljs-keyword">end</span>());
  
    <span class="hljs-keyword">return</span> shorturl;
}
  
// <span class="hljs-keyword">Function</span> <span class="hljs-keyword">to</span> <span class="hljs-keyword">get</span> <span class="hljs-type">integer</span> ID back <span class="hljs-keyword">from</span> a short url
long <span class="hljs-type">int</span> shortURLtoID(string shortURL)
{
    long <span class="hljs-type">int</span> id = <span class="hljs-number">0</span>; // initialize result
  
    // A simple base <span class="hljs-keyword">conversion</span> logic
    <span class="hljs-keyword">for</span> (<span class="hljs-type">int</span> i=<span class="hljs-number">0</span>; i &lt; shortURL.length(); i++)
    {
        <span class="hljs-keyword">if</span> (<span class="hljs-string">'a'</span> &lt;= shortURL[i] &amp;&amp; shortURL[i] &lt;= <span class="hljs-string">'z'</span>)
          id = id*<span class="hljs-number">62</span> + shortURL[i] - <span class="hljs-string">'a'</span>;
        <span class="hljs-keyword">if</span> (<span class="hljs-string">'A'</span> &lt;= shortURL[i] &amp;&amp; shortURL[i] &lt;= <span class="hljs-string">'Z'</span>)
          id = id*<span class="hljs-number">62</span> + shortURL[i] - <span class="hljs-string">'A'</span> + <span class="hljs-number">26</span>;
        <span class="hljs-keyword">if</span> (<span class="hljs-string">'0'</span> &lt;= shortURL[i] &amp;&amp; shortURL[i] &lt;= <span class="hljs-string">'9'</span>)
          id = id*<span class="hljs-number">62</span> + shortURL[i] - <span class="hljs-string">'0'</span> + <span class="hljs-number">52</span>;
    }
    <span class="hljs-keyword">return</span> id;
}

</code></pre>
<p></p><p><strong>Multiple machines:</strong></p><p>
</p><p>If we are dealing with massive data of our service, distributed storage can increase our capacity. The idea is simple, get a hash code from original URL and go to corresponding machine then use the same process as a single machine. For routing to the correct node in cluster, Consistent Hashing is commonly used.</p><p>
</p><p>Following is the pseudo code for example,</p><p>
</p><p><strong>Get shortened URL</strong></p><p>
</p><ul>
<li>
<p></p><p>hash original URL string to 2 digits as hashed value hash_val</p><p>
</p></li>
<li>
<p></p><p>use hash_val to locate machine on the ring</p><p>
</p></li>
<li>
<p></p><p>insert original URL into the database and use getShortURL function to get shortened URL short_url</p><p>
</p></li>
<li>
<p></p><p>Combine hash_val and short_url as our final_short_url (length=8) and return to the user</p><p>
</p></li>
</ul>
<p></p><p><strong>Retrieve original from short URL</strong></p><p>
</p><ul>
<li>
<p></p><p>get first two chars in final_short_url as hash_val</p><p>
</p></li>
<li>
<p></p><p>use hash_val to locate the machine</p><p>
</p></li>
<li>
<p></p><p>find the row in the table by rest of 6 chars in final_short_url as short_url</p><p>
</p></li>
<li>
<p></p><p>return original_url to the user</p><p>
</p></li>
</ul>
<p></p><p><strong>Other factors</strong>:</p><p>
</p><p>One thing I’d like to further discuss here is that by using GUID (Globally Unique Identifier) as the entry ID, what would be pros/cons versus incremental ID in this problem?</p><p>
</p><p>If you dig into the insert/query process, you will notice that using random string as IDs may sacrifice performance a little bit. More specifically, when you already have millions of records, insertion can be costly. Since IDs are not sequential, so every time a new record is inserted, the database needs to go look at the correct page for this ID. However, when using incremental IDs, insertion can be much easier – just go to the last page.</p><p>
</p><pre><code>You can connect <span class="hljs-keyword">with</span> me <span class="hljs-symbol">here:</span> <span class="hljs-symbol">https:</span>/<span class="hljs-regexp">/www.linkedin.com/in</span><span class="hljs-regexp">/shashi-bhushan-kumar-709a05b5/</span>
</code></pre>
<p></p><p>References: <a href="http://blog.gainlo.co/index.php/2016/03/08/system-design-interview-question-create-tinyurl-system/">http://blog.gainlo.co/index.php/2016/03/08/system-design-interview-question-create-tinyurl-system/</a><br>
<a href="https://www.geeksforgeeks.org/how-to-design-a-tiny-url-or-url-shortener/">https://www.geeksforgeeks.org/how-to-design-a-tiny-url-or-url-shortener/</a></p></div></div><div class="tag-list-container__2cDj"><div class="css-9sdfuf"><span class="css-vh6pmz">system design</span></div></div></div>
