export default class MySocket {
    constructor (url, openCallback, otherHeader) {
        this.name = url;
        this.messageList = [];
        this.otherHeader = otherHeader;
        let that = this;
        let socket = new WebSocket(url);
        socket.onopen = function(e){
            openCallback(that)
        };
        socket.onmessage = function(e){
            that.onmessage(e)
        };
        socket.onclose = function(e){
            
        };
        socket.onerror = function(e){
            
        };

        this.socket = socket;
    }

    onmessage(e){
        let data = JSON.parse(e.data);
        let msg = this.messageList[data.transactionId];
        if(msg != null) {
            msg(data, this);
        }else{
            this.otherHeader(data, this);
        }
    }

    send(obj, callback) {
        var uuid = new Date().getTime() + Math.random().toString(36);
        this.messageList[uuid] = callback;
        obj["transactionId"] = uuid
        debugger
        this.socket.send(JSON.stringify(obj))
    }
}