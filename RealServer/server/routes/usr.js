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
connection.query('select * from usr ' +
'order by timestamp desc;', function (error, cursor) {
res.json(cursor); });
});

// 회원가입
/*
router.post('/usr/signup', function(request, response, next) {

	var usrid = request.body.usrid;
	var pass = request.body.pass;
    var usrname = request.body.usrname;
    var usremail = request.body.usremail;

	var files = request.files;

	if (usrid == undefined || pass == undefined ||
	   	usrname == undefined || usremail == undefined || files.length < 1) {

		response.sendStatus(403);
	}
	else {

		connection.query('insert into usr(usrid, pass, usrname, usremail) values (?, ?, ?, ?);', [ req.body.usrid, req.body.pass, req.body.usrname, req.body.usremail ], function (error, info) {

			if (error != undefined)
				response.sendStatus(503);
			else
				response.redirect('/' + info.insertId); //리디렉트 어디로 해줘야해? 빼도되나?
		});
	}
});*/

router.post('/usr/signup', function(request, response, next){
    connection.query('insert into usr(usrid, usrname, pass, usremail) values (?, ?, ?, ?);', [req.body.usrid, req.body.usrname, req.body.pass, req.body.usremail], function (error, info) {

		if (error == null) {

			connection.query('select * from usr where usrid=?;', [info.insertId], function (error, cursor) {
				
				if (cursor.length > 0) { res.json({
					result : true,
                    urlid : cursor[0].usrid,
                    usrname : cursor[0].usrname,
                    pass : cursor[0].pass,
                    usremail : cursor[0].usremail });
			} else

			res.status(503).json({ result : false, reason : "Cannot post article" }); });

		} else

		res.status(503).json(error); });
}

/*
// 로그인
router.post('/usr/login', function(req, res, next) {
    
        connection.query('select * from usr where usremail=? and pass=?;', [req.body.usremail, req.body.pass], function (error, cursor) {

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

*/


// 회원탈퇴
/*
router.post('/usr/signdown', function(req, res, next) {
    
        connection.query('delete * from usr where usremail=?;', [req.body.usremail], function (error, cursor) {

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
*/

module.exports = router;