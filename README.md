# serialportNdk
所有的so 包都打好了，可以直接用，但是调用jni方法需要把两个SerialPort.java 和 SerialPortFinder.java 放入到文件夹为 zyserialport中，必须是。如果自己想重新打，直接改掉c和h文件里的方法就行，记得方法名对应包名加类名:谷歌开源的c++方法有_1，原因是文件夹也是_,所以区分要加_1;如果没有则只需要_即可。
