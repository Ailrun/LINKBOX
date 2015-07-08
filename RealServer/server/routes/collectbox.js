var express = require('express');
var mysql = require('mysql');

var router = express.Router();
var connection = mysql.createConnection({

	'host' : 'aws-rds-linkbox.cjfjhr6oeu3e.ap-northeast-1.rds.amazonaws.com',
	'user' : 'LINKBOX',
	'password' : 'dlrpqkfhdnflek',
	'database' : 'LINKBOX',
});

router.get('/{usrid}/cbid', function(req, res, next) {
  
	connection.query('select * from collectbox where cbid=?;', [req.params.cbid], function (error, cursor) {

                if (cursor.length > 0) {

                        res.json(cursor[0]);
                }
                else {
                        res.status(503).json({
                            result : false,
							reason : "Cannot find selected article"
						});
                }
        });
});


module.exports = router;