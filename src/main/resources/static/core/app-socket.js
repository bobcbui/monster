import request from '../lib/request.js';

class AppSocket {
    constructor(url, openCallback, otherHeader) {
        let that = this;
        that.name = url;
        that.messageList = [];
        that.otherHeader = otherHeader;

        let socket = new WebSocket(url);
        socket.onopen = (e) => {
            openCallback(that)
        };
        socket.onmessage = (e) => {
            that.onmessage(e)
        };
        socket.onclose = (e) => {

        };
        socket.onerror = (e) => {

        };

        that.socket = socket;
    }

    onmessage(e) {
        let data = JSON.parse(e.data);
        let msg = this.messageList[data.transactionId];
        if (msg != null) {
            msg(data, this);
        } else {
            this.otherHeader(data, this);
        }
    }

    send(obj, callback) {
        var uuid = new Date().getTime() + Math.random().toString(36);
        this.messageList[uuid] = callback;
        obj["transactionId"] = uuid
        this.socket.send(JSON.stringify(obj))
    }

    close() {
        this.socket.close();
    }

    print() {
        return "AppSocket"
    }

}


export const createGroupSocket = (account, openCallback, otherHeader) => {
    let ws = decodeWsAccount(account);
    request({
        method: 'get',
        url: '/one-token',
    }).then(response => {
        new AppSocket(
            ws + "?checkUrl=" + document.location.origin + "/check/" + response.data,
            openCallback,
            otherHeader
        );
    });
}

export const createMemberSocket = (account, openCallback, otherHeader) => {
    createGroupSocket(account, openCallback, otherHeader);
}

export const createLocalSocket = (ws, openCallback, otherHeader) => {
    new AppSocket(ws, openCallback, otherHeader);
}
