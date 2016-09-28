//Casey Scott
//Exercise 02
//09/27/2016
//APL_Week 1

import UIKit

//Part 1: Simple Functions
//1
func aString()
{
    print("A hard coded string.")
}

aString()

//2
var str = "A DIFFERENT STRING"

func newlyFormattedString(stringA: String) -> String
{
   let new = ("The string passed in to this function was \(stringA)")
    return new
}

print(newlyFormattedString(stringA: str))

//Part 2: Random Bool Array Function

//1

func functionWithNoPramsAndNoReturn()
{
    var numOfTrue = 0
    var numOfFalse = 0
    var boolArray = [Bool]()
    while boolArray.count != 10
        {
            let x = arc4random_uniform(10)
            
            if(x==0||x==2||x==4||x==8||x==6){
                boolArray.append(false)
                
            }else if(x==1||x==3||x==5||x==7||x==9){
                boolArray.append(true)
                
            }
    
    }
    var x = 0
    for _ in boolArray
    {
        if (boolArray[x] == true) {
            numOfTrue += 1
            x += 1
        }else if boolArray[x]==false{
            numOfFalse += 1
            x += 1
        }
    }
   
        print(boolArray)
    print("True values = \(numOfTrue)\nFalse values = \(numOfFalse)")
    

}

functionWithNoPramsAndNoReturn()


//Part 3: Nested Arrays Function

//1
let array1 = [0.1,0.2,0.3,0.4,0.5]
let array2 = [1.1,1.2,1.3,1.4,1.5]
let array3 = [2.1,2.2,2.3,2.4,2.5]
let array4 = [3.1,3.2,3.3,3.4,3.5]
let array5 = [4.1,4.2,4.3,4.4,4.5]
var arrayOfArrays = [array1,array2,array3,array4, array5]

//2
func inArrayOfArraysOutString(array: [[Double]]) ->String{
    var str = "String of an array of doubles = "
    var i=0
    for _ in 1...array.count{
        
    
    for arrays in array[i]{
        
        
        str.append(String(arrays))
        str.append(" ")
    }
        i+=1
    }
    
    return str
}

print(inArrayOfArraysOutString(array: arrayOfArrays))