const { createApp } = Vue;

createApp({
	data () {
		return {
			firstName: '',
			lastName: '',
			email: '',
			password: ''
		}
	},
	created() {
		console.log('hola')
	},
	methods: {
		login() {
			console.log(this.email)
			console.log(this.password)
			axios.post('/api/login',`email=${this.email}&password=${this.password}`)
				.then(response => window.location.href = "http://localhost:8080/web/accounts.html")
			.catch(err => {
				Swal.fire({
					icon: 'error'
				  })
			})
		},
		signup() {
			if(!this.firstName || !this.lastName || !this.email || !this.password) {
				console.log('error')
			} else {
				axios.post('/api/clients', `firstName=${this.firstName}&lastName=${this.lastName}&email=${this.email}&password=${this.password}`)
					.then(response => this.login())
				.catch(err => console.log(err))
			}
		}
	}
}).mount('#app')






const signUpButton = document.getElementById('signUp');
const signInButton = document.getElementById('signIn');
const signUpButtonSm = document.getElementById('signupbtn-sm');
const signInButtonSm = document.getElementById('signinbtn-sm');
const container = document.getElementById('container');
const signInContainer = document.getElementsByClassName('sign-in-container-sm');
const signUpContainer = document.getElementsByClassName('sign-up-container-sm');

signUpButton.addEventListener('click', () => {
	container.classList.add("right-panel-active");
});

signInButton.addEventListener('click', () => {
	container.classList.remove("right-panel-active");
});

signUpButtonSm.addEventListener('click', (e) => {
	e.preventDefault();
	signInContainer[0].classList.add('d-none'); 
	signUpContainer[0].classList.remove('d-none');
	signUpContainer[0].classList.add('d-flex')
});

signInButtonSm.addEventListener('click', (e) => {
	e.preventDefault();
	signUpContainer[0].classList.add('d-none'); 
	signInContainer[0].classList.remove('d-none');
	signInContainer[0].classList.add('d-flex')
});

