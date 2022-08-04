package br.com.alura.challenge.finance.repository;

import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.alura.challenge.finance.model.Expenditure;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class ExpenditureRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    ExpenditureRepository repository;
    
    @Test
    @DisplayName("Should persist entity.")
    public void successWhenPersistEntity() {

        int sizeExpected = 1;

        Expenditure entity = new Expenditure("Test");
        entityManager.persist(entity);
        
        List<Expenditure> all = repository.findAll();
        
        assertSame(sizeExpected, all.size());

    }

}
