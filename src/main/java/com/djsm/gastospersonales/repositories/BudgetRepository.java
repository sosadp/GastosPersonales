package com.djsm.gastospersonales.repositories;

import com.djsm.gastospersonales.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface BudgetRepository extends JpaRepository<Budget,Long>{

        Budget findByBudgetDate(Date date);
}
