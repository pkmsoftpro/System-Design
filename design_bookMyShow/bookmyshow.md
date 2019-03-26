https://leetcode.com/discuss/interview-question/system-design/124803/Design-BookMyShow

<p><strong>Describe how would you design an online ticketing system that sells movie tickets like BookMyShow.</strong></p><p>
</p><p><strong>Where:</strong> I was asked this problem for an interview for Oracle.</p><p>
</p><p><strong>Solution:</strong> I started with defining goals and requirement of the system. Few functional and non functional requirement are as follows -</p><p>
</p><p><strong>Functional Requirements:</strong></p><p>
</p><ol>
<li>The system should be able to list down cities where its cinemas are located.</li>
<li>Upon selecting the city, the system should display the movies released in that particular city to that user.</li>
<li>Once the user makes his choice of the movie, the system should display the cinemas running that movie and its available shows.</li>
<li>The user should be able to select the show from a cinema and book their tickets.</li>
<li>The system should be able to show the user the seating arrangement of the cinema hall.</li>
<li>The user should be able to select multiple seats according to their choice.</li>
<li>The user should be able to distinguish between available seats from the booked ones.</li>
<li>Users should be able to put a hold on the seats for 5/10 minutes before they make a payment to finalize the booking.</li>
<li>The system should serve the tickets First In First Out manner</li>
</ol>
<p></p><p><strong>Non-Functional Requirements:</strong></p><p>
</p><ol>
<li>The system would need to be highly concurrent as there will be multiple booking requests for the same seat at any particular point in time. The design should be such that system handles such ambiguity fairly.</li>
<li>Secure and ACID compliant.</li>
</ol>
<p></p><p><strong>Database Model:</strong></p><p>
</p><ol>
<li>City</li>
<li>Movie</li>
<li>Cinema</li>
<li>Cinema Hall</li>
<li>Show</li>
<li>Seat</li>
<li>Booking</li>
<li>User</li>
<li>Payment</li>
</ol>
<p></p><p><strong>Relationships between them:</strong></p><p>
</p><ol>
<li>A City can have multiple Cinemas.</li>
<li>A Cinema can have multiple halls.</li>
<li>A Movie can will have many Shows.</li>
<li>A Show will have multiple Bookings.</li>
<li>A User can have multiple bookings.</li>
</ol>
<p></p><p><strong>How to handle concurrency such that no two users are able to book same seat?</strong><br>
Utilize transaction isolation levels.<br>
<strong>SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;</strong> for the seat and booking update. As Serializable level guarantees safety from Dirty, Nonrepeatable and Phantoms reads.</p></div></div><div class="tag-list-container__2cDj"><div class="css-9sdfuf"><span class="css-vh6pmz">system design</span><span class="css-vh6pmz">bookmyshow</span></div></div></div>
