Final project template 
======================
This is the template for the final project in the course. Your application should be run on [Application.java](src%2Fedu%2Fnajah%2Fcap%2Fdata%2FApplication.java)

**Do not change any existing code in the template. You can add new classes and methods as you see fit.**

## Project Description
*we designed aproject that simulates the soccial media apps from the back end prespictive we simulated data exporting and convirting using java while follwing the solid priceple and desing patterns when needed and recorded every operation using the logger librare from java *
## The class diagram for the project
![yousefsamara](https://github.com/yousefQJ19/Advance-project/assets/92521652/580f316a-2208-4f11-97e2-5d527b25e0d9)

### explanation

**we divided the project into 4 section each one works separitly**

  * the Export section that does the expoting of data from the data bace for each service the combines them in the handler
  * the Convert section the handles the conversion between the data that we exported from (zip to text to pdf then zip contains the pdf files)
  * the Delete section theat handles the deletion of data
    * soft delete
    * hard delete
  * the Upload section that handles the uploading data to google drive ,dropbox and sending it by email

### What each __Section__ does and contains spaceficly

* Exporter
  * UserActivitiesExporter : concret class exports the the user activite data from the activite services data base
  * UserPaymentExporter : concret class exports the the user payment data from the payment services data base
  * UserPostsExporter : concret class exports the the user posts data from the posts services data base
  * UserProfileExporter : concret class exports the the user profile data from the user services data base
  * ExportHandler : used as the template design pattern becouse all the exporters do simular operation to reduce cobuling and code writen
    
* Converter
  * IConvert : the Base interface we add to break the dependance inversion betwen the project and the convert classes
  * ConvertPdfToZip : concret class converts pdf files into a zip files then deletes the pdf files
  * ConvertTextTopdf : concret class converts a single text file into a pdf file
  * ConvertZipToPdf : concret class extracts a zip files containing a text files and converting each of them into pdf file
  * ConvertFactory : return a converter dependeng on the type requsted made it to prevent cobuling
  * ConvertContext : receves a Converter and do the converting as the receved converter and prevents cubling insted of using
    
* Delete
  * IDelete :the Base interface for the service contains the delete function and made to break the dependance inversion between the app and the delete classes
  * HardDeleteProcessor : concret class that does the hard delet that deletes the user as he never existed
  * SoftDeleteProcessor : concret class that does the soft delet of data and keeps the user profiles information
  * DeleteFactory : return a deleter as the requsted type and it used to prevent the cubbling
  * DeleteContext : receves a deleter and des the delte operation as the receved delete type

* Upload
 * IUpload : the Base interface for the service contains the send function and made to break the dependance inversion between the app and the upload classes
 * SendByEmail : the function receves an email for the recever and send the email with a zip files containing the requsted data as an attachment to the users email
 * UploadToGoogleDrive : the function receves an email then upload the zip files to a google drive then sends the link of it as email to the user
 * UploadToDropbos : the function receves an email then uploads the zip files to dropbox then sends the link of to the user email as a massage

* Adding a new user
 AddUserConfirmation : this service used to prevent the app from creating an account that has the same name as a deleted one 


## Summary
**we designed tha 4 section while followin the solid princeple mainly fucosed on cubbling dependance inversion and the single responsaplete problem we ensured that the app is scalable flexable and and and every operation excuted in the app is recorded in the log files using the logger for any accountabilte and the app designed focusing on the readers**
