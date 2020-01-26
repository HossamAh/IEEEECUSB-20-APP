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
    
    console.log(payload);
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

    console.log(payload);
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
    console.log(payload);

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


exports.sendNotificationCommitteesMessages = functions.database.ref("/Committees chatting/{chatId}/Messages/{messageId}").onWrite((event,context)=>
{
    const MessageID = context.params.messageId;
    const CommitteeID = context.params.chatId;
    
    if(CommitteeID === "IEEECUSB_Chat")
    {
        CommitteeName = "IEEECUSB";        
    }
    else{
    var CommitteeNameArray2 =CommitteeID.split("_Chat")[0];
    var CommitteeNameArray =CommitteeNameArray2.split(" ");
    var CommitteeName = CommitteeNameArray[0]+CommitteeNameArray[1];
    }
    console.log(CommitteeName);
    
    const payload=
    {
        data:
        {
            data_type:"direct_message",
            title:"newMessage from "+CommitteeID.split("_Chat")[0],
            message:event.after.val().text,
            message_id:MessageID,
            sender_name:event.after.val().name,
            sender_id:event.after.val().user_id,
            Committee:CommitteeID.split("_Chat")[0],
        }
    }
    console.log(payload);
    
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
exports.sendNotificationPrivateMessages = functions.database.ref("/PrivateChatting/{chatId}/Messages/{messageId}").onWrite((event,context)=>
{
    //get the chatid of the person receiving the notification because we need to get their token
    const ChatRoomId = context.params.chatId; 
	console.log("ChatRoomId: ", ChatRoomId);
    console.log("data",event.after.val());
    
	//get the user id of the person who sent the message
	const senderId = event.after.child('user_id').val();
	console.log("senderId: ", senderId);
	
	//get the message
	const message = event.after.child('text').val();
	console.log("message: ", message);
	
	//get the message id. We'll be sending this in the payload
	const messageId = context.params.messageId;
	console.log("messageId: ", messageId);
	
	//query the users node and get the name of the user who sent the message
	return admin.database().ref("/users/" + senderId).once('value').then(snap => {
		const senderName = snap.child("user_Name").val();
        console.log("senderName: ", senderName);
        //we have everything we need
        //Build the message payload and send the message
        console.log("Construction the notification message.");
        const payload = {
        data: {
                data_type: "direct_message",
                title: "New Message from " + senderName,
                message: message,
                message_id: messageId,
                profile_id: senderId,
                sender_name:senderName,
                }
                };
                var committeeName = "";
                var receiverId;
                    var senderStartIndex = ChatRoomId.search(senderId);
                    if(senderStartIndex > 0)
                    {
                        receiverId = ChatRoomId.slice(0,senderStartIndex);
                    }
                    else
                    {
                        receiverId = ChatRoomId.slice(senderStartIndex);
                    }    
                
                    return admin.database().ref('/users/'+receiverId).once('value').then(snap=>
                        {
                            const receiver_name = snap.child('user_Name').val();
                            console.log("Receiver name: ", receiver_name);
                            const token = snap.child('Messaging_Token').val();
                            console.log("Receiver Token: ", token);
                            return admin.messaging().sendToDevice(token, payload)
                            .then(function(response) {
                                        console.log("payload is : " ,payload.data);
                                        console.log("Successfully sent message:", response);
                                        return;
                                    })
                                    .catch(function(error) {
                                        console.log("Error sending message:", error);
                                        return;
                                    });
                                });
                    
                        });        
                                            
    });

 /*   const MessageID = context.params.messageId;
    const CHATID = context.params.chatId;

    
    const payload=
    {
        data:
        {
            data_type:"direct_message",
            title:"newMessage from "+event.after.val().name,
            message:event.after.val().text,
            message_id:MessageID,
            sender_name:event.after.val().name,
            profile_id:event.after.val().user_id,
            
        }
    }
    console.log(payload);
    
    return admin.messaging().sendToTopic(CommitteeName,payload).then(function(response)
    {
      console.log("Successfully sent message: ",response);  
      return;
    }).catch(function(error)
    {
        console.log("Error sending message: ",error)
        return;
    });
   */ 

