Solution 1:
https://leetcode.com/discuss/interview-question/system-design/124858/First-non-repeating-word-in-a-file-File-size-can-be-100GB.
It could be 1000's of 100GB files

We could cluster of parsers, filter, NoSql Database.

    Parsers servers: fill 100MB queue of String separated by white space, then feeds queues to the load balancer
    Load Balancer: distributing queue to filtering servers.
    Filter server: filters-outs enclosures, punctuation, special character, emails and non-words. Also, correctly identifying common errors, such single quotes used as an apostrophe.
    NoSql database: holds single occurring words and transmits an exclusion list of repeated words to the filters.

Extra: use ZooKeeper to monitor the server and DB to transmission configuration and update tasks.

Solution 2:
Create a HashMap for the unique words and their index, create a HashSet for the duplicated words.
<pre>
1- Iterate through words
2- Check if in HashMap
3- If in HashMap, remove from HashMap and add to HashSet.
4- If not in HashMap, check if in HashSet
5- If in HashSet, skip
6- If not in HashSet, add to HashMap with index
7- Iterate through HashMap
8- Return entry with smallest value (index)
</pre>
Solution 3:
This seems like a single pass two linked list approach makes the most sense to me.
Here is a C# implementation, tested with very small string. Over all structure will be one linked list node for every unique word in the 'file' - variable in my case.

using System;
using System.Text.RegularExpressions;
using System.Collections.Generic;

namespace FirstNonRepeatingWord
{
	class MainClass
	{
		public static void Main(string[] args)
		{
                        // Could use args as input
			string Input = "The quick brown fox live in a The fox house on quickly road";
         
                        // Two linked lists with the node closest to the head being the first found:
                        //  1) For the words that are unique so far, wont know until we read them all.
                        //  2) For the words that we found before and know are not unique.
			LinkedList<string> UniqueWordsSoFar = new LinkedList<string>();
			LinkedList<string> NonUniqueWord = new LinkedList<string>();

			Regex ItemRegex = new Regex (@"\w*");
			foreach (Match MatchedWord in ItemRegex.Matches(Input))
			{
				Console.WriteLine("Match: " + MatchedWord.Value);
				if (NonUniqueWord.Find(MatchedWord.Value) == null)
				{
					if (UniqueWordsSoFar.Find (MatchedWord.Value) == null)
					{
						UniqueWordsSoFar.AddLast(MatchedWord.Value);
					}
					else
					{
						UniqueWordsSoFar.Remove(MatchedWord.Value);
						NonUniqueWord.AddLast(MatchedWord.Value);
					}
				}
			}
		
			Console.WriteLine ("First Unique word: " + UniqueWordsSoFar.First.Value);
		}
	}
}

Oops should have read word at a time.... I may post a fix later.... not much different from the regex approach conceptually, just a lot less memory used! :-)
