# MyOpenTrader
Welcome to the MyOpenTrader Source repository. 


```
Copyright (C) 2015 Stephan Grotz - stephan@myopentrader.org
March 2015 - v0.1.2-alpha
```


Thanks for showing interest in MyOpenTrader (MOT). MOT is (yet another) a complex-event based open-source trading-engine, which is free to use for everyone. I have started MOT in 2012 and I have been using MOT ever since successfully. The best way of describing MOT is a flexible open-source trading framework. It is built from ground up as a parallel computing engine, which allows to do large scale parallel backtesting.  
MOT is distributed under the GPL v3 License and is free to use for everyone without warranty. MyOpenTrader.org is not responsible for any of your trading losses and can not be held accountable. Use the framework at your OWN risk.
 
 
IMPORTANT LINKS:
* [MyOpentrader.org] (https://www.myopentrader.org)
* [wiki.myopentrader.org] (https://wiki.myopentrader.org)
* [forum.myopentrader.org] (http://forum.myopentrader.org)
* [Download the binaries from Github] (https://github.com/sgrotz/myopentrader/releases)
* [Download the sources from Github] (https://github.com/sgrotz/myopentrader)


REQUIREMENTS:
* MySQL Server installed and configured.
* Java 1.6++

  
CONTENT INFORMATION - directory structure:
* javadoc:	Contains the development api, useful for your own development
* bin: 		Contains all of the binary scripts, such as startup or shutdown scripts. 
* conf: 	Contains all of the configuration files, used by MyOpenTrader.
* libs:		Contains all external libraries
* resources: 	Contains all resources, such as sql schema and default data


GETTING STARTED:
The easiest way to get started is:
* to set up your database (import the schema & default data!!)
* start the embedded broker through bin/runEmbeddedMessageBus.sh|.bat
* start the tick generator/simulator through bin/runTickGenerator.sh|.bat
* start the MyOpenTraderCore Engine through bin/startMyOpenTraderCore_daemon.bat|.sh
* Take a look at the [development guide] (https://wiki.myopentrader.org/confluence/display/MOTD/Development+Guide) 

Happy trading :)

Take a look at the wiki for more detailed information (https://wiki.myopentrader.org)


CHANGELOG: 
* June 18: Included new status field for strategies. This allows gradually closing strategies. 
* Apr 4:  Added new [Collective2.com] (https://www.collective2.com/) Connector. Orders/Strategies can now get tagged with Collective2 systems. Allows external strategy monitoring.
* Mar 30: Released v0.1.2. Added embedded webserver for MyOpenTraderWeb frontend. Embedded SHIRO for authentication and cleaned up css-mess in the frontend.
* Feb 01: Released v0.1-alpha

