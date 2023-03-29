package com.freud.ms;

import com.freud.ms.config.Configuration;
import com.freud.ms.exception.ConfigurationLoadException;

public class ModbusSimulator
{
    public static void main( String[] args ) throws ConfigurationLoadException {
        Configuration.load();
    }
}
