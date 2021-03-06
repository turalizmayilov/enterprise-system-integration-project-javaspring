package com.buildit.common.application.dto;

import com.buildit.common.domain.model.Money;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.ResourceSupport;

import javax.persistence.Column;
import java.math.BigDecimal;

// @Value(staticConstructor = "of")
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor(staticName = "of")
public class MoneyDTO extends ResourceSupport {

	@Column(precision = 8, scale = 2)
	BigDecimal total;

	public Money toModel() {
		return Money.of(total);
	}

}