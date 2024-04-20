package com.freud.ms.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import com.freud.ms.config.models.ModbusSimulatorVO;
import com.freud.ms.exception.ConfigurationLoadException;

public final class GlobalConfiguration {

	public static final String CONFIG_FILE_NAME = "config.xml";

	public static ModbusSimulatorVO configuration;

	private static String loadFileContent() throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(CONFIG_FILE_NAME))));
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\r\n");
			}
		} catch (Exception e) {

		} finally {
			if (br != null) {
				br.close();
			}
		}
		return sb.toString();
	}

	public static void load() throws ConfigurationLoadException {
		Reader reader = null;
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(ModbusSimulatorVO.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			reader = new StringReader(loadFileContent());
			GlobalConfiguration.configuration = (ModbusSimulatorVO) unmarshaller.unmarshal(reader);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ConfigurationLoadException("Not able to load the configuration file, please check it.");
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
