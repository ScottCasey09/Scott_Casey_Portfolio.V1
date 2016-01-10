using System;

namespace Scott_Casey_Functions_Assignment
{
	class MainClass
	{
		public static void Main (string[] args)
		{
			/*
			  Casey Scott
			  11-19-2015
			  section 01
			  Functions Assignment
			*/

			//Ask your for their name and record the answer
			Console.WriteLine ("Please enter your name to begin, then press return.");


			//Create a variable to hold the answer
			string userName = Console.ReadLine ();
		

			//Validate the users response
			while (string.IsNullOrWhiteSpace (userName)) {

				//Report a problem to the user if left blank
				Console.WriteLine ("I'm sorry I didn't catch your name. Please enter your name and press return.");

				userName = Console.ReadLine ();
			}

			//tell the user to enter the loan amount
			//say hello to the user
			Console.WriteLine ("Hello {0}, welcome to my amoritzed home loan calculation program.\r\nLet's start by getting some numbers from you.\r\nPlease enter the amount of the loan in US Dollars.\r\nPress return when finished.", userName);

			//Capature the users response
			string loanString = Console.ReadLine ();

			//Declare the variables to hold the user prompts
			decimal loan;
			decimal loanLength;
			decimal interestRate;


			//Convert to double
			//Validate the user typed a number
			while (!decimal.TryParse (loanString, out loan)) {

				//Tell the user to only enter whole numbers
				Console.WriteLine ("You have entered something other than a number.\r\nPlease type in your loan total in US Dollars, then press enter.");

				//Recapture the users answer
				loanString = Console.ReadLine ();
			}
			//Report a comment and prompt the user to enter the loan term
			//Give conditional responses
			if (loan >= 500000) {
				Console.WriteLine ("Okay, so you are looking for a large loan.\r\nNow, please enter the length of the loan term in months please {0}.", userName);
				//Capture the response in a string
				string loanLengthString = Console.ReadLine ();


				//Convert to decimal
				//Validate response
				while (!decimal.TryParse (loanLengthString, out loanLength)) {

					//Report to the user that they typed something other then a number
					Console.WriteLine ("You have entered something other than a number.\r\nPlease enter the loan length in months please.");

					//Catch the response
					loanLengthString = Console.ReadLine ();
				}
			} else {
				//Report a comment and prompt the user to enter the loan term
				Console.WriteLine ("Okay so a normal size loan, Please enter the length of the loan term in months please {0}.", userName);

				//Capture the response in a string
				string loanLengthString = Console.ReadLine ();


				//Convert to decimal
				//Validate response
				while (!decimal.TryParse (loanLengthString, out loanLength)) {
					
					//Report to the user that they typed something other then a number
					Console.WriteLine ("You have entered something other than a number.\r\nPlease enter the loan length in months please.");

					//Catch the response
					loanLengthString = Console.ReadLine ();

				}
			
			}
			//declare function to decimal
			decimal totalPaymentBeforeInt = beforeInterest (loan, loanLength);

			//round the total 
			decimal roundedTotal = Math.Round (totalPaymentBeforeInt, 2);

			//Give the total per month before we add in the interest
			Console.WriteLine ("You entered ${0} for the loan amount and {1} months for the term.\r\nThe total monthly payment would be ${2} per month.\r\nNow lets see what it will really be after those greedy lenders get their money", loan, loanLength, roundedTotal);

			//Prompt the user for the interest rate
			Console.Write ("Please tell me what the lenders intrest rate is as a decimal.\r\nFor example if the interset rate is 6%, enter in 0.06\r\nPress return when done.");

			//Capture the users answer in string
			string interestString = Console.ReadLine ();

			//Convert string to number with validation
			while (!decimal.TryParse (interestString, out interestRate)) {

				//Report a problem with the users entry
				Console.WriteLine ("You entered something other then an interger.\r\nPlease enter the intrest rate percentage as a decimal then press return.");
				 
				//Capture the re-entery
				interestString = Console.ReadLine ();


			
				
			}
			//Converted variable for rate per period
			//Report to the user
			Console.WriteLine ("{0}, You entered {1} as your intrest rate.\r\nMy program has completed the calculations.", userName, interestRate);



			//Convert interest rate to per period
			//Convert to interest rate to double
			double interestRateConverted = Convert.ToDouble(interestRate);
			double convertedRateMultiplied = interestRateConverted / 12;

			//Convert user prompted variables to doubles
			double loanDouble = Convert.ToDouble(loan);
			double loanLengthDouble = Convert.ToDouble (loanLength);

			//Create a variable to hold the function
			double finalPayment = amLoanPayment (loanDouble, convertedRateMultiplied, loanLengthDouble);

			//Round final payment to simulate money
			double endingValue = Math.Round (finalPayment,2);

			//Report the final payment after the interest to the user
			Console.WriteLine ("You total monthly payment after interest will be ${0}", endingValue);
		

			/*      Tests:
			 * 
		 * I entered $20,000.00 for loan amount
		 * 60 months for the term   = (function #1) $333.33 - checked on calculator
		 * 7.5% as a decimal - 0.075 = (function #2) $400.76 - checked on amortization loan calculator
		 *  
		 * I entered $100,000.00 for the loan
		 * 360 months for the term   = (finction #1) $278.78 - checked on calculator
		 * 7.5% as a decimal - .075 = (function #2) $699.21 - checked on amortization loan calculator
		 * 
		 *  I entered $50,000.00 for loan amount
		 * 48 months for the term   = (function #1) $1,041.67 - checked on calculator
		 * 5% as a decimal - 0.05 = (function #2) $1151.46 - checked on amortization loan calculator
		 */


		}
		
		//Create function to calculate the payment before interest
		public static decimal beforeInterest (decimal loanAm,decimal term){

			//Do the math
			decimal paymentBefore = loanAm/term;

			//Return answer to main
			return paymentBefore;
		
		}

		//Create a function to calculate the monthly amortized loan payment

		public static double amLoanPayment(double loanAmount,double rate, double length){

			//Do the math for the final payment with interest
			double numerA = Math.Pow ((1+rate),length);
			double numer = rate * numerA;
			double denom = Math.Pow((1+rate),length);
			denom--;
			double preTotal = numer/denom;
			double payment = loanAmount * preTotal;

			//round the total to  the 100TH for money.
			double paymentRounded = Math.Round (payment,2);

			//Return the final payment to the main
			return paymentRounded;
		}


		              
	}
		
}
