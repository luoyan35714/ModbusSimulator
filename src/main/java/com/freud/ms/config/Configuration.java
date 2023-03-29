package com.freud.ms.config;

import com.freud.ms.config.models.ModbusSimulatorVO;
import com.freud.ms.exception.ConfigurationLoadException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.*;

public final class Configuration {

    private static final String CONFIG_FILE_NAME="config.xml";

    private static String loadFileContent() throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try{
            br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(CONFIG_FILE_NAME))));
            String line = null;
            while((line=br.readLine())!=null){
                sb.append(line).append("\r\n");
            }
        }catch (Exception e){

        }finally {
            if( br != null ){
                br.close();
            }
        }
        return sb.toString();
    }
    public static ModbusSimulatorVO load() throws ConfigurationLoadException {

        ModbusSimulatorVO modbusSimulatorVO = null;
        Reader reader = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ModbusSimulatorVO.class);
            // xml转为对象的接口 反序列化
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            reader = new StringReader(loadFileContent());
            modbusSimulatorVO = (ModbusSimulatorVO)unmarshaller.unmarshal(reader);
            return modbusSimulatorVO;
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
