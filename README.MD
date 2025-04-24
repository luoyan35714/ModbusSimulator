[ [EN](README.MD) | [CN](README_CN.MD) ]

## Modbus Simulator

ModbusSimulator is a java based modbus protocol server simulator that can run on multiple platforms such as Windows, Linux and Macos.


### How to config

+ Configuration file name is `config.xml`
+ `config_sample.xml`: Here's a sample that shows all of the configuration parameters you can use. 


### Configuration parameters
- protocolType: `[MODBUS_RTU, MODBUS_ASCII, MODBUS_TCP]`
- serialConnection: Serial connection configuration
    - comPort: String, `COM*` in windows, `/dev/tty*` in linux
	- baudrate: `[300, 600, 1200, 2400, 4800, 9600, 14400, 19200, 38400, 56000, 57600, 115200]`
	- dataBits: `[7, 8]`
	- stopBits: `[1, 2]`
	- parity: `[NONE, ODD, EVEN]`
- tcpConnection: TCP connection configuration
    - port: TCP Port
- modbusDataDefinition
	- slaveId: `[1, 255]` - Addiotional Address in modbus.
	- functionCode: `[01, 02, 03, 04]` - Function code in modbus.
	- address: `[0, 65535]` - Start address
	- quality: `[1, 4096]` - quality of the coils(function code 01 and 02) or register(function code 03 and 04)
	- return06Exception: `[true, false]` - If it's true, the response will always be 86 exception.
- modbusDataList
    - modbusData: The number of `modbusData` must be consistent with `modbusDataDefinition.quality`.
    	- autoChange: `[true, false]` - If it's true, the `statusValue` will change every sec. Only works for function code 01 and 02.
    	- statusValue: `[on, off]` - Coils value, `on` means 1, `off` means 0 in binary. Only works for function code 01 and 02.
    	- random: `[true, false]` If it's true, the `value` will change randomly every second. Only works for function code 03 and 04.
    	- autoIncrement: `[true, false]` If it's true, the `value` will change add one every second. Only works for function code 03 and 04.
    	- value: `[-32768, 32767]` - Register value. Only works for function code 03 and 04.


### How to start 

#### Local start

+ setup the `config.yaml`

+ start the server
```bash
$ mvn clean install
$ java -jar target/ModbusSimulator.jar
```

+ log: `logs/*.log` files

#### Docker start

+ setup the `config.yaml`

+ build docker image
```bash
$ mvn clean install
$ docker build -t modbus-simulator:v1.0 .
```

+ start server
```bash
# Normal test
$ docker run -it --rm --name modbus-simulator modbus-simulator:v1.0

# expose the port
$ docker run -it -d -p 8888:8888 --name modbus-simulator modbus-simulator:v1.0

# mount out log files
$ mkdir ms_logs
$ docker run -it -d -p 8888:8888 -v $(pwd)/ms_logs:/app/logs --name modbus-simulator modbus-simulator:v1.0
```


### Serial Port

Please extract following files from `references/rxtx-2.2pre2-bins.zip` into the `$JAVA_HOME\bin` folder before using the serial connection.
- `rxtxSerial.dll` - (for windows)
- `librxtxSerial.so & librxtxParallel.so` - (for linux)
- `librxtxSerial.jnilib` - (for mac)


### Useful links

Modbus Org: [https://modbus.org/](https://modbus.org/)

Modbus Protocol: [https://www.hifreud.com/tag/#Modbus-ref](https://www.hifreud.com/tag/#Modbus-ref)

RXRX: [http://rxtx.qbang.org/wiki/index.php/Download](http://rxtx.qbang.org/wiki/index.php/Download)

Virtual serial port Driver: [https://www.eltima.com/products/vspdxp/](https://www.eltima.com/products/vspdxp/)

Modbus Poll: [https://www.modbustools.com/modbus_poll.html](https://www.modbustools.com/modbus_poll.html)

Modbus Slave: [https://www.modbustools.com/modbus_slave.html](https://www.modbustools.com/modbus_slave.html)
