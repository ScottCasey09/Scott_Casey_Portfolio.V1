using System;

namespace Scott_Casey_Conditionals_Assignment
{
	class MainClass
	{
		public static void Main (string[] args)
		{
			/*Casey Scott
			 * 11-10-2015
			 * Section 01
			 * Conditionals Assignment
			*/
			 
			//Variable for users score
			int usersTotalScore = 0;

			//Introduce the purpose of this program
			Console.WriteLine ("Welcome to The Pit Stop Golf Course.\r\nWe are pleased to have you at our new state\r\nof the art quick play three hole par 3 golf course.");

			//Use a new Console.Writeline to keep the code line nicer to read
			//Continue purpose
			Console.WriteLine ("Included in your golf expirence is this score keeper program.\r\nNo longer will you have to add your own scores.\r\n");

			//Prompt user for their user name
			Console.WriteLine ("Before you tee off, lets get set up.\r\nPlease enter your user name then press return.");

			//Capture users name and store as string
			string userName = Console.ReadLine ();

			//Create reprompt if left blank
			if (string.IsNullOrWhiteSpace (userName)) {
				//Tell user that this can not be left blank
				Console.WriteLine ("You did't enter a user name. Please enter a user name and press return.");
				userName = Console.ReadLine ();
			}

			//Welcome user by name
			Console.WriteLine ("Hello " + userName + ", welcome to the Pit Stop Golf Course.\r\nAt the end of every hole enter your total score then press return.\r\nThe max score for each hole is 9 strokes.");



			//Strat at hole #1 and collect users score
			Console.WriteLine ("After completeing this hole enter your stroke count as an integer of 1-9 and press return.");
			string holeOneString = Console.ReadLine ();

			//Create new int to store users scores
			int holeOneCount; 

			//Make sure the user typed an integer
			if (!(int.TryParse (holeOneString, out holeOneCount))) {
				Console.WriteLine ("You typed something other then an integer of 1-9. Please re-enter your score as an integer of 1-9.");

				//Convert string to int
				holeOneString = Console.ReadLine ();
				int.TryParse (holeOneString, out holeOneCount);
					


			}
			//Give responses to the user based on entry
			//If the user scored a hole in one
			if (holeOneCount == 1) {
				Console.WriteLine ("Hole in one, AMAZING!! What a shot!!");

				//If the user scored a 2
			} else if (holeOneCount == 2) {
				Console.WriteLine ("Great job, you dominated this hole and got a birdie!!");

				//If the user scored a 3
			} else if (holeOneCount == 3) {
				Console.WriteLine ("You got a par on the first hole.");

				//If the user scored a 3	
			} else if (holeOneCount == 4) {
				Console.WriteLine ("You got a boggy on the first hole.");
			
				//If the user scored a 5
			} else if (holeOneCount == 5) {
				Console.WriteLine ("Double boggy, let's try to lose a couple strokes on the next one.");
			
				//If the user scored a 6 or higher
			} else if (holeOneCount >= 6 && holeOneCount<=9) {
				Console.WriteLine ("Tighten up your game. Maybe you should stick to a video game.");

				//If the user enters something over the max of 9
			} else {
				Console.WriteLine ("You maxed out this hole. You get a 9.");
			
			}
			//Convert int holeOneCount to 9 if user entry is too high
			if(holeOneCount>9){
				holeOneCount = 9;
				
			}

			//Declare new score total
			int usersScoreAfterOne = usersTotalScore + holeOneCount;

			//Print to user their new score total
			Console.WriteLine ("Your score is now " +usersScoreAfterOne);

			//Declare int for hole #2
			int holeTwoCount;

			//Go on to the next hole
			Console.WriteLine("On to hole #2. Go ahead and tee off. Then when your finished with the hole type your stroke count and press return.");
			string holeTwoCountString = Console.ReadLine ();


			//Give user response on wrong entry
			if (!(int.TryParse (holeTwoCountString, out holeTwoCount))) {
				Console.WriteLine ("You typed something other then an integer. Please re-enter your score as an integer.");


			
				//Store the users information
				holeTwoCountString = Console.ReadLine ();
				//Convert string to int.TryParse
				int.TryParse (holeTwoCountString, out holeTwoCount);
			}
				
			//Give responses to the user based on entry
			//If the user scored a hole in one
			if (holeTwoCount == 1) {
					Console.WriteLine ("Hole in one, AMAZING!! What a shot!!");

				//If the user scored a 2
				} else if (holeTwoCount == 2) {
				Console.WriteLine ("Great job, you dominated this hole and got a birdie!!");

				//If the user scored a 3
				} else if (holeTwoCount == 3) {
					Console.WriteLine ("You got a par on the second hole.");

				//If the user scored a 4
				} else if (holeTwoCount == 4) {
					Console.WriteLine ("You got a boggy on the second hole.");

				//If the user scored a 5
				} else if (holeTwoCount == 5) {
					Console.WriteLine ("Double boggy, let's try to lose a couple strokes on the next one.");

				//If the user scored a 6 or higher
			} else if (holeTwoCount >= 6 && holeTwoCount<=9) {
					Console.WriteLine ("Tighten up your game. Maybe you should stick to a video game.");

				//If the user enters something over the max of 9
			} else {
				Console.WriteLine ("You maxed out this hole. You get a 9.");

			}
			//Convert int holeOneCount to 9 if user entry is too high
			if(holeTwoCount>9){
				holeTwoCount = 9;

			}

				




			//Declare new score total
			int usersScoreAfterTwo = (usersTotalScore + holeOneCount) + holeTwoCount;

			//Print to user their new score total
			Console.WriteLine ("Your score is now " + usersScoreAfterTwo);

			//Declare int for hole #2
			int holeThreeCount;

			//Go on to the next hole
			Console.WriteLine("On to the last hole. Go ahead and tee off. Then when your finished with the hole type your stroke count and press return.");
			string holeThreeCountString = Console.ReadLine ();


			//Give user response on wrong entry
			if (!(int.TryParse (holeThreeCountString, out holeThreeCount))) {
				Console.WriteLine ("You typed something other then an integer. Please re-enter your score as an integer.");



				//Store the users information
				holeTwoCountString = Console.ReadLine ();
				//Convert string to int.TryParse
				int.TryParse (holeThreeCountString, out holeThreeCount);
			}

			//Give responses to the user based on entry
			if (holeThreeCount == 1) {
				Console.WriteLine ("Hole in one, AMAZING!! What a shot!!");

				//If the user got a 2
			} else if (holeThreeCount == 2) {
				Console.WriteLine ("Great job, you dominated this hole and got a birdie!!");
				//If the user scored a 3
			} else if (holeThreeCount == 3) {
				Console.WriteLine ("You got a par on the last hole.");
				//If the user scored a 4
			} else if (holeThreeCount == 4) {
				Console.WriteLine ("You got a boggy on the last hole.");
				//If the user scored a 5
			} else if (holeThreeCount == 5) {
				Console.WriteLine ("Double boggy.");

			//If the user scored a 6 or higher
			} else if (holeThreeCount >= 6 && holeThreeCount<=9) {
				Console.WriteLine ("You got a +3 or higher.");
			
				//If the user enters something over the max of 9
			} else {
				Console.WriteLine ("You maxed out the final hole. You get a 9.");

			}
			//Convert int holeOneCount to 9 if user entry is too high
			if(holeThreeCount>9){
				holeThreeCount = 9;

			}

			//Declare new score total
			int finalScore = ((usersTotalScore) + (holeOneCount) + (holeTwoCount) + (holeThreeCount));

			//Print to user their new score total
			Console.WriteLine ("Your final score is " + finalScore+ ".");

			//let the user know they were really good
			if (finalScore <=6){
				Console.WriteLine ("WOW!!, " +userName+ ", Fantastic!! You should think about going pro.");
			
				//Give the user a compliment and good bye
			} else if(finalScore <= 9 && finalScore>6){
				Console.WriteLine ("You did great on this course " +userName+ ". Thanks for stoping by.");
			
				//Tell user good bye
			} else {
				Console.WriteLine("Thanks " +userName+ " for playing the Pit Stop Golf Course.");
			}


			//Tests

			// I left the name question blank and It read back the the null or blank space command and then asked me to re-enter my name.

			//For of the score entry spaces, if I were to use a letter or leave it blank the console would tell me to re-enter a score 1-9

			//For any number entered by the user that was high than the score limit the program would automatically read a 9 by default

			/*I tested the math and outcome by entering 2 for each entry and each time it read back the proper line and added the scores to 6
			 * and the end result was read back correctly with "userName+ " Fantastic!! You should think about going pro." because it was 6 or less*/

			// I tested using numbers with double digits and the outcome read all 9s for a final score of 27 which read the "else statement" at the end

			// I used all hole in ones and that is what was read back by the program for a total score of three which read back the "if statemant."

			//I typed decimal numbers and the program read back to re-enter an integer.

			//The program was tested every step of the way during creation to eliminate mass amounts of debugging at once.

			//Plus entered many different combination of numbers and the code was translated perfectly.
		}


		
	}
}
