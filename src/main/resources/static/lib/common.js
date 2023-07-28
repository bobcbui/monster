

let decodeWsAccount = (account) => {
    let s = account.split("@");
    let name = s[1].replaceAll(".","/");
    let ym = s[2]
    return "ws://"+ym+"/"+name;
}

let down = (id) => {
    try{
        var div = document.getElementById("show_words_"+id);
        div.scrollTop = div.scrollHeight;
    }catch(error){
        console.log("skldjfkladjsfkljasdkfjkdjfkdjfkd")
    }
}