package br.com.darlan.tasks.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Address {

    @JsonProperty("cep")
    public String zipcode;
    @JsonProperty("logradouro")
    public String street;
    @JsonProperty("complemento")
    public String complement;
    @JsonProperty("bairro")
    public String neighborhood;
    @JsonProperty("localidade")
    public String city;
    @JsonProperty("uf")
    public String state;

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
