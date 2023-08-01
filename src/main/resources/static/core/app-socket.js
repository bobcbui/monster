import request from '../lib/request.js';

class AppSocket {
    constructor(url, openCallback, otherHeader) {
        let that = this;
        that.name = url;
        that.funcList = [];
        that.otherHeader = otherHeader;
        that.ok = false;

        this.socket = new WebSocket(url);
        this.socket.onopen = (e) => {
            that.ok = true;
            openCallback(that)
        };

        this.socket.onmessage = (e) => { that.onmessage(e); };
        this.socket.onclose = (e) => { };
        this.socket.onerror = (e) => { };
    }

    onmessage(e) {
        let data = JSON.parse(e.data);
        let func = this.funcList[data.transactionId];
        if (func != null) {
            func(data, this);
        } else {
            this.otherHeader(data, this);
        }
    }

    async send(obj, callback) {
        // 等待ok == true
        let that = this;
        if (this.ok) {
            var uuid = new Date().getTime() + Math.random().toString(36);
            that.funcList[uuid] = callback;
            obj["transactionId"] = uuid
            that.socket.send(JSON.stringify(obj))
        } else {
            setTimeout(function () {
                that.send(obj, callback);
            }, 50);
        }
    }

    close() {
        this.socket.close();
    }

    print() {
        return "AppSocket"
    }

}


export const createGroupSocket = async (account, openCallback, otherHeader) => {
    let ws = decodeWsAccount(account);
    let response = await request({
        method: 'get',
        url: '/one-token',
    })
    
    new AppSocket(
        ws + "?checkUrl=" + document.location.origin + "/check/" + response.data,
        openCallback,
        otherHeader
    );
}

export const createMemberSocket = (account, openCallback, otherHeader) => {
    createGroupSocket(account, openCallback, otherHeader);
}

export const createLocalSocket = (ws, openCallback, otherHeader) => {
    new AppSocket(ws, openCallback, otherHeader);
}
