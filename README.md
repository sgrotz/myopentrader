# MyOpenTrader
Welcome to the MyOpenTrader Source repository. 


/*
* Copyright (C) 2015 Stephan Grotz - stephan@myopentrader.org
* Feb 2015 - v0.1-alpha
*/
 
 
IMPORTANT LINKS:
* [MyOpentrader.org] (http://www.myopentrader.org)
* [wiki.myopentrader.org] (http://wiki.myopentrader.org)
* [forum.myopentrader.org] (http://forum.myopentrader.org)


REQUIREMENTS:
* MySQL Server installed and configured.
* Java 1.6++

  
CONTENT INFORMATION - directory structure:
* api: 	Contains the development api, useful for your own development
* bin: 	Contains all of the binary scripts, such as startup or shutdown scripts. 
* conf: Contains all of the configuration files, used by MyOpenTrader.
* libs:	Contains all external libraries
* resources: 	Contains all resources, such as sql schema and default data


GETTING STARTED:
The easiest way to get started is:
* to set up your database (import the schema & default data!!)
* start the embedded broker through bin/runEmbeddedMessageBus.sh|.bat
* start the tick generator/simulator through bin/runTickGenerator.sh|.bat
* start the MyOpenTraderCore Engine through bin/startMyOpenTraderCore_daemon.bat|.sh

Happy trading :)

Take a look at the wiki for more detailed information (http://wiki.myopentrader.org)
