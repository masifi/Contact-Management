package me.imadmasifi.contact.views.contacts;

import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import java.util.Optional;
import java.util.UUID;
import me.imadmasifi.contact.data.entity.Contact;
import me.imadmasifi.contact.data.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

@PageTitle("Contacts")
@Route(value = "contacts/:contactID?/:action?(edit)")
@Tag("contacts-view")
@JsModule("./views/contacts/contacts-view.ts")
public class ContactsView extends LitTemplate implements HasStyle, BeforeEnterObserver {

    private final String CONTACT_ID = "contactID";
    private final String CONTACT_EDIT_ROUTE_TEMPLATE = "contacts/%s/edit";

    // This is the Java companion file of a design
    // You can find the design file inside /frontend/views/
    // The design can be easily edited by using Vaadin Designer
    // (vaadin.com/designer)

    @Id
    private Grid<Contact> grid;

    @Id
    private TextField name;
    @Id
    private TextField email;
    @Id
    private TextField phone;

    @Id
    private Button cancel;
    @Id
    private Button save;

    private BeanValidationBinder<Contact> binder;

    private Contact contact;

    private final ContactService contactService;

    @Autowired
    public ContactsView(ContactService contactService) {
        this.contactService = contactService;
        addClassNames("contacts-view");
        grid.addColumn(Contact::getName).setHeader("Name").setAutoWidth(true);
        grid.addColumn(Contact::getEmail).setHeader("Email").setAutoWidth(true);
        grid.addColumn(Contact::getPhone).setHeader("Phone").setAutoWidth(true);
        grid.setItems(query -> contactService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(CONTACT_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(ContactsView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Contact.class);

        // Bind fields. This is where you'd define e.g. validation rules

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.contact == null) {
                    this.contact = new Contact();
                }
                binder.writeBean(this.contact);

                contactService.update(this.contact);
                clearForm();
                refreshGrid();
                Notification.show("Contact details stored.");
                UI.getCurrent().navigate(ContactsView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the contact details.");
            }
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<UUID> contactId = event.getRouteParameters().get(CONTACT_ID).map(UUID::fromString);
        if (contactId.isPresent()) {
            Optional<Contact> contactFromBackend = contactService.get(contactId.get());
            if (contactFromBackend.isPresent()) {
                populateForm(contactFromBackend.get());
            } else {
                Notification.show(String.format("The requested contact was not found, ID = %s", contactId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(ContactsView.class);
            }
        }
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getLazyDataView().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Contact value) {
        this.contact = value;
        binder.readBean(this.contact);

    }
}
