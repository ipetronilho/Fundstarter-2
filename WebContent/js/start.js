var websocket = null;
		// sets up a connection and event handlers
	
        window.onload = function() { // URI = ws://10.16.0.165:8080/WebSocket/ws
        	connect('ws://' + window.location.host + '/ficha5Hey/ws');
            document.getElementById("myid").focus();
        }
		
        function connect(host) { // connect to the host websocket
        	writeToHistory('Trying to connect to Host = '+host);
        	if ('WebSocket' in window) {
                websocket = new WebSocket(host);
                writeToHistory('There was websock. in window.');
        	}
            else if ('MozWebSocket' in window)
                websocket = new MozWebSocket(host);
            else {
                writeToHistory('Get a real browser which supports WebSocket.');
                return;
            }
			writeToHistory('Session begun');
            websocket.onopen    = onOpen; // set the event listeners below
            websocket.onclose   = onClose;
            websocket.onmessage = onMessage;
            websocket.onerror   = onError;
            
        }
	
        function onOpen(event) {
            //writeToHistory('Connected to ' + window.location.host);
            writeToHistory('Hi!');
        }
        
        function onClose(event) {
            writeToHistory('WebSocket closed.');
            document.getElementById('myid').onkeydown = null;
        }
        
        function onMessage(message) { // print the received message
            writeToHistory(message.data);
        }
        
        function onError(event) {
            writeToHistory('WebSocket error (' + event.data + ').');
            document.getElementById('myid').onkeydown = null;
        }
        
        function doSend() {
            var message = document.getElementById('myid').value;
            if (message != '')
                websocket.send(message); // send the message
            document.getElementById('myid').value = '';
        }

        function writeToHistory(text) {
            var history = document.getElementById('history');
            var line = document.createElement('p');
            line.style.wordWrap = 'break-word';
            line.innerHTML = text;
            history.appendChild(line);
            history.scrollTop = history.scrollHeight;
        }
        