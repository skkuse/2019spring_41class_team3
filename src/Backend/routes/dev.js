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



// Python proces promise creation
function runPy(){
  return new Promise(async function(resolve, reject){
        let options = {
        mode: 'text',
        pythonOptions: ['-u'],
        scriptPath: 'recommendation_system/',//Path to your script
       };
        await PythonShell.run('rec.py', options, function (err, results) { 
        if (err) throw err;
    resolve(1)
   });
 })
} 

/* Reads from recommendation_system/recommendations.csv and updates
  every user's recommendations field in firebase
*/
router.get('/recommendations', function (req, res) {
  runPy().then(function (val) {
    fs.createReadStream('recommendation_system/recommendations.csv')  
    .pipe(csv())
    .on('data', (row) => {
        if(row.length > 1) {
          userId = row[0];
          row.shift();
          req.db.ref('/Users/' + userId).update({recommendations: row}, function(error){
            if (error){
              console.log(error);
            } else {
              console.log("GET /dev/recommendation for user " + userId + " : Success with values: " + row + "\n");
            }
          });
        }
    })
    .on('end', () => {
    });
    res.send("HTTP GET Request");
  })
});

module.exports = router;