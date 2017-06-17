# algs4 java environment setting on debian8.8

标签（空格分隔）： java algs4 deiban8

---

这是我使用Debian系统配置Algorithm Part I 课程需要的Java环境的一些记录，之前保存在自己的OneNote笔记中，现在将其整理为md文件。
系统配置如下：

 - Linux debian8.8
 - 系统内核： 4.11.3

官方的Linux环境配置在这：[Hello World in Java on Linux][1].
但是我在根据以上配置的时候，出现了一些问题，后来google将问题解决了，所以做个笔记。

## 0. install Java ##
You will use the Java Platform, Standard Edition Development Kit (JDK 8).
需要使用JDK8, 可以查新一下系统现在的使用的哪个版本：

    [username:~/] javac -version
    javac 1.7.0_111
    [username:~/] java -version
    java version "1.7.0_111"
    Java(TM) SE Runtime Environment (build 1.7.0_111-b14)
    Java HotSpot(TM) 64-Bit Server VM (build 25.111-b14, mixed mode)
系统包管理器java的版本过低.
Install the Java Platform, Standard Edition Development Kit (JDK 8), either from Oracle or OpenJDK.

    [username:~/] sudo add-apt-repository ppa:webupd8team/java
    [username:~/] sudo apt-get update
    [username:~/] sudo apt-get install oracle-java8-installer
    [username:~/] sudo apt-get install oracle-java8-set-default
这一步安装JDK8按照以上步骤我的系统会报错，后来找到了[解决办法][2]：

    [username:~/] su -
    [username:~/] echo "deb http://ppa.launchpad.net/webupd8team/java/ubuntu xenial main" | tee /etc/apt/sources.list.d/webupd8team-java.list
    [username:~/] echo "deb-src http://ppa.launchpad.net/webupd8team/java/ubuntu xenial main" | tee -a /etc/apt/sources.list.d/webupd8team-java.list
    [username:~/] apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys EEA14886
    [username:~/] apt-get update
    [username:~/] apt-get install oracle-java8-installer
    [username:~/] exit
确定JDK8是否安装好：

    [username:~/] javac -version
    javac 1.8.0_131
    [username:~/] java -version
    java version "1.8.0_131"
    Java(TM) SE Runtime Environment (build 1.8.0_131-b11)
    Java HotSpot(TM) 64-Bit Server VM (build 25.131-b11, mixed mode)
现在JDK8已经安装完毕。

## 1.   Install a Programming Environment ##

 - Create a directory /usr/local/algs4. 创建目录 /usr/local/algs4
 

    [username:~/] cd /usr/local
    [username:/usr/local] sudo mkdir algs4
    [username:/usr/local] sudo chmod 755 algs4

 - Navigate to the subdirectory /usr/local/algs4. 转到 /usr/local/algs4目录下
 

    [username:/usr/local] cd algs4
    [username:/usr/local/algs4] pwd
    /usr/local/algs4

 - Download the textbook libraries from algs4.jar and the Java wrapper scripts from javac-algs4 and java-algs4. 下载库文件algs4.jar 和javac-algs4、java-algs4 俩个scripts,这样以后在Terminal下就能愉快的 javac-algs4 helloworld.java 和 java-algs4 helloworld.
 
 

    [username:/usr/local/algs4] sudo wget http://algs4.cs.princeton.edu/code/algs4.jar
    [username:/usr/local/algs4] sudo wget http://algs4.cs.princeton.edu/linux/javac-algs4
    [username:/usr/local/algs4] sudo wget http://algs4.cs.princeton.edu/linux/java-algs4
    [username:/usr/local/algs4] sudo chmod 755 javac-algs4 java-algs4
    [username:/usr/local/algs4] sudo mv javac-algs4 /usr/local/bin
    [username:/usr/local/algs4] sudo mv java-algs4 /usr/local/bin

 - Download DrJava from drjava.jar, the wrapper script from drjava, and the configuration file from .drjava.下载drjava,这是一个Java开发环境
 

    [username:/usr/local/algs4] sudo wget http://algs4.cs.princeton.edu/linux/drjava.jar
    [username:/usr/local/algs4] sudo wget http://algs4.cs.princeton.edu/linux/drjava
    [username:/usr/local/algs4] sudo wget http://algs4.cs.princeton.edu/linux/.drjava
    [username:/usr/local/algs4] sudo chmod 755 drjava
    [username:/usr/local/algs4] sudo mv drjava /usr/local/bin
    [username:/usr/local/algs4] sudo mv .drjava ~
**！！！****Attention**, 这里需要注意： 执行完以上命令之后，你需要：
[username:/usr/local/algs4] sudo chgrp username ~/.drjava
[username:/usr/local/algs4] sudo chown username ~/.drjava
将.drjava文件的用户组和用户更改为你自己，我们下载.java的时候，将其所有属性复制了，如果不进行更改，使用drjava的时候，进行个性化设置的时候后出现IO错误！！！
 - Download Checkstyle 7.4 from checkstyle.zip; our Checkstyle configuration file from checkstyle-algs4.xml; and the Checkstyle wrapper script from from checkstyle-algs4，下载Checkstyle
 

    [username:/usr/local/algs4/] sudo wget http://algs4.cs.princeton.edu/linux/checkstyle.zip
    [username:/usr/local/algs4/] sudo wget http://algs4.cs.princeton.edu/linux/checkstyle-algs4.xml
    [username:/usr/local/algs4/] sudo wget http://algs4.cs.princeton.edu/linux/checkstyle-suppressions.xml
    [username:/usr/local/algs4/] sudo wget http://algs4.cs.princeton.edu/linux/checkstyle-algs4
    [username:/usr/local/algs4/] sudo unzip checkstyle.zip
    [username:/usr/local/algs4/] sudo chmod 755 checkstyle-algs4
    [username:/usr/local/algs4/] sudo mv checkstyle-algs4 /usr/local/bin

 - Download Findbugs 3.0.1 from findbugs.zip; our Findbugs configuration file from findbugs.xml; and the Findbugs wrapper script from findbugs-algs4.下载findbugs.
  

    [username:/usr/local/algs4/] sudo wget http://algs4.cs.princeton.edu/linux/findbugs.zip
    [username:/usr/local/algs4/] sudo wget http://algs4.cs.princeton.edu/linux/findbugs.xml
    [username:/usr/local/algs4/] sudo wget http://algs4.cs.princeton.edu/linux/findbugs-algs4
    [username:/usr/local/algs4/] sudo unzip findbugs.zip
    [username:/usr/local/algs4/] sudo chmod 755 findbugs-algs4
    [username:/usr/local/algs4/] sudo mv findbugs-algs4 /usr/local/bin

 - For these wrapper scripts to work, it is important that /usr/local/bin is in your PATH environment variable. This is very likely the case. If not, see the Troubleshooting section below.


之后按照官方的教程熟悉如何使用drjava, algs4-javac, algs4-java, findbugs, checkstyle.
    

  [1]: http://algs4.cs.princeton.edu/linux/
  [2]: http://www.webupd8.org/2014/03/how-to-install-oracle-java-8-in-debian.html