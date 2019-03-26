https://leetcode.com/discuss/interview-question/system-design/124804/Design-Pastebin

<div class="post-area__3YJL"><div class="root__3bcS"><div class="user-info__2b-x"><span class="name__2jm2"></span></div><div class="content-area__2vnF"><p>It was asked to me in <strong>Amazon</strong> interview for SDE-2 position.</p>
	<p><strong>What is Pastebin?</strong><br>
Pastebin web service enable users to store plain text over the network and generate unique URLs to access the uploaded data. It is also used to share data over the network quickly, as users would just need to pass the URL to let other users see it.</p><p>
</p><p><strong>Functional Requirements:</strong></p><p>
</p><ol>
<li>Users should be able to upload plain text called as "Pastes".</li>
<li>Upon uploading the paste user should be provided unique url to access it.</li>
<li>Users should only be able to upload text.</li>
<li>Url's should be expired after certain period of time, if expiration time not provided by the user.</li>
</ol>
<p></p><p><strong>Non-Functional Requirements:</strong></p><p>
</p><ol>
<li>System should be highly reliable i.e no data loss.</li>
<li>System should be highly available i.e user should be able to access their paste most of the time.</li>
<li>Minimum latency to fetch user pastes.</li>
</ol>
<p></p><p><strong>Database Design:</strong></p><p>
</p><ol>
<li>
<p></p><p>User table<br>
a. name<br>
b. email<br>
c. created_date<br>
d. last_login<br>
e. userId - PK</p><p>
</p></li>
<li>
<p></p><p>Paste<br>
a. paste_name<br>
b. url<br>
c. content<br>
d. expiration_time<br>
e. created_date<br>
f. userId - FK<br>
g. pasteId - PK</p><p>
</p></li>
</ol>
<p></p><p><strong>System APIs</strong></p><p>
</p><p>Can be implemented as Restful Webservices as -</p><p>
</p><ol>
<li>createPaste</li>
<li>updatePaste</li>
<li>getPaste</li>
<li>deletePaste</li>
</ol>
<p></p><p><strong>High Level Design</strong></p><p>
</p><p>At a high level, the system needs an application server that can serve read and write request. Application server will store paste data on block storage. All the metadata related to paste and user will be stored into a database. At a high level, various cache servers and load balancer can be configured to improve performance and scalabilty of the system.</p><p>
</p><p><img src="https://discuss.leetcode.com/assets/uploads/files/1517558990493-high-level-design-resized.png" alt="0_1517558987394_High Level Design.PNG"></p></div></div><div class="tag-list-container__2cDj"><div class="css-9sdfuf"><span class="css-vh6pmz">system design</span><span class="css-vh6pmz">pastebin</span></div></div></div>
