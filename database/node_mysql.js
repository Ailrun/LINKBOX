var fs = require('fs');

var ejs = require('ejs');
var mysql = require('mysql');
var body_parser = require('body-parser');

var Connect = require('connect');
var Router = require('router');

var connection = mysql.createConnection({
   
    user : 'LINKBOX',              // appjam mysql server id
    host : 'aws-rds-linkbox.cjfjhr6oeu3e.ap-northeast-1.rds.amazonaws.com',
    password : 'dlrpqkfhdnflek',   // appjam mysql server password
    database : 'LINKBOX'           // appjam mysql default database
});

var connect = new Connect();
var router = new Router();

connect.use(body_parser.urlencoded({ 'extended' : true }));

router.get('/', function (request, response) {

    fs.readFile('list.ejs', 'utf8', function (error, data) {
        
        if (!error) {
            
            connection.query('select * from url;', function (error, query) {
                        // candidate table select query 실행
                response.writeHead(200, { 'Content-Type' : 'text/html; charset=utf-8;' });
                response.end(ejs.render(data, { 'query' : query }));
            });
        }
        else {
         
            response.writeHead(503, { 'Content-Type' : 'text/html;' });
            response.end();
        }
    });
});


router.get('/insert', function (request, response) {

    fs.readFile('insert.html', 'utf8', function (error, data) {
        
        if (!error) {
            
            response.writeHead(200, { 'Content-Type' : 'text/html' });
            response.end(data);
        }
        else {
         
            response.writeHead(503, { 'Content-Type' : 'text/html;' });
            response.end();
        }
    });
});

router.post('/insert', function (request, response) {
                    // /insert query문 실행 candiate table에 name, part, introduce vlaue에 ? ? ? 대입
    connection.query('insert into url (url, urlname) values (?, ?);',
                  [ request.body.name, request.body.part, request.body.introduce ]);
    
    response.writeHead(302, { 'Location' : '/' });
    response.end();
});


router.get('/update/:id', function (request, response) {

   fs.readFile('update.ejs', 'utf8', function (error, data) {
       
       connection.query('select * from url where id = ?;', [ request.params.id ], 
                        // 요청받은 id에 대한 candidate table에서 select query문 실행
                        function (error, query) {
           
                            if (!error && query.length > 0) {
                            
                                response.writeHead(200, { 'Content-Type' : 'text/html' });
                                response.end(ejs.render(data, { 'item' : query[0] }));
                            }
                            else {
                            
                                response.writeHead(404, { 'Content-Type' : 'text/html' });
                                response.end();
                            }
                        });
   });
});

router.post('/update/:id', function (request, response) {
                        // candidate update query문 실행, 요청받은 id에 대해서 name, part, introduce update
    connection.query('update url set url=?, urlname=? where id=?;',
                     [ request.body.name, request.body.part, request.body.introduce, request.params.id ]);
    
    response.writeHead(302, { 'Location' : '/' });
    response.end();
});


router.get('/delete/:id', function (request, response) {
                    // 요청받은 id에 대해 candidate table내 delete query문 실행
    connection.query('delete from url where id=?;', [ request .params.id ]);
    
    response.writeHead(302, { 'Location' : '/' });
    response.end();
});


connect.use(router);
connect.listen(8080, function () {

    console.log("Server running on port 8080 :)");
});