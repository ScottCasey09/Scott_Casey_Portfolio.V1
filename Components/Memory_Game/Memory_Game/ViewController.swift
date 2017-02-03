//
//  ViewController.swift
//  Memory_Game
//
//  Created by Casey Scott on 1/6/17.
//  Copyright © 2017 Casey Scott. All rights reserved.
//

import UIKit
import CoreData

var transitionStatus = ""


class ViewController: UIViewController {
    
    /* ManagedObjectsContext - Our Notepad. We write on our note pad then save the notepad to the device.
     It's our Data Middle Man between our code and the Hard Drtive.*/
    
    var managedContext: NSManagedObjectContext!
    
    /* NSEntityDescription - USED to help build our entity by describing a specific Entity from our .xcdatamodel file.*/
    private var entityDescription: NSEntityDescription!
    
    /* NSManagedObject - Used to represent the entity type 'NumTaps' that we created in our xcdatamodel file.
     We use the entity description to help us build the right kind on entity.*/
    
    /* This is where our data lives, everything else is just setup. */
    private var scores: NSManagedObject!

    @IBAction func exitButtonPush(_ sender: Any) {
        exit(0)
    }
   
    @IBAction func leadeBoardsButtonPress(_ sender: Any) {
        
        transitionStatus = "segeueFromMain"
        performSegue(withIdentifier: "GoToLeaderBoardFromMain", sender: self)
        
    }
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.restorationIdentifier = "main"
            }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
   

}

