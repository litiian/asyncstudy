# tools
整合工具
# trains for Thoughtworks homework
# 环境
* Java >= 1.8 
* Maven version 3.5.3 

# 编译
* mvn clean compile exec:java -Dexec.mainClass="com.thoughtworks.homework.trains.Mainclass" -Dexec.args="输入文件路径 " -Dexec.cleanupDaemonThreads=false
* 文件路径举例：
  *  windows：
  mvn clean compile exec:java -Dexec.mainClass="com.thoughtworks.homework.trains.Mainclass"  -Dexec.args="D:/Input.txt" -Dexec.cleanupDaemonThreads=false
 * linux 或 mac:
  该改输入文件路径为对应系统文件格式 
