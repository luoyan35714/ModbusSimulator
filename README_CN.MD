[ [EN](README.MD) | [CN](README_CN.MD) ]

## Modbus Simulator

ModbusSimulator 是一个基于Java编写的Modbus协议模拟器，可以运行在不同的操作系统，包括Windows, Macos 和 Linux


### 配置

+ 核心配置文件 `config.xml`
+ `config_sample.xml`: 一个包含了所有可用配置的样例文件


### 配置参数
- protocolType: `[MODBUS_RTU, MODBUS_ASCII, MODBUS_TCP]`
- serialConnection: 串口链接配置
    - comPort: String类型, 在windows下请配置为`COM*` in windows, 在Linux下请配置为`/dev/tty*`
	- baudrate: `[300, 600, 1200, 2400, 4800, 9600, 14400, 19200, 38400, 56000, 57600, 115200]`
	- dataBits: `[7, 8]`
	- stopBits: `[1, 2]`
	- parity: `[NONE, ODD, EVEN]`
- tcpConnection: TCP 链接配置
    - port: TCP 端口
- modbusDataDefinition
	- slaveId: `[1, 255]` - 单元标志
	- functionCode: `[01, 02, 03, 04]` - 功能码
	- address: `[0, 65535]` - 开始地址
	- quality: `[1, 4096]` - 线圈的数量(功能码01或者02) 或 寄存器的数量(功能码03或者04)
	- return06Exception: `[true, false]` - 设置是否返回错误，当设置为true，所有的返回都是Exception
- modbusDataList
    - modbusData: `modbusData`的数量必须与`modbusDataDefinition.quality`的数量保持一致
    	- autoChange: `[true, false]` - 当设置为true的时候, `statusValue`每秒钟会改变一次，只在功能码为01或02的时候生效.
    	- statusValue: `[on, off]` - 线圈的值, `on`代表二进制1, `off` 代表二进制0. 只在功能码为01或02的时候生效..
    	- random: `[true, false]` - 当设置为true的时候, `value`每秒钟会改变一次，只在功能码为03或04的时候生效
    	- autoIncrement: `[true, false]` - 当设置为true的时候, `value`每秒钟会改变一次，只在功能码为03或04的时候生效
    	- value: `[-32768, 32767]` - 寄存器的值，只在功能码为03或04的时候生效


### 如何启动

#### 本地启动

+ 配置 `config.yaml`

+ 启动server
```bash
$ mvn clean install
$ java -jar target/ModbusSimulator.jar
```

+ log: `logs/*.log` files

#### Docker 启动

+ 配置 `config.yaml`

+ 构建 docker image
```bash
$ mvn clean install
$ docker build -t modbus-simulator:v1.0 .
```

+ 启动 server
```bash
# Normal test
$ docker run -it --rm --name modbus-simulator modbus-simulator:v1.0

# expose the port
$ docker run -it -d -p 8888:8888 --name modbus-simulator modbus-simulator:v1.0

# mount out log files
$ mkdir ms_logs
$ docker run -it -d -p 8888:8888 -v $(pwd)/ms_logs:/app/logs --name modbus-simulator modbus-simulator:v1.0
```


### 串口

当时用串口链接的时候，需要把`references/rxtx-2.2pre2-bins.zip`文件解压到`$JAVA_HOME\bin`中
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
