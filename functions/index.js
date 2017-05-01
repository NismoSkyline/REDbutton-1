const functions = require('firebase-functions');

const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.checkRequestApprovements = functions.database.ref('/Groups/{group}/requests/{request}')
    .onWrite(event => {
        const request = event.data.val();
        const requestKey = event.data.key;
        approvementsCount = request.agreeCount;
        if (approvementsCount == 2) {
            var ref = event.data.ref.parent.parent;
            var groupName = ""
            return ref.once("value")
                .then(function(snapshot){
                    groupName = snapshot.child("name").val();
                    const promise1 = event.data.ref.parent.parent.parent.parent.child('Users').child(request.userId).child('groups').child(groupName).set(true);
                    const promise2 = event.data.ref.parent.parent.child('members').child(request.userId).set(true);
                    event.data.ref.parent.child(requestKey).set(null);
                });
      }
    });