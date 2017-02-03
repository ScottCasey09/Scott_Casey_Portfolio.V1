//
//  GameViewController.swift
//  Rock_Paper_Sissiors
//
//  Created by Casey Scott on 1/16/17.
//  Copyright © 2017 Casey Scott. All rights reserved.
//

import UIKit
import MultipeerConnectivity

class GameViewController: UIViewController, MCSessionDelegate {
    
    //MARK: - UIOutlets
    
    @IBOutlet weak var player1_H1: UIImageView!
    @IBOutlet weak var player1_H2: UIImageView!
    @IBOutlet weak var player1_H3: UIImageView!
    @IBOutlet weak var player1_H4: UIImageView!
    @IBOutlet weak var player1_H5: UIImageView!
    @IBOutlet weak var player1_H6: UIImageView!
    @IBOutlet weak var player1_H7: UIImageView!
    @IBOutlet weak var player2_H1: UIImageView!
    @IBOutlet weak var player2_H2: UIImageView!
    @IBOutlet weak var player2_H3: UIImageView!
    @IBOutlet weak var player2_H4: UIImageView!
    @IBOutlet weak var player2_H5: UIImageView!
    @IBOutlet weak var player2_H6: UIImageView!
    @IBOutlet weak var player2_H7: UIImageView!
    @IBOutlet weak var player1_Name: UILabel!
    @IBOutlet weak var player2_Name: UILabel!
    @IBOutlet weak var player1_Selection: UIImageView!
    @IBOutlet weak var player2_Selection: UIImageView!
    @IBOutlet weak var readyButton: UIButton!
    @IBOutlet weak var sissorsCheck: UIImageView!
    @IBOutlet weak var paperCheck: UIImageView!
    @IBOutlet weak var rockCheck: UIImageView!
    @IBOutlet weak var rockManButton: UIButton!
    @IBOutlet weak var papermanButton: UIButton!
    @IBOutlet weak var sissorsManButton: UIButton!
    @IBOutlet weak var mainCountDown: UILabel!
    @IBOutlet weak var addCountDown_IPAD: UILabel!
    @IBOutlet weak var player1_View: UIView!
    @IBOutlet weak var player1_Health_View: UIView!
    @IBOutlet weak var player2_View: UIView!
    @IBOutlet weak var player2_Health_View: UIView!
    @IBOutlet weak var widthConstraintForP1View: NSLayoutConstraint!
    @IBOutlet weak var widthConstraintForP2View: NSLayoutConstraint!
    @IBOutlet weak var playAgainButton: UIButton!
    
    //MARK: - Properties
    
    var peerID: MCPeerID! //Our device's ID or name as viewed as by other "boowsing" devices
    var session: MCSession! //The connection between devices
    var browser: MCBrowserViewController! //Prebuilt ViewController that searches for nearby advertisers.
    var advertiser: MCAdvertiserAssistant! //Helps us easily advertise ourselves to nearby MCbrowsers.
    var serviceID = "mdf2-RPSGame"// channel
    var timer = Timer()//Timer for the countdown
    var selectedImage: UIImage? = nil // The selected image of the user
    var player1_Health: [UIImageView] = [] //Array of the player 1 health vievs
    var player2_Health: [UIImageView] = [] //Array of the player 2 health vievs
    var player1_Health_Copy: [UIImageView] = [] //Copy Array of the player 1 health vievs for replay
    var player2_Health_Copy: [UIImageView] = [] //Copy Array of the player 2 health vievs for replay
    var message = "" //Message that gets passed to the peer for directions.. Commands..
    var counter = 0 // Countdown counter
    var device = "ipad" // device
    
    
    //MARK: - Load
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        session.delegate = self
        player1_Name.text = peerID.displayName
        for peer in session.connectedPeers{
            player2_Name.text = peer.displayName
            print(peer.displayName)
        }
        disableAllButtons()
        
        //Create array of health views for each palyer
        player1_Health = [player1_H1, player1_H2, player1_H3, player1_H4, player1_H5, player1_H6, player1_H7]
        player2_Health = [player2_H1, player2_H2, player2_H3, player2_H4, player2_H5, player2_H6, player2_H7]
        
        //make a copy  of these arrays for replay^^^^^
        player1_Health_Copy = player1_Health
        player2_Health_Copy = player2_Health
        
        //Assign the selectedImage a default value
        selectedImage = #imageLiteral(resourceName: "RockMan")
    }
    
    //MARK: - Actions
    
    //Action for exiting and disconnecting from any peer
    @IBAction func exitButtonPressed(_ sender: Any) {
        
        //Set the message to disconnect
        message = "disconnect"
        
        //Unwrap the optional and encode it
        if let data = message.data(using: .utf8){
            //Send the message to the peer
            try? session.send(data, toPeers: session.connectedPeers, with: .reliable)
            //disconnect form peers
            session.disconnect()
            print("Unwind to root")
            
            //Go back to the main/start screen
            dismiss(animated: true, completion: nil)
        }
    }
    
    //Action for playing the game over again
    @IBAction func playAgain(_ sender: Any) {
        
        //Hide the playagain button
        self.playAgainButton.isHidden = true
        self.playAgainButton.isEnabled = false
        self.readyButton.isHidden = false
        //Re-stock the health arrays to original capacity
        self.player1_Health = self.player1_Health_Copy
        self.player2_Health = self.player2_Health_Copy
        
        //Make all of the health views visable
        for health in self.player1_Health{
            health.isHidden = false
        }
        for health in self.player2_Health{
            health.isHidden = false
        }
        
        //Set the meaasge to pass as play again
        message = "play_again"
        
        //Unwrap the optional and send the message to the peer
        if let data = message.data(using: .utf8){
            try? session.send(data, toPeers: session.connectedPeers, with: .reliable)
        }
    }
    
    //Acting for selecting the character you wish to play
    @IBAction func changeSelectPlay(_ sender: UIButton) {
        //Change the selected Play
        switch sender.tag{
        case 0:
            selectRockMan()
        case 1:
            selectPaperMan()
        case 2:
            selectSissorsMan()
        default:
            break;
        }
        
    }
    
    //action for when the user selects the ready button
    @IBAction func readyButtonPress(_ sender: UIButton) {
        
        //Set the countdwn time to ready
        mainCountDown.text = "Ready"
        
        //Send a message to the peer to see if they are ready
        let readymessage = "P1Ready"
        if let data = readymessage.data(using: .utf8){
            try? self.session.send(data, toPeers: session.connectedPeers, with: .reliable)
            
        }
       
        //Hide the ready button
        readyButton.isHidden = true
        let delay = DispatchTime.now() //NO Delay
        DispatchQueue.main.asyncAfter(deadline: delay) {
            
            
            print(self.message)
            if self.message == "startGame"{
                //Check for the connection
                if self.session.connectedPeers.count > 0{
                    //Acts when ready is pressed
                    
                    //Turn on all buttons
                    self.enableAllButtons()
                    //Set the countdown to ready
                    self.mainCountDown.text = "Ready"
                    //For IPAD, make the text the same as the main countdown/display text
                    self.addCountDown_IPAD.text = self.mainCountDown.text
                    
                    DispatchQueue.main.async() {
                        //Start a timer to increment by one second, calling the updateCounter method every second
                        self.timer = Timer.scheduledTimer(timeInterval: 1, target:self, selector: #selector(self.updateCounter), userInfo: nil, repeats: true)
                        //Change the views back to the original white
                        self.changeplayerViewsToOriginalWhites()
                    }
                    //Set the message to start
                    let message = "Start"
                    //make a string of the message in utf data format
                    if let encodedString = message.data(using: String.Encoding.utf8){
                        //Send the message
                        try? self.session.send(encodedString, toPeers: self.session.connectedPeers, with: .reliable)
                        
                    }
                    
                }else{
                    //Create an alert controller to notify the user that the connection has been lost
                    let alert = UIAlertController(title: "Not Connected", message: "You are not connected to any challengers", preferredStyle: .alert)
                    let ok = UIAlertAction(title: "OK", style: .cancel, handler: { (ok) in
                        if ok.isEnabled{
                            self.session.disconnect()
                            self.dismiss(animated: true, completion: nil)
                        }
                    })
                    //Add the button/action
                    alert.addAction(ok)
                    
                    //Present the controller
                    self.present(alert, animated: true, completion: nil)
                }
            }
        }
        
    }
    
    //MARK: - MCSessionDelegate
    
    func session(_ session: MCSession, didReceive data: Data, fromPeer peerID: MCPeerID) {//Required
        print("Session did recieve DATA from peer")
        syncCommunications(data: data, peerID: peerID)
        
    }
    //Method that gets called first in the sssion process
    func session(_ session: MCSession, peer peerID: MCPeerID, didChange state: MCSessionState) {//Required
        print("Session did change state!!!!!!")
        
        if session.connectedPeers.count == 0{
            //Create an alert controller to notify the user that the connection has been lost
            let alert = UIAlertController(title: "Not Connected", message: "You are not connected to any challengers", preferredStyle: .alert)
            let ok = UIAlertAction(title: "OK", style: .cancel, handler: { (ok) in
                if ok.isEnabled{
                    session.disconnect()
                    self.dismiss(animated: true, completion: nil)
                }
            })
            
            alert.addAction(ok)
            
            //Present the controller
            present(alert, animated: true, completion: nil)
        }
        
    }
    func session(_ session: MCSession, didReceive stream: InputStream, withName streamName: String, fromPeer peerID: MCPeerID) { //Required
        
    }
    func session(_ session: MCSession, didStartReceivingResourceWithName resourceName: String, fromPeer peerID: MCPeerID, with progress: Progress) { //Required
        
    }
    func session(_ session: MCSession, didFinishReceivingResourceWithName resourceName: String, fromPeer peerID: MCPeerID, at localURL: URL, withError error: Error?) { //Required
        
    }
    
    //MARK: - Custom_Functions
    
    //Function that disables all of the character selector buttons
    func disableAllButtons(){
        //set all buttons to be not selectable     OFF
        rockManButton.isEnabled = false
        papermanButton.isEnabled = false
        sissorsManButton.isEnabled = false
    }
    //Function that enables all of the character selector buttons
    func enableAllButtons(){
        //set all buttons to be selectable   ON
        rockManButton.isEnabled = true
        papermanButton.isEnabled = true
        sissorsManButton.isEnabled = true
    }
    //Function for  counting down the counter and passing a message to the peer
    func updateCounter(){
        
        //Check to see if the counter is nil (unwrap the optional)
        if let counter = mainCountDown.text{
            //Use a swithc statement to check for current status of the countdoun
            switch counter {
                //Decrease the number every second
            //set the label to the proper text for the timer's status
            case "Ready":
                self.mainCountDown.text = "3"
            case "3":
                self.mainCountDown.text = "2"
            case "2":
                self.mainCountDown.text = "1"
            case "1":
                //On shoot send a message to the peer and update the UI.
                self.mainCountDown.text = "SHOOT"
            case "SHOOT":
                //Disabel all character buttons selection buttons
                self.disableAllButtons()
                //UnWrap the optional image
                if let image = self.selectedImage{
                    //set the selected image to the players box
                    self.player1_Selection.image = image
                    //Create a message of your selected image
                    self.message = self.playersImage(image: image)
                }else{
                    //Default selection for the times users forget to sleect an image
                    self.selectRockMan()
                    self.player1_Selection.image = #imageLiteral(resourceName: "RockMan")
                }
                //Send the message to the peer
                if let data = self.message.data(using: String.Encoding.utf8){
                    try? self.session.send(data, toPeers: self.session.connectedPeers, with: .reliable)
                }
                //Stop the timer
                self.timer.invalidate()
                //Set the message property to a blank string
                self.message = ""
                
            default:
                break;
            }
            //Update the text in the additional label for the IPAD UI
            addCountDown_IPAD.text = self.mainCountDown.text
        }
    }
    //Functions for selecting the chosen characters
    func selectRockMan(){
        rockCheck.isHidden = false
        sissorsCheck.isHidden = true
        paperCheck.isHidden = true
        selectedImage = rockManButton.currentImage
    }
    func selectPaperMan(){
        rockCheck.isHidden = true
        sissorsCheck.isHidden = true
        paperCheck.isHidden = false
        selectedImage = papermanButton.currentImage
    }
    func selectSissorsMan(){
        rockCheck.isHidden = true
        sissorsCheck.isHidden = false
        paperCheck.isHidden = true
        selectedImage = sissorsManButton.currentImage
    }
    //Function for assigning a string representation of the image's name that's selected
    func playersImage(image: UIImage) -> String{
        var img = ""
        switch image {
        case #imageLiteral(resourceName: "RockMan"):
            img = "rock"
        case #imageLiteral(resourceName: "PaperMan"):
            img = "paper"
        case #imageLiteral(resourceName: "SissorsMan"):
            img = "sissors"
        default:
            break;
        }
        return img
    }
    //Function for checking for a winner
    func checkForWinner(){
        //assign the images a value
        if let image1 = player1_Selection.image{
            if let image2 = player2_Selection.image{
                //Use a switch statement to check for the select combination
                switch image1 {
                //Check if the user selected rock
                case #imageLiteral(resourceName: "RockMan"):
                    if image2 == #imageLiteral(resourceName: "RockMan"){
                        print("\(self.player2_Name.text!) has rock \(self.player1_Name.text!) has rock")
                        //Do nothing
                        changeColorsToYellow()
                    }
                    else if image2 == #imageLiteral(resourceName: "PaperMan"){
                        print("\(self.player2_Name.text!) has paper \(self.player1_Name.text!) has rock")
                        player1_Health[0].isHidden = true
                        player1_Health.remove(at: 0)
                        changeColorsToRed1Green2()
                    }
                    else if image2 == #imageLiteral(resourceName: "SissorsMan"){
                        print("\(self.player2_Name.text!) has sissors \(self.player1_Name.text!) has rock")
                        player2_Health[0].isHidden = true
                        player2_Health.remove(at: 0)
                        changeColorsToGreen1Red2()
                    }
                //Check if the user selected paper
                case #imageLiteral(resourceName: "PaperMan"):
                    if image2 == #imageLiteral(resourceName: "RockMan"){
                        print("\(self.player2_Name.text!) has rock \(self.player1_Name.text!) has paper")
                        player2_Health[0].isHidden = true
                        player2_Health.remove(at: 0)
                        changeColorsToGreen1Red2()
                    }
                    else if image2 == #imageLiteral(resourceName: "PaperMan"){
                        print("\(self.player2_Name.text!) has paper \(self.player1_Name.text!) has paper")
                        //Do nothing
                        changeColorsToYellow()
                    }
                    else if image2 == #imageLiteral(resourceName: "SissorsMan"){
                        print("\(self.player2_Name.text!) has sissors \(self.player1_Name.text!) has paper")
                        player1_Health[0].isHidden = true
                        player1_Health.remove(at: 0)
                        changeColorsToRed1Green2()
                    }
                //Check if the user selected sissors
                case #imageLiteral(resourceName: "SissorsMan"):
                    if image2 == #imageLiteral(resourceName: "RockMan"){
                        print("\(self.player2_Name.text!) has rock \(self.player1_Name.text!) has sissors")
                        player1_Health[0].isHidden = true
                        player1_Health.remove(at: 0)
                        changeColorsToRed1Green2()
                    }
                    else if image2 == #imageLiteral(resourceName: "PaperMan"){
                        print("\(self.player2_Name.text!) has paper \(self.player1_Name.text!) has sissors")
                        player2_Health[0].isHidden = true
                        player2_Health.remove(at: 0)
                        changeColorsToGreen1Red2()
                    }
                    else if image2 == #imageLiteral(resourceName: "SissorsMan"){
                        print("\(self.player2_Name.text!) has sissors \(self.player1_Name.text!) has sissors")
                        //Do nothing
                        changeColorsToYellow()
                    }
                default:
                    
                    break;
                }
            }
            //Send theese changes to the main thread
            //after delay
            let delay = DispatchTime.now() //NO Delay
            DispatchQueue.main.asyncAfter(deadline: delay) {
                //Check to see if player 2 wins
                if self.player1_Health.count == 0{
                    //Set the text for the winner and loser
                    self.addCountDown_IPAD.text = "Winner"
                    self.mainCountDown.text = "Loser"
                    //Make the play again button appear and be enabled
                    self.playAgainButton.isEnabled = true
                    self.playAgainButton.isHidden = false
                    //send a message that gives the peer the option to play again
                    self.message = "get_ready"
                    //encode the data
                    if let data = self.message.data(using: .utf8){
                        //send the message
                        try? self.session.send(data, toPeers: self.session.connectedPeers, with: .reliable)
                    }
                    //Check to see if player 1 wins
                } else if self.player2_Health.count == 0{
                    //Set the text for the winner and loser
                    self.addCountDown_IPAD.text = "Loser"
                    self.mainCountDown.text = "Winner"
                    //Make the play again button appear and be enabled
                    self.playAgainButton.isEnabled = true
                    self.playAgainButton.isHidden = false
                    //send a message that guves the peer the option to play agaoin
                    self.message = "get_ready"
                    //encode the data
                    if let data = self.message.data(using: .utf8){
                        //send the message
                        try? self.session.send(data, toPeers: self.session.connectedPeers, with: .reliable)
                    }
                }
                else{
                    //If no winner then
                    DispatchQueue.main.async {
                        self.readyButton.isHidden = false
                        self.mainCountDown.text = "-"
                        self.addCountDown_IPAD.text = "-"
                        print("Else block in check for win")
                    }
                    
                }
            }
        }
    }
    //custom functions that change the colors of the views for when a player wins, loses or ties. Also changes the color back to white for the next match.
    func changeColorsToRed1Green2(){
        player2_View.backgroundColor = #colorLiteral(red: 0.7067440152, green: 0.9862468839, blue: 0.6349543929, alpha: 1)//Green
        player2_Health_View.backgroundColor = #colorLiteral(red: 0.7067440152, green: 0.9862468839, blue: 0.6349543929, alpha: 1)//Green
        player1_View.backgroundColor = #colorLiteral(red: 0.7294117647, green: 0.2352941176, blue: 0.3137254902, alpha: 1)//red
        player1_Health_View.backgroundColor = #colorLiteral(red: 0.7294117647, green: 0.2352941176, blue: 0.3137254902, alpha: 1)//red
    }
    func changeColorsToGreen1Red2(){
        
        player1_View.backgroundColor = #colorLiteral(red: 0.7067440152, green: 0.9862468839, blue: 0.6349543929, alpha: 1)//Green
        player1_Health_View.backgroundColor = #colorLiteral(red: 0.7067440152, green: 0.9862468839, blue: 0.6349543929, alpha: 1)//Green
        player2_View.backgroundColor = #colorLiteral(red: 0.7294117647, green: 0.2352941176, blue: 0.3137254902, alpha: 1)//red
        player2_Health_View.backgroundColor = #colorLiteral(red: 0.7294117647, green: 0.2352941176, blue: 0.3137254902, alpha: 1)//red
        
        
    }
    func changeColorsToYellow(){
        
        player1_View.backgroundColor = #colorLiteral(red: 0.9214392304, green: 0.8957192898, blue: 0.4654401541, alpha: 1)//Yellow
        player1_Health_View.backgroundColor = #colorLiteral(red: 0.9214392304, green: 0.8957192898, blue: 0.4654401541, alpha: 1)//Yellow
        player2_Health_View.backgroundColor = #colorLiteral(red: 0.9214392304, green: 0.8957192898, blue: 0.4654401541, alpha: 1)//Yellow
        player2_View.backgroundColor = #colorLiteral(red: 0.9214392304, green: 0.8957192898, blue: 0.4654401541, alpha: 1)//Yellow
        
    }
    func changeplayerViewsToOriginalWhites(){
        
        player1_View.backgroundColor = #colorLiteral(red: 0.9660692811, green: 0.9502800107, blue: 0.9499314427, alpha: 1)//White
        player1_Health_View.backgroundColor = #colorLiteral(red: 0.9660692811, green: 0.9502800107, blue: 0.9499314427, alpha: 1)//White
        player2_Health_View.backgroundColor = #colorLiteral(red: 0.9660692811, green: 0.9502800107, blue: 0.9499314427, alpha: 1)//White
        player2_View.backgroundColor = #colorLiteral(red: 0.9660692811, green: 0.9502800107, blue: 0.9499314427, alpha: 1)//White
    }
    func syncCommunications(data: Data, peerID: MCPeerID){
        print("Searching for a match in the case statements")
        //Build a new string from the encoded NSData object. AKA(Unencoding)
        if let messageText = String(data: data, encoding: String.Encoding.utf8){
            //Dispatch to the main thread
            DispatchQueue.main.async {
                
                switch messageText.lowercased(){
                case "start":
                    //Acts when ready is pressed
                    self.readyButton.isHidden = true
                    self.enableAllButtons()
                    self.mainCountDown.text = "Ready"
                    self.addCountDown_IPAD.text = self.mainCountDown.text
                    //Dispatch to the main thread
                    DispatchQueue.main.async() {
                        self.timer = Timer.scheduledTimer(timeInterval: 1, target:self, selector: #selector(self.updateCounter), userInfo: nil, repeats: true)
                        self.changeplayerViewsToOriginalWhites()
                    }
                    //reset the message to a clear command
                    self.message = ""
                    
                case "rock":
                    self.player2_Selection.image = #imageLiteral(resourceName: "RockMan")
                    let delay = DispatchTime.now() + 0.5
                    //Dispatch to the main thread with a delay
                    DispatchQueue.main.asyncAfter(deadline: delay, execute: {
                        self.checkForWinner()
                        //print("From Peer - \(peerID.displayName) \n" + self.printHealth())   //Line telling the health levels   //Test
                    })
                    //reset the message to a clear command
                    self.message = ""
                    
                case "paper":
                    self.player2_Selection.image = #imageLiteral(resourceName: "PaperMan")
                    let delay = DispatchTime.now() + 0.5
                    //Dispatch to the main thread with a delay
                    DispatchQueue.main.asyncAfter(deadline: delay, execute: {
                        self.checkForWinner()
                        //print("From Peer - \(peerID.displayName) \n" + self.printHealth())   //Line telling the health levels   //Test
                    })
                    //reset the message to a clear command
                    self.message = ""
                    
                case "sissors":
                    self.player2_Selection.image = #imageLiteral(resourceName: "SissorsMan")
                    let delay = DispatchTime.now() + 0.5
                    //Dispatch to the main thread with the delay
                    DispatchQueue.main.asyncAfter(deadline: delay, execute: {
                        self.checkForWinner()
                        //print("From Peer - \(peerID.displayName) \n" + self.printHealth())   //Line telling the health levels   //Test
                    })
                    //reset the message to a clear command
                    self.message = ""
                    
                case "disconnect":
                    //Dispatch to the main thread
                    DispatchQueue.main.async {
                        self.session.disconnect()
                        self.dismiss(animated: true, completion: nil)
                    }
                    //reset the message to a clear command
                    self.message = ""
                    
                case "play_again":
                    //Dispatch to the main thread
                    DispatchQueue.main.async {
                        self.playAgainButton.isHidden = true
                        self.playAgainButton.isEnabled = false
                        self.readyButton.isHidden = false
                        //Re-stock the health arrays to original capacity
                        self.player1_Health = self.player1_Health_Copy
                        self.player2_Health = self.player2_Health_Copy
                        
                        //Make all of the health views visable
                        for health in self.player1_Health{
                            health.isHidden = false
                        }
                        for health in self.player2_Health{
                            health.isHidden = false
                        }
                        
                        //reset the message to a clear command
                        self.message = ""
                    }
                case "get_ready":
                    //Dispatch to the main thread
                    DispatchQueue.main.async {
                        //UN-hide the play again button and enable it
                        self.playAgainButton.isHidden = false
                        self.playAgainButton.isEnabled = true
                    }
                //Case to check if both players are ready
                case "p1ready":
                    
                    DispatchQueue.main.async {
                        self.addCountDown_IPAD.text = "Ready"
                        
                        if self.addCountDown_IPAD.text == "Ready"{
                            
                            self.message = "startGame"
                            
                            if self.mainCountDown.text == "Ready"{
                                
                                //Start the countdown
                                self.updateCounter()
                            }
                            
                        }
                    }
                    
                default:
                    break;
                }
            }
        }
    }
    
    //Change Constraints for orientations on IPad
    func changeConstraints(mulitplier: CGFloat){
        
        //Use an animation
        UIView.animate(withDuration: 1.5, delay: 0.0, options: .curveEaseInOut, animations: {
            //Change the multiplier to the constraints
            self.widthConstraintForP1View = self.widthConstraintForP1View.setMultiplier(multiplier: mulitplier)
            self.widthConstraintForP2View = self.widthConstraintForP2View.setMultiplier(multiplier: mulitplier)
        }, completion: nil)
        
    
}

//MARK: Device_Rotation

//This meathod is not being called?????  WHY??????
override func viewWillTransition(to size: CGSize, with coordinator: UIViewControllerTransitionCoordinator) {
    
    if device.lowercased() == "ipad"{
        //Check orientations
        //Landscape
        if UIDevice.current.orientation.isLandscape{
            //change the multiplier with animations
            changeConstraints(mulitplier: 0.39)
            print("Device Rotated to landscape")
            
        }
        //Portrait
        if UIDevice.current.orientation.isPortrait{
            //change the multiplier with animations
            changeConstraints(mulitplier: 0.47)
            print("Device Rotated to portrait")
        }
    }
}
//Comment this block back in to print a line that tells the current health(array.count) of each player
//Comment back in the calls in the sync function
//
//    func printHealth() -> String{
//        let s = " player 1 health count = \(String(player1_Health.count)) and player 2 health count = \(String(player2_Health.count))"
//
//        return s
//    }
/*
 // MARK: - Navigation
 
 // In a storyboard-based application, you will often want to do a little preparation before navigation
 override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
 // Get the new view controller using segue.destinationViewController.
 // Pass the selected object to the new view controller.
 }
 */

}
