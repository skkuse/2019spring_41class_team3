/* This route is used for testing in the development environment */
var express = require('express');
var router = express.Router();
var csv = require('fast-csv');  
var fs = require('fs');
var {PythonShell} = require('python-shell')

/* GET dev index page page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Dev EXPRESS' });
});

/* Get test route. */
router.get('/test', function (req, res) {
  // Database is defined in req.db
  // The collection /TestMessages is referenced and TestMessage variable inside
  // is assigned value defined in the object inside the set() paramater.
  req.db.ref('/TestMessages').set({testMessage: 'asdadada'}, function(error){
    if (error){
      console.log(error);
    } else {
      console.log("GET /dev/test: Success!");
    }
  });
  res.send("HTTP GET Request");
});




module.exports = router;