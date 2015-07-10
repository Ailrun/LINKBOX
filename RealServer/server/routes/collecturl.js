var express = require('express');
var mysql = require('mysql');
var router = express.Router();


var connection = mysql.createConnection({

    'host' : 'aws-rds-linkbox.cjfjhr6oeu3e.ap-northeast-1.rds.amazonaws.com',
    'user' : 'LINKBOX',  
    'password' : 'dlrpqkfhdnflek',
    'database' : 'LINKBOX'
});

//url 가져오기
router.get('/:cbid/urllist', function(req, res, next) {
    connection.query('select * from url where usrid = ? and cbid = ? ', [req.body.usrid, req.params.cbid], function (error, cursor) {
        res.json(cursor);
    });
});

//url 추가
router.post('/:usrid/:cbid/addurl', function(req, res, next){
    connection.query('SELECT MAX(urlid) as max from url;', function(error, cursor){
        connection.query('INSERT INTO url (urlid, urlname, urlthumbnails, address, usrid, cbid) values(?, ?, ?, ?, ?, ?);', [cursor[0].max+1, req.body.urlname, req.body.urlthumbnails, req.body.address, req.params.usrid, req.params.cbid], function(error, info) {
        console.log(error);
            if(error != undefined){
                res.sendStatus(503);
            }
            else{
                res.json({
                    "result":cursor[0].max
                });
                console.log(error);
            }
        });
    });
});

//url 삭제
router.post('/:cbid/removeurl', function(req, res, next) {
    connection.query('delete from url where cbid=? and urlid = ?;', [req.params.cbid, req.body.urlid], function (error, cursor) {
        console.log(error);
        if (error == undefined){
            res.json({result : 'true'});
        }
        else{
            res.status(503).json({result : 'false'});
        }
    });
});
module.exports = router;