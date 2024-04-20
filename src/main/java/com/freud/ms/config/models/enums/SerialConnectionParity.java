package com.freud.ms.config.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SerialConnectionParity {

	NONE(0), 
	ODD(1),
	EVEN(2),
	MARK(3),
	SPACE(4);
	
	private int value;
}
