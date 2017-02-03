//
//  multiplyer.swift
//  Memory_Game
//
//  Created by Casey Scott on 1/13/17.
//  Copyright © 2017 Casey Scott. All rights reserved.
//

import Foundation
import  UIKit

//This extention changes the multiplier of a constraint
extension NSLayoutConstraint {
   
    //Take in a CGFloat and return a NSLayoutConstraint
    func setMultiplier(multiplier:CGFloat) -> NSLayoutConstraint {
        
        //Deactivate the current constraint
        NSLayoutConstraint.deactivate([self])
        
        //Create a new constraint
        let newConstraint = NSLayoutConstraint(
            item: firstItem,
            attribute: firstAttribute,
            relatedBy: relation,
            toItem: secondItem,
            attribute: secondAttribute,
            multiplier: multiplier,
            constant: constant)
        
        newConstraint.priority = priority
        newConstraint.shouldBeArchived = self.shouldBeArchived
        newConstraint.identifier = self.identifier
        
        //Activate the new constraint
        NSLayoutConstraint.activate([newConstraint])
        //Return the constraint with the new value
        return newConstraint
    }
}
