/* This route is used for testing in the development environment */
var express = require('express');
var router = express.Router();

/* GET dev index page page. */
router.get('/', function(req, res, next) {
  res.render('upload', { title: 'Item Upload' });
});

/* Get test route. */
router.post('/item', function (req, res) {
  // Database is defined in req.db
  // The collection /TestMessages is referenced and TestMessage variable inside
  // is assigned value defined in the object inside the set() paramater.
  
  req.db.ref('/items').push().set(req.body, function(error){
    if (error){
      console.log(error);
    } else {
      console.log("GET /dev/test: Success!");
    }
  });
  res.send("HTTP GET Request");
});

module.exports = router;