using System;

namespace TotalProject
{
	class MainClass
	{
		public static void Main (string[] args)
		{
			/*
			 * Casey Scott
			 * 11-11-2015
			 * total project
			 * section 01
			*/

			//Calculate the area and perimeter of a rectangle from user prompts

			//Tell the user what we are doin and then ask fro the width
			Console.WriteLine ("Hello! We are going to find the area and perimeter of a rectangle.\r\nPlease type in a value for the width and hit return.");

			//Capture the users response
			string widthString = Console.ReadLine ();

			//Declare a variable to hold the converted value
			double width;

			//Validate the user is typing in a valid number using a while loop
			while (!double.TryParse (widthString, out width)) {

				//alert the user of a error
				Console.WriteLine ("Please only type numbers and do not leave blank.");
				//Recapture the users response, in the same variable
				widthString = Console.ReadLine ();

			}

			//Tell the user the width and say thanks and ask for the length
			Console.WriteLine ("Got it! You entered the width of {0}\r\nPlease enter a length now and press return.", width);

			//Create a variable to capture the users response
			string lengthString = Console.ReadLine ();

			//declare the variable to hold the converted value
			double length;

			//Validate using a while loop
			while (!double.TryParse (lengthString, out length)) {

				//alert the user of a error
				Console.WriteLine ("Please only type numbers and do not leave blank.");
				//Recapture the users response, in the same variable
				lengthString = Console.ReadLine ();
		
			
			}
			//Tell the user we got the lenght and tell them next step
			Console.WriteLine ("Thank you! You entered a width of {0}, and a length of {1}.\r\nWe can calculate the perimeter now.", width, length);

			//Create a function to calculate the perimeter
			//Function call the calcPeri method

			//Remember to batch the returned value with a variable
			double perimeter = calcPeri(width,length);

			//Report to the user
			Console.WriteLine("The perimeter of the rectangle is {0}.",perimeter);

			//Create a function to calculate the area
			//Function Call it and store the returned value
			double area = calcArea(width,length);

			//Report to the user
			Console.WriteLine("The area of the rectangle is {0}.",area);

			//Tell the user we want to find the volume and ask the height
			Console.WriteLine("Let's find the volume if the rectangle if it has it. Please enter the hight and press enter.");

			//Create a variable to hold hight
			string heightString = Console.ReadLine();

			//DEclare the height
			double height;


			//Validat the user variable
			//Validate using a while loop
			while (!double.TryParse (heightString, out height)) {

				//alert the user of a error
				Console.WriteLine ("Please only type numbers and do not leave blank.");
				//Recapture the users response, in the same variable
				heightString = Console.ReadLine ();


			}
			//Report to the user
			Console.WriteLine("You typed in the height of {0}.\r\nWe will now find the volume.", height);


			//Create another function
			//Function call and save the returned value
			double volume = calcVolume(width,length,height);

			//Final output to user
			Console.WriteLine("The volume is {0}.",volume);

			/*
			 width = 4
			 length = 5
			 height = 6

			computer calculated
			perimeter = 18
			area = 20
			volume = 120

             all values were checked with calculator

			*/

		}

		public static double calcPeri (double wid,double len){

			//Create a variable for perimeter and do the math
			double peri = 2*wid + 2*len;

			//Return the perimeter value
			return peri;

		}

		public static double calcArea (double w, double l){

			//Do the math and storein variable
			double area = w * l;

			//Return to the maun method
			return area;

		}

		public static double calcVolume (double w,double l, double h){

			//Calculate the volume
			double volume = w*l*h;

			//Return the value to the main
			return volume;
		}
	}
}
