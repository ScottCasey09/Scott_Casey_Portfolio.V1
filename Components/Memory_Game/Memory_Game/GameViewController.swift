//
//  GameViewController.swift
//  Memory_Game
//
//  Created by Casey Scott on 1/7/17.
//  Copyright © 2017 Casey Scott. All rights reserved.
//

import UIKit
import CoreData

class GameViewController: UIViewController, UIPickerViewDelegate, UIPickerViewDataSource {
    
    //MARK: - properties of the view controller
    
    var images: [UIImage] = []
    var imagesCopy: [UIImage] = []
    var selectedImages: [UIImage] = []
    var cards: [UIImageView] = []
    var cardsCopy: [UIImageView] = []
    var selections: [UIButton] = []
    var selectors: [UIButton] = []
    var first: UIImageView? = nil
    var second: UIImageView? = nil
    var score: Int = 0
    var imageIndexes: [Int] = []
    var device: String = "IPad"
    var picksMade: Int = 0
    var pickComps: [String] = []
    var initials = ["A","A","A"]
    
    //MARK: - UI Outlets
    
    @IBOutlet weak var winnerMessageView: UIImageView!
    @IBOutlet weak var startGameButton: UIButton!
    @IBOutlet weak var exitButton: UIButton!
    @IBOutlet weak var newGameButton: UIButton!
    @IBOutlet weak var position0_0: UIImageView!
    @IBOutlet weak var position0_1: UIImageView!
    @IBOutlet weak var position0_2: UIImageView!
    @IBOutlet weak var position0_3: UIImageView!
    @IBOutlet weak var position0_4: UIImageView!
    @IBOutlet weak var position1_0: UIImageView!
    @IBOutlet weak var position1_1: UIImageView!
    @IBOutlet weak var position1_2: UIImageView!
    @IBOutlet weak var position1_3: UIImageView!
    @IBOutlet weak var position1_4: UIImageView!
    @IBOutlet weak var position2_0: UIImageView!
    @IBOutlet weak var position2_1: UIImageView!
    @IBOutlet weak var position2_2: UIImageView!
    @IBOutlet weak var position2_3: UIImageView!
    @IBOutlet weak var position2_4: UIImageView!
    @IBOutlet weak var position3_0: UIImageView!
    @IBOutlet weak var position3_1: UIImageView!
    @IBOutlet weak var position3_2: UIImageView!
    @IBOutlet weak var position3_4: UIImageView!
    @IBOutlet weak var position3_3: UIImageView!
    @IBOutlet weak var position4_0: UIImageView!
    @IBOutlet weak var position4_1: UIImageView!
    @IBOutlet weak var position4_2: UIImageView!
    @IBOutlet weak var position4_3: UIImageView!
    @IBOutlet weak var position4_4: UIImageView!
    @IBOutlet weak var position5_0: UIImageView!
    @IBOutlet weak var position5_1: UIImageView!
    @IBOutlet weak var position5_2: UIImageView!
    @IBOutlet weak var position5_3: UIImageView!
    @IBOutlet weak var position5_4: UIImageView!
    @IBOutlet weak var position0_0Selector: UIButton!
    @IBOutlet weak var position0_1Selector: UIButton!
    @IBOutlet weak var position0_2Selector: UIButton!
    @IBOutlet weak var position0_3Selector: UIButton!
    @IBOutlet weak var position0_4Selector: UIButton!
    @IBOutlet weak var position1_0Selector: UIButton!
    @IBOutlet weak var position1_1Selector: UIButton!
    @IBOutlet weak var position1_2Selector: UIButton!
    @IBOutlet weak var position1_3Selector: UIButton!
    @IBOutlet weak var position1_4Selector: UIButton!
    @IBOutlet weak var position2_0Selector: UIButton!
    @IBOutlet weak var position2_1Selector: UIButton!
    @IBOutlet weak var position2_2Selector: UIButton!
    @IBOutlet weak var position2_3Selector: UIButton!
    @IBOutlet weak var position2_4Selector: UIButton!
    @IBOutlet weak var position3_0Selector: UIButton!
    @IBOutlet weak var position3_1Selector: UIButton!
    @IBOutlet weak var position3_2Selector: UIButton!
    @IBOutlet weak var position3_3Selector: UIButton!
    @IBOutlet weak var position3_4Selector: UIButton!
    @IBOutlet weak var position4_0Selector: UIButton!
    @IBOutlet weak var position4_1Selector: UIButton!
    @IBOutlet weak var position4_2Selector: UIButton!
    @IBOutlet weak var position4_3Selector: UIButton!
    @IBOutlet weak var position4_4Selector: UIButton!
    @IBOutlet weak var position5_0Selector: UIButton!
    @IBOutlet weak var position5_1Selector: UIButton!
    @IBOutlet weak var position5_2Selector: UIButton!
    @IBOutlet weak var position5_3Selector: UIButton!
    @IBOutlet weak var position5_4Selector: UIButton!
    @IBOutlet weak var timer: UILabel!
    @IBOutlet weak var attempts: UILabel!
    @IBOutlet weak var leftSpaceView: UIView!
    @IBOutlet weak var rightSpaceView: UIView!
    @IBOutlet weak var leftSpaceConstraint: NSLayoutConstraint!
    @IBOutlet weak var rightSpaceConstraint: NSLayoutConstraint!
    @IBOutlet weak var initialsDoneButton: UIButton!
    @IBOutlet weak var initialsPicker: UIPickerView!
    @IBOutlet weak var initalsLabel: UILabel!
    @IBOutlet weak var tintView: UIView!
    @IBOutlet weak var initialsView: UIView!
    
    //Create timer variables
    var gameTimer = Timer()
    var timeCounterSec = 0
    var timeCounterMin = 0
    
    //Create a managed object context
    var managedContext: NSManagedObjectContext!
    
    //Create an entity description to help build the entity
    private var entityDescription: NSEntityDescription!
    
   //Create the managed object for our data to live in
    private var scores: NSManagedObject!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        score = 0
        
        /* Set up Core Data Objects */
        
        /* Make sure our stored property is the same as the App's managedObjectContext */
        managedContext = (UIApplication.shared.delegate as! AppDelegate).CDStack.context
        
        /* Fill out our entity Description */
        entityDescription = NSEntityDescription.entity(forEntityName: "Scores", in: managedContext)
        
        /* Use the description to make numTaps a "NumTaps" entity. And write it on our notepad. */
        scores = NSManagedObject(entity: entityDescription, insertInto: managedContext)
        
        //Check to see which device is running
        //if the device is an iPad
        if UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiom.pad{
            device = "ipad"
            //Card set for ipad
            cards += [position0_0,position0_1, position0_2, position0_3, position0_4, position1_0, position1_1, position1_2, position1_3, position1_4, position2_0, position2_1, position2_2, position2_3, position2_4, position3_0, position3_1, position3_2 , position3_3, position3_4, position4_0 ,position4_1 , position4_2, position4_3, position4_4, position5_0, position5_1, position5_2, position5_3, position5_4]
            //Make array of selectors
            selectors += [position0_0Selector, position0_1Selector, position0_2Selector, position0_3Selector, position0_4Selector, position1_0Selector, position1_1Selector, position1_2Selector, position1_3Selector, position1_4Selector, position2_0Selector, position2_1Selector, position2_2Selector, position2_3Selector, position2_4Selector, position3_0Selector, position3_1Selector, position3_2Selector, position3_3Selector, position3_4Selector, position4_0Selector, position4_1Selector, position4_2Selector, position4_3Selector, position4_4Selector, position5_0Selector ,position5_1Selector, position5_2Selector, position5_3Selector, position5_4Selector]
            
            
        }//If the device is an iphone
        else if UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiom.phone{
            device = "iphone"
            //Card set for iphone
            cards += [position0_0,position0_1, position0_2, position0_3, position1_0, position1_1, position1_2, position1_3, position2_0, position2_1, position2_2, position2_3, position3_0, position3_1, position3_2 , position3_3, position4_0 ,position4_1 , position4_2, position4_3]
            //Make array of selectors
            selectors += [position0_0Selector, position0_1Selector, position0_2Selector, position0_3Selector, position1_0Selector, position1_1Selector, position1_2Selector, position1_3Selector, position2_0Selector, position2_1Selector, position2_2Selector, position2_3Selector, position3_0Selector, position3_1Selector, position3_2Selector, position3_3Selector, position4_0Selector, position4_1Selector, position4_2Selector, position4_3Selector]
        }
        
        //Make Array of images
        images += [#imageLiteral(resourceName: "image0"), #imageLiteral(resourceName: "image1"), #imageLiteral(resourceName: "image2"), #imageLiteral(resourceName: "image3"), #imageLiteral(resourceName: "image4"), #imageLiteral(resourceName: "image5"), #imageLiteral(resourceName: "image6"), #imageLiteral(resourceName: "image7"), #imageLiteral(resourceName: "image8"), #imageLiteral(resourceName: "image9"), #imageLiteral(resourceName: "image10"), #imageLiteral(resourceName: "image11"), #imageLiteral(resourceName: "image12"), #imageLiteral(resourceName: "image13"), #imageLiteral(resourceName: "image14"), #imageLiteral(resourceName: "image15"), #imageLiteral(resourceName: "image16"), #imageLiteral(resourceName: "image17"), #imageLiteral(resourceName: "image18"), #imageLiteral(resourceName: "image19"), #imageLiteral(resourceName: "image20"), #imageLiteral(resourceName: "image21"), #imageLiteral(resourceName: "image22"), #imageLiteral(resourceName: "image23"), #imageLiteral(resourceName: "image24"), #imageLiteral(resourceName: "image25"), #imageLiteral(resourceName: "image26"), #imageLiteral(resourceName: "image27"), #imageLiteral(resourceName: "image28"), #imageLiteral(resourceName: "image29"), #imageLiteral(resourceName: "image30"), #imageLiteral(resourceName: "image31"), #imageLiteral(resourceName: "image32"), #imageLiteral(resourceName: "image33"), #imageLiteral(resourceName: "image34"), #imageLiteral(resourceName: "image35"), #imageLiteral(resourceName: "image36"), #imageLiteral(resourceName: "image37"), #imageLiteral(resourceName: "image38"), #imageLiteral(resourceName: "image39"), #imageLiteral(resourceName: "image40"), #imageLiteral(resourceName: "image41"), #imageLiteral(resourceName: "image42"), #imageLiteral(resourceName: "image43"), #imageLiteral(resourceName: "image44"), #imageLiteral(resourceName: "image45"), #imageLiteral(resourceName: "image46")]
        
        //Make a copy of the cards array
        cardsCopy = cards
        
        //Disable all selectors
        for selector in selectors{
            selector.isEnabled = false
            //Make the selectors single touch only
            selector.isMultipleTouchEnabled = false
            selector.isExclusiveTouch = true
        }
        
        //Populate the picker view componets
        pickComps = ["A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z",".","-"," ","'"]
        
        initialsPicker.delegate = self
        initialsPicker.dataSource = self
        
    }
    
    
    //MARK: - Interface Actions
    
    //Action for the selecting of the cards
    @IBAction func selection(_ sender: UIButton){

        //Add the selection to a counter
        selections += [sender]
        
        //Make sure only 2 have been selected and revealed
        if selections.count <= 2{
      
            //Refresh the layout
            view.layoutIfNeeded()
            //Animate the turning of the card(Could use work)
            UIView.animate(withDuration: 0.8) {
                //Remove the color of the view
                sender.alpha = 0
                //Refresh the layout
                self.view.layoutIfNeeded()
            }
            
            //Assign the tag value to the selections array
            
            //Check for two selections
            if selections.count == 2{
                //Unwrap the optional for the attempts count "String"
                if let value = attempts.text{
                //Add one to the count of matches
                attempts.text = String(Int(value)!+1)

                }
                
                //Iterate over the cards to collect the tag properties of the buttons for both first and second selection
                for x in cardsCopy{
                    if x.tag == selections[0].tag{
                        first = x
                    }
                }
                for y in cardsCopy{
                    if y.tag == selections[1].tag{
                        second = y
                    }
                }
                //Disable the selection actions
                for select in selectors{
                    select.isEnabled = false
                }
                //Check to see if there is a match
                if first?.image == second?.image{
                    score += 1
                    //Turn the card green
                    first?.backgroundColor = #colorLiteral(red: 0.721568644, green: 0.8862745166, blue: 0.5921568871, alpha: 1)
                    second?.backgroundColor = #colorLiteral(red: 0.721568644, green: 0.8862745166, blue: 0.5921568871, alpha: 1)
                    
                    //Remove the cards from the game
                    //Delay the removal process so the user can see the match
                    let delay = DispatchTime.now() + 0.75
                    DispatchQueue.main.asyncAfter(deadline: delay) {
                        for card in self.cardsCopy{
                            
                            //Remove the cards
                            if self.first?.tag == card.tag{
                                card.isHidden = true
                            }
                            if self.second?.tag == card.tag{
                                card.isHidden = true
                            }
                        }
                        //Enable all of the selectors
                        for select in self.selectors{
                            select.isEnabled = true
                        }
                    }
                    
                        //Check for the WIN!!
                      
                        //Check to see if all of the matches have been made
                        if score == cardsCopy.count / 2{
                            
                            //Disable the exit and new game buttons
                            exitButton.isEnabled = false
                            newGameButton.isEnabled = false
                            
                            //Create a delay for the winner message
                            let delay = DispatchTime.now() + 2.25
                            DispatchQueue.main.asyncAfter(deadline: delay, execute: {
                                
                                //Display the  initials entry view
                                self.tintView.isHidden = false
                                
                                //Animate the winner message droping in
                                //Set the y property to be off of the screen
                                self.winnerMessageView.center.y -= 300
                                //Drop the message in
                                UIImageView.animate(withDuration: 2, animations: {self.winnerMessageView.center = CGPoint.init(x: self.winnerMessageView.center.x, y: self.winnerMessageView.center.y + 300)}, completion: nil)
                                
                                //Display the winner message
                                self.winnerMessageView.isHidden = false

                                self.initialsView.center.y += 300
                                UIImageView.animate(withDuration: 2, animations: {self.initialsView.center = CGPoint.init(x: self.initialsView.center.x, y: self.initialsView.center.y - 300)}, completion: nil)
                                self.initialsView.isHidden = false
                                
                            })

                            //Stop the timer
                            gameTimer.invalidate()
                            //Reset the cards for new game
                            cards = cardsCopy
                    }
                    
                    //Clear the selections array for next turn
                    selections.removeAll()
                    
                }
                else{
                    //Turn the cards red
                    first?.backgroundColor = #colorLiteral(red: 0.8078431487, green: 0.02745098062, blue: 0.3333333433, alpha: 1)
                    second?.backgroundColor = #colorLiteral(red: 0.8078431487, green: 0.02745098062, blue: 0.3333333433, alpha: 1)
                    //Delay the game for 3 seconds for when the wrong guesses are made
                    let delay = DispatchTime.now() + 3
                    DispatchQueue.main.asyncAfter(deadline: delay) {
                        //re-establish the selectors for re-selecting
                        for selection in self.selections{
                            //refresh the layout just to make sure
                            self.view.layoutIfNeeded()
                            //Animation
                            UIView.animate(withDuration: 0.1) {
                                //Reinstate the background color
                                selection.alpha = 1
                                //flip over the card
                                selection.isHidden = false
                                //Referesh the layout
                                self.view.layoutIfNeeded()
                                
                            }
                        }
                        //reset the background color to white
                        self.first?.backgroundColor = #colorLiteral(red: 1.0, green: 1.0, blue: 1.0, alpha: 1.0)
                        self.second?.backgroundColor = #colorLiteral(red: 1.0, green: 1.0, blue: 1.0, alpha: 1.0)
                        //Clear the selections array for next attempt
                        self.selections.removeAll()
                        //re-Enable all of the selectors
                        for select in self.selectors{
                            select.isEnabled = true
                        }
                    }
                }
            }
        }
    }
    
    //Go back to root
    @IBAction func backButtonPress(_ sender: UIButton) {
        //Dismis th current view controller
        dismiss(animated: true, completion: nil)
    }
    //Action for when the start button is pressed
    @IBAction func startButtonPush(_ sender: UIButton) {
        
        //Set picksMade to 0
        picksMade = 0
        
        //Unhide all of the images
        for image in cardsCopy{
            image.isHidden = false
        }
        
        //Unhide the new game button
        newGameButton.isHidden = false
        //Hide the start button
        startGameButton.isHidden = true
        //Enable all of the slectors
        for selector in selectors{
            selector.isEnabled = true
            selector.isHidden = true
            selector.alpha = 1
        }
        //Unhide all of the selectors
        
        //Layout the cards
        layoutCards()
        
        //Delay the game 5 seconds
        for selector in selectors{
            //Delay for the start of the game for preview
            let delay = DispatchTime.now() + 5
            DispatchQueue.main.asyncAfter(deadline: delay) {
                selector.isHidden = false
            }
        }
        //set up a timer that increeses in intervals of 1 second and repeast the action until invalidated
        //Delay the timer for 5 seconds
        let delay = DispatchTime.now() + 5
        DispatchQueue.main.asyncAfter(deadline: delay) {
            self.gameTimer = Timer.scheduledTimer(timeInterval: 1, target:self, selector: #selector(self.updateCounter), userInfo: nil, repeats: true)
        }
    }
    //Action thats fired when the new game button is selected
    @IBAction func newGameSelect(_ sender: UIButton) {
        //Stop the timer if it is running
        gameTimer.invalidate()
        //Hide the minner message
        winnerMessageView.isHidden = true
        //Update match count and timer to 0
        attempts.text = "0"
        timer.text = "00:00"
        //Reveal the start button
        startGameButton.isHidden = false
        
        //Reset Time Variables
        timeCounterSec = 0
        timeCounterMin = 0
        
        //Hide the new game button
        newGameButton.isHidden = true
    }
    
    //method called when the device rotates
    override func viewWillTransition(to size: CGSize, with coordinator: UIViewControllerTransitionCoordinator) {
        if device.lowercased() == "ipad"{
            //Check orientations
            //Landscape
            if UIDevice.current.orientation.isLandscape{
                //change the multiplier with animations
                changeConstraints(mulitplier: 0.125)
                
            }
            //Portrait
            if UIDevice.current.orientation.isPortrait{
                //change the multiplier with animations
                changeConstraints(mulitplier: 0.045)
            }
        }
    }
    @IBAction func doneWithInitialsButtonPressed(_ sender: Any) {
        
        //Update the notePad with the values from the current round
        scores.setValue(initalsLabel.text, forKey: "name")
        
        scores.setValue(attempts.text, forKey: "attempts")
        
        scores.setValue(timer.text, forKey: "time")
        
        //Build the time stamp
        let date = NSDate()
        let calendar = NSCalendar.current
        let day = calendar.component(.day, from: date as Date)
        let month = calendar.component(.month, from: date as Date)
        let year = calendar.component(.year, from: date as Date)
        let dateString = "\(String(day)) / \(String(month)) / \(String(year))"

        scores.setValue(dateString, forKey: "date")
        
        //Save the data to the Coredata model
        (UIApplication.shared.delegate as! AppDelegate).CDStack.saveContext()
        
        transitionStatus = "segeueFromGame"
        
        //Go to the view of the scores
        performSegue(withIdentifier: "GoToLeaderBoard", sender: self)
        
    }
    
    //MARK: - Custom Functions
    
    //Change Constraints for orientations on IPad
    func changeConstraints(mulitplier: CGFloat){
        
        //Tell the UI to redraw
        self.view.layoutIfNeeded()
        //Use an animation
        UIView.animate(withDuration: 1, animations: {
            
            //Change the multiplier to the constraints
            self.leftSpaceConstraint = self.leftSpaceConstraint.setMultiplier(multiplier: mulitplier)
            self.rightSpaceConstraint = self.rightSpaceConstraint.setMultiplier(multiplier: mulitplier)
            //Tell the UI to redraw
            self.view.layoutIfNeeded()
        })
    }
    
    // function that updates the timer to the format of "00:00"
    func updateCounter() {
        //Dertimine the interval length
        timeCounterSec += 1
        //Make a string for the minutes
        var minutes = String(timeCounterMin)
        //if the counter hits 60 seconds the add a minute and reset the seconds
        if timeCounterSec == 60{
            timeCounterMin += 1
            timeCounterSec = 0
        }
        //Check to see if there is even a minute, if not minutes is a double zero
        if timeCounterMin < 1{
            minutes = "00"
        }
        //Set the format
        timer.text = minutes + ":" + String(timeCounterSec)
        //Check to see if there are less then 10 seconds, if so add a zero to the front of the seconds
        if timeCounterSec < 10{
            //Foramt the seconds
            timer.text = minutes + ":0" + String(timeCounterSec)
        }
    }
    
    //Function for laying out the cards
    func layoutCards(){
        
        //Make copy of cards for replay
        cards = cardsCopy
        
        //Make array of indexes for selecting images at random
        imagesCopy = images
        
        //Assign background color to each card
        for card in cards{
            card.backgroundColor = #colorLiteral(red: 1, green: 1, blue: 1, alpha: 1)
        }
        
        //Choose the randomly selected images from image set
        for _ in 0...(cards.count-1)/2{
            let i = arc4random_uniform(UInt32(imagesCopy.count))
            print(String(i))
            self.selectedImages.append(imagesCopy[Int(i)])
            imagesCopy.remove(at: Int(i))
            
        }
        
        //Counter for the cards
        var cardCount = cards.count-1
        
        //Assign each selected image to a card placement
        for index in 0...selectedImages.count-1{
            
            var i = arc4random_uniform(UInt32(cardCount))
            
            cards[Int(i)].image = selectedImages[index]
            
            cards.remove(at: Int(i))
            
            cardCount = cards.count-1
            
            i = arc4random_uniform(UInt32(cardCount))
            
            cards[Int(i)].image = selectedImages[index]
            
            cards.remove(at: Int(i))
            
            cardCount = cards.count-1
            
        }
        //Clear the selected images for next game
        selectedImages.removeAll()
    }
    
    
    //MARK: - UIPickerViewDataSource
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return pickComps.count
    }
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 3
    }
    
    //MARK: - UIPickerViewDelegate
    
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        return pickComps[row]
    }
    func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        
        //Assign the label a new value based on what row is selected in each component of the picker
        switch component {
        case 0:
            initials[0] = pickComps[row]
        case 1:
            initials[1] = pickComps[row]
        case 2:
            initials[2] = pickComps[row]
        default:
            break;
        }
        
        initalsLabel.text = "\(initials[0])\(initials[1])\(initials[2])"
    }
    
    //MARK: - Navigation
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        
        
    }
}
