package com.djsm.gastospersonales.controller;


import com.djsm.gastospersonales.model.Budget;
import com.djsm.gastospersonales.repositories.BudgetRepository;
import com.djsm.gastospersonales.service.BudgetService;
import com.djsm.gastospersonales.util.CustomErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class BudgetApiController {

    public static Logger LOGGER = LoggerFactory.getLogger(BudgetApiController.class);

    @Autowired
    BudgetService budgetService;

    @RequestMapping(value = "/budget",method = RequestMethod.GET)
    public ResponseEntity<List<Budget>> listAllBudget(){
        LOGGER.info("Estoy AQUIIIIII......");
        List<Budget> budgets = budgetService.findAllBudget();

        if (budgets.isEmpty()){
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<List<Budget>>(budgets, HttpStatus.OK);

    }

    @RequestMapping(value = "/budget/{id}",method = RequestMethod.GET)
    public ResponseEntity<?> getBudget(@PathVariable("id") Long id){

        Budget budget= budgetService.findById(id);

        if (budget==null){
            LOGGER.error("No hay presupuesto con el id  {}",id);
            return new ResponseEntity(new CustomErrorType("No hay presupuesto con el id "+id),HttpStatus.NO_CONTENT);

        }

        return new ResponseEntity<Budget>(budget,HttpStatus.OK);

    }

    @RequestMapping(value = "/budget/",method = RequestMethod.POST)
    public ResponseEntity<?> createBudget(@RequestBody Budget budget, UriComponentsBuilder ucBuilder){

        LOGGER.info("Creando nuevo presupuesto {} ",budget.getId());

        Budget currentBudget = new Budget();

        if(budgetService.isExistBudget(budget)){

            LOGGER.error("Ya existe un presupuesto con el nombre {} ",budget.getName());

            return new ResponseEntity(new CustomErrorType("No se puede crear el presupuesto "+budget.getName()),HttpStatus.CONFLICT);

        }

        budgetService.saveBudget(budget);

        HttpHeaders headers= new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/budget/{id}").buildAndExpand(budget.getId()).toUri());
        return new ResponseEntity<String>(headers,HttpStatus.OK);
    }


    @RequestMapping(value = "/budget/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateBudget(@PathVariable("id") long id, @RequestBody Budget budget){
        LOGGER.info("Se actualizara el budget {}",id);

        Budget currentBudget=budgetService.findById(id);

        if (currentBudget==null){

            LOGGER.error("No se encontro el budget {}", id);

            return  new ResponseEntity(new CustomErrorType("No se puede actualizar Budget "+id),HttpStatus.NOT_FOUND);
        }

        currentBudget.setName(budget.getName());

        currentBudget.setBudgetDate(budget.getBudgetDate());

        budgetService.updateBudget(currentBudget);

        return new ResponseEntity<Budget>(currentBudget,HttpStatus.OK);

    }

    @RequestMapping(value = "/budget/{id}",method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteBudget(@PathVariable("id") long id){
        LOGGER.info("Se buscará y elmininará el Budget {}",id);
        Budget budget= budgetService.findById(id);

        if (budget==null){

            LOGGER.error("El budget {}",id,"  no se encuentra ");
            return new ResponseEntity(new CustomErrorType("No se puede encontrar "+id),HttpStatus.NOT_FOUND);

        }

        budgetService.deleteById(id);

        return new ResponseEntity<Budget>(HttpStatus.NO_CONTENT);



    }





}
