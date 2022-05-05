package me.imadmasifi.contact.data.entity;

import dev.hilla.Nonnull;
import javax.persistence.Entity;
import javax.validation.constraints.Email;

@Entity
public class Contact extends AbstractEntity {

    @Nonnull
    private String name;
    @Email
    @Nonnull
    private String email;
    @Nonnull
    private String phone;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

}
