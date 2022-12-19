const { createApp } = Vue;

createApp({
    data() {
        return {
            client: {},
            cards: [],
            checked: undefined,
            debitCards: [],
            creditCards: []
        }
    },
    created() {
        this.checked = JSON.parse(localStorage.getItem("checked"))
        axios.get('http://localhost:8080/api/clients/1')
            .then(data => {
                this.client = data.data;
                this.cards = data.data.cards;
                console.log(this.cards)
                this.debitCards = this.cards.filter((card) => card.type === "DEBIT")
                this.creditCards = this.cards.filter((card) => card.type === "CREDIT")
            })  
        .catch(err => console.log(err))
    },
    methods: {
        
    },
    computed: {
        saveOnLocalStorage() {
            localStorage.setItem("checked", JSON.stringify(this.checked)); 
            console.log(this.checked)
        }
    }
        
}).mount('#app')