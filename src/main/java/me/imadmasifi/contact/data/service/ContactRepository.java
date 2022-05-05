package me.imadmasifi.contact.data.service;

import java.util.UUID;
import me.imadmasifi.contact.data.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, UUID> {

}