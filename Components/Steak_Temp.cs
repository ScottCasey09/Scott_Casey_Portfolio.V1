using System;

namespace SteakTempature

{
	class MainClass
	{
		public static void Main (string[] args)
		{
			/*
			Casey Scott
			11-4-15
			steak tempature
			section 01
			*/

			//Determine what cook temp our steak is for certine tempature

			/*
			 Rare = 130-140
			 Medium rare + 140-145
			 Medium = 145-160
			 Well Done = 160-170
			*/

			//Ask user for their temp of the steak
			Console.WriteLine("We are goint to determine the steak doneness lever\r\nWhat is the tempature of your steak in degrees f.\r\nPease press return when done.");

			//Capature the users response and store it
			string steakTempString = Console.ReadLine();

			//Convert string to number
			int steakTemp = int.Parse(steakTempString);

			//Start our conditional

			if (steakTemp <= 130) {
				Console.WriteLine ("This is uncooked meat.");
				
			} else if (steakTemp <= 140) {
				Console.WriteLine ("The meat is rare.");
			} else if (steakTemp <= 145) {
				Console.WriteLine ("The steak is medium rare.");
				
			} else if (steakTemp <= 160) {
				Console.WriteLine ("The steak is medium.");
				
			} else if (steakTemp <= 170) {
				Console.WriteLine ("The steak is well done.");
				
			} else {
				Console.WriteLine ("The steak is burnt. ");
			 
			}






		}
	}
}
