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
router.get('/:cbid/urllist/:usrid', function(req, res, next) {
    connection.query('select * from collecturl where ?', [req.params.usrid], function (error, cursor) {
        res.json(cursor);
    });
});

//url 추가
router.post('/:cbid/addurl', function(req, res, next){
    connection.query('SELECT MAX(urlid) AS max from url;', function(error, cursor){
    connection.query('INSERT INTO url (urlid, urlname, urlthumbnail, address) values(?, ?, ?, ?);', [cursor[0].max+1, req.body.urlname, req.body.urlthumbnail, req.body.address], function(error, info) {
        connection.query('SELECT MAX(cuid) AS max from collecturl;', function(error, extra){
                    connection.query('INSERT INTO collecturl (cuid, urlid, cbid) values(?, ?, ?, ?);', [extra[0].max+1, cursor[0].max+1, req.params.cbid], function(error, ) {
                        
        });
        });

            if(error != undefined)
                response.sendStatus(503);
        
            else{
                response.json({
                    "result":cursor[0].max
                });
                console.log(error);
            }
        });
    });
});
/*
//박스추가
router.post('/:usrid/addbox', function(request, response, next){

    connection.query('SELECT MAX(cbid) as max from collectbox;', function(error, cursor){
    connection.query('INSERT INTO collectbox (cbid, cbname, usrid) values(?,?,?);', [cursor[0].max+1, request.body.cbname, request.params.usrid], function(error, info) {
        console.log(error);
            if(error != undefined){
                response.sendStatus(503);
            }
            else{
                response.json({
                    "result":cursor[0].max
                });
                console.log(error);
            }
        });
    });
});
*/



/*
//링크 추가
router.post('/collecturl/{cbid}/addurl', function(request, response, next) {

 connection.query('insert into url(urlname, address, urlthumbnail) values (?, ?, ?);', [ request.body.urlname, request.body.address, request.body.urlthumbnail ], function (error, result) {
     if (error) {
        console.log("err", error);
            res.json({
                        result : 'fail'
                    });
        }
        else {
        console.log("result", result);
            res.json({
                        result : 'success'

                    });
                }
        });
});



//링크삭제
router.post('/collecturl/{cbid}/removeurl', function(req, res, next) {
    
        connection.query('delete * from url where urlid;', [req.body.urlid], function (error, cursor) {

                                if (cursor.length > 0) {
                                        var result = cursor[0];
                                        res.json({
                                    result : true,
                                                                                                                               });
                                }
                                else {
                                        res.status(503).json({
                                 result : false,
                                });
                                }
                        });

                      
               });



//링크 수정
router.post('/collecturl/{cbid}/editurl', function(req, res, next) {
    
 connection.query("UPDATE url SET urlname=? Where urlid=?;", [req.body.urlname, req.body.urlid], function(error, result) {
     if (error) {
            console.log("err", error);
            res.json({
                        result : 'fail'
                    });
  } else {
            console.log("result", result);
            res.json({
                        result : 'success'
                    });
            }
    });

});

//링크 리스트 보내기
router.get('/collecturl/{cbid}/urllist', function(req, res, next) {
  
   connection.query('select urlname, urlid, address, ulrthumbnail from url order by timestamp desc;', function (error, cursor) {
      
      res.json(cursor);
   });
});
*/

module.exports = router;