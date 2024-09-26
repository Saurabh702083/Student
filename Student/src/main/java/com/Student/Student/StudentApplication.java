package com.Student.Student;

import com.Student.Student.Reprositry.StudentRepository;
import com.Student.Student.entities.Address;
import com.Student.Student.entities.Laptop;
import com.Student.Student.entities.Student;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


@SpringBootApplication
public class StudentApplication implements CommandLineRunner {
	@Autowired
	private StudentRepository studentRepository;
	//private Logger logger= LoggerFactory.getLogger(LearnSpringOrmApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(StudentApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
//		Student student=new Student();
//		student.setStudentName("Saurabh mahto");
//		student.setAbout("i am eng.");
//		student.setStudentId(10);
//
//		Laptop laptop=new Laptop();
//		laptop.setModelNumber("14789");
//		laptop.setBrand("Dell");
//		laptop.setLaptopId(123);
//		laptop.setStudent(student);
//		student.setLaptop(laptop);
//		Student save = studentRepository.save(student);

		Student student=new Student();
		student.setStudentName("ravi ");
		student.setAbout("i am eng.");
		student.setStudentId(11);

		Address a1= new Address();
		a1.setAddressId(131);
		a1.setStreet("1/115");
		a1.setCity("brc");
		a1.setCountry("IND");
		a1.setStudent(student);

		Address a2= new Address();
		a2.setAddressId(132);
		a2.setStreet("1/116");
		a2.setCity("MFP");
		a2.setCountry("IND");
		a2.setStudent(student);

		List<Address> addressList = new ArrayList<>();
		addressList.add(a1);
		addressList.add(a2);
		
		student.setAddressList(addressList);

		Student save = studentRepository.save(student);



	}

	}
