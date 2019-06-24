var express = require('express');
var router = express.Router();


router.get('/', function(req, res, next) {
  res.send('Discovery system');
});

/* Get user's recommendations */
router.get('/:user', function (req, res) {
    req.auth.verifyIdToken(req.headers.authorization.replace("Bearer ", ""))
    .then(function(decodedToken) {
      let uid = decodedToken.uid;
      if (uid !== null) {
        // By Miikael
        // Gets the user data
        req.db.ref().child('Users').child(uid).once('value',
        (user) => {
          user = user.val();
          if(user.recommendations){
            // User's recommendations
            var recommendations = user.recommendations;
            var returnableItems = {};
            var gender;
            // Depending on the user's gender, we should either choose from
            // list of topman items or zara items
            if(user.user_gender == 'Male'){
              gender = 'topman';
            }else{
              gender = 'zara';
            }
            var arr = [];
            var items = [];
            // Insert 30 unique random recommended items' id to an array
            while(arr.length < 30){
                var r = Math.floor(Math.random()*recommendations.length) + 1;
                if(arr.indexOf(r) === -1){
                  if(recommendations[r]){
                    arr.push(r);
                    items.push(recommendations[r]);
                  }
                }
            }
            // Gather all the promises needed for retrieving every items data from firebase
            var itemPromises = items.map(id => {
                  return req.db.ref().child('items').child(gender).child(id).once('value');
            })
            // Wait for all of the promises to be done
            Promise.all(itemPromises)
            .then(items => {
              // Append the results to the object array
              items.forEach(it => {
                returnableItems[it.ref_.path.pieces_[2]] = it.val();
              })
              // Respond the res with json items
              res.json(returnableItems);
            })
            .catch(err => {
              console.log(err);
            })
          }
        })
      }
    })
});

module.exports = router;