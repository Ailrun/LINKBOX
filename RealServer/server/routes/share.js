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
router.post('/:cbid/addusr', function(req, res, next){
    connection.query('SELECT usrid FROM usr WHERE usremail = ?;', [req.body.usremail], function(error, cursor){
        connection.query('insert into share (usrid, cbid) values (?, ?);', [cursor[0].usrid, req.params.cbid], function(error, info) {  console.log(error)
        if(error != undefined){
            res.sendStatus(503);
        }
            else{
                res.json({"result":true              
                });
            }
            
        });
    
    });
});

//
router.get('/:cbid/urllist', function(req, res, next) {
    
    connection.query('SELECT usrid FROM share WHERE cbid = ?;', [req.params.cbid], function (error, cursor){
        console.log(cursor.length)
        connection.query('SELECT usrid, usrname, usremail, usrprofile FROM usr where usrid = ?;', [cursor.usrid], function (error, info){
        
            if(error != undefined){
                res.sendStatus(503);
            }
            else{
                res.json(info);
            }
            
        });       
    });
});


module.exports = router;
