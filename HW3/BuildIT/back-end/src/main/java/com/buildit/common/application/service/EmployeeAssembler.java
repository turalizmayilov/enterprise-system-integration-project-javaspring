package com.buildit.common.application.service;

import com.buildit.common.application.dto.EmployeeDTO;
import com.buildit.common.domain.model.Employee;
import com.buildit.procurement.web.controller.PlantHireRequestController;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

@Service
public class EmployeeAssembler extends ResourceAssemblerSupport<Employee, EmployeeDTO> {

	public EmployeeAssembler() {
		super(PlantHireRequestController.class, EmployeeDTO.class);
	}

	@Override
	public EmployeeDTO toResource(Employee employee) {
		EmployeeDTO dto = createResourceWithId(employee.getId(), employee);

		dto.set_id(employee.getId());
		dto.setFirstName(employee.getFirstName());
		dto.setLastName(employee.getLastName());
		dto.setRole(employee.getRole());

		return dto;
	}

}