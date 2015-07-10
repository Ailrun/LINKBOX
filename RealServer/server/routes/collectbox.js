var express = require('express');
var router = express.Router();
var mysql = require('mysql');

var connection = mysql.createConnection({

    'host' : 'aws-rds-linkbox.cjfjhr6oeu3e.ap-northeast-1.rds.amazonaws.com',
    'user' : 'LINKBOX',  
    'password' : 'dlrpqkfhdnflek',
    'database' : 'LINKBOX'
});


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


//박스삭제
router.post('/:usrid/removebox', function(req, res, next) {
        connection.query('delete from collectbox where cbid=?;', [req.body.cbid], function (error, cursor) {
             console.log(error);
            if (error == undefined) {
                            res.json({
                                     result : 'true'
                                                                                                                                        });
                                }
            else {
                            res.status(503).json({
                                     result : 'false'
                                    });
                                }
                        });
                      
            });



//박스 수정
router.post('/:usrid/editbox', function(req, res, next) {
    
 connection.query("UPDATE collectbox SET cbname=? Where cbid=?;", [req.body.cbname, req.body.cbid], function(error, result) {
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


//usrid가 가지고 있는 박스 리스트 보내기
router.get('/:usrid/boxlist', function(req, res, next) {
  
   connection.query('select cbname, cbid from collectbox where usrid=?;', [req.params.usrid], function (error, cursor) {
      
      res.json(cursor);
   });
});

//cbid가 같은(동일한 collectbox공유) usrsData 보내기
router.get('/:cbid/usrlist', function(req, res, next) {
   connection.query('select usrid, usrname, usremail, usrprofile from usr where cbid=?;', [req.params.cbid], function (error, cursor) {
                       res.json({
                         "usrid":cursor[0].usrid,
                         "usrname":cursor[0].usrname,
                         "usremail":cursor[0].usremail,
                         "usrprofile":cursor[0].usrprofile});
       
   });
});

module.exports = router;
