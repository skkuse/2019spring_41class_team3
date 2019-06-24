var createError = require('http-errors');
var express = require('express');
var path = require('path');
var cookieParser = require('cookie-parser');
var logger = require('morgan');
var firebase = require("firebase-admin");
var serviceAccount = require("./serviceAccountKey.json");

//Dotenv enables the use of .env file
// .env file is used for defining environment variables
// such as database usernames and passwords
// PORT=2345 in .env file can be accessed by
// process.env.PORT
var dotenv = require("dotenv");
dotenv.config();

//Set up the DB by authenticating with the private key in serviceAccountKey.json
firebase.initializeApp({
  credential: firebase.credential.cert(serviceAccount),
  databaseURL: process.env.databaseURL
});

var indexRouter = require('./routes/index');
var usersRouter = require('./routes/users');
var devRouter = require('./routes/dev');
var uploadRouter = require('./routes/upload');
var crawlerRouter = require('./routes/crawl');
var discoveryRouter = require("./routes/discovery");
var recommendationsRouter = require('./routes/recommendation');

var app = express();

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

//Middleware that assigns req.db to be equal to the firebase database object
//This makes it so the database can be accessed in every route
var databaseMiddleware =  function(req, res, next){
  req.db = firebase.database();
  next();
}
var authMiddleware = function(req, res, next) {
  req.auth = firebase.auth();
  if(req.headers.authorization){
    req.auth.verifyIdToken(req.headers.authorization.replace("Bearer ", ""))
    .then(function(decodedToken) {
      req.token = decodedToken;
      next();
    }).catch(function(error) {
      console.error("Authentication failed");
      next();
    });
  } else {
    console.error("Request is not authenticated");
    if(req.app.get('env') === 'development'){
      req.token = {uid: "kOcru895A4Svq0M8tYygOND8jQb2"};
      next();
    }
  }

}
//This actually couples the databaseMiddleware to the express app.
app.use(databaseMiddleware);
app.use(authMiddleware);

app.use('/', indexRouter);
app.use('/users', usersRouter);
app.use('/dev', devRouter);
app.use('/upload/', uploadRouter);
app.use('/crawl/', crawlerRouter);
app.use('/discovery/', discoveryRouter);
app.use('/recommendation/', recommendationsRouter);
// Run the recommendation job in 2 minute intervals
require('./recommendationJob.js');

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  next(createError(404));
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});

module.exports = app;
