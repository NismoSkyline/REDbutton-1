const functions = require('firebase-functions');

const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.checkRequestApprovements = functions.database.ref('/Groups/{group}/requests/{request}')
    .onWrite(event => {
         const original = event.data.val();
         const datakey = event.data.key;

         i = original.agreeCount;
         if (i == 2) {

        var ref = event.data.ref.parent.parent;
        var groupName = "someName"
        return ref.once("value")
            .then(function(snapshot){
                groupName = snapshot.child("name").val();
                const promise1 = event.data.ref.parent.parent.parent.parent.child('Users').child(original.userId).child('groups').child(groupName).set(true);
                const promise2 = event.data.ref.parent.parent.child('members').child(original.userId).set(true);
                 event.data.ref.parent.child(datakey).set(null);
            });
      }
    });