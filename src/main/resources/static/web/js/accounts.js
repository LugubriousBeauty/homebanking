const { createApp } = Vue;

createApp({
    data() {
        return {
            client: {},
            accounts: [],
            checked: undefined
        }
    },
    created() {
        this.checked = JSON.parse(localStorage.getItem("checked"))
        this.loadData();
    },
    methods: {
        loadData() {
            axios.get('http://localhost:8080/api/clients/current')
            .then(data => {
                this.client = data.data;
                this.accounts = data.data.accounts;
                console.log(this.client)
            })
            .catch(err => console.log(err))
        },
        click() {
            axios.post('/api/logout').then(response => window.location.href = "http://localhost:8080/web/index.html")
        },
        createAccount() {
            axios.post('http://localhost:8080/api/clients/current/accounts')
                .then(data => {
                    console.log(data);
                    this.loadData();
                })
            .catch(err => console.log(err))
        }
    },
    computed: {
        saveOnLocalStorage() {
            localStorage.setItem("checked", JSON.stringify(this.checked)); 
            console.log(this.checked)
        }
    }
        
}).mount('#app')