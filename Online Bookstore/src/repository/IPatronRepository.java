package repository;

import entities.Patron;
import java.util.Optional;

public interface IPatronRepository {
    void addPatron(Patron patron);
    Optional<Patron> getPatronById(String patronId);
    Optional<Patron> getPatronByUsername(String username);
    boolean updatePatron(Patron patron);
}
