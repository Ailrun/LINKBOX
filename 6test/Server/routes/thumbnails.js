var express = require('express');
var mysql = require('mysql');
var router = express.Router();
var connection = mysql.createConnection({

    'host' : 'aws-rds-linkbox.cjfjhr6oeu3e.ap-northeast-1.rds.amazonaws.com', 
    'user' : 'LINKBOX', 
    'password' : 'dlrpqkfhdnflek', 
    'database' : 'sopt', });
router.get('/', function(req, res, next) {
connection.query('select id, title, timestamp from board ' +
'order by timestamp desc;', function (error, cursor) {
res.json(cursor); });
});
module.exports = router;