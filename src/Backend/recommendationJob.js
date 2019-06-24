//By Miikael
var {PythonShell} = require('python-shell')

// Python process promise creation
function runPy(){
    return new Promise(async function(resolve, reject){
          let options = {
          mode: 'text',
          pythonOptions: ['-u'],
          scriptPath: 'recommendation_system/',//Path to your script
         };
         // Make the pythonshell run the script 'rec.py' and wait for finishing
          await PythonShell.run('rec.py', options, function (err, results) { 
          if (err) throw err;
      resolve(1)
     });
   })
  }
//Setup the timer interval for running the recommendation job,
// This runs every 2 minutes for now.
var minutes = 2, the_interval = minutes * 60 * 1000;
setInterval(function() {
  console.log("Starting recommendations");
  runPy().then(function (val) {
    console.log("done");
    })
}, the_interval);
