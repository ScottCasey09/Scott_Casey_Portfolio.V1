//Casey Scott
//Exercise 03
//09/27/2016
//APL_Week1

import UIKit



//Intermediate Functions

//1 2 Parameters, With Return
var passingString = "uPPer AnD LoWeR CaSeD StRinG"
func twoPramFuncWithReturn(booleon: Bool, str: String) -> String{
    
    if booleon == true{
        return str.uppercased()
    }else if booleon == false{
        return str.lowercased()
    }else{
        return "The string \(str) did not change"

    }
}
print(twoPramFuncWithReturn(booleon: true, str: passingString))
print(twoPramFuncWithReturn(booleon: false, str: passingString))


//2 No Parameter, With Return
func randomIntsWithReturn() -> [Int]{
    var array = [Int]()
    
    for _ in 1...10{
    let randomNumber = arc4random_uniform(100)+1
        array.append(Int(randomNumber))
    }
    return array
}
print(randomIntsWithReturn())


//3 Function with Tuple Return
let array = randomIntsWithReturn()

func tupleReturn(arrayOfInts: [Int]) -> (min: Int, median: Int, max: Int){
    
    
    var arrayOfInt = arrayOfInts.sorted()
    var x = 0
    var theMedian = arrayOfInt[0]
    let theMin = arrayOfInt[0]
    if arrayOfInts.count % 2 == 0{
        
        x = arrayOfInts.count / 2
        
        theMedian = (arrayOfInt[x] + arrayOfInt[x-1])/2

    }else{
         theMedian = (arrayOfInt[arrayOfInts.count/2])

    }
    let theMax = arrayOfInt[arrayOfInts.count-1]
    
 
    
    return (theMin, theMedian,theMax)
}
print(array.sorted())//To varify

print("The largest value found was \(tupleReturn(arrayOfInts: array).max)\nThe smallest value found was \(tupleReturn(arrayOfInts: array).min)\nAnd the meadian value found was \(tupleReturn(arrayOfInts: array).median)")


//4 Function Containing Switch Statement and Range Operator

func switchWithRangeOperator(arrayOfInts: [Int])
{
    var i = 0
   var case1 = 0
    var case2 = 0
    var case3 = 0
    for number in arrayOfInts{
        let x = number
    switch i<arrayOfInts.count {
    case (x < 34):
        case1 += 1
        i += 1
    case (x > 33 && x < 67):
        case2 += 1
        i += 1
    case (x > 66):
        case3 += 1
        i += 1
    default:
        print("Uh-Oh something went wrong!!")
    }
    }
    print(arrayOfInts.sorted())//To verify
    
    print("Case one was hit \(case1) times.\nCase two was hit \(case2) times.\nCase three was hit \(case3) times")
    
}

switchWithRangeOperator(arrayOfInts: randomIntsWithReturn())

