const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();
// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });

exports.sendNotificationCommitteeEvent = functions.database.ref("/Committees/{CommitteeID}/Events/{EventID}").onWrite((event,context)=>
{
    const EventID = context.params.EventID;
    const CommitteeID = context.params.CommitteeID;

    const payload=
    {
        data:
        {
            data_type:"Committee_Event",
            Topic:event.after.val().topic,
            Details:event.after.val().details,
            Date:event.after.val().date,
            Location:event.after.val().location,
            Target:event.after.val().target,
            ImageUrl:"",
            EventID:EventID,
        }
    }
    var CommitteeNameArray =CommitteeID.split(" ");
    var CommitteeName = CommitteeNameArray[0]+CommitteeNameArray[1];
    console.log(CommitteeName);

    return admin.messaging().sendToTopic(CommitteeName,payload).then(function(response)
    {
      console.log("Successfully sent message: ",response);  
      return;
    }).catch(function(error)
    {
        console.log("Error sending message: ",error)
        return;
    });
    
});


exports.sendNotificationGeneralEvent = functions.database.ref("/IEEECUSB/Events/{EventID}").onWrite((event,context)=>
{
    const EventID = context.params.EventID;
    const payload=
    {
        data:
        {
            data_type:"General_Event",
            Topic:event.after.val().topic,
            Details:event.after.val().details,
            Date:event.after.val().date,
            Location:event.after.val().location,
            Target:event.after.val().target,
            ImageUrl:"",
            EventID:EventID,
        }
    }

    return admin.messaging().sendToTopic("IEEECUSB",payload).then(function(response)
    {
      console.log("Successfully sent message: ",response);  
      return;
    }).catch(function(error)
    {
        console.log("Error sending message: ",error)
        return;
    });
    
});


exports.sendNotificationCommitteeTask = functions.database.ref("/Committees/{CommitteeID}/Tasks/{TasksID}").onWrite((event,context)=>
{
    const TasksID = context.params.TasksID;
    const CommitteeID = context.params.CommitteeID;

    const payload=
    {
        data:
        {
            data_type:"Committee_Task",
            Topic:event.after.val().topic,
            Details:event.after.val().details,
            Deadline:event.after.val().deadlineDate,
            Location:"",
            Target:event.after.val().target,
            ImageUrl:"",
            TasksID:TasksID,
        }
    }
    var CommitteeNameArray =CommitteeID.split(" ");
    var CommitteeName = CommitteeNameArray[0]+CommitteeNameArray[1];
    console.log(CommitteeName);

    return admin.messaging().sendToTopic(CommitteeName,payload).then(function(response)
    {
      console.log("Successfully sent message: ",response);  
      return;
    }).catch(function(error)
    {
        console.log("Error sending message: ",error)
        return;
    });
    
});