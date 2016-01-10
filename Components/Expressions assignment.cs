using System;

namespace Scott_Casey_Expressions_Assignment_Trial
{
	class MainClass
	{
		public static void Main (string[] args)
		{
			/*
			  Casey Scott
               11-2-15
              Expressions Assignment
			*/

			//Introduce yourself 
			//describe your solution center
			string introduction = "HI,my name is Casey and welcome to my mulch bag calculator";
			Console.WriteLine (introduction);


			//State the reason for my solution center
			Console.WriteLine("This center to help you with figuring out many bags of mulch you will need\r\nto put thoes final touches on your landscape project");

			//Describe how many square feet are in one bag
			//Prompt user for lenght of the project
			Console.WriteLine ("Each bag contains 3sq.ft\r\nWhat is the length of your area in feet?\r\nPress enter when done.");

			//Record the users answer
			string lengthstring = Console.ReadLine ();

			//convert the string into number data
			double lenght = double.Parse (lengthstring);


			//Prompt user for the width
			Console.WriteLine("What is the width of your area in feet?\r\nPress enter when done.");

			//Record the users answer
			string widthstring = Console.ReadLine ();

			//Convert the string into number data
			double width = double.Parse (widthstring);

		

			//Multiply the length by the width and print to screen
			Console.WriteLine (lenght*width+"sq.ft");

			//Divide the total by 3
			double total = lenght * width / 3;

			//Convert double to a whole number/int
			int totalConverted = (int)total;
            
			//Add one more bag to round up and to ensure coverage
			totalConverted++;	 
			    
			//Let the user know how many bags they will need  
			//Devide the square footage by 3
			Console.WriteLine("With the square footage you gave us you need " +totalConverted+ " bags of mulch \r\nto put the finishing tuches on\r\nwhat will be a beautiful landscape." );



		

		}
	}
}
