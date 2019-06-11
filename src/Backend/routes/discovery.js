/* This route is used for testing in the development environment */
var express = require('express');
var router = express.Router();

/* GET dev index page page. */
router.get('/', function(req, res, next) {
  res.send('Discovery system');
});

router.get('/:user', function (req, res) {
  // Check token validity
  if (req.token !== null) {
    let uid = req.token.uid;
    // Retrieve user gender
    req.db.ref().child("Users").child(uid).child("user_gender")
    .once('value', function(v) {
      let target = v.val() === "Male"? "topman": "zara";

      // Retrieve items (for now, randomly)
      req.db.ref().child('items').child(target)
      .once('value', function(items) {
        // Randomly select items from list
        let JSON = items.toJSON();
        let keys = Object.keys(JSON);
        let out = {};
        let arr = [];
        while(arr.length < 20) {
          let r = Math.floor(Math.random()*keys.length);
          if(arr.indexOf(r) === -1) {
            arr.push(r);
            out[keys[r]] = JSON[keys[r]];
          }
        }
        // Send result to user
        res.json(out)
        console.log("Sent recommendations to UID: " + uid);
        console.log(out);
      });
    });
  }
});

module.exports = router;