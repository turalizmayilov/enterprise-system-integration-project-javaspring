package com.buildit.procurement.application.services.integration;

import com.buildit.common.application.dto.BusinessPeriodDTO;
import com.buildit.procurement.application.dto.*;
import com.buildit.procurement.application.services.ConstructionSiteService;
import com.buildit.procurement.application.services.assemblers.RentItToBuildItPlantInventoryEntryAssembler;
import com.buildit.procurement.domain.enums.PHRStatus;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Component
class LocalRentItService implements RentalPartnerService {

	@Autowired
	RentItToBuildItPlantInventoryEntryAssembler rent2buildEntryAssembler;

	@Autowired
	ConstructionSiteService constructionSiteService;

	@Override
	public String getPartnerName() {
		return "LocalRentIt";
	}

	@Override
	public String getApiUrl() {
		return "http://localhost:8090";
	}

	public Collection<PlantInventoryEntryDTO> queryPlantCatalog(String name, LocalDate startDate, LocalDate endDate) {
		HttpHeaders headers = new HttpHeaders();

		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

		final String url = getApiUrl() + "/api/plants";
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
				.queryParam("name", name)
				.queryParam("startDate", startDate)
				.queryParam("endDate", endDate);

		HttpEntity<List<RentItPlantInventoryEntryDTO>> entity = new HttpEntity<>(headers);

		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<List<RentItPlantInventoryEntryDTO>> response =
				restTemplate.exchange(
						builder.toUriString(),
						HttpMethod.GET,
						entity,
						new ParameterizedTypeReference<List<RentItPlantInventoryEntryDTO>>() {
						}
				);

		List<RentItPlantInventoryEntryDTO> entries = response.getBody();

		List<PlantInventoryEntryDTO> ret = rent2buildEntryAssembler.toResources(entries);

		return ret;
	}

//	public boolean isAvailableDuringPeriod(String href, BusinessPeriod period) {
//		return true;
//	}

	public PlantInventoryEntryDTO fetchPlantEntry(String href) {
		HttpHeaders headers = new HttpHeaders();

		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

		HttpEntity<?> entity = new HttpEntity<>(headers);

		ResponseEntity<RentItPlantInventoryEntryDTO> response =
				new RestTemplate().exchange(
						href,
						HttpMethod.GET,
						entity,
						new ParameterizedTypeReference<RentItPlantInventoryEntryDTO>() {
						}
				);

		RentItPlantInventoryEntryDTO entry = response.getBody();

		return rent2buildEntryAssembler.toResource(entry);
	}

	public Pair<PurchaseOrderDTO, PHRStatus> createPurchaseOrder(String href, BusinessPeriodDTO businessPeriodDTO, Long constructionSiteId) {
		PlantInventoryEntryDTO rentItEntry = fetchPlantEntry(href);
		// String respondTo = getApiUrl() + "/callbacks/orderStateChanged";
		ConstructionSiteDTO site = constructionSiteService.readOne(constructionSiteId);
		RentItCreatePORequestDTO rentItPORequest =
				RentItCreatePORequestDTO.of(rentItEntry.getExternalId(), businessPeriodDTO, site.getAddress());

		RentItPurchaseOrderDTO createdRemotePO = doCreatePurchaseOrder(rentItPORequest);

		PHRStatus newPHRStatus = createdRemotePO.getStatus().convertToPHRStatus();

		PurchaseOrderDTO po = new PurchaseOrderDTO();

		po.setExternalId(createdRemotePO.get_id());
		po.setHref(createdRemotePO.get_links().get("self").get("href"));

		Pair<PurchaseOrderDTO,PHRStatus> ret = Pair.of(po, newPHRStatus);

		return ret;
	}

	private RentItPurchaseOrderDTO doCreatePurchaseOrder(RentItCreatePORequestDTO request) {
		HttpHeaders headers = new HttpHeaders();

		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

		HttpEntity<RentItCreatePORequestDTO> entity = new HttpEntity<>(request, headers);

		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<RentItPurchaseOrderDTO> response =
				restTemplate.exchange(
						getApiUrl() + "/api/orders",
						HttpMethod.POST,
						entity,
						new ParameterizedTypeReference<RentItPurchaseOrderDTO>() {
						}
				);

		RentItPurchaseOrderDTO order = response.getBody();

		return order;
	}

	public void sendRemittanceAdvice(Long supplierId, RemittanceAdviceDTO remittanceAdvice) {
//		throw new NotImplementedException();
	}

	public RentItExtensionRequestDTO sendExtensionRequest(Long supplierId, Long purchaseOrderExternalId, LocalDate newEndDate) {
		HttpHeaders headers = new HttpHeaders();

		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

		ExtensionRequestDTO request = new ExtensionRequestDTO();

		request.setNewEndDate(newEndDate);

		HttpEntity<ExtensionRequestDTO> entity = new HttpEntity<>(request, headers);

		RestTemplate restTemplate = new RestTemplate();

		String url = getApiUrl() + "/api/orders/" + purchaseOrderExternalId + "/requestExtension";

		ResponseEntity<RentItExtensionRequestDTO> response =
				restTemplate.exchange(
						url,
						HttpMethod.POST,
						entity,
						new ParameterizedTypeReference<RentItExtensionRequestDTO>() {
						}
				);

		RentItExtensionRequestDTO rentItExtensionRequestDTO = response.getBody();

		return rentItExtensionRequestDTO;
	}

	@Override
	public boolean cancelPurchaseOrder(Long supplierId, Long purchaseOrderExternalId) {
		HttpHeaders headers = new HttpHeaders();

		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

		HttpEntity<Object> entity = new HttpEntity<>(headers);

		RestTemplate restTemplate = new RestTemplate();

		String url = getApiUrl() + "/api/orders/" + purchaseOrderExternalId + "/cancel";

		try {
			ResponseEntity<Object> response =
					restTemplate.exchange(
							url,
							HttpMethod.POST,
							entity,
							new ParameterizedTypeReference<Object>() {
							}
					);
			if (!response.getStatusCode().is2xxSuccessful()) {
				return false;
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
