const {createApp} = Vue;

let url = window.location.search;
let myurl = new URLSearchParams(url)
let finalUrl = myurl.get("token")


createApp({
    created() {
        axios.post('http://localhost:8080/api/confirm-account', `token=${finalUrl}`) 
    }
}).mount('#app')