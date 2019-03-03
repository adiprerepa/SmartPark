"use strict";
const express = require('express');
const bodyParser = require('body-parser');

const app = express();

// Run server to listen on port 55555.
const server = app.listen(55555, () => {
  console.log('listening on *:55551');
});

const io = require('socket.io')(server);

app.use(bodyParser.urlencoded({ extended: false } ));
app.use(express.static('static'));

// Set socket.io listeners.
io.on('connection', (socket) => {
  console.log('a client connected');
  document.getElementById('status').innerHTML = "Connection Opened";

  socket.on('disconnect', () => {
    console.log('client disconnected');
    document.getElementById('status').innerHTML = "Connection Closed";
  });
});
