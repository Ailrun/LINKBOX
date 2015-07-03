var express = require('express');
var mysql = require('mysql');

var router = express.Router();
var connection = mysql.createConnection({

	'host' : 'aws-rds-instance.clpupei7ldak.us-west-1.rds.amazonaws.com',
	'user' : 'user',
	'password' : 'aws_password',
	'database' : 'sopt',
});

router.get('/', function(req, res, next) {
  
	connection.query('select id, title, timestamp from board order by timestamp desc;', function (error, cursor) {
		
		res.json(cursor);
	});
});

module.exports = router;
