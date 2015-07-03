var express = require('express');
var mysql = require('mysql');

var router = express.Router();
var connection = mysql.createConnection({       //mysql 연결 생성

	'host' : 'aws-rds-linkbox.cjfjhr6oeu3e.ap-northeast-1.rds.amazonaws.com',
	'user' : 'LINKBOX',
	'password' : 'dlrpqkfhdnflek',
	'database' : 'linkbox',
});

router.get('/:urlid', function(req, res, next) {       // /:content_id get 오면 
  
	connection.query('select * from url where id=?;', [req.params.urlid], function (error, cursor) {
                                                            // 위 query문 실행
                if (cursor.length > 0) {

                        res.json(cursor[0]);                // 길이가 0보다 크면 json파일 리턴
                }
                else {

                        res.status(503).json({
						
							result : false,
							reason : "Cannot find selected article"
						});
                }
        });
});

router.post('/', function(req, res, next) {                   // / post오면 

        connection.query('insert into url(name, address) values (?, ?);', [req.body.url, req.body.url], function (error, info) {                                               // 위의 query 문인 title, content 에 내용채움

                if (error == null) {

                        connection.query('select * from url where id=?;', [info.insertId], function (error, cursor) {
                                                                // error가 아니면 query 문 수행
                                if (cursor.length > 0) {

                                        var result = cursor[0];

                                        res.json({

												result : true,      // 값 post
                                                urlid : result.urlid,
                                                url : result.url,
                                                address : result.address,
                                        });
                                }
                                else {

                                        res.status(503).json({
						
											result : false,
											reason : "Cannot post article"
										});
                                }
                        });
                }
                else {

                        res.status(503).json(error);
                }
        });
});

module.exports = router;