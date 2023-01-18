/*   
Author: Jaleta F. Tesgera 

Code Objective: To generate minimum , maximum and ending balances by monthYear for all customers from the data.

Instructions are in the Read Me text files.

References
----------
. How to read CSV file in Java - Javatpoint  Source: https://www.javatpoint.com/how-to-read-csv-file-in-java
. Read a CSV File into an Array - YouTube  Source: https://www.youtube.com/watch?v=-Aud0cDh-J8
. Array vs ArrayList in Java - GeeksForGeeks  Source:  https://www.geeksforgeeks.org/array-vs-arraylist-in-java/

Code Conceptual Explanation
---------------------------

After the initialization of variables (along with descriptive purposes), arrays, and string builder (to build the output csv file) this code has four 
sections. The first section, "VERIFICATION", as its name states is used to run through the data split the lines into arrays prior to operating the 
numbers to verify the existence of mandatory cells. This will first detect and ignore any empty lines in between the data. Following, it will detect 
missing Customer Id and Date from the data and report it to the user along with the line the error occurred and immediately exits from the code. In
the FAQs since it's described that all numbers are integers, and date will be in the correct format, I did not do any date and integer regex 
validation. Following in the second section, the code will read the file and take the first line as a point of reference (Empty first lines are ignored 
in a csv thus forcing existence of the first line data after valiadtion). Next in the main part of the code, the third section, using the variables 
we have from the first line we establish a series of if statements. Whenever the next line has a different Customer Id from the previous one or a 
different monthYear from the previous one it calculates the minimum , maximum and ending balances along with the previous monthYear and Customer ID and 
adds it to the array and appends the values to the filewriter (new csv file). Additionally, if the integer value in the last column is missing 
(transactions) the code translates that as a zero, since maybe transactions were not made on that day. Once this is done we reset all the values and 
start looping through the lines again. Once we looped through the values and gathered all the different customers and monthYear data available we end 
with the same steps of calculating the minimum , maximum and ending balances again. Finally, we put the results and the location of the new csv forward.

Result
------
Result of the calculation on the run page, and a new csv file in the output folder named "Result.csv".
I have also started a rough draft for the GUI, "Insight_Generator_GUI" which is in the same directory, for future development into an app.
*/

package insight_generator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.FileWriter;

public class Insight_Generator {


    public static void main(String[] args) throws FileNotFoundException, IOException {
       
   // Initially decalre varibales with descriptive names   
   String[] inputCsvArray = null;
   String customerId = null;
   String monthYear = null;
   String month = null;
   String year = null;
   int minBalance = 0;
   int maxBalance = 0;
   int endBalance = 0;
   int iteratingSum = 0;
   int integerValue;
   int lineReport = 1;
   int errorCount = 0;
   String outputFileName = "Result.csv";
   String sourceFile = "../TestFiles/Input - LargeData.csv";
   
        /* Additional Test Sources: 
           Instruction: Replace the "sourceFile" string with the file sources below
   
           Original File: "../Input.csv"
   
           Test File 1 (Missing Id): "../TestFiles/Input - NoID.csv"
           Test File 2 (Missing Date): "../TestFiles/Input - NoDate.csv"
           Test File 3 (Missing Transactions): "../TestFiles/Input - NoTransaction.csv"  (No error generated considers missing transactions at 0)
           Test File 4 (Missing All Columns): "../TestFiles/Input - NoAllCol.csv"
           Test File 5 (Customers with Multiple Month): "../TestFiles/Input - MultipleMonth.csv"
           Test File 6 (Large Data): "../TestFiles/Input - LargeData.csv"  (>1000 lines multiple months and year)
           
        */

   // Initially decalre arrays 
   List<List<String>> records = new ArrayList<>();
   ArrayList<Integer> dailyBalance = new ArrayList<Integer>();
   ArrayList<String> output = new ArrayList<String>();
   ArrayList<String> errorReport = new ArrayList<String>();
   
   //Initially call the CreateBuilder to create the output csv file
   StringBuilder outputString = new StringBuilder();

   
        // - - - - - - - - - 1. VERIFCIATION - - - - - - - - - - //
        
        // throw a Try catch statement to find and read the csv file
        try (BufferedReader br = new BufferedReader(new FileReader(sourceFile))) {
        String line;
        
            while ((line = br.readLine()) != null) {
                
                
         // Make a by looping through the values and splitting their parts on commas. 
        inputCsvArray = line.split(",");
        records.add(Arrays.asList(inputCsvArray));
         
               // verify the existence of the Customer ID and respective transaction date across the data. Report all the error and line of data 
               if (inputCsvArray.length == 0) {
                    lineReport++;
                    continue; // this line will ignore empty lines 
               }
               else if (inputCsvArray[0] == "") {
                    errorReport.add("There is a missing Customer ID at line " + lineReport + " of the file.");
                    errorCount++;
               }
               
               if (inputCsvArray[1] == "") {     
                    errorReport.add("There is a missing Date at line " + lineReport + " of the file.");
                    errorCount++;
               }
               
                lineReport++; 
            }
            
            // If there are errors then display the message
            if (errorCount > 0) {
              System.out.println("Error! We unfortunately could not develop your output right now.");
              System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - "); 
              for (int i=0; i<errorReport.size(); i++){
                  System.out.println(errorReport.get(i));
              }
              System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - "); 
              System.out.println("Please adjust the missing cells and try again.");
              System.exit(0);
            }

        }
        // Throw this statement if the file is not found
        catch (FileNotFoundException e) {
            
            System.out.println("Error. Unfortuntely the system can't find the file in the specified source!");
            System.out.println("Please point the system to the right directory and try again.");
            System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -"); 
            System.exit(0);
        }
   
   // - - - - - - - - - 2. FIRST TEST LINE TEST VARIABLES - - - - - - - - - - - - //
   // Collect first line comparison variables  
   try (BufferedReader br = new BufferedReader(new FileReader(sourceFile))) {
        String line = br.readLine();
        
         // Make a by looping through the values and splitting their parts on commas. 
        inputCsvArray = line.split(",");
        records.add(Arrays.asList(inputCsvArray));
        
        // Test ID for comparison
        customerId = inputCsvArray[0];
        
        // Test monthYear for comparison
        String[] parts = inputCsvArray[1].split("/");
        month = parts[0]; 
        year = parts[2]; 
   }
        
      // - - - - - - - 3. START OPERATING THE DATA - - - - - - - - - //
      // Start Reading the lines again
      BufferedReader br = new BufferedReader(new FileReader(sourceFile));
      String line;    
        
        
    // Read the values from a CSV file and record all the values in an array
    // WHILE LOOP STARTS HERE   
    while ((line = br.readLine()) != null) {
                 
        // Skip all the empty lines (empty tuples in a 3 column csv have ",," structure)
        if (line.equals(",,")) {
             continue;
         }
         
                  
        // Make a new array by looping through the values and splitting their parts on commas. 
        inputCsvArray = line.split(",");
        records.add(Arrays.asList(inputCsvArray));
        String[] parts = inputCsvArray[1].split("/");
       
        // if the previous Cutsomer ID, month, or Year is not the same stop the loop and report the data
        if ( (!(customerId.equals(inputCsvArray[0]))) || (!(month.equals(parts[0]))) || (!(year.equals(parts[2]))) ){
        
       //Max Balance
       // Create a loop that comapres every value to eachother and keeps updating the maxBalance with the greatest number until loop is complete.
       maxBalance = dailyBalance.get(0);
        for (int i = 1; i < dailyBalance.size(); i++) {
            if (maxBalance < dailyBalance.get(i))
                maxBalance = dailyBalance.get(i);
        }
        
        // Min Balance
        // Create a loop that comapres every value to eachother and keeps updating the minBalance with the least number until loop is complete.
        minBalance = dailyBalance.get(0);
        for (int i = 1; i < dailyBalance.size(); i++) {
            if (minBalance > dailyBalance.get(i))
                minBalance = dailyBalance.get(i);
        } 
        
          // Finally add all the values we collected into the array and append them by the format required i.e. CustomerID, MM/YYYY, Min Balance, Max Balance, Ending Balance.
          outputString.append(customerId).append(",").append(monthYear).append(",").append(minBalance).append(",").append(maxBalance).append(",").append(endBalance).append("\n");
          output.add(customerId + "," + monthYear + "," + minBalance + "," + maxBalance + "," + endBalance);
          
          //Reset the variales and arrays
             customerId = inputCsvArray[0];
             month = parts[0];
             year = parts[2];
             monthYear = null;
             minBalance = 0;
             maxBalance = 0;
             endBalance = 0;
             iteratingSum = 0; 
             dailyBalance.clear();
             
        }
        
        
        // Account Number 
        // The account number is the 1st column of the array
        customerId = inputCsvArray[0];
        
        // monthYear
        // Take out the date by splittign the string into mm/dd/yy and printing the first and last part from the new array
        month = parts[0];  
        year = parts[2]; 
        monthYear = month + "/" + year;
        
         // Start Calculations Here
        /* Convert the last column (Transaction) into an Int value for comparision and calculation. 
           However, if non-existent take the value as 0 (no transactions happend on that day). */
        if ( inputCsvArray.length == 2 ){
            integerValue = 0;
        }
        else {
              integerValue = Integer.parseInt(inputCsvArray[2]); 
       }
        
        //Fill the content of the established array "dailyBalance" with Balance on every given day of the monthYear to prepare for comparision of the Max and Min Balance
        iteratingSum += integerValue;
        dailyBalance.add(iteratingSum);

        // End Balance
        // Find the end balance by adding all the numbers in the last column(Transaction) or the last tuple of the "dailyBalance" array
        endBalance += integerValue;
        
   
        
      } // WHILE LOOP ENDS HERE
    
    
      
        //Max Balance
       // Create a loop that comapres every value to eachother and keeps updating the maxBalance with the greatest number until loop is complete.
       maxBalance = dailyBalance.get(0);
        for (int i = 1; i < dailyBalance.size(); i++) {
            if (maxBalance < dailyBalance.get(i))
                maxBalance = dailyBalance.get(i);
        }
        
        // Min Balance
        // Create a loop that comapres every value to eachother and keeps updating the minBalance with the least number until loop is complete.
        minBalance = dailyBalance.get(0);
        for (int i = 1; i < dailyBalance.size(); i++) {
            if (minBalance > dailyBalance.get(i))
                minBalance = dailyBalance.get(i);
        } 
        
        
        
          // - - - - - - - - - - - - -  4. RESULT - - - - - - - - - - - - - //
          
          // Finally add all the values we collected into the array and append them by the format required i.e. CustomerID, MM/YYYY, Min Balance, Max Balance, Ending Balance.
          outputString.append(customerId).append(",").append(monthYear).append(",").append(minBalance).append(",").append(maxBalance).append(",").append(endBalance).append("\n");
          output.add(customerId + "," + monthYear + "," + minBalance + "," + maxBalance + "," + endBalance);
          
          // Display the Result
          System.out.println("Congratulations! Data was sucessfully generated!"); 
          System.out.println("You will find the sample result below.");
          System.out.println("The CSV file generated (Result.csv) is in the output folder."); 
          System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - "); 
          System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - "); 
          int outputLength = output.size();
          for(int i=0; i<outputLength; i++){
          if (i==0) System.out.println("Sample Output: " + output.get(i));
          else      System.out.println("               " + output.get(i));  
          }
          System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - "); 
          System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - "); 
          
          // Create a new csv with the appended values (identical to the one on the result page)
           try ( FileWriter fileWriter = new FileWriter("../Output/" + outputFileName) ) {
                 fileWriter.write(outputString.toString());
          }
          catch(Exception ex) {
                ex.printStackTrace();
          }
        
     
        
  } 
       
}  

         // -------------------------------- CODE ENDS HERE --------------------------------------- //
