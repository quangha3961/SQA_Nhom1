package beebooks.service;

import beebooks.entities.Contact;
import beebooks.repository.ContactRepository;
import org.springframework.stereotype.Service;

@Service
public class ContactServiceImpl implements IContactService{
    private final ContactRepository contactRepository;

    public ContactServiceImpl(ContactRepository contactRepository, ContactService contactService) {
        this.contactRepository = contactRepository;
        this.contactService = contactService;
    }

    private final ContactService contactService;

    @Override
    public boolean deleteContact(Integer id) {
        if(id>=1){
            Contact contact = contactService.getById(id);
            if(contact!=null){
                contactRepository.delete(contact);
                return true;
            }
        }
        return false;
    }
}
