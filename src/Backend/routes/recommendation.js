/* This route is used for testing in the development environment */
var express = require('express');
var router = express.Router();

/* GET dev index page page. */
router.get('/', function(req, res, next) {
  res.send('Discovery system');
});

/* Get test route. */
router.get('/:user', function (req, res) {
  req.auth.verifyIdToken(req.headers.authorization.replace("Bearer ", ""))
  .then(function(decodedToken) {
    let uid = decodedToken.uid;
    if (uid !== null) {
      req.db.ref().child('items').child('topman').once('value', 
      (v) => {
        let JSON = v.toJSON();
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
        res.json(out)
        console.log("Sent recommendations to UID: " + uid);
      });
    }
  }).catch(function(error) {
    console.error(error);
  });
});

module.exports = router;