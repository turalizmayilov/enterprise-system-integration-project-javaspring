package com.buildit.procurement.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

// TODO - VALUE OBJECT?
@Entity
@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "of")
public class PlantInventoryEntry {

	@Id // href is the identifier
	@URL
	String href;

	@Column(nullable = false)
	String name;

}