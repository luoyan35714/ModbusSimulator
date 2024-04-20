package com.freud.ms.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ModbusExceptionCode {

	ERROR_01((byte) 0X01),

	ERROR_02((byte) 0X02),

	ERROR_03((byte) 0X03),

	ERROR_04((byte) 0X04),
	
	ERROR_06((byte) 0X06);

	private byte code;
}
