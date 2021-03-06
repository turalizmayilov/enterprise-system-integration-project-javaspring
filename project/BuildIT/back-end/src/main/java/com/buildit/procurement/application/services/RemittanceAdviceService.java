package com.buildit.procurement.application.services;

import com.buildit.procurement.application.dto.RemittanceAdviceDTO;
import com.buildit.procurement.application.services.integration.IntegrationService;
import com.buildit.procurement.domain.model.RemittanceAdvice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RemittanceAdviceService {

	@Autowired
	PlantHireRequestService plantHireRequestService;

	@Autowired
	PurchaseOrderService purchaseOrderService;

	@Autowired
	InvoiceService invoiceService;

	@Autowired
	IntegrationService integrationService;

	@Autowired
	SupplierService supplierService;

	public RemittanceAdviceDTO create(Long invoiceId, String note) {
		Long supplierId = findSupplierId(invoiceId);

		RemittanceAdvice remittanceAdvice = null; // TODO

		RemittanceAdviceDTO remittanceAdviceDTO = null; // TODO assemblers etc.

		integrationService.sendRemittanceAdvice(supplierId, remittanceAdviceDTO);

		return null; // TODO
	}

	private Long findSupplierId(Long invoiceId) {
		/*
		InvoiceDTO invoiceDTO = invoiceService.readOne(invoiceId);

		PurchaseOrder purchaseOrder = purchaseOrderService.readModel(invoiceDTO.getPurchaseOrder().getHref());

		Long phrId = purchaseOrder.getPlantHireRequest().getId();

		PlantHireRequestDTO plantHireRequest = plantHireRequestService.readOne(phrId);

		return plantHireRequest.getSupplier().get_id();
		*/
		return supplierService.getFirstAsModel().getId(); // TODO
	}

}
