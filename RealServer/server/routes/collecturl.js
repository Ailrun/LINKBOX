var express = require('express');
var mysql = require('mysql');
var router = express.Router();

var connection = mysql.createConnection({

    'host' : 'aws-rds-linkbox.cjfjhr6oeu3e.ap-northeast-1.rds.amazonaws.com',
    'user' : 'LINKBOX',  
    'password' : 'dlrpqkfhdnflek',
    'database' : 'LINKBOX'
});


//링크 추가
router.post('/collecturl/{cbid}/addurl', function(request, response, next) {

 connection.query('insert into collecturl(urlname, address, urlthumbnail) values (?, ?, ?, ?);', [ request.body.urlname, request.body.address, request.body.urlthumbnail ], function (error, result) {
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
};



//링크삭제
router.post('/collecturl/{cbid}/removeurl', function(req, res, next) {
    
        connection.query('delete * from collecturl where urlname;', [req.body.urlname], function (error, cursor) {

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
    
 connection.query("UPDATE collecturl WHERE urlname=;", [req.body.urlname], function(error, result) {
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
  
   connection.query('select urlname, urlid, address, ulrthumbnail from collecturl order by timestamp desc;', function (error, cursor) {
      
      res.json(cursor);
   });
});

module.exports = router;