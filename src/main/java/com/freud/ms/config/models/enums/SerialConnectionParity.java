package com.freud.ms.config.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SerialConnectionParity {

	NONE(0), 
	ODD(1),
	EVEN(2);
	
	private int value;
}
