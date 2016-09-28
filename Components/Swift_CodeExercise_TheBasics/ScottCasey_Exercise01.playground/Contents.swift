// Casey Scott
// 09/27/2016
// APL Week 1
// Exercise 01

import UIKit

//Part 1: Variable Declarations

//1
let number = 0
var word = ""
var trueFalse = true
var doublesArray = [Double] ()
let floatArray = [Float]()


//2
var implicitIntArray: [Int]
let consDouble: Double
var int32Var: UInt32
var boolArray: [Bool]=[true,false]
let consString: String


//Part 2: String Concatention
var firstName = "Casey"
var lastName = "Scott"
var fullName = firstName+" "+lastName
var age = 29
let introduction = "My full name is \(fullName), and I am \(age) years old."
print(introduction)

//Part 3: Arrays, Conditionals, and Loops

//1
var arrayOfStrings = [String]()
//2
arrayOfStrings += ["One","Two","Three"]
//3
if arrayOfStrings.count == 3
{
    //1
    for string in arrayOfStrings{
        print(string)}
}else
    //2
{print("I must have added the wrong number of elements to the array.")}


//Part 4: Random Values
//1
let randomNumber = Int(arc4random_uniform(11))
 print(randomNumber)
//2
let randomNumberHigh = UInt32(arc4random_uniform(UINT32_MAX)+1)
print(randomNumberHigh)
//3
let randomNumberFiveAndTwentyFive = Int(arc4random_uniform(21)+5)
print(randomNumberFiveAndTwentyFive)
//4
func roundToDesPlace() ->Double
{
    let randomDouble = (Double(arc4random_uniform(UINT32_MAX))/Double(UINT32_MAX))
    let y = Double(round(randomDouble*10)/10)
    return y
}
print(roundToDesPlace())

//Part 5: Documentation Scavenger Hunt - Arrays

//1

//Found all of these Instance Methods in the apple developer documentation
//URL: https://developer.apple.com/reference/swift/array

var instanceMethods = ["append()","poplast()","reserveCapacity()","withUnsafeBufferPointer()","withUnsafeMutableBufferPointer()","distance()","dropFirst()","dropLast()","elementsEqual()","enumerated()","filter()","first()","flatMap()","forEach()","formIndex()","index()","insert()","joined()","lexicographicalPrecedes()","makeIterator()","map()","max()","min()","prefix()","remove()","removeAll()","replaceSubrange()","reversed()","sort()","sorted()","split()","starts()","sufix()","Contains()","elementsEqual()","partition()","reduce()"]

//2
//Found all of these properties of arrays in the apple developer documentation
//URL: https://developer.apple.com/reference/swift/array
let propertiesOfArrays = ["capacity","count","debugDescription","description","endIndex","startIndex","customMirror","first","indices","isEmpty","last","lazy","underestimatedCount"]

//3

/* 1.What collection types other than Arrays are available in the Swift Standard Library currently?
 
 -Arrays
 -Dictionary
 -Set
 -Stack - Generic
 
2.What is the size of an Int object in bits?
 
 -8 bit
 -16 bit
 -32 bit
 -64 bit
 -Come in signed and unsigned form
 
3.What is a Unary Operator?
 
-Operators that operate on a single target.
 
 
4.When talking about Integers, the documentation mentions that they can be signed and unsigned. What values can a signed Int hold?
 
 -Int can store any value between -2,147,483,648 and 2,147,483,647.
 

 
 */