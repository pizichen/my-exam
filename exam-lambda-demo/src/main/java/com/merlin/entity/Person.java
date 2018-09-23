package com.merlin.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * 人口模型
 */
@Data
public class Person implements Serializable {
	
	private static final long serialVersionUID = 432377340643264749L;
	
	private String firstName, lastName, job, gender;  
	private int salary, age;  
	  
	public Person(String firstName, String lastName, String job,  
	                String gender, int age, int salary)       {  
	          this.firstName = firstName;  
	          this.lastName = lastName;  
	          this.gender = gender;  
	          this.age = age;  
	          this.job = job;  
	          this.salary = salary;  
	}  
}
