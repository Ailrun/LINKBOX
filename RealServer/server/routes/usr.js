var express = require('express');
var router = express.Router();
var mysql = require('mysql');

/*//해시 키 for password : 이건 클라쪽에서 준영이가 하겠대 패스워드 암호화.
var myHash = function myHash(key){
    var hash= crypto.createHash('sha1');
    hash.update(key);
    return hash.digest('hex');
}*/


var connection = mysql.createConnection({
    'host' : 'aws-rds-linkbox.cjfjhr6oeu3e.ap-northeast-1.rds.amazonaws.com',
    'user' : 'LINKBOX',
    'password' : 'dlrpqkfhdnflek',
    'database' : 'LINKBOX'
});

router.get('/', function(req, res, next) {
    connection.query('select * from usr ', function (error, cursor) {
        res.json(cursor);
    });
});


//회원가입
router.post('/signup', function(request, response, next){
    
    connection.query('SELECT MAX(usrid) AS max from usr;', function(error, cursor){
    connection.query('INSERT INTO usr (usrid, usrname, usremail, pass) values(?, ?, ?, ?);', [cursor[0].max+1, request.body.usrname, request.body.usremail, request.body.pass], function(error, info) {
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
                         
//로그인
router.post('/login', function(req, res, next) {
    connection.query('SELECT * FROM usr where usremail = ? and pass = ?',[req.body.usremail, req.body.pass], function(error, cursor) {
        if(error != undefined){
            res.sendStatus(503);
        }
        else{
            if(cursor[0] == 0){
                res.json({"result":0})
            }
            else{
                res.json({"result":true,
                         "usrid":cursor[0].usrid,
                         "usrname":cursor[0].usrname,
                         "usremail":cursor[0].usremail,
                          "pass":cursor[0].pass,
                         "usrprofile":cursor[0].usrprofile});
            }
        }
    });

});

// 회원탈퇴
router.post('/signdown', function(req, res, next) {
    
        connection.query('delete usrid from usr where usremail = ? and pass = ?;', [req.body.usremail, req.body.pass], function (error, cursor) {
             console.log(error);
            if (error != undefined) {
                            resp.json({
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


module.exports = router;