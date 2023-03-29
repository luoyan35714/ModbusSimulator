package com.freud.ms.config.models;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ModbusDataDefinition {

    	private String slaveId;
		private String functionCode;
		private String address;
		private String quality;
		private String dataType;
        private Boolean return06Exception;

}
