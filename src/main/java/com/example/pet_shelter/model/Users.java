package com.example.pet_shelter.model;

import javax.persistence.*;

@Entity
@Table(name = "Users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;                 // Идентификатор

    @Column(name = "first_name")
    private String firstName;       // Имя пользователя

    @Column(name = "last_name")
    private String lastName;        //Фамилия пользователя

    @Column(name = "user_phone_number")
    private String userPhoneNumber; // Телефон пользователя

    @Column(name = "user_email")
    private String userEmail;        // e-mail пользователя

    @ManyToOne
    @JoinColumn(name = "dog_id")
    private Dogs dog;

    @ManyToOne
    @JoinColumn(name = "cat_id")
    private Cats cat;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Dogs getDog() {
        return dog;
    }

    public void setDog(Dogs dog) {
        this.dog = dog;
    }

    public Cats getCat() {
        return cat;
    }

    public void setCat(Cats cat) {
        this.cat = cat;
    }
}
