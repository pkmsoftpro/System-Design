https://www.coursera.org/lecture/ml-foundations/effect-of-popular-items-3rmRx

Build a simple co-occurance matrix and use it to recommend items to users: 
https://www.coursera.org/learn/ml-foundations/lecture/CdmdR/collaborative-filtering-people-who-bought-this-also-bought
Note: YOU DO NOT NEED TO KNOW MACHINE LEARNING TO UNDERSTAND THIS CONCEPT.

<div class="discuss-markdown-container"><p>Here is my design from the simplest to more optimized</p><p>
</p><ol>
<li>
<p></p><p>Find all the users who bought item A and then count all the other items these uses bought. Return a list of items sorted by purchase count. Use a table join to find all the other uses who bought item A and then use a map-reduce (?) to return the sorted item count. This approach is too expensive to run at real time.</p><p>
</p></li>
<li>
<p></p><p>Use Bayesian Model: define P(A) the probability of item A is purchased. P(X) the probability of item X is purchased.<br>
For each item X, we calculate P(X | A) = P (A|X) * P(X) / P(A)</p><p>
</p></li>
<li>
<p></p><p>Use Matrix Factorization:<br>
Build a recommendation system using Matrix Factorization. The input matrix is M with N rows (number of users) and L columns (number of items). Each entry at M_ij is either 1 or 0 indicating user i purchased item j or not. Using standard algorithms we can get a low rank matrix U and P, where U is N x K and P is K x L. There are K categories.<br>
U_ik indicates user i's affinity to category k and P_kj indicates how much item j should be categorized into k.</p><p>
</p></li>
</ol>
<p></p><p>If we have a users past history, we can easily calculate vector P and U and gets the users purchase intention.</p><p>
</p><p>If it is a new user, we can train a logistic regression model based on user feature for each category k and calculate the U vector for the new user.</p><p>
</p><p>If it is a new item, we can train a logistic regression model based on item feature for each category k and calculate the P vector for the new item.</p><p>
</p><p>Multiplying P and U vector, we get the probability of a user who will purchase an item X. Return the list of items sorted by probability.</p></div>

<pre>
<div class="discuss-markdown-container"><p>
This is a graph problem. Therefore, we need N x N space to represent the bi-directional graph. N being number of products there are. Say there are 4 products, A, B, C, D. Edges of the graph will represent number of buys relationship.<br>
If a user buys product A with product B. Then in the graph, edges A to B and B to A will be incremented once. Similar situation if a user buys A, B, C together, that means 12 edges will need to be updated.<br>
That equals to 2^(# of products bought) - (# of products bought) updates to the graph. Lets call this U for updates.<br>
Next, each node/product in the graph will have a min heap with a fixed size. Say that size is 10. This will represent the 10 best items/edges bought with this product. Therefore, when someone buys products, it will check that product's top min heap and compares it to the updated edge. If the edge(which represents # of buys) is greater than the one on top of the min heap, then it will replace it then heapify. The buy() method will be U(log(size)) run-time if it requires a replacement, else O(1) for each product.</p><p>
</p><p>Now to the recommendation system. getRecommendations() will be a constant run-time of size of the heap. Pop off the heap or if its an array implementation of a heap, just go down the array.</p><p>
</p><p>Run-time break down:<br>
getRecommendations(product) -&gt; # of recommendations<br>
buy(list of products) -&gt; ((2^(# of products bought) - (# of products bought))) * log(# of recommendations) worst case for each product</p><p>
</p><p>space complexity = # of products * # of products and # of products * size</p></div>
</pre>
