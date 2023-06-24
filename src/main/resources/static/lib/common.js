

function decodeAccount(account) {
    let decode = window.atob(account)
    console.log(decode)
    let split = decode.split("$")
    let version = split[0];
    let host = split[1];
    let member = split[2];
    let ws = "";
    if(version == "v1"){
        ws += "ws://"+host+"/"+member;
    }
    if(version == "v2"){
        ws += "wss://"+host+"/"+member;
    }
    return ws;
}

function down(id){
    try{
        debugger
        var div = document.getElementById("show_words_"+id);
        div.scrollTop = div.scrollHeight;
    }catch(error){
        console.log(error)
        console.log("skldjfkladjsfkljasdkfjkdjfkdjfkd")
    }
}