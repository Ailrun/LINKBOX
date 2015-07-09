var express = require('express');
var router = express.Router();
var mysql = require('mysql');

var connection = mysql.createConnection({

    'host' : 'aws-rds-linkbox.cjfjhr6oeu3e.ap-northeast-1.rds.amazonaws.com',
    'user' : 'LINKBOX',  
    'password' : 'dlrpqkfhdnflek',
    'database' : 'LINKBOX'
});


//박스 추가
router.post('/collectbox/{usrid}/addbox', function(request, response, next) {

 connection.query('insert into collectbox(cbname) values (?);', [ request.body.cbname ], function (error, result) {
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



//박스삭제
router.post('/collectbox/{usrid}/removebox', function(req, res, next) {
    
        connection.query('delete * from collectbox where cbname;', [req.body.cbname], function (error, cursor) {

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



//박스 수정
router.post('/collectbox/{usrid}/editbox', function(req, res, next) {
    
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

//박스 리스트 보내기
router.get('/collectbox/{usrid}/boxlist', function(req, res, next) {
  
   connection.query('select cbname, cbid from collectbox order by timestamp desc;', function (error, cursor) {
      
      res.json(cursor);
   });
});

module.exports = router;