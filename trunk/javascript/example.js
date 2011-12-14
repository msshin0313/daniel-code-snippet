var http = require('http');
var url = "resnick1.si.umich.edu"
var port = 13370
http.createServer(function (req, res) {
  res.writeHead(200, {'Content-Type': 'text/plain'});
    res.end('Hello World\n');
    }).listen(port, url);
console.log('Server running at ' + url + ' with port: ' + port);
