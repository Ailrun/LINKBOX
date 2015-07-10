var express = require('express');
var router = express.Router();
var mysql = require('mysql');

var connection = mysql.createConnection({
    'host' : 'aws-rds-linkbox.cjfjhr6oeu3e.ap-northeast-1.rds.amazonaws.com',
    'user' : 'LINKBOX',  
    'password' : 'dlrpqkfhdnflek',
    'database' : 'LINKBOX'
});


//collectbox에 공유하고자 하는 usr추가
router.post('/:cbid/addusr/:usremail', function(req, res, next){
    connection.query('SELECT usrid FROM usr WHERE usremail = ? ', [req.params.usremail], function(erroe, cursor){
        console.log(cursor)
        connection.query('INSERT INTO share (usrid, cbid) values (?, ?);' [cursor.usrid, req.params.cbid], function(error, info) {
        if(error != undefined){
            res.sendStatus(503);
        }
            else{
                info.json({"result":true              
                });
            }
            
        });
    
    });
});

//
router.get('/:usrid/boxlist', function(req, res, next) {
  
   connection.query('SELECT cbid, cbname FROM collectbox WHERE usrid=?;', [req.params.usrid], function (error, cursor) {
      
      res.json(cursor);
   });
});


module.exports = router;
