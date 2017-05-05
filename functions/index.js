const functions = require('firebase-functions');

const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.checkRequestApprovements = functions.database.ref('/Groups/{group}/requests/{request}')
    .onWrite(event => {
        const request = event.data.val();
        const requestKey = event.data.key;
        var approvementsCount = request.agreeCount;
        if (approvementsCount == 2) {
            var ref = event.data.ref.parent.parent;
            var groupName = ""
            return ref.once("value")
                .then(function(snapshot){
                    groupName = snapshot.child("name").val();
                }).then(() => {
                    event.data.ref.parent.parent.parent.parent.child('Users').child(request.userId).child('pending').child(groupName).set(null);
                }).then(() => {
                    event.data.ref.parent.parent.child('members').child(request.userId).set(true);
                }).then(() => {
                    event.data.ref.parent.child(requestKey).set(null);
                });
      }
    });

exports.groupMemberListener = functions.database.ref('/Groups/{group}/members/{member}')
    .onWrite( event => {
        const userId = event.data.key;
        const groupName = event.data.ref.parent.parent.key;
        console.log(userId, groupName);
        const userGroupMembershipRef = admin.database().ref('Users').child(userId).child('groups').child(groupName);

        if (event.data.exists() && !event.data.previous.exists()) {
            console.log("On member added");
            return userGroupMembershipRef.set(true);
        }
        else if (!event.data.exists() && event.data.previous.exists()) {
            console.log("On member deleted");
            return userGroupMembershipRef.set(null);
        } else {
            return;
        }
    });