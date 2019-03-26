https://www.coursera.org/lecture/ml-foundations/effect-of-popular-items-3rmRx

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
