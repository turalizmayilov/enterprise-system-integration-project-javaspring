package com.buildit.procurement.application.services;

import com.buildit.procurement.application.dto.InvoiceDTO;
import com.buildit.procurement.application.dto.RentItInvoiceDTO;
import com.buildit.procurement.application.services.assemblers.InvoiceAssembler;
import com.buildit.procurement.domain.enums.InvoiceStatus;
import com.buildit.procurement.domain.model.Invoice;
import com.buildit.procurement.domain.model.PurchaseOrder;
import com.buildit.procurement.domain.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InvoiceService {

	@Autowired
	InvoiceRepository repository;

	@Autowired
	RemittanceAdviceService remittanceAdviceService;

	@Autowired
	PurchaseOrderService purchaseOrderService;

	@Autowired
	InvoiceAssembler assembler;

	@Transactional
	public InvoiceDTO add(RentItInvoiceDTO invoice) {
        PurchaseOrder po;
	    try {
            po = purchaseOrderService.findByExternalId(invoice.getPurchaseOrderId());
        }
        catch (IllegalArgumentException ex) {
	        return null;
        }

		Invoice localInvoice = new Invoice();

		localInvoice.setPurchaseOrder(po);
		localInvoice.setDueDate(invoice.getDueDate());
		localInvoice.setLatePayment(false);
		localInvoice.setStatus(InvoiceStatus.PENDING);
		localInvoice.setPayableAmount(invoice.getPayableAmount());

		localInvoice = repository.save(localInvoice);

		return assembler.toResource(localInvoice);
	}

	@Transactional
	public InvoiceDTO accept(Long invoiceId) {
		Optional<Invoice> maybeInvoice = repository.findById(invoiceId);

		if (!maybeInvoice.isPresent()) {
			throw new IllegalArgumentException("No invoice found by ID: " + maybeInvoice);
		}

		Invoice invoice = maybeInvoice.get();

		if (invoice.getStatus().isTransitionAllowed(InvoiceStatus.ACCEPTED)) {
			remittanceAdviceService.create(invoiceId, "Paid to bank account");
			invoice.setStatus(InvoiceStatus.ACCEPTED);
			invoice = repository.save(invoice);
			// TODO: send remittance advice to RentIt
		} else {
			throw new IllegalStateException("Cannot shift invoice from state " + invoice.getStatus() + " to state " + InvoiceStatus.ACCEPTED);
		}
		return assembler.toResource(invoice);
	}

	@Transactional(readOnly = true)
	public Collection<InvoiceDTO> readAll() {
		Collection<Invoice> all;

		all = repository.findAll();

		return all.stream().map(inv -> assembler.toResource(inv)).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public InvoiceDTO readOne(Long invoiceId) { return assembler.toResource(readModel(invoiceId));}

	@Transactional(readOnly = true)
	public Invoice readModel(Long id) {
		Optional<Invoice> maybeInvoice = repository.findById(id);

		if (!maybeInvoice.isPresent()) {
			throw new IllegalArgumentException("Cannot invoice with id: " + id);
		}

		return maybeInvoice.get();
	}

}