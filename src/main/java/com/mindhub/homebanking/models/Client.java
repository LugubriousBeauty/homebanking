package com.mindhub.homebanking.models;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity //crea una tabla en la DB (en este caso de cliente)
public class Client {
    @Id //indica que la propiedad de abajito es la clave primaria (primary key). Cada objeto debe de tener una primary key UNICA
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native") //esa primary key es generada por la DB y se lo indicamos con esta notacion
    @GenericGenerator(name = "native", strategy = "native") //se usa para denotar un generador especifico (cómo se va a generar). Es una anotacion de hybernate
    private Long id;
    private String firstName, lastName, email;
    @OneToMany(mappedBy="client", fetch=FetchType.EAGER) /*especificamos la relacion entre objetos (en este caso uno a muchos). El
    mapped by sirve para aclarar con qué atributo de la otra clase tenemos asociada esta clase
    con el fetch type eager aclaramos que queremos cargar las cuentas de nuestro cliente cuando cargamos al mismo, es decir, simultaneamente*/
    private Set<Account> accounts = new HashSet<>(); //inicializar en memoria un set vacio
    public Client() { }

    public Client(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Long getId() { return id; }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Account> getAccounts() { return accounts; }

    public void addAccount(Account account) {
        account.setClient(this);
        accounts.add(account);
    }
}
