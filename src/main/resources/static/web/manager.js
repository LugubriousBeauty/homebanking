const { createApp } = Vue;

createApp({
    data() {
        return {
            client: {},
            accounts: []
        }
    },
    created() {
        axios.get('http://localhost:8080/api/clients/1')
            .then(data => {
                this.client = data.data;
                this.accounts = data.data.accounts;
                console.log(this.client)
            })
        .catch(err => console.log(err))
    },
    methods: {

    }
}).mount('#app')