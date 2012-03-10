var http = require('http'),
    util = require('util');
  
var port = 13370

http.createServer(function (req, res) {
  //console.log(util.inspect(req));
  if (req.url.indexOf('/download') == 0) {
    req.url.replace('/download', '/');
  }
  console.log(req.url);
  res.writeHead(200, {'Content-Type': 'text/plain'});
    res.end('Hello World\n');
    }).listen(port);
console.log('Server running locally at port: ' + port);
