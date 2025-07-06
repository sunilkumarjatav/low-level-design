package repository;

import entities.Patron;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PatronRepository implements IPatronRepository {

    private Map<String, Patron> patrons = new HashMap<>(); // {patronId: Patron_object}
    private Map<String, String> usernameToPatronId = new HashMap<>(); // {username: patronId}

    @Override
    public void addPatron(Patron patron) {
        patrons.put(patron.getPatronId(), patron);
        usernameToPatronId.put(patron.getUsername(), patron.getPatronId());
    }

    @Override
    public Optional<Patron> getPatronById(String patronId) {
        return Optional.ofNullable(patrons.get(patronId));
    }

    @Override
    public Optional<Patron> getPatronByUsername(String username) {
        String patronId = usernameToPatronId.get(username);
        return Optional.ofNullable(patrons.get(patronId));
    }

    @Override
    public boolean updatePatron(Patron patron) {
        if (patrons.containsKey(patron.getPatronId())) {
            patrons.put(patron.getPatronId(), patron);
            // In case username changed, update the lookup map
            usernameToPatronId.put(patron.getUsername(), patron.getPatronId());
            return true;
        }
        return false;
    }
}
