package me.imadmasifi.contact.data.endpoint;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.Endpoint;
import dev.hilla.Nonnull;
import java.util.Optional;
import java.util.UUID;
import me.imadmasifi.contact.data.entity.Contact;
import me.imadmasifi.contact.data.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Endpoint
@AnonymousAllowed
public class ContactEndpoint {

    private final ContactService service;

    @Autowired
    public ContactEndpoint(ContactService service) {
        this.service = service;
    }

    @Nonnull
    public Page<@Nonnull Contact> list(Pageable page) {
        return service.list(page);
    }

    public Optional<Contact> get(@Nonnull UUID id) {
        return service.get(id);
    }

    @Nonnull
    public Contact update(@Nonnull Contact entity) {
        return service.update(entity);
    }

    public void delete(@Nonnull UUID id) {
        service.delete(id);
    }

    public int count() {
        return service.count();
    }

}
