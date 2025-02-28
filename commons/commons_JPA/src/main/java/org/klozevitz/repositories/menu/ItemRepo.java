package org.klozevitz.repositories.menu;

import org.klozevitz.enitites.menu.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepo extends JpaRepository<Item, Long> {
    List<Item> findByCategoryId(long categoryId);
}
