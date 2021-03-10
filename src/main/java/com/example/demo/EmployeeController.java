package com.example.demo;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeRepository repository;

    public EmployeeController(EmployeeRepository repository) {
        this.repository = repository;
    }

    @GetMapping("")
    public Iterable<Employee> listAll(){
        return this.repository.findAll();
    }

    @GetMapping("/{id}")
    public Object returnEmployee(@PathVariable Long id){
        return this.repository.existsById(id) ?
                 this.repository.findById(id) :
                "This employee does not exist";
    }

    @PostMapping("")
    public Employee insertEmployee(@RequestBody Employee employee){
        return this.repository.save(employee);
    }

    @DeleteMapping("/{id}")
    public String deleteEmployee(@PathVariable Long id){
        this.repository.deleteById(id);
        return "A user has been delete. Remaining employees: " + this.repository.count();
    }

    @PatchMapping("/{id}")
    public Employee updateEmployee(@RequestBody Employee employee, @PathVariable Long id){
        if(this.repository.existsById(id)){
            Employee oldEmployee = this.repository.findById(id).get();
            oldEmployee.setName(employee.getName());
            oldEmployee.setDate(employee.getDate());
            return this.repository.save(oldEmployee);
        }else{
            return this.repository.save(employee);
        }
    }

}
