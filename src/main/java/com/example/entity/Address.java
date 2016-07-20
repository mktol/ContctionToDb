package com.example.entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

@Entity
@Table(name = "ADDRESS")
//@Access(value= AccessType.FIELD)
public class Address {

    @Id
    @Column(name = "emp_id", unique = true, nullable = false)
    @GeneratedValue(strategy= GenerationType.IDENTITY)
/*    @GeneratedValue(generator = "gen")
    @GenericGenerator(name = "gen", strategy = "foreign", parameters = { @Parameter(name = "property", value = "name") })*/
    private Long id;

    @Column(name = "address_line1")
    private String addressLine1;

    @Column(name = "zipcode")
    private String zipcode;

    @Column(name = "city")
    private String city;

    @ManyToOne(fetch=FetchType.LAZY)
    //    @PrimaryKeyJoinColumn
    private User user;

    public Address() {
    }

    public Address(String addressLine1, String zipcode, String city, User user) {
        this.addressLine1 = addressLine1;
        this.zipcode = zipcode;
        this.city = city;
        this.user = user;
    }

    public Address(String addres, String zipcode, String city) {
        this.addressLine1 = addres;
        this.zipcode = zipcode;
        this.city = city;
    }
     public void addUser(User user){
         this.user = user;
         if(!user.getAddresses().contains(this)){
             user.addAddress(this);
         }
     }

    //Getter setter methods

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "AddressLine1= " + addressLine1 + ", City=" + city
                + ", Zipcode=" + zipcode;
    }
}