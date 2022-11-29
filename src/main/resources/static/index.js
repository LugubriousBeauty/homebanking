const { createApp } = Vue;

createApp({
    data() {
        return {
            data: {},
            clients: [],
            firstName: '',
            lastName: '',
            email: '',
            client: {}
        }
    },
    created() {
        this.loadData();
    },
    methods: {
        loadData() {
            axios.get('http://localhost:8080/clients')
                .then(response => {
                    console.log(response)
                    this.data = response.data;
                    this.clients = response.data._embedded.clients;
                })
                .catch(error => console.log(error))
        },
        addClient() {
            if (this.email.toLowerCase().includes('.com')) {
                this.client = {
                    firstName: this.firstName, lastName: this.lastName, email: this.email
                }
                this.postClient(this.client);
            }
        },
        deleteClient(client) {
            let idIndex = client._links.client.href.lastIndexOf("/");
            console.log(client._links.client.href);
            console.log(idIndex);
            let id = client._links.client.href.substring(idIndex + 1);
            console.log(id)
            axios.delete(`http://localhost:8080/clients/${id}`)
                .then(data => this.loadData())
        },
        postClient(client) {
            console.log(this.client)
            axios.post('http://localhost:8080/clients', client)
                .then(data => this.loadData())
        },
        async modifyClient(client) {
            const { value: formValues } = await Swal.fire({
                title: 'Modify client information',
                html:
                    `<input id="swal-input1" class="swal2-input" value="${client.firstName}">` +
                    `<input id="swal-input2" class="swal2-input" value="${client.lastName}">` +
                    `<input id="swal-input3" class="swal2-input" value="${client.email}">`,
                focusConfirm: false,
                preConfirm: () => {
                  return [
                        document.getElementById('swal-input1').value,
                        document.getElementById('swal-input2').value,
                        document.getElementById('swal-input3').value   
                    ]
                }
              })
              
            if (formValues[0] && formValues[1] && formValues[2].includes('@') && formValues[2].includes('.')) {
                let idIndex = client._links.client.href.lastIndexOf("/");
                let id = client._links.client.href.substring(idIndex + 1);
                client = {firstName: formValues[0], lastName: formValues[1], email: formValues[2]}
                Swal.fire(
                    'The client has been modified succesfully',
                    '',
                    'success'
                )
                axios.put(`http://localhost:8080/clients/${id}`, client)
                    .then(response => this.loadData())
            } else if (!formValues[0] || !formValues[1] || !formValues[2]) {
                Swal.fire({
                    icon: 'error',
                    title: "Error",
                    text: 'Input values must not be empty'
                })
            } else {
                Swal.fire({
                    icon: 'error',
                    title: "Error",
                    text: 'email must contain @ and .'
                })
            }
        },
        async modifyProperty(client, property, value) {
            const { value: formValues } = await Swal.fire({
                title: property,
                html:
                  `<input id="swal-input1" class="swal2-input" value="${value}">`,
                focusConfirm: false,
                preConfirm: () => {
                  return [
                    document.getElementById('swal-input1').value
                  ]
                }
            })
            let idIndex = client._links.client.href.lastIndexOf("/");
                let id = client._links.client.href.substring(idIndex + 1);
                let aux = {};
            const update = () => {
                axios.patch(`http://localhost:8080/clients/${id}`, aux)
                    .then(response => this.loadData())
                    .catch(err => console.log(err))
            }
            if (formValues) {
                switch (property) {
                    case 'First name':
                        aux['firstName'] = formValues[0];
                        update();
                        break;
                    case 'Last name':
                        aux['lastName'] = formValues[0];
                        update();
                        break;
                    case 'Email':
                        if (formValues[0].includes('.') && formValues[0].includes('@')) {
                            aux['email'] = formValues[0];
                            update();
                        } else {
                            Swal.fire({
                                icon: 'error',
                                title: "Error",
                                text: 'email must contain @ and .'
                            })
                        }
                        break;
                }
                      
            }
            
        }
    }
}).mount('#app')