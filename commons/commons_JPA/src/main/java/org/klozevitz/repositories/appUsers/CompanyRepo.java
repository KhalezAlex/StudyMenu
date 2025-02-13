package org.klozevitz.repositories.appUsers;

import org.klozevitz.enitites.appUsers.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepo extends JpaRepository<Company, Long> {
}
