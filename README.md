# PICoursework

## Backend

### Garmin API Data Collection

#### Importing FIT Files

1. Users must import their FIT activity files from their device, by connecting over Bluetooth or wired connection on their desktop, and using the following URL https://connect.garmin.com/. Instructions are clear after registering on how to import files.
2. Move them to the following folder (~ denotes root of the project) "~/Backend/DataCollection/FITActivity".
3. These can then be processed by garmin_collection_server.py - checking for when new files are entered.
**(LATER BUT MORE DIFFICULT PROCESS TO DEAL WITH ALL OF THIS WITH ANOTHER API SERVICE SUCH THAT THE USER ONLY HAS TO INTERACT WITH THE UI)**

#### Official Garmin SDK Implementation For Extracting Data from FIT Files

#### Server Process and Endpoint for Feeding Extracted Data

### Data Model

![PIDataModel drawio](https://github.com/jonahbkaplan/PICoursework/assets/106742444/df2b308a-1562-4443-bbca-f6536d1ffe02)

The existing data model can be extended when we add new features. This is a semi-structured *schema* which must be enforced by all of the services we write (see below). 

### Servicing Architecture (interfacing between backend & UI) 

![PIServicingArchitecture drawio](https://github.com/jonahbkaplan/PICoursework/assets/106742444/0e1843b0-dba7-472e-a91d-efe79e34c344)

The above uses a **microservices** architecture, every UI component talks to an individual service which handles obtaining its relevant information, as well as writing to the database. This works by sending JSON requests from the UI component to the service through a POST request, and waiting for a JSON response on a GET request. JavaHTTP libraries can be integrated with JavaFX to enable this functionality. 


### Stress / Sensory Overload Prediction Model

## Frontend

### Mood & Emotional Reactivity Tracking Boards & Tools

### Productivity Tracking Tools

### Data Presentation for Predictions and Raw Productivity Tracking

### JavaFX UI Implementation
