exports.get = function(req, res) {
    console.log(req.param("id"))};
    res.render('insert');
};

exprorts.post = function(req, res) {
    console.log(req.body);
    res.render('insert');
}