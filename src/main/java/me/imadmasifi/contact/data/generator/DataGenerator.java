package me.imadmasifi.contact.data.generator;

import com.vaadin.exampledata.DataType;
import com.vaadin.exampledata.ExampleDataGenerator;
import com.vaadin.flow.spring.annotation.SpringComponent;
import java.time.LocalDateTime;
import me.imadmasifi.contact.data.entity.Contact;
import me.imadmasifi.contact.data.entity.SamplePerson;
import me.imadmasifi.contact.data.service.ContactRepository;
import me.imadmasifi.contact.data.service.SamplePersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@SpringComponent
public class DataGenerator {

    @Bean
    public CommandLineRunner loadData(SamplePersonRepository samplePersonRepository,
            ContactRepository contactRepository) {
        return args -> {
            Logger logger = LoggerFactory.getLogger(getClass());
            if (samplePersonRepository.count() != 0L) {
                logger.info("Using existing database");
                return;
            }
            int seed = 123;

            logger.info("Generating demo data");

            logger.info("... generating 100 Sample Person entities...");
            ExampleDataGenerator<SamplePerson> samplePersonRepositoryGenerator = new ExampleDataGenerator<>(
                    SamplePerson.class, LocalDateTime.of(2022, 5, 5, 0, 0, 0));
            samplePersonRepositoryGenerator.setData(SamplePerson::setFirstName, DataType.FIRST_NAME);
            samplePersonRepositoryGenerator.setData(SamplePerson::setLastName, DataType.LAST_NAME);
            samplePersonRepositoryGenerator.setData(SamplePerson::setEmail, DataType.EMAIL);
            samplePersonRepositoryGenerator.setData(SamplePerson::setPhone, DataType.PHONE_NUMBER);
            samplePersonRepositoryGenerator.setData(SamplePerson::setDateOfBirth, DataType.DATE_OF_BIRTH);
            samplePersonRepositoryGenerator.setData(SamplePerson::setOccupation, DataType.OCCUPATION);
            samplePersonRepositoryGenerator.setData(SamplePerson::setImportant, DataType.BOOLEAN_10_90);
            samplePersonRepository.saveAll(samplePersonRepositoryGenerator.create(100, seed));

            logger.info("... generating 100 Contact entities...");
            ExampleDataGenerator<Contact> contactRepositoryGenerator = new ExampleDataGenerator<>(Contact.class,
                    LocalDateTime.of(2022, 5, 5, 0, 0, 0));
            contactRepositoryGenerator.setData(Contact::setName, DataType.WORD);
            contactRepositoryGenerator.setData(Contact::setEmail, DataType.EMAIL);
            contactRepositoryGenerator.setData(Contact::setPhone, DataType.PHONE_NUMBER);
            contactRepository.saveAll(contactRepositoryGenerator.create(100, seed));

            logger.info("Generated demo data");
        };
    }

}