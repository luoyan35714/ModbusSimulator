package com.freud.ms.config.models;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ModbusData {
        private Boolean autoChange;
		private Boolean random;
		private Boolean autoIncrease;
		private String value;
}
