##赶工随便写的东西，但是正常使用没有问题
***
####目前有两个已知**BUG**：

1. info.txt为空的时候打开软件会报数组下标越界，因为获取文本那写死了；
2. 抢课系统开放前0-10分钟会重启一次，在系统重启完成，也就是http://class.sise.com.cn:7001/sise/login.jsp页面可以进入了就可以打开软件开始循环，否则登录页面进不去的话程序会报错；

* 第一次抢课的时候没有测试；在后续抢课时候成功蹲到十多节课；
* 程序是模拟浏览器的动作，有课才会提交申请，因为蜜罐的脚本有些地方有逻辑错误所以又写了一份当做练手
* 程序报错在error.txt中会有报错信息，除开以上两个BUG还有新的报错可以跟我说一声，有空改2333
* 打包好的jar包在目录tools下，已打包成.exe
***
2019/6/26  
　　--By 龙猫