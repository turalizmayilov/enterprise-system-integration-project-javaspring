package com.buildit.common.domain.model;

import com.buildit.procurement.domain.enums.Role;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	Long id;

	@Enumerated(EnumType.STRING)
	Role role;

	String firstName;

	String lastName;

}
