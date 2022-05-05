package me.imadmasifi.contact.data.service;

import java.util.UUID;
import me.imadmasifi.contact.data.entity.SamplePerson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SamplePersonRepository extends JpaRepository<SamplePerson, UUID> {

}