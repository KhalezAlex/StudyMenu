package org.klozevitz.repositories.menu;

import org.klozevitz.enitites.menu.TestQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestQuestionRepo extends JpaRepository<TestQuestion, Long> {
}
