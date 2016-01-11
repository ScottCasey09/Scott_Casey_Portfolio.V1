using System;

namespace Functions_Worksheet
{
	class MainClass
	{
		public static void Main (string[] args)
		{
			/*
			Casey Scott
			11-10-2015
			section 01
			Functions worksheet
			*/

			//Write out our givens

			decimal originalCost = 20m;
			decimal taxPercent = .07m;

			//Go write our finction
			//function call
			//Remember to create a variable to catch the returned value
			decimal results = totalCost(originalCost, taxPercent);

			//Report results
			Console.WriteLine("The total cost of the item plus tax is ${0}.\r\n",results);

			//End of Plus Tax - all values checked



			//Givens for the circumference of a circle
			double radius = 4;
			double pie = Math.PI;
			double circResults = circumferenceOfACircle(radius, pie);


			//Report the result of the circumference
		Console.WriteLine("The circumference of a circle with the radius of "+radius+", is {0}.\r\n ", circResults);

		

			//Givens for stings
			double victimsWeightlb = 200;
			double stingTakes = 8.666666667;
			double stingResults = beeStings (stingTakes,victimsWeightlb);

			//Print results of bee stings
			Console.WriteLine("It takes {0} bee stings to kill this animal.",stingResults);

		}
		//Sample problem
		public static decimal totalCost(decimal cost, decimal tax){

			//Do math in order to find total cost
			decimal taxMoney = cost * tax;

			//Add tax money to the actual cost
			decimal totalCost = cost + taxMoney;

				//Return the value back to our main method
				return totalCost;


		}
		//Circumference of a circle
	
		public static double circumferenceOfACircle(double rad,double pi)
		{
			//Do the math for the circumference of a circle
			double circumferenceOfACircle = rad * pi;

			//return this to the main
			return circumferenceOfACircle;

	
		}


		//Stung!
		public static double beeStings(double stings,double weightlb)
		{
			//Do the math
			double beeStings = weightlb/stings;

			//Return to main
			return beeStings;




		}

	}

}
