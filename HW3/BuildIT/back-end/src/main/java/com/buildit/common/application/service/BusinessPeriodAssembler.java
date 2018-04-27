package com.buildit.common.application.service;

import com.buildit.common.application.dto.BusinessPeriodDTO;
import com.buildit.common.domain.model.BusinessPeriod;
import com.buildit.procurement.application.dto.CommentDTO;
import com.buildit.procurement.domain.model.Comment;
import com.buildit.procurement.web.controller.PlantHireRestController;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

@Service
public class BusinessPeriodAssembler extends ResourceAssemblerSupport<BusinessPeriod, BusinessPeriodDTO> {

    public BusinessPeriodAssembler() {
        super(PlantHireRestController.class, BusinessPeriodDTO.class);
    }

    @Override
    public BusinessPeriodDTO toResource(BusinessPeriod period) {
        BusinessPeriodDTO dto = BusinessPeriodDTO.of(period.getStartDate(), period.getEndDate());

        return dto;
    }

}
