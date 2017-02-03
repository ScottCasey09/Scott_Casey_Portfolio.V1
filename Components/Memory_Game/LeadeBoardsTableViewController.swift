//
//  LeadeBoardsTableViewController.swift
//  Memory_Game
//
//  Created by Casey Scott on 1/23/17.
//  Copyright © 2017 Casey Scott. All rights reserved.
//

import UIKit
import CoreData

class LeadeBoardsTableViewController: UITableViewController {
    
    
    let headerIdentifier = "Header"
    let nibIdentifier = "CustomHeader"
    
    //MARK - Properties
    
    //Create a managed object context property
    var managedContext: NSManagedObjectContext!
    
    //Create an entity description
    private var entityDescription: NSEntityDescription!
    
    /* NSManagedObject - Used to represent the entity type 'NumTaps' that we created in our xcdatamodel file.
     We use the entity description to help us build the right kind on entity.*/
    
    //Create a managed object for our data to live in
    private var scores: NSManagedObject!
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        /* Set up Core Data Objects */
        
        /* Make sure our stored property is the same as the App's managedObjectContext */
        managedContext = (UIApplication.shared.delegate as! AppDelegate).CDStack.context
        
        /* Fill out our entity Description */
        entityDescription = NSEntityDescription.entity(forEntityName: "Scores", in: managedContext)
        
        //Manually assign the Indetifier to the nib
        tableView.register(UINib(nibName: nibIdentifier, bundle: nil), forHeaderFooterViewReuseIdentifier: headerIdentifier)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    // MARK: - Table view data source
    
    //Set the size of the header
    override func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        return 70
    }
    
    override func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        let header = tableView.dequeueReusableHeaderFooterView(withIdentifier: headerIdentifier) as! CustomHeader
        
        header.name.text = "Player"
        header.time.text = "Time"
        header.date.text = "Date"
        header.attempts.text = "Attempts"
        
        header.contentView.backgroundColor = #colorLiteral(red: 0.4274509804, green: 0.7215686275, blue: 0.5568627451, alpha: 1)
        
        return header
    }
    override func numberOfSections(in tableView: UITableView) -> Int {
        // #warning Incomplete implementation, return the number of sections
        return 1
    }
    
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        
        var count = 0
        //Get the data off of the notepad
        let fetchRequest = NSFetchRequest<NSFetchRequestResult>(entityName: "Scores")
        
        do{
            // Assign the data to an array variable
            if let results = try managedContext.fetch(fetchRequest) as? [NSManagedObject]{
                
                //Use the count of the array to determine the row count
                count = results.count
            }
        }
        catch{
            print("Load Failed")
        }
        //Return the total count of the array
        return count
        
    }
    
    //Set up each cell from the core data
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "Cell", for: indexPath) as! TableViewCell
        
        // Configure the cell...
        
        //Get the data off of the notepad
        let fetchRequest = NSFetchRequest<NSFetchRequestResult>(entityName: "Scores")
        
        //Sort the array of data objects by the time
        let sortDescriptor = NSSortDescriptor(key: "time", ascending: true)
        let sortDescriptors = [sortDescriptor]
        fetchRequest.sortDescriptors = sortDescriptors
        
        do{
            // Assign the data to an array variable
            if var results = try managedContext.fetch(fetchRequest) as? [NSManagedObject]{
                
                //Pass the array indexed item to the managed object
                scores = results[indexPath.row]
                
                //UpDate the cell with each score object
                cell.numberLabel.text = String(indexPath.row+1)
                cell.initialsLabel.text = scores.value(forKey: "name") as? String
                cell.attemptsLabel.text = scores.value(forKey: "attempts") as? String
                cell.timeLabel.text = scores.value(forKey: "time") as? String
                cell.dateLabel.text = scores.value(forKey: "date") as? String
                
                //Set the background colors for every other row
                if indexPath.row % 2 == 0{
                    cell.backgroundColor = #colorLiteral(red: 0.4274509804, green: 0.7215686275, blue: 0.5568627451, alpha: 1)
                }else{
                    cell.backgroundColor = #colorLiteral(red: 0.5094137192, green: 0.7557314038, blue: 0.5996403694, alpha: 1)
                }
            }
        }
        catch{
            print("Load Failed")
        }
        
        // Return the configured cell
        return cell
    }
    
    
    /*
     // Override to support conditional editing of the table view.
     override func tableView(_ tableView: UITableView, canEditRowAt indexPath: IndexPath) -> Bool {
     // Return false if you do not want the specified item to be editable.
     return true
     }
     */
    
    /*
     // Override to support editing the table view.
     override func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCellEditingStyle, forRowAt indexPath: IndexPath) {
     if editingStyle == .delete {
     // Delete the row from the data source
     tableView.deleteRows(at: [indexPath], with: .fade)
     } else if editingStyle == .insert {
     // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
     }
     }
     */
    
    /*
     // Override to support rearranging the table view.
     override func tableView(_ tableView: UITableView, moveRowAt fromIndexPath: IndexPath, to: IndexPath) {
     
     }
     */
    
    /*
     // Override to support conditional rearranging of the table view.
     override func tableView(_ tableView: UITableView, canMoveRowAt indexPath: IndexPath) -> Bool {
     // Return false if you do not want the item to be re-orderable.
     return true
     }
     */
    
    /*
     // MARK: - Navigation
     
     // In a storyboard-based application, you will often want to do a little preparation before navigation
     override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
     // Get the new view controller using segue.destinationViewController.
     // Pass the selected object to the new view controller.
     }
     */
    
    //Action for dismissing the leaderboards to the main view
    @IBAction func goBackButton(_ sender: UIButton){
        
        //Make the game transition to the main view controller
        if transitionStatus == "segeueFromGame"{
            let storyBoard : UIStoryboard = UIStoryboard(name: "Main", bundle:nil)
            
            let nextViewController = storyBoard.instantiateViewController(withIdentifier: "main") as! ViewController
            self.present(nextViewController, animated:true, completion:nil)
        }
        else{
            dismiss(animated: true, completion: nil)
        }
    }
}
