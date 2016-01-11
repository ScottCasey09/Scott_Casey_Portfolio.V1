using System;

namespace ZombieLoopingMadness
{
	class MainClass
	{
		public static void Main (string[] args)
		{
			//Zombie Madness

			//Givens first
			//Start with 1 zombie
			//It can bite 4 people in a day
			//8 days.

			//How many zombies do we have?
			int numZombies = 1;

			//Number of bites per zombie per day
			int numBites = 4;

			//How many days?
			int days = 8;

			//Create our for to cycle through each day
			for(int i =1; i<=days; i++){

				//What happens in a day?
				//How many new zombies get created?
				int newZombies = numZombies * numBites;

				//end of the day these new zombies join our zombie horde
				numZombies += newZombies;

				//Tell the public how many zombies we have each day
				Console.WriteLine("There are {0} zombieson day #{1}",numZombies,i);


				
			}

			//How lon it will take to reach a million zombies

			int numDays = 1;

			int ZombieHoardNumber = 1;

			while(ZombieHoardNumber<=1000000){

				//Happens each day
				int bittenPeople = ZombieHoardNumber * numBites;

				//end of the day those become zombies

				ZombieHoardNumber += bittenPeople;

				//Report to the people how many zombies there are
				Console.WriteLine("On day #{0}, there are {1} zombies!", numDays, ZombieHoardNumber);

				//End of the day, we increase the day #
				numDays++;
				
			}
		}
	}
}
