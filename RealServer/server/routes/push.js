var express = require('express');
var gcm = require('node-gcm');

var router = express.Router();

var server_api_key = '';
var sender = new gcm.Sender(server_api_key);

var registrationIds = [];


router.get('/register/all', function(req, res, next) {

  res.status(200).json(registrationIds);
});

router.get('/register/:token', function(req, res, next) {

  registrationIds.push(req.params.token);
  res.status(200).send("Okay !!");
});

router.get('/message', function(req, res, next) {

  res.render('send');
});

router.post('/message', function(req, res, next) {

  var message = new gcm.Message({

      collapseKey: 'demo',
      delayWhileIdle: true,
      timeToLive: 3,

      data: {

          title: req.body.title,
          message: req.body.message
      }
  });

  sender.send(message, registrationIds, 4, function (err, result) {

      console.log(result);
  });

  res.status(200).send("Message Sent !!");
});

module.exports = router;
