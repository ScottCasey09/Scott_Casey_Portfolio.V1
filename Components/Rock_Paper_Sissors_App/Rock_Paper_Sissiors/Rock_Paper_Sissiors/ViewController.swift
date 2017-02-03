//
//  ViewController.swift
//  Rock_Paper_Sissiors
//
//  Created by Casey Scott on 1/16/17.
//  Copyright © 2017 Casey Scott. All rights reserved.
//
let segueIDentifier = "GoToGame"

import UIKit
import MultipeerConnectivity

class ViewController: UIViewController, MCBrowserViewControllerDelegate, MCSessionDelegate {
    
    //MARK: - UIOutlets
    
    @IBOutlet weak var statusMessage: UILabel!
    @IBOutlet weak var findButton: UILabel!
    @IBOutlet weak var buttonLabel: UILabel!
    @IBOutlet weak var rightSpacerConstraint: NSLayoutConstraint!
    @IBOutlet weak var leftSpacerConstraint: NSLayoutConstraint!
    
    //MARK: - Variables
    
    var device = "ipad"
    var peerID: MCPeerID! //Our device's ID or name as viewed as by other "boowsing" devices
    var session: MCSession! //The connection between devices
    var browser: MCBrowserViewController! //Prebuilt ViewController that searches for nearby advertisers.
    var advertiser: MCAdvertiserAssistant! //Helps us easily advertise ourselves to nearby MCbrowsers.
    let serviceID = "mdf2-RPSGame"// channel
    
    //MARK: - Load
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        if UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiom.pad{
            device = "ipad"
 
        }//If the device is an iphone
        else if UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiom.phone{
            device = "iphone"
      
        }
        
        //SetUp our MC Objects
        peerID = MCPeerID(displayName: UIDevice.current.name)
        //Use PeerID tp setUp a session
        session = MCSession(peer: peerID)
        session.delegate = self
        
        //Setup and start advertising immediately
        advertiser = MCAdvertiserAssistant(serviceType: serviceID, discoveryInfo: nil, session: session )
        advertiser.start()
        
    }
    
    //MARK: - Actions
    
    //Action for when the user presses the find challengers button
    @IBAction func findChallengerButtonTapped(_ sender: Any) {
        
        if buttonLabel.text?.lowercased() == "challenger a player"{
            //Our browser will look for advertisers that share the same service ID
            browser = MCBrowserViewController(serviceType: serviceID, session: session)
            browser.delegate = self
            //Present the Browser to the user
            self.present(browser, animated: true, completion: nil)
  
        }
        //Back up navigation to the game just in case of a failed segeue
        if buttonLabel.text?.lowercased() == "ready"{
            performSegue(withIdentifier: segueIDentifier, sender: self)
        }
        
    }
    
    //MARK: - Custom_Functions
    
    //Change Constraints for orientations on IPad
    func changeConstraints(mulitplier: CGFloat){
        
       
        //Use an animation
        UIView.animate(withDuration: 3, animations: {
            //Change the multiplier to the constraints
            self.leftSpacerConstraint = self.leftSpacerConstraint.setMultiplier(multiplier: mulitplier)
            self.rightSpacerConstraint = self.rightSpacerConstraint.setMultiplier(multiplier: mulitplier)
  
        })
        UIView.commitAnimations()
    }
    
        //MARK: - MCSessionDelegate
    
    //Gets called when the peer recieves the data
    func session(_ session: MCSession, didReceive data: Data, fromPeer peerID: MCPeerID) {//Required
        
        DispatchQueue.main.async {
            //Make the app move to the next view controller
            self.performSegue(withIdentifier: segueIDentifier, sender: self)//2
        }
    }
    
    //Method that gets called first in the sssion process
    func session(_ session: MCSession, peer peerID: MCPeerID, didChange state: MCSessionState) {//Required
        
        //This whole callback happens on a background tread
        DispatchQueue.main.async{
            
            //Check to see if the peer has connected
            if state == MCSessionState.connected{
                if session.connectedPeers.count > 1 {
                    //Update the status label
                    self.statusMessage.text = "Status: Connected to \(session.connectedPeers.count) Peers"
                    
                }else{
                    //Update the status label
                    self.statusMessage.text = "Status: Connected to " + peerID.displayName
                    //Send message
                    //Encode our string to get NSData Object
                    let message = "Ready"
                    if let encodedString = message.data(using: String.Encoding.utf8){
                        //send the NSData object to the peer
                        try? session.send(encodedString, toPeers: session.connectedPeers, with: .reliable)
                    }
                    //Set the button text to ready  .....  not realy necessary but is a good backup
                    self.buttonLabel.text = "Ready"
                }
                //If the peer is trying to connect
            }else if state == MCSessionState.connecting{
                //Update the status label
                self.statusMessage.text = "Status: Connecting..."
            }
                //If the peer is not connected
            else if state == MCSessionState.notConnected{
                //Update the status label
                self.statusMessage.text = "Status: No Connection!"
                
            }
            
        }
    }
    func session(_ session: MCSession, didReceive stream: InputStream, withName streamName: String, fromPeer peerID: MCPeerID) {//Required
        
    }
    func session(_ session: MCSession, didStartReceivingResourceWithName resourceName: String, fromPeer peerID: MCPeerID, with progress: Progress) {//Required
        
    }
    func session(_ session: MCSession, didFinishReceivingResourceWithName resourceName: String, fromPeer peerID: MCPeerID, at localURL: URL, withError error: Error?) {//Required
        
    }
    
    //MARK: - MCBrowserViewControllerDelegate
    
    //This method is called when the done button is pressed in  the browser view controller used to locate and connect to peers
    func browserViewControllerDidFinish(_ browserViewController: MCBrowserViewController) {
        browserViewController.dismiss(animated: true) {//Required
            self.performSegue(withIdentifier: segueIDentifier, sender: self)
        }
        
    }
     //This method is called when the cancel button is pressed in  the browser view controller used to locate and connect to peers
    func browserViewControllerWasCancelled(_ browserViewController: MCBrowserViewController) { //Required
        
        findButton.text = "Challenger A Player"
        browserViewController.dismiss(animated: true, completion: nil)
    }
    
    //MARK: - DeviceRotation
    
    override func viewWillTransition(to size: CGSize, with coordinator: UIViewControllerTransitionCoordinator) {
        if device.lowercased() == "ipad"{
            //Check orientations
            //Landscape
            if UIDevice.current.orientation.isLandscape{
                //change the multiplier with animations
                changeConstraints(mulitplier: 0.15)
                print("Device Rotated to landscape")
                
            }
            //Portrait
            if UIDevice.current.orientation.isPortrait{
                //change the multiplier with animations
                changeConstraints(mulitplier: 0.001)
            }
        }
    }

    
    //MARK: - Navigation

    //Get the gameViewController ready for the game by passing the session and all other properties
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        let destination = segue.destination as! GameViewController
        
        //Pass the properties to the next view controller
        destination.peerID = peerID
        destination.advertiser = advertiser
        destination.browser = browser
        destination.serviceID = serviceID
        destination.session = session
        destination.device = device
    }
    //Unwind action for when the GemaViewController is exited
    @IBAction func unwindToRoot(segue: UIStoryboardSegue){
        print("Unwind to root")
        //Change the button text back to original start phrase
        findButton.text = "Challenger A Player"
        //Make sure all peers are disconnected
        session.disconnect()
        statusMessage.text = "Status: No Connection!"
        if browser != nil{
        browser.dismiss(animated: false, completion: nil)
        }
    }
    
    //MARK: - Memory Warning
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
}

